/**
 *
 */
package com.nms.message;

import java.io.Serializable;

/**
 * 消息头接口
 * @author dxzhan
 */
public interface Header extends Serializable
{
    /**
     * 获得操作码
     * @return
     */
	String getOpCode();

    /**
     * 设置操作码
     * @param opCode
     */
	void setOpCode(String opCode);

    /**
     * 获得版本号
     * @return
     */
	int getVersion();

    /**
     * 设置版本号
     * @param version
     */
	void setVersion(Integer version);
}
