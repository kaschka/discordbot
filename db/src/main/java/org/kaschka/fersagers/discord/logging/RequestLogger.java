package org.kaschka.fersagers.discord.logging;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestLogger implements Filter {

    private static final Logger logger = LoggerFactory.getLogger("requestlog");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
            FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        long time = System.currentTimeMillis();
        String log = "";
        log += String.format("method: %s, ", request.getMethod());
        log += String.format("uri: %s, ", request.getRequestURI() + (request.getQueryString() == null ? "" : "?" + request.getQueryString()));
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            log += String.format("duration: %s, ", (System.currentTimeMillis() - time));

            HttpServletResponse response = (HttpServletResponse) servletResponse;
            log += String.format("HTTP-Statuscode: %s", response.getStatus());
            logger.info(log);
        }
    }

    @Override
    public void destroy() {
    }
}
