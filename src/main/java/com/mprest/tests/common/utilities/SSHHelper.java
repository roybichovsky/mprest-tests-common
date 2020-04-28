package com.mprest.tests.common.utilities;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Vector;

@Slf4j
public class SSHHelper {

    private final String SFTP_KIND = "sftp";

    private final JSch ssh;
    private final String host;
    private final String user;
    private final String password;
    private final int port;

    public SSHHelper(String host, String user, String password) {
        ssh = new JSch();
        this.host = host;
        this.user = user;
        this.password = password;
        port = 22;
    }

    public SSHHelper(String host, int port, String user, String password) {
        ssh = new JSch();
        this.host = host;
        this.user = user;
        this.password = password;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    private Channel connect(String kind) {

        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        config.put("PreferredAuthentications", "password");
        config.put("ServerAliveInterval", 60);
        config.put("ServerAliveCountMax", 1440);
        try {
            Session session = ssh.getSession(user, host, port);
            session.setConfig(config);
            session.setPassword(password);
            session.connect();
            Channel channel = session.openChannel(kind);
            channel.connect();
            return channel;
        } catch (Exception ex) {
            String errorStr = String.format("The SSH client Could not connect to host '%s'.", host);
            log.error(errorStr, ex);
            return null;
        }
    }

    private ChannelSftp connectSftp() {
        return (ChannelSftp) connect(SFTP_KIND);
    }

    private void disconnect(Channel channel) {
        if (channel == null)
            return;

        Session session = null;
        try {
            session = channel.getSession();
            channel.disconnect();
            session.disconnect();
        } catch (JSchException ex) {
            String errorStr = String.format("The SSH client could not disconnect from host '%s'.", host);
            log.error(errorStr, ex);
        }
    }

    public boolean upload(String localFullFileName, String remotePath, String remoteFileName) {
        File localFile = new File(localFullFileName);
        return upload(localFile, remotePath, remoteFileName);
    }

    public boolean upload(File localFile, String remotePath, String remoteFileName) {
        ChannelSftp sftp = connectSftp();
        if (sftp == null) {
            log.error("Could not upload the file '{}' to host '{}' on directory: '{}' because the SSH client could not connect.", localFile.getAbsolutePath(), host, remotePath);
            return false;
        }

        try {
            InputStream fileStream = new FileInputStream(localFile);
            String currentDir = getWorkingDirectory(sftp);
            if (currentDir == null) {
                log.error("Could not upload the file '{}' to host '{}' on directory: '{}' because the working directory could not be detected.", localFile.getAbsolutePath(), host, remotePath);
                return false;
            }
            sftp.cd(remotePath);
            sftp.put(fileStream, remoteFileName);
            sftp.cd(currentDir);
            log.info("Uploaded the file '{}' to host '{}' on directory: '{}'.", localFile.getAbsolutePath(), host, remotePath);

            return true;
        } catch (Exception ex) {
            String errorStr = String.format("Could not upload the file '%s' to host '%s' on directory: '%s'.", localFile.getAbsolutePath(), host, remotePath);
            log.error(errorStr, ex);
            return false;
        } finally {
            disconnect(sftp);
        }
    }

    public File download(String remotePath, String remoteFileName) {
        ChannelSftp sftp = connectSftp();
        if (sftp == null) {
            log.error("Could not download the file '{}' from host '{}' on directory: '{}' because the SSH client could not connect.", remoteFileName, host, remotePath);
            return null;
        }

        try {
            String currentDir = getWorkingDirectory(sftp);
            if (currentDir == null) {
                log.error("Could not download the file '{}' from host '{}' on directory: '{}' because the working directory could not be detected.", remoteFileName, host, remotePath);
                return null;
            }

            sftp.cd(remotePath);
            InputStream downloadStream = sftp.get(remoteFileName);
            byte[] downloadBytes = IOUtils.toByteArray(downloadStream);
            sftp.cd(currentDir);
            File downloadedFile = new File(remoteFileName);
            FileUtils.writeByteArrayToFile(downloadedFile, downloadBytes);
            log.info("Downloaded the file '{}' from host '{}' on directory: '{}'.", remoteFileName, host, remotePath);
            return downloadedFile;
        } catch (Exception ex) {
            String errorStr = String.format("Could not download the file '%s' to host '%s' on directory: '%s'.", remoteFileName, host, remotePath);
            log.error(errorStr, ex);
            return null;
        } finally {
            disconnect(sftp);
        }
    }

    private String getWorkingDirectory(ChannelSftp sftp) {
        try {
            return sftp.pwd();
        } catch (Exception ex) {
            String errorStr = String.format("Could not detect the working directory on host '%s'.", host);
            log.error(errorStr, ex);
            return null;
        }
    }

    public boolean itemExists(String fullPath) {

        ChannelSftp sftp = connectSftp();
        if (sftp == null) {
            log.error("Could not check the status of item '{}' on host '{}' because the SSH client could not connect.", fullPath, host);
            return false;
        }
        try {
            SftpATTRS attrs = sftp.stat(fullPath);
            log.info("The item '{}' exists on host '{}'.", fullPath, host);
            return true;
        } catch (SftpException e) {
            log.info("The item '{}' does not exist on host '{}'.", fullPath, host);
            return false;
        } finally {
            disconnect(sftp);
        }
    }

    public boolean createDirectory(String fullPath) {

        ChannelSftp sftp = connectSftp();
        if (sftp == null) {
            log.error("Could not create the directory '{}' on host '{}' because the SSH client could not connect.", fullPath, host);
            return false;
        }
        try {
            sftp.mkdir(fullPath);
            log.info("Created the directory '{}' on host.", fullPath, host);
            return true;
        } catch (Exception ex) {
            String errorStr = String.format("Could not create the directory '%s' on host '%s'.", fullPath, host);
            log.error(errorStr, ex);
            return false;
        } finally {
            disconnect(sftp);
        }
    }

    public boolean deleteFile(String remotePath, String fileName) {

        ChannelSftp sftp = connectSftp();
        if (sftp == null) {
            log.error("Could not delete the file '{}' in directory '{}' on host '{}' because the SSH client could not connect.", fileName, remotePath, host);
            return false;
        }
        try {
            String currentDir = getWorkingDirectory(sftp);

            sftp.cd(remotePath);
            sftp.rm(fileName);
            sftp.cd(currentDir);
            log.info("Deleted the file '{}' in directory '{}' on host '{}'.", fileName, remotePath, host);
            return true;
        } catch (Exception ex) {
            String errorStr = String.format("Could not delete the file '%s' in directory '%s' on host '%s'.", fileName, remotePath, host);
            log.error(errorStr, ex);
            return false;
        } finally {
            disconnect(sftp);
        }
    }

    public boolean deleteDirectory(String fullPath) {
        ChannelSftp sftp = connectSftp();
        if (sftp == null) {
            log.error("Could not delete the directory '{}' on host '{}' because the SSH client could not connect.", fullPath, host);
            return false;
        }
        try {
            deleteDirectory(sftp, fullPath);
            log.info("Deleted the directory '{}' on host '{}'.", fullPath, host);
            return true;
        } catch (Exception ex) {
            String errorStr = String.format("Could not delete the directory '%s' on host '%s'.", fullPath, host);
            log.error(errorStr, ex);
            return false;
        } finally {
            disconnect(sftp);
        }
    }

    private void deleteDirectory(ChannelSftp sftp, String target) throws SftpException {
        boolean isDirectory = sftp.stat(target).isDir();
        if (!isDirectory) {
            sftp.rm(target);
        } else {
            sftp.cd(target);
            Vector<ChannelSftp.LsEntry> entries = sftp.ls(".");
            for (ChannelSftp.LsEntry currentEntry : entries) {
                String entryName = currentEntry.getFilename();
                if ((!entryName.equals(".")) && (!entryName.equals(".."))) {
                    deleteDirectory(sftp, currentEntry.getFilename());
                }
            }
            sftp.cd("..");
            sftp.rmdir(target);
        }
    }

    public SSHCommand executeCommand(String command) {
        try {
            SSHCommand sshCmd = new SSHCommand(host, port, user, password, command);
            sshCmd.execute();
            log.info("Executed the command '{}' on host '{}'.", command, host);
            return sshCmd;
        } catch (Exception ex) {
            String errorStr = String.format("Could not execute the command '%s' on host '%s'.", command, host);
            log.error(errorStr, ex);
            return null;
        }
    }
}