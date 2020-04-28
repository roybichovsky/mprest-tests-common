package com.mprest.tests.common.process.management.remote.windows;

import com.mprest.tests.common.utilities.SSHCommand;
import com.mprest.tests.common.utilities.SSHHelper;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RemoteProcessHelper {

    public static SSHCommand executeCommand(SSHHelper sshHelper, String executableName, String[] arguments) {
        String args = String.join(" ", arguments);
        String cmdLine = String.format("nohup ssh & %s %s", executableName, args);
        return sshHelper.executeCommand(cmdLine);
    }

    public static List<Integer> getRemoteProcesses(SSHHelper sshHelper, String executableName) {
        String command = queryAllInstancesOfProcess(executableName);
        SSHCommand sshCmd = sshHelper.executeCommand(command);
        return parseProcessId(sshCmd.getInputStream(), executableName);
    }

    public static void killRemoteProcess(SSHHelper sshHelper, int ProcessPid) {
        String command = killProcessByID(ProcessPid);
        sshHelper.executeCommand(command);
    }

    private static String queryAllInstancesOfProcess(String executableName) {
        return String.format("Tasklist /FI \"IMAGENAME eq %s\"", executableName);
    }

    private static String killProcessByID(int processId) {
        return String.format("Taskkill /F /PID %S", processId);
    }

    /**
     * Example of expected output:
     * <p>
     * Image Name                     PID Session Name        Session#    Mem Usage
     * ========================= ======== ================ =========== ============
     * notepad.exe                   8568 Console                    1     15,716 K
     */
    private static List<Integer> parseProcessId(InputStream stream, String executableName) {
        List<Integer> processes = new ArrayList<>();

        BufferedReader input = new BufferedReader(new InputStreamReader(stream));
        try {
            String line;
            while ((line = input.readLine()) != null) {
                if (line.startsWith(executableName)) {
                    line = line.replaceAll("\\s+", ",");
                    String[] lineParts = line.trim().split(",");
                    String processIdText = lineParts[1].trim();
                    int processId = Integer.parseInt(processIdText);
                    processes.add(processId);
                }
            }
            input.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return processes;
    }
}