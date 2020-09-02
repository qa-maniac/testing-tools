package tools;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

/*
* Check documentation here:
* http://spec-zone.ru/RU/Java/Docs/8/api/javax/naming/InitialContext.html#ENVIRONMENT
*
* Check comments here:
* https://ru.wikipedia.org/wiki/Java_Naming_and_Directory_Interface
* http://spec-zone.ru/RU/Java/Docs/8/technotes/guides/jndi/jndi-dns.html
*/

public class EmailValidator {

//    private final String TEST_EMAIL = "test@elegro.eu";
//    private final String TEST_EMAIL = "elegro.qa@i.ua";
    private final String TEST_EMAIL = "test-emails@i.ua";


    public enum EmailCheckResult {
        NOT_EMAIL       (false, "Given value is not an email"),
        NOT_VALID_EMAIL (false, "Given value does not exist"),
        NO_DOMAIN       (false, "Given value has no domain"),
        SMTP_NOT_READY  (false, "SMTP service on given host is not ready"),
        NOT_SMTP        (false, "Given domain is not a SMTP"),
        SENDER_REJECTED (false, "Sender has been rejected by SMTP service"),
        OK              (true,  "Given value is exists and working"),
        ;

        public boolean isCorrect;
        public String description;

        EmailCheckResult(boolean isCorrect, String description) {
            this.isCorrect = isCorrect;
            this.description = description;
        }
    }


    public EmailCheckResult check(String emailValue) {

        // check if the value is not email
        int atSignPosition = emailValue.indexOf('@');
        if (atSignPosition == -1) return EmailCheckResult.NOT_EMAIL;


        String domainValue = emailValue.substring(++atSignPosition);
        ArrayList<String> mxList;

        // check if the value has domain
        try {
            mxList = getMX(domainValue);
        } catch (NamingException ex) {
            return EmailCheckResult.NO_DOMAIN;
        }
        if (mxList.size() == 0) return EmailCheckResult.NO_DOMAIN;
        else return checkMxHosts(emailValue, mxList);
    }


    private ArrayList<String> getMX(String domainValue) throws NamingException {
        // Set environment for DNS provider
        Hashtable<String, String> environment = new Hashtable<>();
        environment.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");

        // Set initial context to get MX (mail exchange) record of given domain
        DirContext initialContext = new InitialDirContext(environment);
        Attributes attrs = initialContext.getAttributes(domainValue, new String[]{"MX"});
        Attribute attr = attrs.get("MX");

        // If MX is empty then get A (host address) of given domain
        if ((attr == null) || (attr.size() == 0)) {
            attrs = initialContext.getAttributes(domainValue, new String[]{"A"});
            attr = attrs.get("A");

            // If A is empty then throw exception
            if (attr == null)
                throw new NamingException("No match for name '" + domainValue + "'");
        }

        ArrayList<String> result = new ArrayList<>();

        // Add all MX hosts to resulting list
        NamingEnumeration<?> enumeration = attr.getAll();
        while (enumeration.hasMore()) {
            String[] mxFields = ((String) enumeration.next()).split(" ");

            // Set correct mail host value
            String mailHost;
            if (mxFields.length == 1)
                mailHost = mxFields[0];
            else if (mxFields[1].endsWith("."))
                mailHost = mxFields[1].substring(0, (mxFields[1].length() - 1));
            else
                mailHost = mxFields[1];
            result.add(mailHost);
        }
        return result;
    }


    private EmailCheckResult checkMxHosts(String emailValue, ArrayList<String> mxList) {
        EmailCheckResult checkResult = EmailCheckResult.OK;
        for (String host : mxList) {
            boolean valid = false;
            try {
                Socket socket = new Socket(host, 25);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                int result = hear(reader);

                // Explanation of 220 response code:
                // https://www.socketlabs.com/blog/21-smtp-response-codes-that-you-need-to-know/
                if (result != 220) throw new Exception("SMTP service on given host is not ready");

                // Explanation of EHLO:
                // https://help.returnpath.com/hc/en-us/articles/220223328-What-is-Extended-HELO-EHLO-
                say(writer, "EHLO elegro.eu");
                result = hear(reader);

                // Response codes:
                // https://sendgrid.com/blog/smtp-server-response-codes-explained/
                if (result != 250) throw new Exception("Given domain is not a SMTP");

                say(writer, "MAIL FROM: <" + TEST_EMAIL + ">");
                result = hear(reader);
                if (result != 250) throw new Exception("Sender has been rejected by SMTP service");

                say(writer, "RCPT TO: <" + emailValue + ">");
                result = hear(reader);

                // Explanation of commands:
                // https://blog.mailtrap.io/smtp-commands-and-responses/
                say(writer, "RSET"); hear(reader);
                say(writer, "QUIT"); hear(reader);

                if (result != 250) throw new Exception("Given value is not exist");

                valid = true;

                reader.close();
                writer.close();
                socket.close();
            } catch (Exception ex) {
                for(EmailCheckResult result : EmailCheckResult.values()) {
                    if(ex.getMessage().equals(result.description))
                        checkResult = result;
                }
            }
            if(valid) return EmailCheckResult.OK;
        }
        return checkResult;
    }


    private int hear(BufferedReader in) throws IOException {
        String line;
        int res = 0;
        while ((line = in.readLine()) != null) {
            String pfx = line.substring(0, 3);
            try {
                res = Integer.parseInt(pfx);
            } catch (Exception ex) {
                res = -1;
            }
            if (line.charAt(3) != '-') break;
        }
        return res;
    }


    private void say(BufferedWriter wr, String text) throws IOException {
        wr.write(text + "\r\n");
        wr.flush();
    }
}