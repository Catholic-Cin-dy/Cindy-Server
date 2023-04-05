package com.app.cindy.service;

import com.app.cindy.domain.Banner;
import com.app.cindy.dto.home.HomeRes;
import com.app.cindy.repository.BannerRepository;
import com.app.cindy.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final BannerRepository bannerRepository;
    private final ProductRepository productRepository;

    public List<HomeRes.HomeBanner> getHomeBannerList(){
        List<HomeRes.HomeBanner> bannerList = new ArrayList<>();
        List<Banner> banner = null;
        banner=bannerRepository.findAll();

        banner.forEach(
                result -> bannerList.add(
                        new HomeRes.HomeBanner(
                            result.getTitle(),
                            result.getContent(),
                            result.getImgUrl()
                        ))
        );

        return bannerList;
    }


    public List<HomeRes.HomeNewProduct> getNewProductList() {
        List<HomeRes.HomeNewProduct> newProductList = new ArrayList<>();
        List<ProductRepository.GetNewProductList> newProduct = null;
        newProduct=productRepository.findNewProduct();

        newProduct.forEach(
                result -> newProductList.add(
                        new HomeRes.HomeNewProduct(
                                result.getProductId(),
                                result.getBrand(),
                                result.getProductName(),
                                result.getProductImgUrl()
                        )
                )
        );

        return newProductList;
    }

//    public List<HomeRes.HomeRecommendProduct> getRecommendProductList() {
//        List<HomeRes.HomeRecommendProduct> recommendProductList = new ArrayList<>();
//        List<ProductRepository.GetRecommendProductList> recommendProduct = null;
//        recommendProduct=productRepository.findRecommendProduct();
//
//        return recommendProductList;
//    }
}
