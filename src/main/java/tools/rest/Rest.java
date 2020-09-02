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

        if(token.length == 0) {
            if      (method.equals(RequestMethod.POST))     return RestAssured.given().header("Content-Type","application/json").post(route);
            else if (method.equals(RequestMethod.GET))      return RestAssured.given().header("Content-Type","application/json").get(route);
            else throw new Exception("Pay attention! You are trying to use incorrect Request Method");
        } else {
            RequestSpecification requestSpecification = RestAssured.given().auth().oauth2(token[0]);
            if      (method.equals(RequestMethod.POST))     return requestSpecification.header("Content-Type","application/json").post(route);
            else if (method.equals(RequestMethod.GET))      return requestSpecification.header("Content-Type","application/json").get(route);
            else throw new Exception("Pay attention! You are trying to use incorrect Request Method");
        }
    }


    public Response executeRequest(String route, RequestMethod method, JSONObject data, String... token) throws Exception {
        RestAssured.baseURI = baseUrl;
        assert(token.length <= 1);

        if(token.length == 0) {
            if      (method.equals(RequestMethod.POST))     return RestAssured.given().header("Content-Type","application/json").body(data.toJSONString()).post(route);
            else if (method.equals(RequestMethod.PUT))      return RestAssured.given().header("Content-Type","application/json").body(data.toJSONString()).put(route);
            else if (method.equals(RequestMethod.PATCH))    return RestAssured.given().header("Content-Type","application/json").body(data.toJSONString()).patch(route);
            else if (method.equals(RequestMethod.DELETE))   return RestAssured.given().header("Content-Type","application/json").body(data.toJSONString()).delete(route);
            else if (method.equals(RequestMethod.GET))      return RestAssured.given().header("Content-Type","application/json").body(data.toJSONString()).get(route);
            else throw new Exception("Pay attention! You are trying to use incorrect Request Method");
        } else {
            RequestSpecification requestSpecification = RestAssured.given().auth().oauth2(token[0]);
            if      (method.equals(RequestMethod.POST))     return requestSpecification.header("Content-Type","application/json").body(data.toJSONString()).post(route);
            else if (method.equals(RequestMethod.PUT))      return requestSpecification.header("Content-Type","application/json").body(data.toJSONString()).put(route);
            else if (method.equals(RequestMethod.PATCH))    return requestSpecification.header("Content-Type","application/json").body(data.toJSONString()).patch(route);
            else if (method.equals(RequestMethod.DELETE))   return requestSpecification.header("Content-Type","application/json").body(data.toJSONString()).delete(route);
            else if (method.equals(RequestMethod.GET))      return requestSpecification.header("Content-Type","application/json").body(data.toJSONString()).get(route);
            else throw new Exception("Pay attention! You are trying to use incorrect Request Method");
        }
    }


    public Response executeFormDataRequest(String url, RequestMethod method, RequestSpecification specification, String... token) throws Exception {
        RestAssured.baseURI = baseUrl;
        assert (token.length <= 1);

        if (token.length == 1)  specification.auth().oauth2(token[0]);

        if      (method.equals(RequestMethod.POST))     return specification.post(url);
        else if (method.equals(RequestMethod.PUT))      return specification.put(url);
        else if (method.equals(RequestMethod.PATCH))    return specification.patch(url);
        else if (method.equals(RequestMethod.DELETE))   return specification.delete(url);
        else if (method.equals(RequestMethod.GET))      return specification.get(url);
        else throw new Exception("Pay attention! You are trying to use incorrect Request Method");
    }


    public String getBaseUrl() {
        return baseUrl;
    }


    public String endpoint() {
        return getBaseUrl();
    }
}