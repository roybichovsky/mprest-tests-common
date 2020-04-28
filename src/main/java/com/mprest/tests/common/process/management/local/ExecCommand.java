package com.mprest.tests.common.process.management.local;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExecCommand {

    private String command;
    private String[] args;
    private File workingDirectory;
    private Map<String, String> environment;

    public ExecCommand(String command, String[] args) {
        this(command, args, null, null);
    }

    public ExecCommand(String command, String[] args, File workingDirectory, Map<String, String> environment) {
        this.command = command;
        this.args = args;
        this.workingDirectory = workingDirectory;
        this.environment = environment;
    }

    public Process start() {
        Process process;
        try {
            List<String> command = new ArrayList<>();
            command.add(this.command); // The process name as the first argument
            if (args != null) {
                Collections.addAll(command, args);
            }

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            if (workingDirectory != null) {
                processBuilder.directory(workingDirectory.getAbsoluteFile());
            }
            if (environment != null) {
                Map<String, String> environment = processBuilder.environment();
                this.environment.forEach(environment::put);
            }
            process = processBuilder.start();
        } catch (Throwable err) {
            String errorStr = String.format("Could not execute the command '%s'", command);
            log.error(errorStr, err);
            process = null;
        }
        return process;
    }
}
