package tools.email;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.StringUtils;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;


public class Gmail_API {

    private static final String USER = Dotenv.load().get("EMAIL_ACCOUNT") + Dotenv.load().get("EMAIL_DOMAIN");
    private static final String APPLICATION_NAME = "TestFramework";
    private static final String TOKENS_DIRECTORY_PATH = "src/main/resources/tokens/gmail";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_MODIFY);
    private static Gmail service;


    public static Gmail startService() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential =  GoogleCredentials.getCredentials(SCOPES, TOKENS_DIRECTORY_PATH, HTTP_TRANSPORT, JSON_FACTORY, USER);

        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }


    public static List<Message> getMessages(String query) throws IOException, GeneralSecurityException {
        service = startService();
        ListMessagesResponse listMessagesResponse;
        if (query.equals("")) listMessagesResponse = service.users().messages().list(USER).execute();
        else listMessagesResponse = service.users().messages().list(USER).setQ(query).execute();
        return listMessagesResponse.getMessages();
    }


    public static String getMessageBodyText(Message message) throws IOException, GeneralSecurityException {
        service = startService();
        Message email = service.users().messages().get(USER, message.getId()).execute();
        return StringUtils.newStringUtf8(Base64.decodeBase64(email.getPayload().getBody().getData()));
    }


    public static List<MessagePartHeader> getMessageHeaders (Message message) throws IOException, GeneralSecurityException {
        service = startService();
        Message email = service.users().messages().get(USER, message.getId()).execute();
        return email.getPayload().getHeaders();
    }
}