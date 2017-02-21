package io.ibole.infrastructure.web.servlet.utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet Utils.
 * 
 */
public class ServletUtils
{
    
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
    
}
