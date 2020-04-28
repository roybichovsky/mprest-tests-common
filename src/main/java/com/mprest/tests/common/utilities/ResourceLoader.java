package com.mprest.tests.common.utilities;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

public class ResourceLoader {

    public static File Load(Class<?> ownerClass, String resourceName) {
        ClassLoader classLoader = ownerClass.getClassLoader();
        URL resourceUrl = classLoader.getResource(resourceName);
        String resourceFileName = resourceUrl.getFile();
        try {
            resourceFileName = URLDecoder.decode(resourceFileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        File resourceFile = new File(resourceFileName);
        return resourceFile;
    }
}