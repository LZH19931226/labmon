package com.hc.web.handler;


import com.hc.my.common.core.bean.ApiResponse;
import com.hc.my.common.core.exception.IedsException;
import com.hc.my.common.core.exception.LabSystemEnum;
import com.hc.my.common.core.exception.TokenException;
import com.hc.my.common.core.struct.Context;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * @author LiuZhiHao
 * @date 2019/10/16 9:25
 * 描述:
 **/
@Configuration
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
public class SpringExceptionHandle {

    private static final Logger logger = LoggerFactory.getLogger(SpringExceptionHandle.class);


    /**
     * 自定义异常的捕获
     * 自定义抛出异常。统一的在这里捕获返回JSON格式的友好提示。
     *
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(value = {IedsException.class})
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public <T> ApiResponse<T> sendError(IedsException exception, HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        String lang = Context.getLang();
        String errText =exception.getText();
        if(StringUtils.isNotBlank(lang) && !StringUtils.equals(lang,"zh")){
            LabSystemEnum labSystemEnum = LabSystemEnum.from(errText);
            errText = labSystemEnum.name();
        }
        logger.error("occurs error when execute url ={} ,message {}", requestUrl, exception.getMessage());
        return new ApiResponse<>(false, exception.getCode(), errText);
    }

    @ExceptionHandler(value = {TokenException.class})
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public <T> ApiResponse<T> sendTokenError(TokenException exception, HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        String errText =exception.getText();
        logger.error("occurs error when execute url ={} ,message {}", requestUrl, exception.getMessage());
        return new ApiResponse<>(false, exception.getCode(), errText);
    }





//    @ExceptionHandler(value = {Exception.class})
//    @ResponseBody
//    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
//    public ApiResponse<String> notAllowed(Exception e, HttpServletRequest request) {
//        String requestUrl = request.getRequestURI();
//        logger.error("occurs error when execute url ={} ,message {}", requestUrl, e.getMessage());
//        return new ApiResponse<>(false, e.getMessage());
//    }
//

}