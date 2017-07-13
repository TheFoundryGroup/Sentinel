package foundry.views;

import com.google.gson.Gson;
import foundry.model.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * NOTE: This class should be completely transient between instances of the server due to the nature of websocket
 * connections. Because of this, object references/pointers are used directly in maps. Do not serialize.
 */
@WebSocket
public class WebsocketHandler {
    
    static final Logger log = LoggerFactory.getLogger(WebsocketHandler.class);
    private static Gson gson = new Gson();
    
    // Store sessions if you want to, for example, broadcast a message to all users
    private static final ConcurrentLinkedQueue<Session> allSessions = new ConcurrentLinkedQueue<>();
    private static final ConcurrentHashMap<Team, List<Session>> teamMap = new ConcurrentHashMap<>(1000);
    private static final ConcurrentHashMap<Session, Team> teamSessions = new ConcurrentHashMap<>(1000);
    private static final ConcurrentHashMap<Judge, List<Session>> judgeMap = new ConcurrentHashMap<>(1000);
    private static final ConcurrentHashMap<Session, Judge> judgeSessions = new ConcurrentHashMap<>(1000);
    
    @OnWebSocketConnect
    public void connected(Session session) throws IOException {
        //do nothing, wait for authentication
    }
    
    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        try {
            if (teamSessions.get(session)!=null) {
                teamMap.get(teamSessions.get(session)).remove(session);
                teamSessions.remove(session);
            } else {
                judgeMap.get(judgeSessions.get(session)).remove(session);
                judgeSessions.remove(session);
            }
            allSessions.remove(session);
        } catch (NullPointerException ignored) {}
    }
    
    @OnWebSocketMessage
    public void message(Session session, String messageRaw) throws IOException {
        WSInboundMessage m = gson.fromJson(messageRaw, WSInboundMessage.class);
        boolean judge = m.judge;
        Team t = null;
        Judge j = null;
        if (judge) {
            j = SentinelModel.getJudge(m.teamName);
        } else {
            t = SentinelModel.getTeam(m.teamName);
        }
        if (Long.parseLong(m.auth)==(judge ? j.getWebsocketAuth() : t.getWebsocketAuth())) {
            if (judge) {
                judgeMap.computeIfAbsent(j, k -> new ArrayList<>());
                judgeMap.get(j).add(session);
                judgeSessions.put(session, j);
            } else {
                teamMap.computeIfAbsent(t, k -> new ArrayList<>());
                teamMap.get(t).add(session);
                teamSessions.put(session, t);
            }
            allSessions.add(session);
            if (!judge) sendSubmissions(session);
            sendClarifications(session);
        } else {
            session.getRemote().sendString("false");
            session.close();
        }
    }
    
    public static void sendSubmissions(Session s) {
        Team t = teamSessions.get(s);
        WSOutboundMessage mess = new WSOutboundMessage("submissions", t.getSubmissions());
        try {
            s.getRemote().sendString(gson.toJson(mess));
        } catch (IOException ignored) {}
    }
    public static void updateSubmission(Team t, Submission s) {
        if (teamMap.get(t)==null) return;
        WSOutboundMessage mess = new WSOutboundMessage("update-submission", s);
        String ans = gson.toJson(mess);
        teamMap.get(t).forEach(sess -> {
            try {
                sess.getRemote().sendString(ans);
            } catch (IOException ignored) {}
        });
    }
    
    public static void sendClarifications(Session s) {
        Team t = teamSessions.get(s);
        Judge j = judgeSessions.get(s);
        if (t==null && j==null) {
            s.close();
            throw new RuntimeException("Session not mapped to team/judge");
        }
        WSOutboundMessage mess = t==null ?
                new WSOutboundMessage("clarifications", SentinelModel.getClarifications()) :
                new WSOutboundMessage("clarifications", SentinelModel.getClarifications(t));
        try {
            s.getRemote().sendString(gson.toJson(mess));
        } catch (IOException ignored) {}
    }
    
    public static void updateClarification(Clarification c) {
        WSOutboundMessage mess = new WSOutboundMessage("update-clarification", c);
        if (c.isGlobal()) {
            allSessions.forEach(s -> {
                try {
                    s.getRemote().sendString(gson.toJson(mess));
                } catch (IOException ignored) {}
            });
        } else {
            judgeSessions.keySet().forEach(s -> {
                try {
                    s.getRemote().sendString(gson.toJson(mess));
                } catch (IOException ignored) {}
            });
            if (teamMap.get(SentinelModel.getTeam(c.getFrom()))!=null) teamMap.get(SentinelModel.getTeam(c.getFrom())).forEach(s -> {
                try {
                    s.getRemote().sendString(gson.toJson(mess));
                } catch (IOException ignored) {}
            });
        }
    }
    
    private static class WSInboundMessage {
        String auth; //has to be a string because javascript uses imprecise floating point
        String teamName;
        boolean judge;
    }
    
    private static class WSOutboundMessage {
        String reason;
        Object data;
        public WSOutboundMessage(String reason, Object data) {
            this.reason = reason;
            this.data = data;
        }
    }
    
}
