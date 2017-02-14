package io.ibole.infrastructure.web.servlet.etag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ETagContentFilter implements Filter {
  
    private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest servletRequest = (HttpServletRequest) req;
		HttpServletResponse servletResponse = (HttpServletResponse) res;
		
		String id = servletRequest.getRequestURI();
		String queryString = servletRequest.getQueryString();
		if (queryString != null) 
			id += queryString;
		
		logger.debug(id);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ETagResponseWrapper wrappedResponse = new ETagResponseWrapper(servletResponse, baos);
		chain.doFilter(servletRequest, wrappedResponse);
				
		byte[] bytes = baos.toByteArray();
		
		String token = '"' + ETagComputeUtils.getMd5Digest(queryString.getBytes()) + '"';
		servletResponse.setHeader("ETag", token); // always store the ETag in the header
		
		String previousToken = servletRequest.getHeader("If-None-Match");
		if (previousToken != null && previousToken.equals(token)) { // compare previous token with the current one
		    logger.debug("ETag match: returning 304 Not Modified.");
			servletResponse.sendError(HttpServletResponse.SC_NOT_MODIFIED);
			servletResponse.setHeader("Last-Modified", servletRequest.getHeader("If-Modified-Since")); // re-use original last modified time
		} else  {
			// first time through - set last modified time to now 
		    Calendar cal = Calendar.getInstance();
		    cal.set(Calendar.MILLISECOND, 0);
		    Date lastModified = cal.getTime();
			servletResponse.setDateHeader("Last-Modified", lastModified.getTime());
				
			logger.debug("Writing body content.");
		    servletResponse.setContentLength(bytes.length);
		    ServletOutputStream sos = servletResponse.getOutputStream();
			sos.write(bytes);
			sos.flush();
			sos.close();
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}
	
	public void destroy() {
	}
}
