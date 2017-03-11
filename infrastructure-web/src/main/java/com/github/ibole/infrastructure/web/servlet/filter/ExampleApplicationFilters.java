package com.github.ibole.infrastructure.web.servlet.filter;

import javax.servlet.Filter;

/**
 * <code>ExampleFilters</code> Builds a filter chain of all specified filters.
 * <p>
 * !!!!!!!!!!!!!!!!!!!!!!!  NO XDOCLET TAG !!!!!!!!!!!!!!!!!!!!!!!!!!
 * <p>
 *  This Listener MUST be declared explicitly in web.xml
 *  <p>
 * <code>   
 *   &lt;filter&gt;   
 *       &lt;filter-name&gt;ApplicationFilters&lt;/filter-name&gt;
 *       &lt;filter-class&gt;
 *           com.github.ibole.infrastructure.web.servlet.filter.ExampleApplicationFilters
 *       &lt;/filter-class&gt;
 *    &lt;/filter&gt;
 *  </code>  
 *  <p>
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 
 * 
 */
public class ExampleApplicationFilters extends BaseServletFilterCollection
{
    /**
     * Constructs a <code>ApplicationFilters</code>
     *
     */
    public ExampleApplicationFilters()
    {
        super(
            new Filter[]
            {
                new ExampleUIFilter()
            }
        );
    }
}
