package com.home.microservices.gateway.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;

public class BookServiceZuulFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return FilterConstants.ROUTE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 6;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        final Integer portOnXML = 8084;
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String url = request.getRequestURL().toString();
//        String port = url.split(":")[1];
//        if (Integer.valueOf(port).equals(portOnXML)) {
            System.out.println("Service on port " + portOnXML + " will accept only " + MediaType.APPLICATION_XML);
            ctx.addZuulRequestHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML);
//        }
        return null;
    }

}
