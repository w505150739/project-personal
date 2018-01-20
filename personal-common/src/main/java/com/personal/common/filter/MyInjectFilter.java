package com.personal.common.filter;

import com.personal.common.exception.BusinessException;
import com.personal.common.util.IPUtil;
import com.personal.common.util.LogUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class MyInjectFilter implements Filter {

    protected LogUtil log = LogUtil.getLogger(getClass());
    protected static Vector list = new Vector();
    protected String enabled = "true";
    protected String failthPath = "sql.jsp";

    static {
        /*
         * [1] |（竖线符号） [2] & （& 符号） [3];（分号） [4] $（美元符号） [5] %（百分比符号） [6] @（at
         * 符号） [7] '（单引号） [8] "（引号） [9] \'（反斜杠转义单引号） [10] \"（反斜杠转义引号） [11]
         * <>（尖括号） [12] ()（括号） [13] +（加号） [14] CR（回车符，ASCII 0x0d） [15]
         * LF（换行，ASCII 0x0a） [16] ,（逗号） [17] \（反斜杠）
         */
        list.add("\"");
        list.add("\\");
        list.add("\'");
        list.add(" or ");
        list.add(" and ");
        list.add(" where ");
        list.add("%20or%20");
        list.add("%20and%20");
        list.add("%20where%20");
        list.add("[");
        list.add("$");
        list.add(".." + File.separator);
        // 请求参数不能能有脚步
        list.add("script");
        list.add("exec");
        list.add("execute");
        list.add("declare");
        list.add("column_name");
        list.add("master");
        list.add("group_concat");
        list.add("information_schema.columns");
        list.add("table_schema");
        list.add("union");
        list.add("truncate");
        list.add("drop");
        list.add("select ");
        list.add("insert");
        list.add("delete");
        list.add("update");
        list.add("grant");
        list.add("having");
        list.add("*");
        list.add("java.util");
        list.add("listFiles()");
        list.add("action:");
        list.add("redirect:");
        list.add("redirectAction:");
        // list.add("%");
    }

    public void destroy() {
        // TODO Auto-generated method stub

    }

    private String getFullUrl(HttpServletRequest request) {

        StringBuffer url = new StringBuffer();
        String scheme = request.getScheme();
        int port = request.getServerPort();
        if (port < 0)
            port = 80; // Work around java.net.URL bug

        url.append(scheme);
        url.append("://");
        url.append(request.getServerName());
        if ((scheme.equals("http") && (port != 80)) || (scheme.equals("https") && (port != 443))) {
            url.append(':');
            url.append(port);
        }
        url.append(request.getRequestURI());

        String queryString = request.getQueryString();

        if (queryString != null)
            url.append('?').append(queryString);

        return url.toString();
    }

    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
        // TODO Auto-generated method stub
        HttpServletRequest httpServletRequest = (HttpServletRequest) arg0;
        HttpServletResponse httpServletResponse = (HttpServletResponse) arg1;
        // 获取GET方法请求参数串
        boolean isXssFind = false;
        String uri = this.getFullUrl(httpServletRequest);

        if (uri.contains("action:") || uri.contains("redirect:") || uri.contains("redirect") || uri.contains("redirectAction:") || uri.contains("HttpServletResponse") || uri.contains("HttpServletRequest") || uri.contains("java.util") || uri.contains("debug") || uri.contains("getWriter") || uri.contains("\"") || uri.contains("'") || uri.contains("$") || uri.contains("*") || uri.contains(".." + File.separator)) {
            log.error("发现疑为跨站脚本攻击，检测判断请求地址包含非法字符:" + uri + ",[ip]:" + IPUtil.getRealIP(httpServletRequest));
            throw new BusinessException("发现疑为跨站脚本攻击，检测判断请求地址包含非法字符:" + uri);
        }
        if (uri.contains("pay")) {
            arg2.doFilter(arg0, arg1);
        } else {
            String queryString = httpServletRequest.getQueryString();
            // 是否启动,web.xml中配置的初始化参数
            if (enabled.equalsIgnoreCase("true")) {
                // 开始XSS(METHOD:GET)和sql注入检测
                if (queryString != null && !queryString.equalsIgnoreCase("null")) {
                    String src = "";
                    src += "_" + queryString;
                    if (src != null && !src.equalsIgnoreCase("null")) {
                        String keyword = "";
                        for (int i = 0; i < list.size(); i++) {
                            keyword = list.get(i).toString();
                            // src=MyStringUtil.escape(src);
                            if (src.indexOf(keyword.toLowerCase()) != -1) {
                                log.error("src:" + src + ",keyword:" + keyword);
                                isXssFind = true;
                                break;
                            }
                        }
                        if (isXssFind) {
                            log.error("发现疑为跨站脚本攻击，检测判断请求地址包含非法字符:" + keyword + ",[ip]:" + IPUtil.getRealIP(httpServletRequest));
                            throw new BusinessException("发现疑为跨站脚本攻击，检测判断请求地址包含非法字符:" + keyword);
                        }
                    }
                }
                // 开始XSS(METHOD:POST)和sql注入检测
                if (!isXssFind) {
                    arg2.doFilter(new RequestWrapperXSS(httpServletRequest, httpServletResponse, list, failthPath), arg1);
                }
            } else {
                arg2.doFilter(arg0, arg1);
            }
        }
    }

    /**
     * 初始化
     */
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

        enabled = arg0.getInitParameter("enabled");
        failthPath = arg0.getInitParameter("failthPath");
        if (enabled == null || "".equals(enabled)) {
            enabled = "true";
        }
        if (failthPath == null || "".equals(failthPath)) {
            enabled = "/sql.jsp";
        }
        log.info("SQL注入检测初始化完成");
    }
}
