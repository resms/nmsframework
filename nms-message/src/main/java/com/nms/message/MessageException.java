/**
 *
 */
package com.nms.message;

/**
 * @author dxzhan
 */
public class MessageException extends Exception
{
	/**
     *
     */
	public MessageException()
	{

		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public MessageException(String message)
	{

		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MessageException(String message, Throwable cause)
	{

		super(message,cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public MessageException(Throwable cause)
	{
	
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
