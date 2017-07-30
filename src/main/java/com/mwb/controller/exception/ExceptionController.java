package com.mwb.controller.exception;

import com.mwb.dao.modle.Log;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * 异常控制
 * 跳转页面
 */
@ControllerAdvice
public class ExceptionController {
	private static final Log LOG = Log.getLog(ExceptionController.class);
	@ExceptionHandler({Exception.class})
	public ModelAndView Exception(Exception ex){

		LOG.error("出错了异常",ex);
		ModelAndView mv = new ModelAndView("404");

		mv.addObject("exception", ex);
		return mv;
	}
	
}

