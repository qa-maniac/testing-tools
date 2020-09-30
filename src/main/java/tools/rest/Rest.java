package tools.rest;


import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;


public class Rest {

    private String baseUrl;


    public Rest(String baseUrl) {
        this.baseUrl = baseUrl;
    }


    public Response executeRequest(String route, RequestMethod method, String... token) throws Exception {
        RestAssured.baseURI = baseUrl;
        assert(token.length <= 1);

        RequestSpecification requestSpecification = RestAssured
                .given()
                .header("Content-Type","application/json");

        if(token.length == 1) requestSpecification = requestSpecification.auth().oauth2(token[0]);

        if      (method.equals(RequestMethod.POST))     return requestSpecification.post(route);
        else if (method.equals(RequestMethod.GET))      return requestSpecification.get(route);
        else throw new Exception("Pay attention! You are trying to use incorrect Request Method");
    }


    public Response executeRequest(String route, RequestMethod method, JSONObject data, String... token) throws Exception {
        RestAssured.baseURI = baseUrl;

        assert(token.length <= 1);
        RequestSpecification requestSpecification = RestAssured
                .given()
                    .header("Content-Type","application/json");

        if(token.length == 0)   requestSpecification = requestSpecification.body(data.toJSONString());
        else                    requestSpecification = requestSpecification.auth().oauth2(token[0]).body(data.toJSONString());

        if      (method.equals(RequestMethod.POST))     return requestSpecification.post(route);
        else if (method.equals(RequestMethod.PUT))      return requestSpecification.put(route);
        else if (method.equals(RequestMethod.PATCH))    return requestSpecification.patch(route);
        else if (method.equals(RequestMethod.DELETE))   return requestSpecification.delete(route);
        else if (method.equals(RequestMethod.GET))      return requestSpecification.get(route);
        else throw new Exception("Pay attention! You are trying to use incorrect Request Method");
    }


    public Response executeRequest(String route, RequestMethod method, RequestSpecification specification, JSONObject data, String... token) throws Exception {
        RestAssured.baseURI = baseUrl;

        assert(token.length <= 1);
        RequestSpecification requestSpecification;
        if(token.length == 0) requestSpecification = specification.body(data.toJSONString());
        else                  requestSpecification = specification.auth().oauth2(token[0]).body(data.toJSONString());

        if      (method.equals(RequestMethod.POST))     return requestSpecification.post(route);
        else if (method.equals(RequestMethod.PUT))      return requestSpecification.put(route);
        else if (method.equals(RequestMethod.PATCH))    return requestSpecification.patch(route);
        else if (method.equals(RequestMethod.DELETE))   return requestSpecification.delete(route);
        else if (method.equals(RequestMethod.GET))      return requestSpecification.get(route);
        else throw new Exception("Pay attention! You are trying to use incorrect Request Method");
    }


    public Response executeRequest(String route, RequestMethod method, RequestSpecification specification, String... token) throws Exception {
        RestAssured.baseURI = baseUrl;
        assert (token.length <= 1);

        if (token.length == 1)  specification.auth().oauth2(token[0]);

        if      (method.equals(RequestMethod.POST))     return specification.post(route);
        else if (method.equals(RequestMethod.PUT))      return specification.put(route);
        else if (method.equals(RequestMethod.PATCH))    return specification.patch(route);
        else if (method.equals(RequestMethod.DELETE))   return specification.delete(route);
        else if (method.equals(RequestMethod.GET))      return specification.get(route);
        else throw new Exception("Pay attention! You are trying to use incorrect Request Method");
    }


    public String getBaseUrl() {
        return baseUrl;
    }
}