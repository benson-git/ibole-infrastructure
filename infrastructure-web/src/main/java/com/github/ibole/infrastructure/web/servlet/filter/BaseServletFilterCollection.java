package com.github.ibole.infrastructure.web.servlet.filter;

import com.github.ibole.infrastructure.web.servlet.utils.ServletUtils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 
 * <code>BaseServletFilterCollection</code> is superclass designed to implement a collection of 
 * servlet Filters. Each servlet Filter specified in the application deployment descriptor (web.xml) 
 * is added to a FilterChain by the Web container. To eliminate the overhead of using many servlet 
 * Filters, This class based applications are allowed to register a servlet Filter, based on 
 * BaseServletFilterCollection, that will manage a collection of servlet Filters (called managed 
 * Filters here). BaseServletFilterCollection sets up it own internal sub-FilterChain.
 * 
 */
public class BaseServletFilterCollection extends BaseServletFilter
{
    /** The terminal element of the sub-FilterChain. */
    private final TerminalFilterChain terminal;

    /** The sub-FilterChain */
    protected final FilterChain filterChain;
    
    /**
     * Constructs a <code>BaseServletFilterCollection</code>. The constructor creates a
     * sub-FilterChain that will call all servlet Filters specified in the constructor
     * in the specified order. 
     *
     * @param pFilters the ordered Filters called by the Filter
     * @since 1.0
     */
    protected BaseServletFilterCollection(Filter[] pFilters)
    {
        terminal = new TerminalFilterChain(getClass().getName());
        
        FilterChain previous = terminal;
        for (int i = pFilters.length - 1; i >= 0; --i)
        {
            previous = new FilterFilterChain(pFilters[i], previous);
        }
        filterChain = previous;
    }
    
    /**
     * {@inheritDoc}
     * Calls the sub-FilterChain. Does NOT call super.doFilter
     */
    public void doFilter(ServletRequest pRequest, ServletResponse pResponse, 
        FilterChain pNextUnmanagedFilterChain) throws IOException, ServletException
    {
        terminal.setFilterChainAsAttribute(pRequest, pNextUnmanagedFilterChain);
        try
        {
            try
            {
                filterChain.doFilter(pRequest, pResponse);
            }
            catch (ServletException e)
            {
                ServletUtils.fixServletException(e);
                throw e;
            }
        }
        finally
        {
            // This is not strictly required as ServletRequest are guaranteed not
            // to be reused.
            terminal.removeFilterChainAttribute(pRequest);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void init(FilterConfig pFilterConfig) throws ServletException
    {
        super.init(pFilterConfig);
        
        FilterChain current = filterChain;
        while (current != terminal)
        {
            ((FilterFilterChain) current).getFilter().init(pFilterConfig);
            current = ((FilterFilterChain) current).getNextFilterChain();
        }
    }
    

}
