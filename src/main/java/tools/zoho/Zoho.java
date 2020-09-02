package tools.zoho;

import io.restassured.response.Response;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Zoho {

    private ZohoApi api = new ZohoApi();


    public void approveUser(String email) {
        Response response = api.getUserByEmail(email);
        String userId = response.jsonPath().get().toString()
                .split(", Typt_of_Activity")[0]
                .split("Stage=null, id=")[1];
        api.approveUser(userId);
    }


    public void approveTransaction(String emailOrId, double amount) {
        String identifier = getIdentifier(emailOrId);
        String date = getDate(true);
        api.approveTransaction(identifier, amount, date);
    }


    private String getIdentifier(String value) {
        if (value.contains("@")) return getTransactionId(value);
        else return value;
    }


    private String getTransactionId(String email) {
        String date = getDate(false);
        Response response = api.getDealsByUserEmail(email, date);
        try {
            int indexOfWallet = response.jsonPath().get().toString().indexOf(date); //"2019-10-28 15:39:00"
            System.out.println(indexOfWallet);
            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(response.jsonPath().get().toString()
                    .split("Deal_Name=")[1]
                    .split(", id=")[1]
                    .split(", ")[0]);
            while (m.find()) {
                return m.group();
            }
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("There are no transactions this date");
        }
        return "There are no transactions this date";
    }


    private String getDate(boolean withSeconds) {
        String pattern = "yyyy-MM-dd'T'HH:mm";
        if (withSeconds) pattern += ":ss";

        DateFormat df = new SimpleDateFormat(pattern);
        Date date = new Date(System.currentTimeMillis() - 30 * 1000);

        return df.format(date);
    }
}