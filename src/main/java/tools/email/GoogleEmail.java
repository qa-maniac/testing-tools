package tools.email;

import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;
import org.jsoup.Jsoup;
import tools.email.constants.GmailHeader;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.LinkedList;
import java.util.List;


public class GoogleEmail {

    private Message message;


    public GoogleEmail(String query) {
        try {
            this.message = Gmail_API.getMessages(query).get(0);
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }


    public List<MessagePartHeader> getHeaders() {
        List<MessagePartHeader> result = new LinkedList<>();
        try {
            result = Gmail_API.getMessageHeaders(message);
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
        return result;
    }


    public String getHeaderValue(GmailHeader headerName) {
        String result = "";
        List<MessagePartHeader> headers = getHeaders();
        for (MessagePartHeader header : headers)
            if (header.getName().equals(headerName.string))
                result = header.getValue();
        return result;
    }


    public String getHtml() {
        String result = "";
        try {
            result = Gmail_API.getMessageBodyText(message);
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
        return result;
    }


    public String getText() {
        String html = getHtml();
        return Jsoup.parse(html).text();
    }


    public Message getMessage() {
        return this.message;
    }
}