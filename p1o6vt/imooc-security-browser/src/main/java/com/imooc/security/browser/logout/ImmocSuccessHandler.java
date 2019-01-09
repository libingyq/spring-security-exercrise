package com.imooc.security.browser.logout;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by lbin8521 on 2019/1/9.
 */
public class ImmocSuccessHandler implements LogoutSuccessHandler {

    private String logOutUrl;

    public ImmocSuccessHandler(String logOutUrl) {
        this.logOutUrl = logOutUrl;
    }

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        //这里会配置推出登陆的json或者重定向到页面的配置
        if (StringUtils.isNotBlank(logOutUrl)) {
            httpServletResponse.sendRedirect(logOutUrl);
        }else{
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(objectMapper.writeValueAsString("退出成功"));
        }
    }
}
