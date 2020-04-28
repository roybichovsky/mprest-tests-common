package com.mprest.tests.common.file.management;

import com.mprest.tests.common.file.management.docker.DockerizedFileProvider;
import com.mprest.tests.common.file.management.local.LocalFileProvider;
import com.mprest.tests.common.utilities.PathHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class FileManagement {

    public static final String LOCAL_SCHEME = "local";
    public static final String DOCKER_SCHEME = "docker";
    private static final String NULL_PARAM = "<null>";
    private static final String TEMP_DIRECTORY_NAME = "temp";
    private FileManagementConfiguration config;
    private java.io.File tempDirectory;

    public FileManagement(FileManagementConfiguration fileManagementConfiguration) {
        config = fileManagementConfiguration;
    }

    public boolean copy(FileCopySettings settings) {
        String sourceHost = (settings.sourceHost != null) ? settings.sourceHost : NULL_PARAM;
        String sourceFile = (settings.sourceHost != null) ? settings.sourceFile : NULL_PARAM;
        String destinationHost = (settings.sourceHost != null) ? settings.sourceHost : NULL_PARAM;
        String destinationFile = (settings.sourceHost != null) ? settings.sourceFile : NULL_PARAM;

        log.debug("Copying '{}' from host '{}' to '{}' on host '{}'", sourceFile, sourceHost, destinationFile, destinationHost);
        java.io.File tempFile = null;
        try {
            FileProvider sourceProvider = getProvider(settings.sourceFile);
            FileProvider destProvider = getProvider(settings.destinationFile);

            if ((sourceProvider == null) || (destProvider == null)) {
                log.error("Could not copy '{}' from host '{}' to '{}' on host '{}' because the file providers could not be resolved", sourceFile, sourceHost, destinationFile, destinationHost);
                return false;
            }

            boolean tempDirExists = validateTempDirectory();
            if (!tempDirExists) {
                log.error("Could not copy '{}' from host '{}' to '{}' on host '{}' because the temporary directory could not be found or created", sourceFile, sourceHost, destinationFile, destinationHost);
                return false;
            }

            tempFile = java.io.File.createTempFile("tests.", null, tempDirectory);
            log.debug("Created a temporary file: '{}'", tempFile.getAbsolutePath());
            FileCopySettings tempSettings = new FileCopySettings(settings.sourceHost, settings.sourceFile, null, tempFile.getAbsolutePath());
            boolean copied = sourceProvider.copyFrom(tempSettings);
            if (!copied) {
                log.error("Could not copy '{}' from host '{}' to '{}' on host '{}' due to an error in the file provider", sourceFile, sourceHost, destinationFile, destinationHost);
                return false;
            }
            settings = new FileCopySettings(null, tempFile.getAbsolutePath(), settings.destinationHost, settings.destinationFile);
            copied = destProvider.copyTo(settings);
            return copied;
        } catch (Throwable err) {
            String errorStr = String.format("Could not copy '%s' from host '%s' to '%s' on host '%s'", sourceFile, sourceHost, destinationFile, destinationHost);
            log.error(errorStr, err);
            return false;
        } finally {
            if (tempFile != null) {
                log.debug("Deleting the temporary file: '{}'", tempFile.getAbsolutePath());
                tempFile.delete();
            }
        }
    }

    public boolean delete(FileDeletionSettings settings) {
        String sourceHost = (settings.sourceHost != null) ? settings.sourceHost : NULL_PARAM;
        String sourceFile = (settings.sourceHost != null) ? settings.sourceFile : NULL_PARAM;
        log.debug("Deleting '{}' from host '{}'", sourceFile, sourceHost);
        FileProvider sourceProvider = getProvider(settings.sourceFile);
        if (sourceProvider == null) {
            log.debug("Could not delete '{}' from host '{}' because the file provider could not be resolved", sourceFile, sourceHost);
            return false;
        }

        boolean deleted = sourceProvider.delete(settings);
        if (!deleted) {
            log.error("Could not delete '{}' from host '{}' due to an error in the file provider", sourceFile, sourceHost);
            return false;
        }
        log.debug("Finished deleting '{}' from host '{}'", sourceFile, sourceHost);
        return true;
    }

    public byte[] get(FileGetSettings settings) {
        String sourceHost = (settings.sourceHost != null) ? settings.sourceHost : NULL_PARAM;
        String sourceFile = (settings.sourceHost != null) ? settings.sourceFile : NULL_PARAM;
        log.debug("Getting '{}' from host '{}'", sourceFile, sourceHost);
        FileProvider sourceProvider = getProvider(settings.sourceFile);
        if (sourceProvider == null) {
            log.error("Could not get '{}' from host '{}' because the file provider could not be resolved", sourceFile, sourceHost);
            return null;
        }
        return sourceProvider.get(settings);
    }

    public boolean put(FilePutSettings settings) {
        String destinationHost = (settings.destinationHost != null) ? settings.destinationHost : NULL_PARAM;
        String destinationFile = (settings.destinationFile != null) ? settings.destinationFile : NULL_PARAM;
        log.debug("Putting '{}' on host '{}'.", destinationFile, destinationHost);
        java.io.File tempFile = null;

        try {
            FileProvider sourceProvider = getProvider(settings.destinationFile);
            if (sourceProvider == null) {
                log.error("Could not put '{}' on host '{}' because the file provider could not be resolved", destinationFile, destinationHost);
                return false;
            }

            boolean tempDirExists = validateTempDirectory();
            if (!tempDirExists) {
                log.error("Could not put '{}' on host '{}' because the temporary directory could not be found or created", destinationFile, destinationHost);
                return false;
            }

            tempFile = java.io.File.createTempFile("tests.", null, tempDirectory);
            log.debug("Created a temporary file: '{}'", tempFile.getAbsolutePath());
            FileUtils.writeByteArrayToFile(tempFile, settings.bytes);
            FileCopySettings tempSettings = new FileCopySettings(null, tempFile.getAbsolutePath(), settings.destinationHost, settings.destinationFile);
            boolean copied = sourceProvider.copyTo(tempSettings);
            if (!copied) {
                log.error("Could not put '{}' on host '{}' due to an error in the file provider", destinationFile, destinationHost);
                return false;
            }

            log.debug("Finished putting '{}' on host '{}'", destinationFile, destinationHost);
            return true;
        } catch (Throwable err) {
            String errorStr = String.format("Could not put '%s' on host '%s'", destinationFile, destinationHost);
            log.error(errorStr, err);
            return false;
        } finally {
            if (tempFile != null) {
                log.debug("Deleting the temporary file: '{}'", tempFile.getAbsolutePath());
                tempFile.delete();
            }
        }
    }

    private FileProvider getProvider(String path) {
        try {

            if (path.startsWith(LOCAL_SCHEME)) {
                return new LocalFileProvider();
            } else if (path.startsWith(DOCKER_SCHEME)) {
                return new DockerizedFileProvider(config.getDocker());
            } else {
                return new LocalFileProvider();
            }
        } catch (Throwable err) {
            String errorStr = String.format("Could not find the file provider for the path '%s'.", path);
            log.error(errorStr, err);
            return null;
        }
    }

    private boolean validateTempDirectory() {
        Path tempPath = PathHelper.combine(System.getProperty("user.dir"), TEMP_DIRECTORY_NAME);
        try {
            if (Files.exists(tempPath))
                return true;

            Path dirPath = Files.createDirectory(tempPath);
            tempDirectory = new java.io.File(dirPath.toString());
            return true;
        } catch (Throwable err) {
            String errorStr = String.format("Could not create the temporary folder '%s'.", tempPath.toString());
            log.error(errorStr, err);
            return false;
        }
    }

}
