package com.nms.message.auth;

import com.nms.message.Header;

/**
 * Created by sam on 16-4-2.
 */
public interface AuthHeader extends Header {
    String getToken();
    void setToken(String token);

    String getState();
    void setState(String state);
}
