package com.nms.util.mail;

/**
 * Copyright (c) 2008,  All rights reserved
 * <p>send mail throuh smtp </p>
 * <p>
 * <p/>
 * </p>
 *
 * @author jeff
 * @version 1.0 2008/12/16
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class SendMail
{
	
	static Logger logger = LoggerFactory.getLogger(SendMail.class);
	
	public static enum EncryptionTypes
	{
		Default,TLS,SSL
	}
	
	/* message for mail */
	private MimeMessage mimeMsg;
	/* session of send mail */
	private Session session;
	/* properties of system */
	private transient Properties props;
	/* user for send mail */
	private String username = "";
	/* password for send mail */
	private String password = "";
	/* object of Multipart,include the mail context */
	private Multipart multipart;
	/* smtp verify password */
	private boolean isAuth = false;
	
	private int encryptionType;
	
	int port = 25;
	
	public SendMail(String smtp,int port)
	{
	
		if(smtp == null || smtp.length() == 0)
		{
			logger.error("the parameter 'smtp' is null");
			return;
		}
		setSmtpHost(smtp,port);
	}
	
	public SendMail(String smtp,int port,String userName,String password)
	{
	
		if(smtp == null || smtp.length() == 0)
		{
			logger.error("the parameter 'smtp' is null");
			return;
		}
		if(userName == null || userName.length() == 0)
		{
			logger.error("the parameter 'userName' is null");
			return;
		}
		setSmtpHost(smtp,port);
		this.username = userName;
		this.password = password;
	}
	
	public SendMail(String smtp,int port,int transtype)
	{
	
		if(smtp == null || smtp.length() == 0)
		{
			logger.error("the parameter 'smtp' is null");
			return;
		}
		setSmtpHost(smtp,port,transtype);
	}
	
	public SendMail(String smtp,int port,String userName,String password,int transtype)
	{
	
		if(smtp == null || smtp.length() == 0)
		{
			logger.error("the parameter 'smtp' is null");
			return;
		}
		if(userName == null || userName.length() == 0)
		{
			logger.error("the parameter 'userName' is null");
			return;
		}
		setSmtpHost(smtp,port,transtype);
		this.username = userName;
		this.password = password;
	}
	
	private void setSmtpHost(String hostName,int port)
	{
	
		try
		{
			if(props == null)
				props = System.getProperties();
			
			setSmtpHost(hostName,port, EncryptionTypes.Default.ordinal());
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * set smtp server and mail transger protocol type
	 *
	 * @param hostName
	 *            name of smtp server
	 * @return void
	 * @throws
	 * @see
	 */
	private void setSmtpHost(String hostName,int port,int transtype)
	{
	
		try
		{
			if(props == null)
				props = System.getProperties();
			
			logger.info("transportype=" + transtype);
			
			props.put("mail.transport.protocol","smtp");
			props.put("mail.smtp.host",hostName);
			props.put("mail.smtp.port",port);
			props.put("mail.smtp.auth",String.valueOf(isAuth));
			// props.put("mail.smtp.debug", "true");
			props.put("mail.smtp.debug","false");
			if(transtype == EncryptionTypes.TLS.ordinal())
			{// transport mail by the TLS
				logger.info("Use TLS");
				props.put("mail.smtp.starttls.enable","true");
				
			}
			else if(transtype == EncryptionTypes.SSL.ordinal())
			{// transport mail by the SSL
				logger.info("Use SSL");
				props.put("mail.smtp.socketFactory.port",port);
				props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
				
			}
			else
			{// transport mail by the Default
				logger.info("Default");
			}
			
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * create the session and message object for mail transfer
	 *
	 * @return boolean ture=success, false=fail
	 * @throws
	 * @see
	 */
	public boolean createMimeMessage()
	{
	
		try
		{
			if(isAuth)
			{
				SmtpAuthenticator auth = new SmtpAuthenticator(username,password);
				session = Session.getInstance(props,auth);
			}
			else
			{
				session = Session.getInstance(props,null);
			}
			
			// session.setDebug(true);
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
			return false;
		}
		
		try
		{
			mimeMsg = new MimeMessage(session);
			multipart = new MimeMultipart();
			return true;
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
			return false;
		}
	}
	
	/**
	 * set the authenticate status
	 *
	 * @param isAuth
	 *            authenticate status
	 * @return void
	 * @throws
	 * @see
	 */
	public void setNeedAuth(boolean isAuth)
	{
	
		this.isAuth = isAuth;
		
		if(props == null)
			props = System.getProperties();
		
		if(isAuth)
		{
			props.put("mail.smtp.auth","true");
		}
		else
		{
			props.put("mail.smtp.auth","false");
		}
	}
	
	/**
	 * set user name and password for mail server to authenticate
	 *
	 * @param name
	 *            user name for sending mail
	 * @param pass
	 *            user password for sending mail
	 * @return void
	 * @throws
	 * @see
	 */
	public void setNamePass(String name,String pass)
	{
	
		username = name;
		password = pass;
	}
	
	/**
	 * set the subject for mail
	 *
	 * @param mailSubject
	 *            mail subject
	 * @return boolean ture=success, false=fail
	 * @throws
	 * @see
	 */
	public boolean setSubject(String mailSubject)
	{
	
		try
		{
			mimeMsg.setSubject(mailSubject,"UTF-8");
			return true;
		}
		catch(Exception e)
		{
			logger.error("set mail subject fail");
			logger.error(e.getMessage());
			return false;
		}
	}
	
	/**
	 * set the conttent for mail
	 *
	 * @param content
	 *            mail content
	 * @return boolean ture=success, false=fail
	 * @throws
	 * @see
	 */
	public boolean setContent(String content)
	{
	
		try
		{
			BodyPart body = new MimeBodyPart();
			body.setContent("<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\">"
					+ content,"text/html;charset=utf-8");
			multipart.addBodyPart(body);
			return true;
		}
		catch(Exception e)
		{
			logger.error("set mail content fail");
			logger.error(e.getMessage());
			return false;
		}
	}
	
	/**
	 * set the affixed file
	 *
	 * @param fileName
	 *            affixed file name
	 * @return boolean ture=success, false=fail
	 * @throws
	 * @see
	 */
	public boolean addFileAffix(String fileName)
	{
	
		if(fileName == null || fileName.length() == 0)
		{
			return true;
		}
		
		try
		{
			BodyPart body = new MimeBodyPart();
			FileDataSource fileds = new FileDataSource(fileName);
			body.setDataHandler(new DataHandler(fileds));
			body.setFileName(fileds.getName());
			multipart.addBodyPart(body);
			
			return true;
		}
		catch(Exception e)
		{
			logger.error("set mail affix file fail");
			logger.error(e.getMessage());
			return false;
		}
	}
	
	/**
	 * set the mail sender
	 *
	 * @param from
	 *            mail address of sender
	 * @return boolean ture=success, false=fail
	 * @throws
	 * @see
	 */
	public boolean setFrom(String from)
	{
	
		try
		{
			mimeMsg.setFrom(new InternetAddress(from));
			return true;
		}
		catch(Exception e)
		{
			logger.error("set mail send user fail");
			logger.error(e.getMessage());
			return false;
		}
	}
	
	/**
	 * set the mail receiver
	 *
	 * @param to
	 *            mail address of receiver
	 * @return boolean ture=success, false=fail
	 * @throws
	 * @see
	 */
	public boolean setTo(String to)
	{
	
		if(to == null)
			return false;
		
		try
		{
			mimeMsg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
			return true;
		}
		catch(Exception e)
		{
			logger.error("set mail reciever user fail");
			logger.error(e.getMessage());
			return false;
		}
	}
	
	/**
	 * set the mail receiver who copy to
	 *
	 * @param copyTo
	 *            mail address of receiver
	 * @return boolean ture=success, false=fail
	 * @throws
	 * @see
	 */
	public boolean setCopyTo(String copyTo)
	{
	
		if(copyTo == null)
			return true;
		try
		{
			mimeMsg.setRecipients(Message.RecipientType.CC,(Address[])InternetAddress.parse(copyTo));
			return true;
		}
		catch(Exception e)
		{
			logger.error("set mail copyto user fail");
			logger.error(e.getMessage());
			return false;
		}
	}
	
	/**
	 * send mail
	 *
	 * @return boolean ture=success, false=fail
	 * @throws
	 * @see
	 */
	private boolean send()
	{
	
		try
		{
			mimeMsg.setContent(multipart);
			mimeMsg.setSentDate(new Date());
			mimeMsg.saveChanges();
			
			Transport transport = session.getTransport("smtp");
			/*
			 * //transport.connect((String)props.get("mail.smtp.host"),
			 * username, password); logger.info("mail.smtp.port = "
			 * +props.get("mail.smtp.port") );
			 * transport.connect((String)props.get
			 * ("mail.smtp.host"),Integer.parseInt
			 * (props.get("mail.smtp.port")+""), username, password);
			 * logger.info("send mail success"); //transport.connect();
			 * transport.sendMessage(mimeMsg,
			 * mimeMsg.getRecipients(Message.RecipientType.TO));
			 */
			
			if(isAuth)
			{
				logger.info("isAuth:" + isAuth);
				// transport.connect((String)props.get("mail.smtp.host"),
				// username, password);
				logger.info("mail.smtp.port = " + props.get("mail.smtp.port"));
				transport.connect((String)props.get("mail.smtp.host"),
						Integer.parseInt(props.get("mail.smtp.port") + ""),username,password);
				logger.info("send mail success");
				
				// transport.connect();
				transport.sendMessage(mimeMsg,mimeMsg.getRecipients(Message.RecipientType.TO));
			}
			else
			{
				logger.info("isAuth:" + isAuth + ",false!");
				transport.send(mimeMsg);
			}
			transport.close();
			// Transport.send(mimeMsg);
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
			logger.error("send mail fail");
			// e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean send(List<String> sendTos)
	{
	
		try
		{
			mimeMsg.setContent(multipart);
			mimeMsg.setSentDate(new Date());
			mimeMsg.saveChanges();
			
			Transport transport = session.getTransport("smtp");
			/*
			 * //transport.connect((String)props.get("mail.smtp.host"),
			 * username, password); logger.info("mail.smtp.port = "
			 * +props.get("mail.smtp.port") );
			 * transport.connect((String)props.get
			 * ("mail.smtp.host"),Integer.parseInt
			 * (props.get("mail.smtp.port")+""), username, password);
			 * logger.info("send mail success"); //transport.connect();
			 * transport.sendMessage(mimeMsg,
			 * mimeMsg.getRecipients(Message.RecipientType.TO));
			 */
			
			if(isAuth)
			{
				logger.info("isAuth:" + isAuth);
				// transport.connect((String)props.get("mail.smtp.host"),
				// username, password);
				logger.info("mail.smtp.port = " + props.get("mail.smtp.port"));
				transport.connect((String)props.get("mail.smtp.host"),
						Integer.parseInt(props.get("mail.smtp.port") + ""),username,password);
				logger.info("send mail success");
				
				// transport.connect();
				final int num = sendTos.size();
				InternetAddress[] addresses = new InternetAddress[num];
				for(int i = 0;i < num;i++)
				{
					addresses[i] = new InternetAddress(sendTos.get(i));
				}
				mimeMsg.setRecipients(MimeMessage.RecipientType.TO,addresses);
				
				transport.sendMessage(mimeMsg,mimeMsg.getRecipients(Message.RecipientType.TO));
			}
			else
			{
				logger.info("isAuth:" + isAuth + ",false!");
				transport.send(mimeMsg);
			}
			transport.close();
			// Transport.send(mimeMsg);
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
			logger.error("send mail fail");
			// e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * send mail ,set the message of mail
	 *
	 * @param subject
	 *            mail subject
	 * @param content
	 *            mail content
	 * @param affixfile
	 *            mail affixed file
	 * @param sendFrom
	 *            mail address of sender
	 * @param sendTo
	 *            mail address of receiver
	 * @param sendTo
	 *            mail address of copy user
	 * @return boolean ture=success, false=fail
	 * @throws
	 * @see
	 */
	public boolean send(String subject,String content,String affixFile,String sendFrom,String sendTo,
			String copyTo)
	{
	
		createMimeMessage();
		if(false == setSubject(subject))
		{
			return false;
		}
		if(false == setContent(content))
		{
			return false;
		}
		if(false == addFileAffix(affixFile))
		{
			return false;
		}
		if(false == setFrom(sendFrom))
		{
			return false;
		}
		if(false == setTo(sendTo))
		{
			return false;
		}
		if(false == setCopyTo(copyTo))
		{
			return false;
		}
		if(false == send())
		{
			return false;
		}
		
		return true;
	}
	
	public boolean smtpSend(String subject,String content,String affixFile,String sendFrom,String sendTo,
			String copyTo)
	{
	
		Socket socket = null;
		DataOutputStream os = null;
		BufferedReader is = null;
		String answer = null;
		
		String hostname = "";
		String port = "";
		String userName = username;
		String userpass = password;
		String newtranstype = "0";
		
		try
		{
			
			if(props != null)
			{
				hostname = props.getProperty("mail.smtp.host");
				port = props.getProperty("mail.smtp.port");
				
			}
			else
			{	
				
			}
			
			if(newtranstype != "" && newtranstype != null
					&& (newtranstype.equals("1") || newtranstype.equals("2")))
			{
				SSLSocketFactory factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
				
				socket = factory.createSocket(hostname,Integer.parseInt(port));
			}
			else
			{
				socket = new Socket(hostname,Integer.parseInt(port));
			}
			
			is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			os = new DataOutputStream(socket.getOutputStream());
			
			os.writeBytes("HELO " + hostname + "\r\n");
			while((answer = is.readLine()) != null)
			{
				logger.info("Server:" + answer);
				if(answer.indexOf("220") != -1)
				{
					break;
				}
			}
			
			System.out.println("check service....");
			// os.writeBytes("AUTH LOGIN\r\n");
			while((answer = is.readLine()) != null)
			{
				if(answer.indexOf("250") == -1)
				{
					break;
				}
				logger.info("Server:" + answer);
			}
			
			logger.info("check usename and password....");
			logger.info("Server:" + answer);
			
			os.writeBytes(userName + "\r\n");
			os.writeBytes(userpass + "\r\n");
			
			while((answer = is.readLine()) != null)
			{
				
				logger.info("Server:" + answer);
				if(answer.indexOf("235") != -1)
				{
					System.out.println("验证成功");
					break;
				}
				else if(answer.indexOf("334") == -1)
				{
					logger.info("验证失败");
					os.close();
					is.close();
					socket.close();
					System.exit(0);
				}
			}
			
			System.out.println("start send mail....");
			os.writeBytes("MAIL From: " + sendFrom + "\r\n");
			os.writeBytes("RCPT To: " + sendTo + "\r\n");
			os.writeBytes("RCPT To: " + copyTo + "\r\n");
			
			os.writeBytes("DATA\r\n");
			while((answer = is.readLine()) != null)
			{
				
				if(answer.indexOf("354") != -1)
				{
					break;
				}
				System.out.println("Server:" + answer);
			}
			
			System.out.println("Server:" + answer);
			System.out.println("正在发送邮件内容....");
			
			os.writeBytes("From: " + sendFrom + "\r\n");
			os.writeBytes("To: " + sendTo + "," + copyTo + "\r\n");
			os.writeBytes("Subject: " + subject + "\r\n");
			os.writeBytes("Content-Type: text/html\r\n");
			os.writeBytes(content);
			
			os.writeBytes("\r\n.\r\n");
			
			while((answer = is.readLine()) != null)
			{
				System.out.println("Server:" + answer);
				if(answer.indexOf("250") != -1)
				{
					break;
				}
			}
			
			os.writeBytes("QUIT\r\n");
			
			while((answer = is.readLine()) != null)
			{
				System.out.println("Server:" + answer);
				if(answer.indexOf("221") != -1)
				{
					System.out.println("邮件发送成功,退出邮箱！");
					break;
				}
			}
			
			os.close();
			is.close();
			socket.close();
			
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
			return false;
		}
		
		return true;
	}
}
