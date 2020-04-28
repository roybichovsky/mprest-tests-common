package com.mprest.tests.common.file.management;

public class FilePutSettings {
    public final byte[] bytes;
    public final String destinationHost;
    public final String destinationFile;

    public FilePutSettings(byte[] bytes, String destinationHost, String destinationFile) {
        this.bytes = bytes;
        this.destinationHost = destinationHost;
        this.destinationFile = destinationFile;
    }
}
