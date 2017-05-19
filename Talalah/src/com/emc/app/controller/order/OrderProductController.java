package com.emc.app.controller.order;

import org.springframework.http.ResponseEntity;

import com.emc.app.entity.order.Order;
import com.emc.app.entity.order.OrderItem;
import com.emc.app.entity.order.OrderTravel;

public interface OrderProductController {
	public ResponseEntity<OrderTravel> pressOrderTravel(int uId, int cpId);
	public ResponseEntity<OrderItem> pressOrderItem(int uId, int cpId);
	public ResponseEntity<Order> pressOrderProducts(int uId, int cId);
}
