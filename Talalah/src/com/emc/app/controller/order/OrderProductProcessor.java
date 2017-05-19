package com.emc.app.controller.order;

import com.emc.app.entity.order.Order;
import com.emc.app.entity.order.OrderItem;
import com.emc.app.entity.order.OrderTravel;

public interface OrderProductProcessor {
	public OrderTravel pressOrderTravel(String rootUrl, int uId, int cpId);
	public OrderItem pressOrderItem(String rootUrl, int uId, int cpId);
	public Order pressOrderProduct(String rootUrl, int uId, int cpId);
}
