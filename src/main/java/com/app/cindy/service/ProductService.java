package com.app.cindy.service;

import com.app.cindy.domain.product.Product;
import com.app.cindy.dto.PageResponse;
import com.app.cindy.dto.product.ProductRes;
import com.app.cindy.repository.ProductLikeRepository;
import com.app.cindy.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductLikeRepository productLikeRepository;
    public PageResponse<List<ProductRes.ProductList>> getProductList(int filter, Integer page, Integer size,Long userId) {
        Pageable pageReq = PageRequest.of(page, size);
        List<ProductRes.ProductList> productList=new ArrayList<>();
        Page<Product> product = null;
        if(filter==0){
            product=productRepository.findAll(pageReq);
        }else{
            product=productRepository.findByCategoryId((long) filter,pageReq);
        }

        product.forEach(
                result -> productList.add(
                        new ProductRes.ProductList(
                                result.getId(),
                                result.getBrand().getName(),
                                result.getName(),
                                result.getImgUrl(),
                                productLikeRepository.existsByProductIdAndUserId(result.getId(),userId)
                ))
        );

        return new PageResponse<>(product.isLast(),productList);
    }
}
