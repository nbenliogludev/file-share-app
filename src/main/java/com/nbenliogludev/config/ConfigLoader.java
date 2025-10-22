package com.nbenliogludev.config;

import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.util.Map;

/**
 * @author nbenliogludev
 */
public final class ConfigLoader {

    public static AppConfig load() {
        var b = AppConfig.builder();

        Map<String,Object> root = readYaml("application.yml");
        if (root != null) b = applyYaml(b, root);

        b = applyEnv(b);
        return b.buildWithDefaults();
    }

    @SuppressWarnings("unchecked")
    private static AppConfig.Builder applyYaml(AppConfig.Builder b, Map<String,Object> root) {
        Object server = root.get("server");
        if (server instanceof Map<?,?> s) {
            Object port = s.get("port");
            if (port instanceof Number n) b.port(n.intValue());
        }
        Object fs = root.get("filesvc");
        if (fs instanceof Map<?,?> f) {
            Object up = f.get("uploadDir");       if (up   instanceof String v) b.uploadDir(v);
            Object dd = f.get("dataDir");         if (dd   instanceof String v) b.dataDir(v);
            Object rd = f.get("retentionDays");   if (rd   instanceof Number n) b.retentionDays(n.intValue());
            Object key= f.get("apiKey");          if (key == null || key instanceof String) b.apiKey((String) key);
            Object pb = f.get("publicBaseUrl");   if (pb   instanceof String v) b.publicBaseUrl(v);
        }
        return b;
    }

    private static AppConfig.Builder applyEnv(AppConfig.Builder b) {
        String p = env("FILESVC_PORT");               if (p != null) try { b.port(Integer.parseInt(p)); } catch (Exception ignored){}
        String up= env("FILESVC_UPLOAD_DIR");         if (up!= null) b.uploadDir(up);
        String dd= env("FILESVC_DATA_DIR");           if (dd!= null) b.dataDir(dd);
        String rd= env("FILESVC_RETENTION_DAYS");     if (rd!= null) try { b.retentionDays(Integer.parseInt(rd)); } catch (Exception ignored){}
        String key=env("FILESVC_API_KEY");            if (key!=null) b.apiKey(key);
        String pb= env("FILESVC_PUBLIC_BASE");        if (pb !=null) b.publicBaseUrl(pb);
        return b;
    }

    private static String env(String k) {
        String v = System.getenv(k);
        return (v != null && !v.isBlank()) ? v : null;
    }

    private static Map<String,Object> readYaml(String resourceName) {
        try (InputStream in = ConfigLoader.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (in == null) return null;
            return new Yaml().load(in);
        } catch (Exception e) {
            System.err.println("WARN: cannot load " + resourceName + ": " + e.getMessage());
            return null;
        }
    }
}