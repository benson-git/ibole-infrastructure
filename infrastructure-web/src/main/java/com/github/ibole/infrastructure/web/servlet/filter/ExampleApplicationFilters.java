package com.github.ibole.infrastructure.web.servlet.filter;

import javax.servlet.Filter;

/**
 * <code>ExampleFilters</code> Builds a filter chain of all specified filters.
 *
 * @ !!!!!!!!!!!!!!!!!!!!!!!  NO XDOCLET TAG !!!!!!!!!!!!!!!!!!!!!!!!!!
 *  This Listener MUST be declared explicitly in web.xml
 *    <filter>   
 *       <filter-name>ApplicationFilters</filter-name>
 *       <filter-class>
 *           com.github.ibole.infrastructure.web.servlet.filter.ExampleApplicationFilters
 *       </filter-class>
 *    </filter>   
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
