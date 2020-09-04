package tools.zoho;

import tools.Config;

import java.util.Properties;


public class ZohoData {

    private String CONFIG_FILE_PATH = "zoho.properties";
    private String clientId;
    private String grantToken;
    private String clientSecret;
    private String redirectUri;
    private String refreshToken;
    private String authToken;
    private long time;


    public ZohoData(boolean useConfigFile) {
        if(useConfigFile) {
            Config config = new Config(CONFIG_FILE_PATH);

            this.clientId = config.get(ZohoDataKey.CLIENT_ID.toString());
            this.grantToken = config.get(ZohoDataKey.CLIENT_GRANT_TOKEN.toString());
            this.clientSecret = config.get(ZohoDataKey.CLIENT_SECRET.toString());
            this.redirectUri = config.get(ZohoDataKey.REDIRECT_URI.toString());
            this.refreshToken = config.get(ZohoDataKey.REFRESH_TOKEN.toString());
            this.authToken = config.get(ZohoDataKey.AUTHTOKEN.toString());
            this.time = Long.parseLong(config.get(ZohoDataKey.TIME.toString()));
        } else {
            Properties properties = System.getProperties();

            this.clientId = properties.getProperty(ZohoDataKey.CLIENT_ID.toString());
            this.grantToken = properties.getProperty(ZohoDataKey.CLIENT_GRANT_TOKEN.toString());
            this.clientSecret = properties.getProperty(ZohoDataKey.CLIENT_SECRET.toString());
            this.redirectUri = properties.getProperty(ZohoDataKey.REDIRECT_URI.toString());
            this.refreshToken = properties.getProperty(ZohoDataKey.REFRESH_TOKEN.toString());
            this.authToken = properties.getProperty(ZohoDataKey.AUTHTOKEN.toString());
            this.time = Long.parseLong(properties.getProperty(ZohoDataKey.TIME.toString()));
        }
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

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setTime(long time) {
        this.time = time;
    }
}