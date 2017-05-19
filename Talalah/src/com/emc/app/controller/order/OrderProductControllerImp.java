package com.emc.app.controller.order;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.emc.app.entity.order.Order;
import com.emc.app.entity.order.OrderItem;
import com.emc.app.entity.order.OrderTravel;

@Controller("orderProductController")
@RequestMapping("/order")
public class OrderProductControllerImp implements OrderProductController {
	@Autowired private ServletContext servletContext;
	@Autowired private OrderProductProcessor orderProductProcessor;
	@RequestMapping(value="/travel/save", method=RequestMethod.POST,produces="application/json")
	@ResponseBody @Override
	public ResponseEntity<OrderTravel> pressOrderTravel(@RequestParam("cId") int cId, @RequestParam("cpId") int cpId) {
		String rootUrl = servletContext.getInitParameter("com.talalah.web.service.engine").trim();
		OrderTravel orderTravel =  orderProductProcessor.pressOrderTravel(rootUrl, cId, cpId);
		return new ResponseEntity<OrderTravel>(orderTravel,HttpStatus.OK);
	}
	@RequestMapping(value="/item/save", method=RequestMethod.POST)
	@ResponseBody @Override
	public ResponseEntity<OrderItem> pressOrderItem(@RequestParam("cId") int cId, @RequestParam("cpId") int cpId) {
		String rootUrl = servletContext.getInitParameter("com.talalah.web.service.engine").trim();
		OrderItem orderItem =  orderProductProcessor.pressOrderItem(rootUrl, cId, cpId);
		return new ResponseEntity<OrderItem>(orderItem,HttpStatus.OK);
	}
	
	@RequestMapping(value="/product/save", method=RequestMethod.POST, produces="application/json")
	@ResponseBody @Override
	public ResponseEntity<Order> pressOrderProducts(@RequestParam("uId") int id, @RequestParam("cId") int cId) {
		String rootUrl = servletContext.getInitParameter("com.talalah.web.service.engine").trim();
		Order order = orderProductProcessor.pressOrderProduct(rootUrl, id, cId);
		return new ResponseEntity<Order>(order, HttpStatus.OK);
	}

}
