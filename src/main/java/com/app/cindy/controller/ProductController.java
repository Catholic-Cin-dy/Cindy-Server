package com.app.cindy.controller;

import com.app.cindy.common.CommonResponse;
import com.app.cindy.domain.user.User;
import com.app.cindy.dto.PageResponse;
import com.app.cindy.dto.product.ProductRes;
import com.app.cindy.service.ProductService;
import com.app.cindy.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import retrofit2.http.PATCH;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "03-상품 🏬 API")
@RequestMapping("/products")
public class ProductController {
    private final RedisService redisService;
    private final ProductService productService;

    @GetMapping("")
    @ApiOperation(value = "03-01 상품 조회. 🏬 API Response #FRAME PRODUCT 01", notes = "")
    public CommonResponse<PageResponse<List<ProductRes.ProductList>>> getProductList(@AuthenticationPrincipal User user,
                                                                                     @Parameter(description = "페이지", example = "0") @RequestParam(required = false,defaultValue = "0" ) @Min(value = 0) Integer page,
                                                                                     @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false,defaultValue = "10")  Integer size,
                                                                                     @RequestParam(value = "filter",required = false,defaultValue="0") Integer filter) {

        System.out.println("filterId : "+filter);
        PageResponse<List<ProductRes.ProductList>> productList = productService.getProductList(filter,page,size,user.getId());
        System.out.println(productList);
        return CommonResponse.onSuccess(productList);
    }

    @GetMapping("/search")
    @ApiOperation(value = "03-02 상품 검색 조회.🏬 API Response #FRAME PRODUCT 01", notes = "")
    public CommonResponse<PageResponse<List<ProductRes.ProductList>>> getProductListByContent(@AuthenticationPrincipal User user,
                                                                                     @Parameter(description = "페이지", example = "0") @RequestParam(required = false,defaultValue = "0" ) @Min(value = 0) Integer page,
                                                                                     @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false,defaultValue = "10")  Integer size,
                                                                                     @RequestParam(value = "content",required = true) String content) {
        PageResponse<List<ProductRes.ProductList>> productList = productService.getProductListByContent(content,page,size,user.getId());
        return CommonResponse.onSuccess(productList);
    }




    @GetMapping("/{productId}")
    @ApiOperation(value = "03-03 상품 상세조회🏬 API Response #FRAME PRODUCT 02", notes = "")
    public CommonResponse<ProductRes.ProductDetail> getProductDetail(@AuthenticationPrincipal User user,
                                                                     @PathVariable("productId") Long productId) {
        ProductRes.ProductDetail productDetail = productService.getProductDetail(user.getId(),productId);
        return CommonResponse.onSuccess(productDetail);
    }



    @GetMapping("/other/{productId}")
    @ApiOperation(value = "03-04 다른 사람이 본 상품 리스트 조회🏬 API Response #FRAME PRODUCT 02", notes = "")
    public CommonResponse<List<ProductRes.ProductList>> getProductOtherList(@AuthenticationPrincipal User user,
                                                                            @Parameter(description = "상품 id", example = "10") @PathVariable("productId") Long productId){
        Long userId= user.getId();
        List<Long> productIds = redisService.getRecentlyViewedProducts(String.valueOf(userId), String.valueOf(productId));

        List<ProductRes.ProductList> productList = new ArrayList<>();
        if(productIds.size()==0){
            productList = productService.getProductBrandList(productId,userId);
        }
        else {
            productList = productService.getProductOtherList(productIds, userId);
        }

        return CommonResponse.onSuccess(productList);
    }

    @GetMapping("/brand/{productId}")
    @ApiOperation(value = "03-05 같은 브랜드 상품 조회 🏬 API Response #FRAME PRODUCT 02", notes = "")
    public CommonResponse<List<ProductRes.ProductList>> getProductBrandList(@AuthenticationPrincipal User user,
                                                                            @Parameter(description = "상품 id", example = "10") @PathVariable("productId") Long productId){
        Long userId= user.getId();

        List<ProductRes.ProductList> productList=productService.getProductBrandList(productId,userId);

        return CommonResponse.onSuccess(productList);
    }


    @PatchMapping("/like/{productId}")
    @ApiOperation(value = "03-06 상품 좋아요/좋아요 취소 🏬 API Response #FRAME PRODUCT 02", notes = "")
    public CommonResponse<String> modifyProductLike(@AuthenticationPrincipal User user,
                                                                            @Parameter(description = "상품 id", example = "10") @PathVariable("productId") Long productId){
        Long userId= user.getId();
        String result="";

        boolean check = productService.existsProductLike(userId,productId);

        if(check){
            productService.deleteProductLike(userId,productId);
            result = "좋아요 취소 성공";
        }
        else{
            productService.postProductLike(userId,productId);
            result = "좋아요 성공";
        }
        return CommonResponse.onSuccess(result);
    }
}
