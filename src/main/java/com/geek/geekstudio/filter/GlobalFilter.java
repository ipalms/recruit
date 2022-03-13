package com.geek.geekstudio.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 编码过滤器，注解方式的过滤器通过文件名的排序作为执行顺序设置响应头，解决跨域
 */
@WebFilter(filterName = "GlobalFilter",urlPatterns = "/*")
public class GlobalFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpRequest.setCharacterEncoding("utf-8");
        httpResponse.setCharacterEncoding("utf-8");
        httpResponse.setContentType("application/json;charset=UTF-8");
        //解决跨域
        //表示是否允许发送Cookie。默认情况下，Cookie不包括在CORS请求之中
        //httpResponse.setHeader("Access-Control-Allow-Credentials","true");
        httpResponse.setHeader("Access-Control-Allow-Origin", httpRequest.getHeader("Origin"));
        httpResponse.setHeader("Access-Control-Allow-Headers", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
        //httpResponse.setHeader("Access-Control-Max-Age", "1728000");
        //对于非简单类型的请求，浏览器回先发送预见请求
        //预检请求首先使用OPTIONS方法发起一个预检请求到服务器，以获知服务器是否允许该实际请求
        if (httpRequest.getMethod().equals("OPTIONS")) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
        }
        chain.doFilter(httpRequest,httpResponse);
    }
}
