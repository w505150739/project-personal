package com.personal.common.filter;

import com.personal.common.exception.BusinessException;
import com.personal.common.util.IPUtil;
import com.personal.common.util.LogUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Vector;

/**
 * @author liuyuzhu
 * @description: ${todo}(这里用一句话描述这个类的作用)
 * @date 2018/1/20 22:47
 */
public class RequestWrapperXSS extends HttpServletRequestWrapper{
    protected LogUtil log = LogUtil.getLogger(getClass());
    private HttpServletRequest request;
    //	private HttpServletResponse serlvetResponse;
    private boolean isXssFind = false;
    private Vector keywords;
//	private String failForwardPath;

    public RequestWrapperXSS(HttpServletRequest servletRequest) {
        super(servletRequest);
        this.request = servletRequest;
    }

    /**
     *
     * @param servletRequest
     * @param serlvetResponse
     */
    public RequestWrapperXSS(HttpServletRequest servletRequest, HttpServletResponse serlvetResponse) {
        super(servletRequest);
        this.request = servletRequest;
//		this.serlvetResponse = serlvetResponse;
    }

    public RequestWrapperXSS(HttpServletRequest servletRequest, HttpServletResponse serlvetResponse, Vector keywords, String failForwardPath) {
        super(servletRequest);
        this.request = servletRequest;
//		this.serlvetResponse = serlvetResponse;
        this.keywords = keywords;
//		this.failForwardPath = failForwardPath;
    }

    /**
     * 覆盖
     */
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        // log.debug("parameter name: "+name+" value:"+values.toString());
        try {
            xssCheck(values, keywords);
        } catch (IllegalAccessError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return values;
    }

    /**
     * 覆盖
     */
    public String getParameter(String para) {
        String postStrInfo = super.getParameter(para);
        // log.debug("parameter name: "+para+" value:"+postStrInfo);
        try {
            // log.debug("parameter name: "+para);
            log.debug("（检测）接收到的[" + request.getMethod() + "]请求参数值: " + postStrInfo);
            xssCheck(postStrInfo, keywords);

        } catch (IllegalAccessError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return postStrInfo;
    }

    /*
     * public ServletInputStream getInputStream() { //ServletInputStream
     * ServletInputStream stream = null; //POST表单信息 String postStrInfo = null;
     * try{ stream = request.getInputStream(); byte[] buffer =
     * IOUtils.toByteArray(stream); postStrInfo = new String(buffer,"UTF-8");
     * //拆分请求参数串 String[] args = postStrInfo.split("\r\n"); for (int i = 0; i <
     * args.length; i++) { String line = args[i]; //过滤分隔符，和请求参数名称
     * if(line.trim().startsWith("-------------------") ||
     * line.trim().startsWith("Content-Disposition") || line.trim().equals("")){
     * log.debug("（忽略）接收到的["+request.getMethod()+"]请求参数值: " + line); continue; }
     * log.debug("（检测）接收到的["+request.getMethod()+"]请求参数值: " + line);
     * xssCheck(line,keywords); }
     *
     * //验证完成 final ByteArrayInputStream bais = new
     * ByteArrayInputStream(buffer); //生成新的ServletInputStream ServletInputStream
     * sis = new ServletInputStream() { public int read() throws IOException {
     * // TODO Auto-generated method stub return bais.read(); } }; stream = sis;
     * }catch(IOException e){ e.printStackTrace(); }
     *
     * return stream; }
     */
    /**
     * @Title: xssCheck
     * @Description: TODO 检测XSS和SQL注入处理类
     * @param @param postStrInfo
     * @param @throws IOException
     * @param @throws IllegalAccessError
     * @return void
     * @throws
     */
    private void xssCheck(String postStrInfo, Vector array) throws IOException, IllegalAccessError {
        if (postStrInfo == null)
            return;
        String src = postStrInfo == null ? "null" : postStrInfo.toLowerCase();
        // src = java.net.URLDecoder.decode(src,"UTF-8");
        // 验证XSS中是否包含相关关键字
        if (src != null && !src.equalsIgnoreCase("null")) {
            String keyword = "";
            for (int i = 0; i < array.size(); i++) {

                keyword = array.get(i).toString();
                if (src.indexOf(keyword) != -1) {
                    log.error("src:" + src + ",keyword:" + keyword);
                    isXssFind = true;
                    break;
                }
            }
            if (isXssFind) {
                log.error("value发现疑为跨站脚本攻击，检测判断请求地址包含非法字符:" + keyword + ",[ip]:" + IPUtil.getRealIP(request));
                throw new BusinessException("发现疑为跨站脚本攻击，检测判断请求地址包含非法字符:" + keyword);
                // throw new java.lang.IllegalAccessError();
            }
        }
    }

    /**
     *
     * @Title: xssCheck
     * @Description: TODO 检测XSS
     * @param @param values
     * @param @param array
     * @param @throws IOException
     * @param @throws IllegalAccessError
     * @return void
     * @throws
     */
    private void xssCheck(String[] values, Vector array) throws IOException, IllegalAccessError {
        if (values == null) {
            return;
        }
        for (int j = 0; j < values.length; j++) {
            // String src = java.net.URLDecoder.decode(values[j],"UTF-8");
            String src = values[j];
            // 验证XSS中是否包含相关关键字
            if (src != null && !src.equalsIgnoreCase("null")) {
                String keyword = "";
                for (int i = 0; i < array.size(); i++) {
                    keyword = array.get(i).toString();
                    if (src.indexOf(keyword) != -1) {
                        log.error("src:" + src + ",keyword:" + keyword);
                        isXssFind = true;
                        break;
                    }
                }
                if (isXssFind) {
                    log.error("value发现疑为跨站脚本攻击，检测判断请求地址包含非法字符:" + keyword + ",[ip]:" + IPUtil.getRealIP(request));
                    throw new BusinessException("发现疑为跨站脚本攻击，检测判断请求地址包含非法字符:" + keyword);
                    // throw new java.lang.IllegalAccessError();
                }
            }
        }
    }
}
