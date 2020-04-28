package com.mprest.tests.common.file.management;

public class FileCopySettings {
    public final String sourceHost;
    public final String sourceFile;

    public final String destinationHost;
    public final String destinationFile;

    public FileCopySettings(String sourceHost, String sourceFile, String destinationHost, String destinationFile) {
        this.sourceHost = sourceHost;
        this.sourceFile = sourceFile;
        this.destinationHost = destinationHost;
        this.destinationFile = destinationFile;
    }

    // Used for copying a local source file
    public FileCopySettings(String sourceFile, String destinationHost, String destinationFile) {
        this(null, sourceFile, destinationHost, destinationFile);
    }
}
