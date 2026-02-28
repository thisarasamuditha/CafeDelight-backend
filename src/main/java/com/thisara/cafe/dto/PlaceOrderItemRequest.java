package com.thisara.cafe.dto;

public class PlaceOrderItemRequest {
    private Long itemId;
    private int quantity;

    public PlaceOrderItemRequest() {
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
