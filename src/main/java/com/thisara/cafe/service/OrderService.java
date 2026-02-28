package com.thisara.cafe.service;

import com.thisara.cafe.dto.OrderItemResponse;
import com.thisara.cafe.dto.OrderResponse;
import com.thisara.cafe.dto.PlaceOrderItemRequest;
import com.thisara.cafe.dto.PlaceOrderRequest;
import com.thisara.cafe.entity.Item;
import com.thisara.cafe.entity.Order;
import com.thisara.cafe.entity.OrderItem;
import com.thisara.cafe.entity.User;
import com.thisara.cafe.repository.ItemRepository;
import com.thisara.cafe.repository.OrderRepository;
import com.thisara.cafe.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest request) {
        validatePlaceOrderRequest(request);

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + request.getUserId()));

        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());

        double totalPrice = 0.0;

        for (PlaceOrderItemRequest requestedItem : request.getItems()) {
            Item item = itemRepository.findById(requestedItem.getItemId())
                    .orElseThrow(() -> new IllegalArgumentException("Item not found: " + requestedItem.getItemId()));

            if (requestedItem.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero for item: " + requestedItem.getItemId());
            }

            if (item.getQuantity() < requestedItem.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for item: " + item.getName());
            }

            double unitPrice = item.getPrice();
            OrderItem orderItem = new OrderItem(item, requestedItem.getQuantity(), unitPrice);
            order.addOrderItem(orderItem);

            item.setQuantity(item.getQuantity() - requestedItem.getQuantity());
            totalPrice += orderItem.getLineTotal();
        }

        order.setTotalPrice(totalPrice);
        Order savedOrder = orderRepository.save(order);

        return toOrderResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        return toOrderResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found: " + userId);
        }

        return orderRepository.findByUser_UserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toOrderResponse)
                .toList();
    }

    private void validatePlaceOrderRequest(PlaceOrderRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Order request is required");
        }

        if (request.getUserId() == null) {
            throw new IllegalArgumentException("userId is required");
        }

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException("At least one item is required to place an order");
        }
    }

    private OrderResponse toOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getOrderId());
        response.setUserId(order.getUser().getUserId());
        response.setCreatedAt(order.getCreatedAt());
        response.setTotalPrice(order.getTotalPrice());

        List<OrderItemResponse> items = order.getOrderItems().stream().map(orderItem -> {
            OrderItemResponse itemResponse = new OrderItemResponse();
            itemResponse.setItemId(orderItem.getItem().getItemId());
            itemResponse.setName(orderItem.getItem().getName());
            itemResponse.setQuantity(orderItem.getQuantity());
            itemResponse.setUnitPrice(orderItem.getUnitPrice());
            itemResponse.setLineTotal(orderItem.getLineTotal());
            return itemResponse;
        }).toList();

        response.setItems(items);
        return response;
    }
}
