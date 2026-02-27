package com.thisara.cafe.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Item {
        @Id
        private Long itemId;
        private String name;
        private String description;
        private double price;
        private int quantity;
        private String imageUrl;

}
