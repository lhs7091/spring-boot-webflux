package com.restfultutorial01.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    private String id;
    private String name;
    private Double price;

    public void updateProduct(String name, Double price){

        if (name != null){
            this.name = name;
        }

        if (price != null){
            this.price = price;
        }
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
