package com.linseven.model;

public class Message<T>
{
	private Header header;
	private T body;
	public final Header getHeader() {
		return header;
	}
	public final void setHeader(Header header) {
		this.header = header;
	}
	public final T getBody() {
		return body;
	}
	public final void setBody(T body) {
		this.body = body;
	}
	@Override
	public String toString() {
		return "Message [header=" + header.toString() + ", body=" + body + "]";
	}
	
}
