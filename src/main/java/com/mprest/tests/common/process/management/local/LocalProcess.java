package com.mprest.tests.common.process.management.local;

import com.mprest.tests.common.process.management.ProcessInstance;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Map;

@Slf4j
public class LocalProcess implements ProcessInstance {

    private final String commandLine;
    private final String[] args;
    private final File workingDirectory;
    private final Map<String, String> environment;
    private int pid;
    private Process process;

    LocalProcess(int pid, String cmdLine, File workingDirectory) {
        this.pid = pid;
        commandLine = cmdLine;
        process = null;
        args = null;
        this.workingDirectory = workingDirectory;
        environment = null;
    }

    LocalProcess(String cmdLine, String[] args, File workingDirectory, Map<String, String> environment) {
        pid = -1;
        process = null;
        commandLine = cmdLine;
        this.args = args;
        this.workingDirectory = workingDirectory;
        this.environment = environment;
    }

    @Override
    public boolean start() {
        log.info("Starting process '{}'...", commandLine);
        try {
            String commandLine = this.commandLine;
            if (commandLine != null) {
                log.info("Running command: '{}'", commandLine);
                ExecCommand exec;
                if (workingDirectory != null) {
                    exec = new ExecCommand(commandLine, args, workingDirectory, environment);
                } else {
                    exec = new ExecCommand(commandLine, args);
                }
                process = exec.start();
                log.info("Started process '{}'.", this.commandLine);
            } else {
                log.info("Could not start process '{}'.", this.commandLine);
                return false;
            }

            return true;
        } catch (Throwable err) {
            String errorStr = String.format("Could not start process '%s'.", commandLine);
            log.error(errorStr, err);
            return false;
        }
    }

    @Override
    public boolean stop() {
        boolean isRunning = isRunning();
        if (!isRunning) {
            log.debug("process '{}' is not running...", commandLine);
            return false;
        }
        log.debug("Stopping process '{}'...", commandLine);
        try {
            if (pid != -1) {
                Runtime rt = Runtime.getRuntime();
                String killCmd = String.format("taskkill /F /PID %d", pid);
                rt.exec(killCmd);
                log.debug("Stopped process '{}'.", commandLine);
                return true;
            }

            if (process != null) {
                process.destroyForcibly();
                log.debug("Stopped process '{}'.", commandLine);
                return true;
            }

            log.error("Could not stop process '{}'.", commandLine);
            return false;
        } catch (Throwable err) {
            String errorStr = String.format("Could not stop process '%s'.", commandLine);
            log.error(errorStr, err);
            return false;
        } finally {
            process = null;
            pid = -1;
        }
    }

    @Override
    public boolean isRunning() {
        if (pid != -1) {
            return pidExists(pid);
        }

        if (process != null) {
            return process.isAlive();
        }

        return false;
    }

    private boolean pidExists(int pid) {
        try {
            final String EXEC = "wmic path win32_process get processid /format:csv";
            Process taskMan = null;
            Runtime rt = Runtime.getRuntime();
            String line;
            taskMan = rt.exec(EXEC);
            BufferedReader input = new BufferedReader(new InputStreamReader(taskMan.getInputStream()));

            while ((line = input.readLine()) != null) {
                try {
                    if (line.equals("")) {
                        continue;
                    }
                    String[] strRecordParts = line.split(",");
                    String strCurrentPid = strRecordParts[1];
                    int currentPid = Integer.parseInt(strCurrentPid);
                    if (currentPid == pid) {
                        return true;
                    }
                } catch (Throwable err) {
                }
            }
        } catch (Throwable err) {
            String strError = String.format("Could not get the PID table while looking for process '%s' with the PID '%d'", commandLine, pid);
            log.error(strError, err);
            return false;
        }
        return false;
    }
}
