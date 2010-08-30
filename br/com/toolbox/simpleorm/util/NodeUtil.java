package br.com.toolbox.simpleorm.util;

import java.util.ArrayList;
import java.util.List;

public class NodeUtil {

	
	private Object value;
	private List<NodeUtil> children = new ArrayList<NodeUtil>();
	private NodeUtil parent = null;
	
	public NodeUtil(){}
	
	
	
	public NodeUtil(Object value, NodeUtil parent) {
		super();
		this.value = value;
		this.parent = parent;
	}



	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public List<NodeUtil> getChildren() {
		return children;
	}
	public void setChildren(List<NodeUtil> children) {
		this.children = children;
	}
	
	
	
}
