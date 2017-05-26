package com.nms.util.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SmtpAuthenticator extends Authenticator
{
	private String name;
	private String password;
	
	public SmtpAuthenticator(String name,String password)
	{
	
		this.name = name;
		this.password = password;
	}
	
	public PasswordAuthentication getPasswordAuthentication()
	{
	
		return new PasswordAuthentication(name,password);
	}
	
	public String getName()
	{
	
		return name;
	}
	
	public String getPassword()
	{
	
		return password;
	}
}
