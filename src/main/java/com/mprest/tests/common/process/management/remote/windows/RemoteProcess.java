package com.mprest.tests.common.process.management.remote.windows;

import com.mprest.tests.common.process.management.ProcessInstance;
import com.mprest.tests.common.utilities.SSHCommand;
import com.mprest.tests.common.utilities.SSHHelper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class RemoteProcess implements ProcessInstance {

    public static final int InvalidProcessId = -1;

    private int processId;
    private String createCmd;
    private String attachCmd;
    private String workingDirectory;
    private String[] arguments;
    private final SSHHelper sshHelper;

    private RemoteProcess(String host, String user, String password) {
        sshHelper = new SSHHelper(host, user, password);
        processId = InvalidProcessId;
    }

    RemoteProcess(int processId, String host, String user, String password, String attachCmd) {
        this(host, user, password);
        this.processId = processId;
        this.attachCmd = attachCmd;
    }

    RemoteProcess(String host, String user, String password, String workingDirectory, String createCmd, String[] arguments) {
        this(host, user, password);
        this.createCmd = createCmd;
        this.arguments = arguments;
        this.processId = InvalidProcessId;
        this.workingDirectory = workingDirectory;
    }

    @Override
    public boolean start() {
        log.info("Starting remote process '{}', {} ", createCmd, arguments);

        SSHCommand sshCmd = RemoteProcessHelper.executeCommand(sshHelper, createCmd, arguments);
        return sshCmd != null;
    }

    @Override
    public boolean stop() {
        boolean isRunning = isRunning();
        if (!isRunning) {
            log.info("Remote process '{}' is not  running", attachCmd);
            return false;
        }
        log.info("Stopping remote process");

        if (processId != InvalidProcessId) {
            log.info("Kill Remote process  '{}'", processId);
            RemoteProcessHelper.killRemoteProcess(sshHelper, processId);
            return true;
        }
        log.error("Failed to kill remote Process");
        return false;
    }

    @Override
    public boolean isRunning() {
        List<Integer> processes = RemoteProcessHelper.getRemoteProcesses(sshHelper, attachCmd);
        if (processes.isEmpty()) {
            return false;
        }
        return processes.contains(processId);
    }
}
