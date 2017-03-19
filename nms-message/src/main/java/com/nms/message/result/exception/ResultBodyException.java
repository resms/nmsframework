/**
 *
 */
package com.nms.message.result.exception;

/**
 * @author dxzhan
 */
public class ResultBodyException extends ResultException
{
	/**
     *
     */
	public ResultBodyException()
	{
	
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param message
	 */
	public ResultBodyException(String message)
	{
	
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param message
	 * @param cause
	 */
	public ResultBodyException(String message,Throwable cause)
	{
	
		super(message,cause);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param cause
	 */
	public ResultBodyException(Throwable cause)
	{
	
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
