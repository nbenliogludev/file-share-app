package com.nbenliogludev.config;

/**
 * @author nbenliogludev
 */
public record AppConfig(
                int port,
                String uploadDir,
                String dataDir,
                int retentionDays,
                String apiKey,
                String publicBaseUrl
        ) {
    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private Integer port, retentionDays;
        private String uploadDir, dataDir, apiKey, publicBaseUrl;

        public Builder port(Integer v){ this.port=v; return this; }
        public Builder uploadDir(String v){ this.uploadDir=v; return this; }
        public Builder dataDir(String v){ this.dataDir=v; return this; }
        public Builder retentionDays(Integer v){ this.retentionDays=v; return this; }
        public Builder apiKey(String v){ this.apiKey=v; return this; }
        public Builder publicBaseUrl(String v){ this.publicBaseUrl=v; return this; }

        public AppConfig buildWithDefaults() {
            int p  = or(port, 7000);
            String up = or(uploadDir, "uploads");
            String dd = or(dataDir, "data");
            int rd = or(retentionDays, 30);
            String key = apiKey;
            String base = or(publicBaseUrl, "http://localhost:" + p);
            return new AppConfig(p, up, dd, rd, key, base);
        }
        private static <T> T or(T v, T def){ return v!=null? v: def; }
    }
}