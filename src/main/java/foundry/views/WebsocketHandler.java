package foundry.views;

import com.google.gson.Gson;
import foundry.model.SentinelModel;
import foundry.model.Submission;
import foundry.model.Team;
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


/**
 * NOTE: This class should be completely transient between instances of the server due to the nature of websocket
 * connections. Because of this, object references/pointers are used directly in maps. Do not serialize.
 */
@WebSocket
public class WebsocketHandler {
    
    static final Logger log = LoggerFactory.getLogger(WebsocketHandler.class);
    private static Gson gson = new Gson();
    
    // Store sessions if you want to, for example, broadcast a message to all users
    private static final ConcurrentHashMap<Team, List<Session>> sessions = new ConcurrentHashMap<>(1000);
    private static final ConcurrentHashMap<Session, Team> sessionMap = new ConcurrentHashMap<>(1000);
    
    @OnWebSocketConnect
    public void connected(Session session) throws IOException {
        //do nothing, wait for authentication
    }
    
    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        try {
            sessions.get(sessionMap.get(session)).remove(session);
            sessionMap.remove(session);
        } catch (NullPointerException ignored) {}
    }
    
    @OnWebSocketMessage
    public void message(Session session, String messageRaw) throws IOException {
        WSInboundMessage m = gson.fromJson(messageRaw, WSInboundMessage.class);
        Team t = SentinelModel.getTeam(m.teamName);
        if (Long.parseLong(m.auth)==t.getWebsocketAuth()) {
            sessions.computeIfAbsent(t, k -> new ArrayList<>());
            sessions.get(t).add(session);
            sessionMap.put(session, t);
            sendSubmissions(t);
        } else {
            session.getRemote().sendString("false");
            session.close();
        }
    }
    
    public static void sendSubmissions(Team t) {
        WSOutboundMessage mess = new WSOutboundMessage("submissions", t.getSubmissions());
        sessions.get(t).forEach(s -> {
            try {
                s.getRemote().sendString(gson.toJson(mess));
            } catch (IOException ignored) {}
        });
    }
    public static void updateSubmission(Team t, Submission s) {
        if (sessions.get(t)==null) return;
        WSOutboundMessage mess = new WSOutboundMessage("update-submission", s);
        String ans = gson.toJson(mess);
        sessions.get(t).forEach(sess -> {
            try {
                sess.getRemote().sendString(ans);
            } catch (IOException ignored) {}
        });
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
