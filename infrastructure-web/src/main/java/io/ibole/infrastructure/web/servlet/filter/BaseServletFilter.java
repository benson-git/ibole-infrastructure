package io.ibole.infrastructure.web.servlet.filter;


import io.ibole.infrastructure.web.servlet.utils.ServletUtils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


/**
 * <code>BaseServletFilter</code> provides the default behavior for filters.
 *
 */
public abstract class BaseServletFilter implements Filter
{
    
    /** The <CODE>FilterConfig</CODE> of this filter */
    private FilterConfig filterConfig;
    
    
    protected BaseServletFilter()
    {
        super();
    }
    
    /**
     * {@inheritDoc}
     */
    public void init(FilterConfig pFilterConfig) throws ServletException
    {
        filterConfig = pFilterConfig;
    }

    /**
     * {@inheritDoc}
     */
    public void destroy()
    {
        filterConfig = null;
    }
    
    /**
     * {@inheritDoc}
     */
    public void doFilter(ServletRequest pRequest, ServletResponse pResponse,
        FilterChain pChain) throws IOException, ServletException
    {
        try
        {
            pChain.doFilter(pRequest, pResponse);
        }
        catch (ServletException e)
        {
            ServletUtils.fixServletException(e);
            throw e;
        }
    }
    
    
    /**
     * Get the <CODE>FilterConfig</CODE> of this filter.
     *
     * @return the <CODE>FilterConfig</CODE> of this filter.
     */
    protected FilterConfig getFilterConfig()
    {
        if (filterConfig == null)
        {
            throw new IllegalStateException("The Filter has not been initialized.");
        }
        
        return filterConfig;
    }
}
