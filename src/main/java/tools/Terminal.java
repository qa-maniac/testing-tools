package tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;


public class Terminal {

    private final String[] WINDOWS_CMD = {"cmd.exe", "/C"};
    private final String[] LINUX_CMD = {"/bin/bash", "-l", "-c"};

    String[] command = null;


    public Terminal() {
        String os = "";
        try {
            os = System.getProperty("os.name");
            if(os.equals("Linux"))
                this.command = LINUX_CMD;
            else
                this.command = WINDOWS_CMD;
        } catch (Exception e) {
            this.command = WINDOWS_CMD;
        }
    }


    public ArrayList<String> run(String... command) {
        ProcessBuilder pb = new ProcessBuilder(concat(this.command, command));
        pb.redirectErrorStream(true);
        try {
            Process process = pb.start();
            process.waitFor();
            ArrayList<String> result = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String outputString;
            while(((outputString = reader.readLine()) != null)) {
                result.add(outputString);
            }
            return result;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }


    private  <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }


    public String getDeviceId() {
        String value = "";
        try {
            String rule = "([0-9A-Z]){12,16}\\w+";
            int index = 0;
            while (index < 12) {
                value = run("adb get-serialno").get(0);
                if (value.matches(rule))
                    break;
                else {
                    index++;
                    System.out.println("Device not found");
                    Thread.sleep(5000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return value;
    }
}