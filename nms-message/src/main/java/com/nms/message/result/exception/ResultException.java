/**
 *
 */
package com.nms.message.result.exception;

import com.nms.message.MessageException;

/**
 * @author dxzhan
 */
public class ResultException extends MessageException
{
	/**
     *
     */
	public ResultException()
	{
	
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param message
	 */
	public ResultException(String message)
	{
	
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param message
	 * @param cause
	 */
	public ResultException(String message,Throwable cause)
	{
	
		super(message,cause);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param cause
	 */
	public ResultException(Throwable cause)
	{
	
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
