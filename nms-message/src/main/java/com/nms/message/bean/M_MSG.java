package com.nms.message.bean;

import java.util.Date;

/**
 * 消息 User: dxzhan Date: 13-8-19
 */
public final class M_MSG
{
	private Integer id;// 消息ID
	private String content;// 消息正文
	private String sender;// 发送方
	private String receiver;// 接收方
	private Date sendDate;// 发送时间
	private Integer state;// 消息状态 0未读 1已读
	private Integer receiveDelete;// 接受方删除
	private Integer sendDelete;// 发送方删除
	private Integer tip;// 是否已提示
	
	public String getMsgContent()
	{
	
		return this.content;
	}
	
	public Integer getMsgId()
	{
	
		return this.id;
	}
	
	public Integer getMsgState()
	{
	
		return this.state;
	}
	
	public Integer getMsgTip()
	{
	
		return tip;
	}
	
	public Integer getReceiveDelete()
	{
	
		return this.receiveDelete;
	}
	
	public String getReceiver()
	{
	
		return receiver;
	}
	
	public Date getSendDate()
	{
	
		return this.sendDate;
	}
	
	public Integer getSendDelete()
	{
	
		return this.sendDelete;
	}
	
	public String getSender()
	{
	
		return sender;
	}
	
	public void setMsgContent(String messageContent)
	{
	
		this.content = messageContent;
	}
	
	public void setMsgId(Integer messageId)
	{
	
		this.id = messageId;
	}
	
	public void setMsgState(Integer messageState)
	{
	
		this.state = messageState;
	}
	
	public void setMsgTip(Integer messageTip)
	{
	
		this.tip = messageTip;
	}
	
	public void setReceiveDelete(Integer receiveDelete)
	{
	
		this.receiveDelete = receiveDelete;
	}
	
	public void setReceiver(String receiver)
	{
	
		this.receiver = receiver;
	}
	
	public void setSendDate(Date sendDate)
	{
	
		this.sendDate = sendDate;
	}
	
	public void setSendDelete(Integer sendDelete)
	{
	
		this.sendDelete = sendDelete;
	}
	
	public void setSender(String sender)
	{
	
		this.sender = sender;
	}
	
}