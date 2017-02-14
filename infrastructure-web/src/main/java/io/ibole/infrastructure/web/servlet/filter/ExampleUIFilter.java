package io.ibole.infrastructure.web.servlet.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Class description goes here.
 * 
 */
public class ExampleUIFilter extends BaseServletFilter
{
    public ExampleUIFilter()
    {
        super();
    }
    
    public void doFilter(ServletRequest pRequest, ServletResponse pResponse,
        FilterChain pChain) throws IOException, ServletException
    {

       super.doFilter(pRequest, pResponse, pChain);

    }
}
