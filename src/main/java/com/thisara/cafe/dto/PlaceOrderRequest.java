package com.thisara.cafe.dto;

import java.util.ArrayList;
import java.util.List;

public class PlaceOrderRequest {
    private Long userId;
    private List<PlaceOrderItemRequest> items = new ArrayList<>();

    public PlaceOrderRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<PlaceOrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<PlaceOrderItemRequest> items) {
        this.items = items;
    }
}
