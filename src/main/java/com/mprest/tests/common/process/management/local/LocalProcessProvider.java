package com.mprest.tests.common.process.management.local;

import com.mprest.tests.common.process.management.AttachToProcessInstanceSettings;
import com.mprest.tests.common.process.management.CreateProcessInstanceSettings;
import com.mprest.tests.common.process.management.ProcessInstance;
import com.mprest.tests.common.process.management.ProcessProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
public class LocalProcessProvider implements ProcessProvider {

    public ProcessInstance getProcess(AttachToProcessInstanceSettings settings) {
        final String TASK_MANAGER_CMD = "wmic path win32_process get name, processid, executablePath, commandline /format:csv";
        Process taskMan = null;
        Runtime rt = Runtime.getRuntime();

        try {
            taskMan = rt.exec(TASK_MANAGER_CMD);
            BufferedReader input = new BufferedReader(new InputStreamReader(taskMan.getInputStream()));
            return getProcess(input, settings.entryPoint, null);
        } catch (Throwable err) {
            return null;
        }
    }

    private ProcessInstance getProcess(BufferedReader input, String target, String workingDirectory) {
        String line;
        target = target.toLowerCase();
        try {
            while ((line = input.readLine()) != null) {
                if (line.equals("")) {
                    continue;
                }
                String[] strRecordParts = line.split(",");
                String node = strRecordParts[0];
                String commandLine = strRecordParts[1];
                String exePath = strRecordParts[2];
                String processName = strRecordParts[3];
                String strPid = strRecordParts[4];

                boolean nameMatched = matchProcessName(target, processName, commandLine);
                if (nameMatched) {
                    File workingDirectoryFile = null;
                    if (workingDirectory != null) {
                        workingDirectoryFile = new File(workingDirectory);
                    } else {
                        try {
                            File exeFile = new File(exePath);
                            exeFile = new File(exeFile.getParentFile().toString());
                            exePath = exeFile.getAbsolutePath();
                            workingDirectoryFile = new File(exePath);
                        } catch (Throwable err) {
                            String errorStr = String.format("Could not get the working directory of: '%s'.", exePath);
                            log.error(errorStr, err);
                        }
                    }
                    int pid = Integer.parseInt(strPid);
                    return new LocalProcess(pid, commandLine, workingDirectoryFile);
                }
            }
        } catch (Throwable err) {
            String errorStr = String.format("Could not get process '%s'.", target);
            log.error(errorStr, err);
        }
        return null;
    }

    private boolean matchProcessName(String target, String processName, String commandLine) {
        final String JAVA_PROCESS_NAME = "java.exe";

        processName = processName.toLowerCase();
        if (processName.equals(JAVA_PROCESS_NAME)) {
            commandLine = commandLine.toLowerCase();
            return commandLine.contains(target);
        } else {
            return target.equals(processName);
        }
    }

    @Override
    public ProcessInstance createProcess(CreateProcessInstanceSettings settings) {

        try {
            String[] args = Arrays.stream(settings.args)
                    .filter(Objects::nonNull)
                    .toArray(String[]::new);

            args = Arrays.stream(args)
                    .map(String::trim)
                    .toArray(String[]::new);

            File workingDirectory = null;
            if (settings.workingDirectory != null) {
                workingDirectory = new File(settings.workingDirectory);
            } else {
                try {
                    File exeFile = new File(settings.entryPoint);
                    workingDirectory = new File(exeFile.getParentFile().toString());
                } catch (Throwable err) {
                    String errorStr = String.format("Could not get the working directory of: '%s'.", settings.entryPoint);
                    log.error(errorStr, err);
                }
            }

            return new LocalProcess(settings.entryPoint, args, workingDirectory, settings.environment);
        } catch (Throwable err) {
            String errorStr = String.format("Could not create process '%s' with entry-point: '%s'.", settings.displayName, settings.entryPoint);
            log.error(errorStr, err);
            return null;
        }
    }
}
