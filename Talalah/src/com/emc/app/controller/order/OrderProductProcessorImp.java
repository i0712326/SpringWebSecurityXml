package com.emc.app.controller.order;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.emc.app.entity.customer.CartItem;
import com.emc.app.entity.customer.CartTravel;
import com.emc.app.entity.customer.Customer;
import com.emc.app.entity.order.Order;
import com.emc.app.entity.order.OrderItem;
import com.emc.app.entity.order.OrderTravel;
import com.emc.app.entity.order.Payment;
import com.emc.app.entity.product.item.Item;
import com.emc.app.entity.product.travel.Travel;
import com.emc.app.entity.shipping.Shipping;

@Component("orderProductProcessor")
public class OrderProductProcessorImp implements OrderProductProcessor {
	@Autowired private RestTemplate restTemplate;
	@Override
	public OrderTravel pressOrderTravel(String rootUrl, int uId, int cpId) {
		
		String url = rootUrl + "/user/get/{id}";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id", uId);
		ResponseEntity<Customer> custResp = restTemplate.getForEntity(url, Customer.class, params);
		Customer customer = custResp.getBody();
		
		url = rootUrl+"/cartProduct/get/{id}";
		params.clear();
		params.put("id", cpId);
		ResponseEntity<CartTravel> cartTvlResp = restTemplate.getForEntity(url, CartTravel.class, params);
		CartTravel cartTravel = cartTvlResp.getBody();
		Travel travel = (Travel) cartTravel.getProduct();
		
		Order order = new Order();
		order.setCustomer(customer);
		order.setNum(cartTravel.getQty());
		order.setTotal(cartTravel.getPrice());
		order.setPayment(new Payment());
		
		OrderTravel orderTravel = new OrderTravel(order,travel);
		orderTravel.setAdult(cartTravel.getAdult());
		orderTravel.setChild(cartTravel.getChild());
		orderTravel.setStart(cartTravel.getStart());
		orderTravel.setEnd(cartTravel.getEnd());
		orderTravel.setDays(cartTravel.getDays());
		orderTravel.setNote(cartTravel.getNote());
		orderTravel.setQuantity(cartTravel.getQty());
		orderTravel.setPrice(cartTravel.getPrice());
		orderTravel.setType("com.emc.app.entity.order.OrderTravel");
		
		url = rootUrl+"/orderProduct/travel/save";
		ResponseEntity<OrderTravel> orderTvlResp = restTemplate.postForEntity(url, orderTravel, OrderTravel.class);
		
		url = rootUrl+"/cartProduct/travel/delete/{id}";
		params.clear();
		params.put("id", cpId);
		restTemplate.delete(url, params);
		
		return orderTvlResp.getBody();
	}

	@Override
	public OrderItem pressOrderItem(String rootUrl, int uId, int cpId) {
		String url = rootUrl + "/user/get/{id}";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id", uId);
		ResponseEntity<Customer> custResp = restTemplate.getForEntity(url, Customer.class, params);
		Customer customer = custResp.getBody();
		
		url = rootUrl+"/cartProduct/get/{id}";
		params.clear();
		params.put("id", cpId);
		ResponseEntity<CartItem> cartItemResp = restTemplate.getForEntity(url, CartItem.class, params);
		CartItem cartItem = cartItemResp.getBody();
		Item item = (Item) cartItem.getProduct();
		
		Order order = new Order();
		order.setCustomer(customer);
		order.setNum(cartItem.getQty());
		order.setTotal(cartItem.getPrice());
		order.setPayment(new Payment());
		
		Shipping shipping = cartItem.getShipping();
		
		OrderItem orderItem = new OrderItem(order, item, shipping);
		
		orderItem.setQuantity(1);
		orderItem.setColor(cartItem.getColor());
		orderItem.setLength(cartItem.getLength());
		orderItem.setWidth(cartItem.getWidth());
		orderItem.setSize(cartItem.getSize());
		orderItem.setHeigth(cartItem.getHeigth());
		orderItem.setPrice(cartItem.getPrice());
		
		url = rootUrl+"/orderProduct/item/save";
		ResponseEntity<OrderItem> orderItemResp = restTemplate.postForEntity(url, orderItem, OrderItem.class);
		
		url = rootUrl+"/cartProduct/item/delete/{id}";
		params.clear();
		params.put("id", cpId);
		restTemplate.delete(url, params);
		
		return orderItemResp.getBody();
	}

	@Override
	public Order pressOrderProduct(String rootUrl, int uId, int cId) {
		String url = rootUrl + "/user/get/{id}";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id", uId);
		ResponseEntity<Customer> custResp = restTemplate.getForEntity(url, Customer.class, params);
		Customer customer = custResp.getBody();
		
		url = rootUrl + "/order/place/cart";
		ResponseEntity<Order> resp = restTemplate.postForEntity(url, customer, Order.class);
		Order order = resp.getBody();
		
		return order;
	}

	
}
