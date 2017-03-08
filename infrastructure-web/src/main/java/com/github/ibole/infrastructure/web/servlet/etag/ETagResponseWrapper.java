package com.github.ibole.infrastructure.web.servlet.etag;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ETagResponseWrapper extends HttpServletResponseWrapper {
	private HttpServletResponse response = null;
	private ServletOutputStream stream = null;
	private PrintWriter writer = null;
	private OutputStream buffer = null;

	public ETagResponseWrapper(HttpServletResponse response, OutputStream buffer) {
		super(response);
		this.response = response;
		this.buffer = buffer;
	}

	public ServletOutputStream getOutputStream() throws IOException {
		if (stream == null)
			stream = new ETagResponseStream(response, buffer);
		
		return stream;
	}

	public PrintWriter getWriter() throws IOException {
		if (writer == null)
			writer = new PrintWriter(new OutputStreamWriter(getOutputStream(), "UTF-8"));
		
		return writer;
	}
	
	public void flushBuffer() throws IOException {
		stream.flush();
	}
}
