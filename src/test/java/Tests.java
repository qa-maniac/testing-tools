import io.restassured.response.Response;
import org.junit.Test;
import org.languagetool.language.AmericanEnglish;
import tools.rest.RequestMethod;
import tools.rest.Rest;
import tools.text.IgnoredSpelling;
import tools.text.Spelling;
import tools.zoho.ZohoApi;

import java.util.Arrays;
import java.util.List;


public class Tests {

    @Test
    public void checkRest() throws Exception {
        String weatherBaseUrl = "http://api.weatherstack.com/";
        Rest weather = new Rest(weatherBaseUrl);

        String accessKeyField = "access_key";
        String accessKeyValue = "4b64b1c9cc345e80d0ac0f6d565486d4";
        String queryField = "query";
        String queryCityValue = "Mykolayiv";

        String currentWeatherRoute = "/current?" + accessKeyField + "=" + accessKeyValue + "&"  + queryField + "=" + queryCityValue;

        Response response = weather.executeRequest(currentWeatherRoute, RequestMethod.GET);
        System.out.println(response.prettyPrint());
//        System.out.println(response.jsonPath().get("location.region").toString());
    }

    @Test
    public void checkText() {
        String text = "In addition to the operations supported directly via REST API endpoints, any operation which can be performed with Workflow Actions can be performed using the REST API, by triggering a Workflow Action which performs the operation.\n" +
                "\n" +
                "This not only enables you to perform many built-in dotCMS operations which do not have a specific REST API endpoint, but it also allows you to trigger any custom Workflow actions you've created (either via a custom Workflow action plugin or using the Custom Code field on a workflow Action).\n" +
                "\n" +
                "For example, the following common dotCMS operations do not have specific REST API endpoints, but can be performed with the REST API by triggering Workflow Actions:";

        List<String> words = Arrays.asList("dotCMS");
        IgnoredSpelling ignore = new IgnoredSpelling(words, null);

        Spelling tool = new Spelling(new AmericanEnglish());
        String result = tool.check(text, ignore);
        System.out.println(result);
    }
}