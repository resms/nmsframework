package com.nms.message.result;

import com.nms.message.Header;

import java.io.Serializable;

/**
 * Created by sam on 16-4-2.
 */
public interface ResultHeader extends Header,Serializable {
    String getToken();
    void setToken(String token);

    String getState();
    void setState(String state);
}
