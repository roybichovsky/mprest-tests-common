package com.mprest.tests.common.file.management;

public interface FileProvider {
    boolean copyTo(FileCopySettings settings);
    boolean copyFrom(FileCopySettings settings);
    boolean delete(FileDeletionSettings settings);
    byte[] get(FileGetSettings settings);
}
