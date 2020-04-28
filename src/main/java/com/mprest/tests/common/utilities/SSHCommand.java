package com.mprest.tests.common.utilities;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class SSHCommand {

    private final String EXEC_KIND = "exec";

    private final JSch ssh;
    private final String host;
    private final String user;
    private final String password;
    private final int port;
    private final String cmd;
    private final ChannelExec channel;

    private InputStream inputStream;

    SSHCommand(String host, int port, String user, String password, String cmd) {
        ssh = new JSch();
        this.host = host;
        this.user = user;
        this.password = password;
        this.port = port;
        this.cmd = cmd;
        channel = getChannel();
    }

    private ChannelExec getChannel() {
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        config.put("PreferredAuthentications", "password");
        try {
            Session session = ssh.getSession(user, host, port);
            session.setConfig(config);
            session.setPassword(password);
            session.connect();
            ChannelExec channel = (ChannelExec) session.openChannel(EXEC_KIND);

            return channel;
        } catch (Exception ex) {
            String errorStr = String.format("The SSH client Could not connect to host '%s'.", host);
            log.error(errorStr, ex);
            return null;
        }
    }

    void execute() throws JSchException, IOException {
        inputStream = channel.getInputStream();
        channel.setCommand(cmd);
        channel.connect();
    }

    public void terminate() {
        disconnect(channel);
    }

    private void disconnect(Channel channel) {
        if (channel == null)
            return;

        channel.disconnect();
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}