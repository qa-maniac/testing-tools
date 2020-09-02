package tools;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.simple.JSONObject;

public class Slack {

    private String webHook;


    public Slack(String webHook) {
        this.webHook = webHook;
    }


    public Response send(String message) {
        RestAssured.baseURI = webHook;
        JSONObject data = new JSONObject();
        data.put("text", message);
        return RestAssured.given()
                .header("Content-Type","application/json")
                .header("Accept", "application/json")
                .body(data.toJSONString())
                .post("");
    }


    public enum WebHook {
        QATEAM  ("https://hooks.slack.com/services/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"),
        TESTING ("https://hooks.slack.com/services/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"),
        ;
        public final String value;
        WebHook(String value) {
            this.value = value;
        }
    }
}