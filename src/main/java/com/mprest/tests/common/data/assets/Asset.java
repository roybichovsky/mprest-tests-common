package com.mprest.tests.common.data.assets;

import java.io.Serializable;

public interface Asset extends Serializable {
    String getId();
    void setId(String id);

    String getType();
    void setType(String type);

    String getVendor();
    void setVendor(String vendor);

    String getName();
    void setName(String name);

    String getOlsonTimeZone();
    void setOlsonTimeZone(String olsonTimeZone);
}
