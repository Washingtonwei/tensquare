package com.tensquare.qa.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //no matter what, we need to let the request move forward
        //interceptor is NOT going to tell if you have authority or not
        //the specific method in service will decide.
        //So what interceptor is doing is simply parse the token, make it
        //available to the method downstream
        String header = request.getHeader("Authorization");
        if(header != null && !"".equals(header)){
            if(header.startsWith("Bearer ")){
                //Get token
                String token = header.substring(7);
                try{
                    Claims claims = jwtUtil.parseJWT(token);

                    if("admin".equals(claims.get("roles"))){
                        request.setAttribute("admin_claims", token);
                    }
                    if("user".equals(claims.get("roles"))){
                        request.setAttribute("user_claims", token);
                    }
                }catch (Exception e){
                    throw new RuntimeException("Token error");
                }
            }
        }
        return true;
    }
}
