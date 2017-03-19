package com.nms.message.bus;

import com.nms.message.Header;

/**
 * Created by sam on 16-4-2.
 */
public interface BusHeader extends Header{
    String getFrom();
    void setFrom(String form);
    String getTo();
    void setTo(String to);
}
