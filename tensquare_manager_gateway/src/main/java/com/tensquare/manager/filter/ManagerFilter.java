package com.tensquare.manager.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

@Component
public class ManagerFilter extends ZuulFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        System.out.println("Zuul filter is working......");
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        //There are two special requests we need not to check header's authorization
        if(request.getMethod().equals("OPTIONS")){
            return null;
        }
        if(request.getRequestURL().indexOf("login") > 0){
            return null;
        }
        String header = request.getHeader("Authorization");
        if(header != null && !"".equals(header)){
            if(header.startsWith("Bearer ")){
                String token = header.substring(7);
                try{
                    Claims claims = jwtUtil.parseJWT(token);
                    String roles = (String) claims.get("roles");
                    if(roles.equals("admin")){
                        //redirect header, let it pass
                        requestContext.addZuulRequestHeader("Authorization", header);
                        return null;
                    }
                }catch (Exception e){
                    requestContext.setSendZuulResponse(false);
                }
            }
        }
        //if the control flow gets to this point, it means this request is bad (No authorization!)
        requestContext.setSendZuulResponse(false);
        requestContext.setResponseStatusCode(403);
        requestContext.setResponseBody("Access denied");
        return null;
    }
}
