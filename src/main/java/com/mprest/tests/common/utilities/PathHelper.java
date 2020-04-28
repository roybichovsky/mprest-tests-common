package com.mprest.tests.common.utilities;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class PathHelper {
    public static Path combine(String ... pieces){
        return Arrays.stream(pieces).reduce(
                /* identity    */ Paths.get(""),
                /* accumulator */ Path::resolve,
                /* combiner    */ Path::resolve);
    }
}
