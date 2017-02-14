package io.ibole.infrastructure.common.utils;

import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>
 * 版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
 * 
 * <p>
 * </p>
 *********************************************************************************************/


/**
 * URL related utility methods.
 */
public final class URLs {
  /**
   * Translates a string into application/x-www-form-urlencoded format using UTF-8 character
   * encoding.
   * 
   * @param string String to be translated
   * @param charset Character encoding
   * 
   * @return the translated String
   */
  public static String encode(String string, Charset charset) {
    try {
      return URLEncoder.encode(string, charset.name());
    } catch (UnsupportedEncodingException ex) {
      throw new UncheckedIOException("Unable to URL encode " + string, ex);
    }
  }

  /**
   * Decodes a application/x-www-form-urlencoded string.
   * 
   * @param string the String to decode
   * @param charset Character encoding
   * 
   * @return the newly decoded String
   */
  public static String decode(String string, Charset charset) {
    try {
      return URLDecoder.decode(string, charset.name());
    } catch (UnsupportedEncodingException ex) {
      throw new UncheckedIOException("Unable to URL decode " + string, ex);
    }
  }

  /**
   * Append query string to a URL.
   * 
   * @param url The URL
   * @param queryString The query string data
   * @param charset Character encoding
   * 
   * @return The URL with query string data appended
   */
  public static String appendQueryString(final String url, Map<String, List<String>> queryString,
      Charset charset) {
    int hashIdx = url.indexOf('#');
    StringBuilder builder = new StringBuilder(hashIdx > 0 ? url.substring(0, hashIdx) : url);
    if (queryString != null && !queryString.isEmpty()) {
      Iterator<Entry<String, List<String>>> itKey = queryString.entrySet().iterator();
      if (itKey.hasNext()) {
        builder.append(url.contains("?") ? '&' : '?');
        while (itKey.hasNext()) {
          Entry<String, List<String>> entry = itKey.next();
          String paramName = entry.getKey();
          for (Iterator<String> itVal = entry.getValue().iterator(); itVal.hasNext();) {
            String paramValue = itVal.next();
            builder.append(encode(paramName, charset)).append("=")
                .append(encode(paramValue, charset)).append(itVal.hasNext() ? "&" : "");
          }
          builder.append(itKey.hasNext() ? "&" : "");
        }
      }
    }
    if (hashIdx > 0) {
      builder.append(url.substring(hashIdx));
    }
    return builder.toString();
  }

  private URLs() {}
}
