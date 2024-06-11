package com.rahul.sdr.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("Product")
@EqualsAndHashCode
public class Product implements Serializable {
    public static final long serialVersionUID = 42L;
    @Id
    private Integer id;
    private String name;
    private Integer quantity;
    private Long price;
}
