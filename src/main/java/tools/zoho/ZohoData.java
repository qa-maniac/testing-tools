package tools.zoho;

import tools.Config;


public class ZohoData {

    private String CONFIG_FILE_PATH = "zoho.properties";
    private String clientId;
    private String grantToken;
    private String clientSecret;
    private String redirectUri;
    private String refreshToken;
    private String authToken;
    private long time;


    public ZohoData() {
        Config config = new Config(CONFIG_FILE_PATH);

        this.clientId     = config.get(ZohoDataKey.CLIENT_ID.toString());
        this.grantToken   = config.get(ZohoDataKey.CLIENT_GRANT_TOKEN.toString());
        this.clientSecret = config.get(ZohoDataKey.CLIENT_SECRET.toString());
        this.redirectUri  = config.get(ZohoDataKey.REDIRECT_URI.toString());
        this.refreshToken = config.get(ZohoDataKey.REFRESH_TOKEN.toString());
        this.authToken    = config.get(ZohoDataKey.AUTHTOKEN.toString());
        this.time         = Long.parseLong(config.get(ZohoDataKey.TIME.toString()));
    }

    public String getClientId() {
        return clientId;
    }

    public String getGrantToken() {
        return grantToken;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public long getTime() {
        return time;
    }

    public String getConfigFilePath() {
        return CONFIG_FILE_PATH;
    }
}