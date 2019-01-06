/**
 * 
 */
package com.imooc.security.browser.support;

/**
 * @author zhailiang
 *	json数据 简单的包装类
 */
public class SimpleResponse {
	
	public SimpleResponse(Object content){
		this.content = content;
	}
	
	private Object content;

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}
	
}
