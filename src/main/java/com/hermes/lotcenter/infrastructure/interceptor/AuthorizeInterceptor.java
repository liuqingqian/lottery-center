package com.hermes.lotcenter.infrastructure.interceptor;

import com.beicai.common.UserHolder;
import com.beicai.common.constants.CommonConstants;
import com.beicai.common.dto.UserDTO;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class AuthorizeInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userStr = request.getHeader(CommonConstants.USER_HEADER);
        if(StringUtils.isNotBlank(userStr)){
            byte[] userDecode = Base64.decodeBase64(userStr);
            if(!ArrayUtils.isEmpty(userDecode)){
                //解析用户信息
                UserDTO user = JSONObject.parseObject(new String(userDecode), UserDTO.class);
                UserHolder.setUser(user);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserHolder.removeUser();
    }

}
