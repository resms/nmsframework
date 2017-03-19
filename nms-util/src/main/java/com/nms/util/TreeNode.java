package com.nms.util;

import java.io.Serializable;
import java.util.List;

public class TreeNode<T> extends JsonMapper implements Serializable
{	
	protected T node;
	protected List<TreeNode<T>> children;
	
	public TreeNode()
	{}
	
	public TreeNode(T node)
	{
	
		super();
		this.node = node;
	}
	
	public TreeNode(T node,List<TreeNode<T>> children)
	{
	
		super();
		this.node = node;
		this.children = children;
	}

	public T getNode()
	{
	
		return node;
	}
	
	public void setNode(T node)
	{
	
		this.node = node;
	}
	
	public List<TreeNode<T>> getChildren()
	{
	
		return children;
	}
	
	public void setChildren(List<TreeNode<T>> children)
	{
	
		this.children = children;
	}
	
	
}
