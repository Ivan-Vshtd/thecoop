package com.example.thecoop.utilities;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author iveshtard
 * @since 11/19/2018
 */
public class RedirectInterceptor extends HandlerInterceptorAdapter {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null){
            String arguments = request.getQueryString() != null ? "?" + request.getQueryString() : "";
            String url = request.getRequestURI() + arguments;
            response.setHeader("Turbolinks-Location", url);
        }
    }
}
