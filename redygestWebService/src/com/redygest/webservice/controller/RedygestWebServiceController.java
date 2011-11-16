package com.redygest.webservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.redygest.webservice.RedygestService;
import com.redygest.webservice.model.Results;

@Controller
public class RedygestWebServiceController {
	private RedygestService redygestService = null;
	
	@RequestMapping(value = "/queryResults/")
	public ModelAndView getQueryResults() {
		redygestService = new RedygestService() {
			@Override
			public Results getQueryResults() {
				Results r= new Results();
				r.setResultCode("SUCCESS");
				return r;
			}
		};
		Results res = redygestService.getQueryResults();
		ModelAndView mav = new ModelAndView("queryResultsXmlView", BindingResult.MODEL_KEY_PREFIX + "results", res);
		return mav;
	}
}
