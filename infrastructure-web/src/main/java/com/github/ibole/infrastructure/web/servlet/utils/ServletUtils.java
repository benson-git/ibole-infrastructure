package com.github.ibole.infrastructure.web.servlet.utils;

import com.google.common.net.HttpHeaders;

import org.apache.commons.lang3.Validate;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Utils.
 * 
 */
public class ServletUtils
{

    // -- 常用数值定义 --//
    public static final long ONE_YEAR_SECONDS = 60 * 60 * 24 * 365;
    
    public static final String SCHEME_HTTP = "http";

    public static final String SCHEME_HTTPS = "https";

    public static final int DEFAULT_HTTP_PORT = 80;

    public static final int DEFAULT_HTTPS_PORT = 443;
    
    private ServletUtils()
    {
        // utility class
    }
    
    /**
     * Ensures that the {@link Throwable#getCause()} is correctly set on the 
     * given exception.
     * <p>
     * A <code>ServletException</code> has an embedded <code>root</code> cause
     * which may not be used to set the cause introduced in the JDK 1.4.
     * 
     * @param pException the exception
     */
    public static void fixServletException(ServletException pException)
    {
        if (pException.getCause() == null)
        {
            Throwable cause = pException.getRootCause();
            if (cause != null)
            {
                pException.initCause(cause);
            }
        }
    }
    
    /**
     * Creates a {@link ServletException} from the given cause.
     *
     * @param pCause the cause of the exception
     * @return the exception
     */
    public static ServletException createServletException(Throwable pCause)
    {
        ServletException exception = new ServletException(pCause);
        fixServletException(exception);
        return exception;
    }
    
    /**
     * Visits the chain of causes of the given exception and for every 
     * {@link ServletException} ensures that the cause is correctly set.
     * <p>
     * A <code>ServletException</code> has an embedded <code>root</code> cause
     * which may not be used to set the cause introduced in the JDK 1.4.
     *
     * @param pThrowable the exception to visit
     */
    public static void fixNestedServletExceptions(Throwable pThrowable)
    {
        Throwable cause = pThrowable.getCause();
        if (cause != null)
        {
            if (cause instanceof ServletException)
            {
                fixServletException((ServletException) cause);
            }
            fixNestedServletExceptions(cause);
        }

    }
    
    public static String getBaseURL(HttpServletRequest request)
    {
        final String scheme = request.getScheme();
        final int port = request.getServerPort();
        final StringBuffer buf = new StringBuffer(128);

        buf.append(scheme).append("://").append(request.getServerName());
        if (!isDefaultPort(scheme, port))
        {
            buf.append(':').append(port);
        }
        buf.append(request.getContextPath());
        return buf.toString();
    }
    
    /**
     * Returns <code>true</code> if the given port is the default port for the given scheme,
     * <code>false</code> otherwise.
     *
     * @param pScheme the URL scheme
     * @param pPort the port number
     * @return <code>true</code> if the given port is the default port for the given scheme,
     * <code>false</code> otherwise
     */
    public static boolean isDefaultPort(String pScheme, int pPort)
    {
        if (SCHEME_HTTP.equals(pScheme) && DEFAULT_HTTP_PORT == pPort)
        {
            return true;
        }
        if (SCHEME_HTTPS.equals(pScheme) && DEFAULT_HTTPS_PORT == pPort)
        {
            return true;
        }
        return false;
    }
    /**
     * 设置客户端缓存过期时间 的Header.
     * @param response HttpServletResponse
     * @param expiresSeconds long
     */
    public static void setExpiresHeader(HttpServletResponse response, long expiresSeconds) {
      // Http 1.0 header, set a fix expires date.
      response.setDateHeader(HttpHeaders.EXPIRES, System.currentTimeMillis() + expiresSeconds * 1000);
      // Http 1.1 header, set a time after now.
      response.setHeader(HttpHeaders.CACHE_CONTROL, "private, max-age=" + expiresSeconds);
    }

    /**
     * 设置禁止客户端缓存的Header.
     * @param response HttpServletResponse
     */
    public static void setNoCacheHeader(HttpServletResponse response) {
      // Http 1.0 header
      response.setDateHeader(HttpHeaders.EXPIRES, 1L);
      response.addHeader(HttpHeaders.PRAGMA, "no-cache");
      // Http 1.1 header
      response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0");
    }

    /**
     * 设置LastModified Header.
     * @param response HttpServletResponse
     * @param lastModifiedDate long
     */
    public static void setLastModifiedHeader(HttpServletResponse response, long lastModifiedDate) {
      response.setDateHeader(HttpHeaders.LAST_MODIFIED, lastModifiedDate);
    }

    /**
     * 设置Etag Header.
     * @param response HttpServletResponse
     * @param etag String
     */
    public static void setEtag(HttpServletResponse response, String etag) {
      response.setHeader(HttpHeaders.ETAG, etag);
    }

    /**
     * 根据浏览器If-Modified-Since Header, 计算文件是否已被修改.
     * 
     * 如果无修改, checkIfModify返回false ,设置304 not modify status.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param lastModified 内容的最后修改时间.
     * @return true if has modified, otherwise return false.
     */
    public static boolean checkIfModifiedSince(HttpServletRequest request,
        HttpServletResponse response, long lastModified) {
      long ifModifiedSince = request.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE);
      if ((ifModifiedSince != -1) && (lastModified < ifModifiedSince + 1000)) {
        response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        return false;
      }
      return true;
    }

    /**
     * 根据浏览器 If-None-Match Header, 计算Etag是否已无效.
     * 
     * 如果Etag有效, checkIfNoneMatchEtag返回false, 设置304 not modify status.
     * 
     * @param etag 内容的ETag.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return true if etag is matched, otherwise return false
     */
    public static boolean checkIfNoneMatchEtag(HttpServletRequest request,
        HttpServletResponse response, String etag) {
      String headerValue = request.getHeader(HttpHeaders.IF_NONE_MATCH);
      if (headerValue != null) {
        boolean conditionSatisfied = false;
        if (!"*".equals(headerValue)) {
          StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");
          while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
            String currentToken = commaTokenizer.nextToken();
            if (currentToken.trim().equals(etag)) {
              conditionSatisfied = true;
            }
          }
        } else {
          conditionSatisfied = true;
        }

        if (conditionSatisfied) {
          response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
          response.setHeader(HttpHeaders.ETAG, etag);
          return false;
        }
      }
      return true;
    }

    /**
     * 设置让浏览器弹出下载对话框的Header.
     * 
     * @param fileName 下载后的文件名.
     * @param response HttpServletResponse
     * @param fileName String
     */
    public static void setFileDownloadHeader(HttpServletResponse response, String fileName) {
      try {
        // 中文文件名支持
        String encodedfileName = new String(fileName.getBytes(), "ISO8859-1");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
            + encodedfileName + "\"");
      } catch (UnsupportedEncodingException e) {
        e.getMessage();
      }
    }

    /**
     * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
     * 
     * 返回的结果的Parameter名已去除前缀.
     * @param request ServletRequest
     * @param prefix String
     * @return parameter map
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
      Validate.notNull(request, "Request must not be null");
      Enumeration paramNames = request.getParameterNames();
      Map<String, Object> params = new TreeMap<String, Object>();
      String pre = prefix;
      if (pre == null) {
        pre = "";
      }
      while (paramNames != null && paramNames.hasMoreElements()) {
        String paramName = (String) paramNames.nextElement();
        if ("".equals(pre) || paramName.startsWith(pre)) {
          String unprefixed = paramName.substring(pre.length());
          String[] values = request.getParameterValues(paramName);
          if (values == null || values.length == 0) {
            values = new String[] {};
            // Do nothing, no values found at all.
          } else if (values.length > 1) {
            params.put(unprefixed, values);
          } else {
            params.put(unprefixed, values[0]);
          }
        }
      }
      return params;
    }

    /**
     * 组合Parameters生成Query String的Parameter部分,并在parameter name上加上prefix.
     * @param params Map
     * @param prefix String
     * @return encoded parameter string
     */
    public static String encodeParameterStringWithPrefix(Map<String, Object> params, String prefix) {
      StringBuilder queryStringBuilder = new StringBuilder();

      String pre = prefix;
      if (pre == null) {
        pre = "";
      }
      Iterator<Entry<String, Object>> it = params.entrySet().iterator();
      while (it.hasNext()) {
        Entry<String, Object> entry = it.next();
        queryStringBuilder.append(pre).append(entry.getKey()).append("=").append(entry.getValue());
        if (it.hasNext()) {
          queryStringBuilder.append("&");
        }
      }
      return queryStringBuilder.toString();
    }

    /**
     * 客户端对Http Basic验证的 Header进行编码.
     * @param userName String
     * @param password String
     * @return encoded username and password
     * 
     */
    public static String encodeHttpBasic(String userName, String password) {
      String encode = userName + ":" + password;
      return "Basic " + Encodes.encodeBase64(encode.getBytes());
    }

    /**
     * 是否是Ajax异步请求
     * 
     * @param request HttpServletRequest
     * @return true if it is a ajax request.
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {

      String accept = request.getHeader("accept");
      String xRequestedWith = request.getHeader("X-Requested-With");
      // Principal principal = UserUtils.getPrincipal();

      // 如果是异步请求，则直接返回信息
      return ((accept != null && accept.indexOf("application/json") != -1 || (xRequestedWith != null && xRequestedWith
          .indexOf("XMLHttpRequest") != -1) /**
       * || (principal != null && principal .isMobileLogin())
       **/
      ));
    }
  
}
