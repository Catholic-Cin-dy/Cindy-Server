package com.app.cindy.domain;

import com.app.cindy.domain.product.Brand;
import com.app.cindy.domain.product.Product;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Category")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
public class Category {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //카테고리 종류
    @Column(name="name")
    private String name;

    @OneToMany(mappedBy="id")
    List<Product> product=new ArrayList<>();
}
