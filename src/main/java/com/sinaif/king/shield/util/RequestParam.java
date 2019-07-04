package com.sinaif.king.shield.util;

import java.util.TreeMap;

public class RequestParam {

	private TreeMap<String, String> header;

	private TreeMap<String, Object> body;

	public RequestParam() {
		// TODO Auto-generated constructor stub
	}

	public void setBody(TreeMap<String, Object> body) {
		this.body = body;
	}

	public TreeMap<String, Object> getBody() {
		return body;
	}

	public void setHeader(TreeMap<String, String> header) {
		this.header = header;
	}

	public TreeMap<String, String> getHeader() {
		return header;
	}
}
