package io.ibole.infrastructure.web.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * <code>FilterFilterChain</code> is the non-terminal member of Guarantee managed an sub-FilterChain.
 * Each servlet Filter specified in the application deployment descriptor (web.xml) is added to a 
 * FilterChain by the Web container. To eliminate the overhead of using many servlet Filters, 
 * This filter based applications are allowed to register a servlet Filter, based on 
 * BaseServletFilterCollection, that will manage a collection of servlet Filters (called managed 
 * Filters here). BaseServletFilterCollection sets up it own internal sub-FilterChain. The 
 * non-terminal elements of its sub-FilterChain are instances of FilterFilterChain. 
 * FilterFilterChain's doFilter method calls the associated managed servlet Filter's doFilter
 * method.
 * 
 * @version 2.0.0
 */
public class FilterFilterChain implements FilterChain
{

    /**
     * Constructs a node of a filter chain.
     *
     * @param pFilter is the node content
     * @param pFilterChain is the next element in the list.
     * @since 1.0
     */
    public FilterFilterChain(Filter pFilter, FilterChain pFilterChain)
    {
        if (pFilter == null)
        {
            throw new IllegalArgumentException("pFilter cannot be null");
        }
        if (pFilterChain == null)
        {
            throw new IllegalArgumentException("pFilterChain cannot be null");
        }

        filter = pFilter;
        filterChain = pFilterChain;
    }

    /**
     * {@inheritDoc}
     */
    public void doFilter(ServletRequest pRequest, ServletResponse pResponse) throws IOException,
        ServletException
    {
        filter.doFilter(pRequest, pResponse, filterChain);
    }

    /** The filter to be called on doFilter. */
    private final Filter filter;

    /** The next filter in the chain. */
    private final FilterChain filterChain;

    /**
     *
     * @return
     * @since 1.0
     */
    Filter getFilter()
    {
        return filter;
    }

    /**
     *
     * @return
     * @since 1.0
     */
    FilterChain getNextFilterChain()
    {
        return filterChain;
    }

}
