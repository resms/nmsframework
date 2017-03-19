package com.nms.message.result;

import com.nms.message.Body;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sam on 16-4-2.
 */
public interface ResultBody extends Body,Serializable {
    List<String> getMsgs();

    void setMsgs(List<String> msgs);

    void addMsg(String msg);
    void addMsg(int index, String msg);
    void addMsg(List<String> msgs);
    void setMsg(int index, String msg);
}
