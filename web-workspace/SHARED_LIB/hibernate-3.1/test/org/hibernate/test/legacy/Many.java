//$Id: Many.java,v 1.1 2004/09/26 05:18:25 oneovthafew Exp $
package org.hibernate.test.legacy;

public class Many {
	Long key;
	One one;
	private int x;
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	
	public void setKey(Long key) {
		this.key = key;
	}
	
	public Long getKey() {
		return this.key;
	}
	
	public void setOne(One one) {
		this.one = one;
	}
	
	public One getOne() {
		return this.one;
	}
}





