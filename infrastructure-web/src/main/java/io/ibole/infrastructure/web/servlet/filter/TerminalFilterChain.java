package io.ibole.infrastructure.web.servlet.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * <code>TerminalFilterChain</code> is the terminal member of an Guarantee managed sub-FilterChain.
 * Each Filter specified in the application deployment descriptor (web.xml) is added to a 
 * FilterChain by the Web container. To eliminate the overhead of using many servlet Filters, 
 * This filter based applications are allowed to register a servlet Filter, based on 
 * BaseServletFilterCollection, that will manage a collection of servlet Filters (called managed 
 * Filters here). BaseServletFilterCollection sets up it own internal sub-FilterChain. The last 
 * element of its sub-FilterChain is an instance of TerminalFilterChain. TerminalFilterChain 
 * delegates its doFilter method by calling the next un-managed FilterChain element as set up by 
 * the Web container. The next un-managed FilterChain element is passed as an attribute of the
 * ServletRequest. 
 * 
 */
public class TerminalFilterChain implements FilterChain
{
    /** The attribute name used to extract the last FilterChain element from the request. */
    private final String filterChainAttributeName;

    /**
     * Constructs a <code>TerminalFilterChain</code> that will use the specified attribute name
     * to extract the next un-managed FilterChain element from the Servlet request.  
     *
     * @param pFilterChainAttributName
     */
    TerminalFilterChain(String pFilterChainAttributName)
    {
        filterChainAttributeName = pFilterChainAttributName;
    }

    /**
     * Sets the next un-managed FilterChain element as an attribute of the ServletRequest. 
     *
     * @param pRequest the request to which the FilterChain attribute is added. 
     * @param pFilterChain the next un-managed FilterChain element.
     * @since 1.0
     */
    void setFilterChainAsAttribute(ServletRequest pRequest, FilterChain pFilterChain)
    {
        pRequest.setAttribute(filterChainAttributeName, pFilterChain);
    }

    /**
     * Removes the next un-managed FilterChain element as an attribute of the ServletRequest. 
     *
     * @param pRequest the request to which the FilterChain attribute was added. 
     *
     */
    void removeFilterChainAttribute(ServletRequest pRequest)
    {
        pRequest.removeAttribute(filterChainAttributeName);
    }

    /**
     * Extracts the next un-managed FilterChain element from the ServletRequest and
     * calls doFilter on it.
     *
     * {@inheritDoc}
     */
    public void doFilter(ServletRequest pRequest, ServletResponse pResponse) throws IOException,
        ServletException
    {
        // Obtain the next filterChain from the request
        FilterChain tmp = (FilterChain) pRequest.getAttribute(filterChainAttributeName);

        // DO NOT remove the request attribute. The filter chain can be called several times.
        tmp.doFilter(pRequest, pResponse);
    }
}
