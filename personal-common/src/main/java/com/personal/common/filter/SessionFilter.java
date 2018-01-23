package com.personal.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.personal.common.util.LogUtil;

/**
 * 过滤请求URL
 *
 * @author huangga
 */
public class SessionFilter implements Filter {

    private final LogUtil logger = LogUtil.getLogger(this.getClass());

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

//    	if (request instanceof HttpServletRequest) {
//            System.out.println("referer: "+((HttpServletRequest) request).getHeader("Referer"));
//        }
    	
        HttpServletRequest rq = ((HttpServletRequest) request);

        String url = rq.getServletPath();
        String sessionId = rq.getSession().getId();
        
        logger.info("sessionId =============== "+sessionId);
        logger.debug("----------" + url + "--------------begin");

        chain.doFilter(request, response);
        logger.debug("----------" + url + "--------------end\n");
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

}
