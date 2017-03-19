/**
 *
 */
package com.nms.message;

import java.io.Serializable;

/**
 * @author dxzhan
 */
public interface Header extends Serializable
{
	String getOpCode();
	void setOpCode(String opCode);
	
	int getVersion();	
	void setVersion(Integer version);
}
