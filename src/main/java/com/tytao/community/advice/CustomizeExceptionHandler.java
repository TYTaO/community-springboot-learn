package com.tytao.community.advice;

import com.alibaba.fastjson.JSON;
import com.tytao.community.dto.ResultDTO;
import com.tytao.community.exception.CustomizeErrorCode;
import com.tytao.community.exception.CustomizeException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice(annotations = Controller.class) // 接受Controller抛出的异常
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class) // 处理Exception异常，Exception
    String handle(Throwable e, Model model, HttpServletRequest request,
                  HttpServletResponse response){
        String contentType = request.getContentType();
        ResultDTO resultDTO;
        if ("application/json".equals(contentType)){
            // 返回json
            if (e instanceof CustomizeException){
                resultDTO =  ResultDTO.errorOf((CustomizeException) e);
            } else {
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }

            try {
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.setStatus(200);
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            return null;
        } else {
            // 错误页面跳转
            if (e instanceof CustomizeException){
                model.addAttribute("message", e.getMessage());
            } else {
                model.addAttribute("message", CustomizeErrorCode.SYS_ERROR.getMessage());
            }
            return "redirect:/error";
        }
    }
}
