package foundry.views;

import foundry.model.SentinelModel;
import foundry.model.Setting;
import foundry.model.SettingWrapper;
import foundry.model.Settings;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Route;
import spark.TemplateViewRoute;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import static foundry.Utilities.generateModel;
import static spark.Spark.halt;

public class SettingsView {
    
    public static TemplateViewRoute handleSettingsGet = (req, res) -> {
        HashMap<String, Object> model = generateModel(req);
    
        //check permissions
        if (!(boolean)req.session().attribute("judge"))
            throw halt(403, "<html><body><h1>Access denied. <a href='/'>Return home.</a></h1></body></html>");
        
        model.put("settings", true);
        ArrayList<SettingWrapper> settings = new ArrayList<>();
        Settings cs = SentinelModel.getSettings();
        Class<Settings> sc = Settings.class;
        Field[] fields = sc.getDeclaredFields();
        for (Field f : fields) {
            Annotation[] annotations = f.getDeclaredAnnotations();
            Setting info = null;
            for (Annotation a : annotations) if (a.annotationType() == Setting.class) info = (Setting)a;
            if (info==null) continue;
            settings.add(new SettingWrapper(info.name(), info.description(), f.get(cs)));
        }
        model.put("currSettings", settings);
        
        return new ModelAndView(model, "templates/settings.vtl");
    };
    
    public static Route handleSettingsPost = (req, res) -> {
        if (req.session()==null || !(boolean)req.session().attribute("judge"))
            throw halt(403, "<html><body><h1>Access denied. <a href='/'>Return home.</a></h1></body></html>");
    
        QueryParamsMap params = req.queryMap();
        
        Settings settings = SentinelModel.getSettings();
        Class<Settings> sc = Settings.class;
        Field[] fields = sc.getDeclaredFields();
        for (Field f : fields) {
            Annotation[] annotations = f.getDeclaredAnnotations();
            Setting info = null;
            for (Annotation a : annotations) if (a.annotationType() == Setting.class) info = (Setting)a;
            if (info==null) continue;
            QueryParamsMap val = params.get(info.name());
            if (f.getType()==boolean.class) {
                f.set(settings, val.value()!=null && val.value().equals("on"));
            }
        }
        
        SentinelModel.saveSettings();
        
        res.redirect("/settings");
        
        return "";
    };
    
}
