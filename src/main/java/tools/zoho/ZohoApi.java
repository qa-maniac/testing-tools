package tools.zoho;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Calendar;
import java.util.Properties;


public class ZohoApi {

    private final String baseUrl = "https://www.zohoapis.com/crm/v2";
    private ZohoData data;

    public enum ApiRoute {
        DEALS_BY_CRITERIA   ("/Deals/search?criteria="),
        DEAL_BY_ID          ("/Deals/"),
        USERS_BY_CRITERIA   ("/Contacts/search?criteria="),
        USER_BY_ID          ("/Contacts/"),
        ;
        public String route;

        ApiRoute(String route) {
            this.route = route;
        }
    }


    public ZohoApi() {
        this.data = new ZohoData();
    }

    public ZohoApi(Properties properties) {
        this.data = new ZohoData(properties);
    }


    public Response getUserByEmail(String email) {
        RestAssured.baseURI = baseUrl;
        String criteria = "(Email:equals:" + email + ")";
        String route = ApiRoute.USERS_BY_CRITERIA.route + criteria;

        RequestSpecification request = RestAssured
                .given()
                .header("Authorization", "Zoho-oauthtoken " + getAccessToken());
        return request.get(route);
    }


    public void approveUser(String id) {
        RestAssured.baseURI = baseUrl;
        String route = ApiRoute.USER_BY_ID.route + id;

        String xmlDataString = "{\"data\":[{\"Document_Verified\":\"Approved\"}]}";
        RequestSpecification request = RestAssured
                .given()
                .header("Authorization", "Zoho-oauthtoken " + getAccessToken()).body(xmlDataString);
        request.put(route);
    }


    public Response getDealsByUserEmail(String email, String date) {
        RestAssured.baseURI = baseUrl;
        String route = ApiRoute.DEALS_BY_CRITERIA.route + "(Contact_Name:equals:" + getUserName(email) + ")&sort_by=Stage_Update_Time&sort_order=desc";
        RequestSpecification request = RestAssured.given()
                .header("Authorization", "Zoho-oauthtoken " + getAccessToken())
                .header("If-Modified-Since", date);
        return request.get(route);
    }


    private String getUserName(String email) {
        Response response = getUserByEmail(email);
        return response.jsonPath().get().toString().split(", Record_Image")[0].split(", Full_Name=")[1];
    }


    public void approveTransaction(String identifier, double amount, String date) {
        RestAssured.baseURI = baseUrl;
        String route = ApiRoute.DEAL_BY_ID.route + identifier;
        String xmlDataString = "{\"data\":[{\"Fiat_Exchange_Fee\":\"1\"," +
                " \"Fiat_Exchange_Rate\":\"1\"," +
                " \"Elegro_Net_Processing_Fee\":\"1\"," +
                " \"Contract_Exchange_Default_Fees\":\"1\"," +
                " \"Buy_Amount\":\"" + amount + "\"," +
                " \"Rate\":\"1\"," +
                " \"Hash\":\"1\"," +
                " \"Date_And_Time_Of_Execution2\":\"" + date + "\"," + // "2020-03-18T12:42:00", 10/18/2019 05:17 PM
                " \"Amount\":\"1\"," +
                " \"Stage\":\"OrderSuccess\"}]}";

        RequestSpecification request = RestAssured
                .given()
                .header("Authorization", "Zoho-oauthtoken " + getAccessToken())
                .body(xmlDataString);
        request.put(route);
    }


    private String getAccessToken() {
        long time = Long.parseLong(data.getTime());
        Calendar date = Calendar.getInstance();

        if (time > date.getTimeInMillis()) return data.getAuthToken();
        else return updateAuthToken();
    }


    private String updateAuthToken() {
        String URL = "https://accounts.zoho.com/oauth/v2/token?" +
                "refresh_token="  + data.getRefreshToken() +
                "&client_id="     + data.getClientId() +
                "&client_secret=" + data.getClientSecret() +
                "&grant_type=refresh_token";
        RequestSpecification request = RestAssured.given();
        Response response = request.post(URL);

        String token = response.jsonPath().get().toString().split(", api_domain")[0].split("access_token=")[1];

        updateZohoData(token);
        return token;
    }


    private void updateZohoData(String token) {
        data.setAuthToken(token);

        Calendar date = Calendar.getInstance();
        data.setTime(Long.toString(date.getTimeInMillis() + 3600000));
    }


    public void generateAccessAndRefreshTokens() {
        String URL = "https://accounts.zoho.com/oauth/v2/token?" +
                "code="           + data.getGrantToken() +
                "&redirect_uri="  + data.getRedirectUri() +
                "&client_id="     + data.getClientId() +
                "&client_secret=" + data.getClientSecret() +
                "&grant_type=authorization_code";
        RequestSpecification request = RestAssured.given();
        Response response = request.post(URL);

        System.out.println(response.asString());
        //{"access_token":"1000.8b45ea8ec09591f63df63ffdd1dd0ab3.0cc6f65564d230f72b82673df8919277","refresh_token":"1000.d5172cc8261377753e45ba9f9a28763d.26438ada3ec6dfaeb990b05a5cfe6895","api_domain":"https://www.zohoapis.com","token_type":"Bearer","expires_in":3600}
    }


    //For cryptoEx app
   /* String token = signIn(email, password);
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("status", "Approved");
    jsonObject.put("id", recordId);
    executeRequest("/crm/kyc/status", RequestMethod.POST, jsonObject, token);*/
}