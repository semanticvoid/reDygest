package com.redygest.webservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.redygest.webservice.Neo4jRedygestService;
import com.redygest.webservice.RedygestService;
import com.redygest.webservice.model.Results;

@Controller
public class RedygestWebServiceController {
	private RedygestService redygestService = null;
	
	@RequestMapping(value = "/queryResults/{hw1}/{r}/{hw2}/")
	public ModelAndView getQueryResults(@PathVariable String hw1, @PathVariable String r, @PathVariable String hw2) {
		redygestService = Neo4jRedygestService.getInstance();
		Results res = redygestService.getQueryResults(hw1,r,hw2);
		ModelAndView mav = new ModelAndView("queryResultsXmlView", BindingResult.MODEL_KEY_PREFIX + "results", res);
		return mav;
	}
}
