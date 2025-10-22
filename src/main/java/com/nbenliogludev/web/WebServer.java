package com.nbenliogludev.web;

import com.nbenliogludev.config.AppConfig;
import io.javalin.Javalin;
import io.javalin.http.ContentType;
import io.javalin.http.staticfiles.Location;
import java.util.Map;

/**
 * @author nbenliogludev
 */
public final class WebServer {
    private final AppConfig cfg;
    private Javalin app;

    public WebServer(AppConfig cfg) { this.cfg = cfg; }

    public void start() {
        app = Javalin.create(conf -> {
            conf.http.defaultContentType = "application/json";
            var res = WebServer.class.getClassLoader().getResource("public");
            if (res != null) {
                conf.staticFiles.add(s -> {
                    s.hostedPath = "/";
                    s.directory = "public";
                    s.location = Location.CLASSPATH;
                });
            } else {
                System.out.println("WARN: classpath:/public not found; skipping static files");
            }
        });

        app.get("/api/health", ctx -> {
            ctx.contentType(ContentType.APPLICATION_JSON);
            ctx.json(Map.of("status", "ok"));
        });

        app.start(cfg.port());
    }
}