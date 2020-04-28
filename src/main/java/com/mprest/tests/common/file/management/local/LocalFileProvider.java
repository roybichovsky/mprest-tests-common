package com.mprest.tests.common.file.management.local;

import com.mprest.tests.common.file.management.*;
import com.mprest.tests.common.utilities.PathHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Slf4j
public class LocalFileProvider implements FileProvider {

    public boolean copyFrom(FileCopySettings settings) {
        try {
            log.debug("Copying '{}' from '{}'", settings.sourceFile, settings.destinationFile);
            Path sourceFilePath = PathHelper.combine(cleanPath(settings.sourceFile));
            Path destinationFilePath = PathHelper.combine(cleanPath(settings.destinationFile));
            Files.copy(sourceFilePath, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            log.debug("Finished copying '{}' from '{}'", settings.sourceFile, settings.destinationFile);
            return true;
        } catch (Throwable err) {
            String errorStr = String.format("Could not copy '%s' from '%s'", settings.sourceFile, settings.destinationFile);
            log.error(errorStr, err);
            return false;
        }
    }

    public boolean copyTo(FileCopySettings settings) {
        try {
            log.debug("Copying '{}' to '{}'", settings.sourceFile, settings.destinationFile);
            Path sourceFilePath = PathHelper.combine(cleanPath(settings.sourceFile));
            Path destinationFilePath = PathHelper.combine(cleanPath(settings.destinationFile));
            Files.copy(sourceFilePath, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            log.debug("Finished copying '{}' to '{}'", sourceFilePath, destinationFilePath);
            return true;
        } catch (Throwable err) {
            String errorStr = String.format("Could not copy '%s' to '%s'...", settings.sourceFile, settings.destinationFile);
            log.error(errorStr, err);
            return false;
        }
    }

    @Override
    public boolean delete(FileDeletionSettings settings) {
        try {
            log.debug("Deleting '{}'", settings.sourceFile);
            Path sourceFilePath = PathHelper.combine(cleanPath(settings.sourceFile));
            if (!Files.exists(sourceFilePath)) {
                log.info("Ignored deleting non-existing file '{}'", sourceFilePath);
                return true;
            }
            Files.delete(sourceFilePath);
            log.debug("Finished deleting '{}'", sourceFilePath);
            return true;
        } catch (Throwable err) {
            String errorStr = String.format("Could not delete '%s'", settings.sourceFile);
            log.error(errorStr, err);
            return false;
        }
    }

    @Override
    public byte[] get(FileGetSettings settings) {
        try {
            log.debug("Getting '{}'", settings.sourceFile);
            Path sourceFilePath = PathHelper.combine(cleanPath(settings.sourceFile));
            File sourceFile = new File(sourceFilePath.toString());
            FileInputStream sourceInputStream = new FileInputStream(sourceFile);
            return IOUtils.toByteArray(sourceInputStream);
        } catch (Throwable err) {
            log.warn("Could not get file: '{}'", err.getMessage());
            return null;
        }
    }

    private String cleanPath(String path) {
        if (path.startsWith(FileManagement.LOCAL_SCHEME)) {
            path = path.substring(FileManagement.LOCAL_SCHEME.length() + 1);
            if (path.charAt(1) == '\\') {
                path = path.substring(0, 1) + ":" + path.substring(1);
            }
        }

        return path;
    }
}
