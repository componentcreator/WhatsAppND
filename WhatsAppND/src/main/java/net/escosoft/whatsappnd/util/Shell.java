package net.escosoft.whatsappnd.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Shell {

    //for internal access
    public Shell() {
    }
    //for external access

    public String sendShellCommand(String[] cmd) {
        System.out.println("\n###executing: " + cmd[0] + "###");
        String AllText = "";
        try {
            String line;
            Process process = new ProcessBuilder(cmd).start();
            BufferedReader STDOUT = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader STDERR = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            try {
                process.waitFor();
            } catch (InterruptedException ex) {
                Logger.getLogger(Shell.class.getName()).log(Level.SEVERE, null, ex);
            }
            while ((line = STDERR.readLine()) != null) {
                AllText = AllText + "\n" + line;
            }
            while ((line = STDOUT.readLine()) != null) {
                AllText = AllText + "\n" + line;
                while ((line = STDERR.readLine()) != null) {
                    AllText = AllText + "\n" + line;
                }
            }
            //log.level0(cmd[0]+"\":"+AllText);
            return AllText;
        } catch (IOException ex) {
            System.out.println("Problem while executing in Shell.sendShellCommand() Received " + AllText);
            return "CritERROR!!!";
        }

    }

}
