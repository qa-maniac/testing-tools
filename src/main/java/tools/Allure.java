package tools;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class Allure {

    private String ip = ip();
    private String reportHomePage = "http://" + ip + ":9999/index.html";
    private String projectPath = System.getProperty("user.dir");
    private String commandReport = "cd " + projectPath + " && mvn allure:report";
    private String commandServe = "cd " + projectPath + " && allure serve target/allure-results --host " + ip + " --port 9999";


    public void showReport() {
        Terminal terminal = new Terminal();
        terminal.run(commandReport);

        new Slack(Slack.WebHook.TESTING.value)
                .send("Testing of *" + projectName() + "* project is completed.\n Report is available by following link: " + reportHomePage);

        terminal.run(commandServe);
    }


    private String ip() {
        String ip = "";
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ip;
    }


    private String projectName() {
        String path = projectPath;
        boolean trigger = true;
        while (trigger) {
            int index = path.indexOf('/');
            if(index >= 0) path = path.substring(index + 1);
            else trigger = false;
        }
        return path;
    }
}