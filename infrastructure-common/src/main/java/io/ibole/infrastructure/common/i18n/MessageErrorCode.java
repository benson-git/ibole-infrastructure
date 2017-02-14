/**
 * 
 */
package io.ibole.infrastructure.common.i18n;

import io.ibole.infrastructure.common.utils.NLS;

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
 * @author bwang (chikaiwang@hotmail.com)
 *
 */
public class MessageErrorCode extends NLS {

  private static final String BUNDLE_NAME = "messages.errorcode";
  //General error message key
  public static String ERROR_FUNCTION_KEY;
  public static String ERROR_PERMISSION_DENIED_KEY;
  public static String ERROR_UNAUTHENTICATED_KEY;
  public static String ERROR_INTERNAL_KEY;
  public static String ERROR_UNAVAILABLE_KEY;
  public static String ERROR_UNKNOWN_KEY;
  //Specific error message key
  public static String CLIENT_ID_REQUIRED_KEY;
  //Token authenticated
  public static String ACCESS_TOKEN_RENEW_FAILED_KEY;

  static {
    // initialize resource bundles
    NLS.initializeMessages(BUNDLE_NAME, MessageErrorCode.class);
  }

}
