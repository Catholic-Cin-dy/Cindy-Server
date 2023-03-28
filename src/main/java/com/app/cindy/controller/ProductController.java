package com.app.cindy.controller;

import com.app.cindy.common.CommonResponse;
import com.app.cindy.domain.user.User;
import com.app.cindy.dto.PageResponse;
import com.app.cindy.dto.product.ProductRes;
import com.app.cindy.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "03-상품 🏬 API")
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping("")
    @ApiOperation(value = "03-01 상품 조회. 🏬 API Response #FRAME PRODUCT 01", notes = "")
    public CommonResponse<PageResponse<List<ProductRes.ProductList>>> getProductList(@AuthenticationPrincipal User user,
                                                                                     @Parameter(description = "페이지", example = "0") @RequestParam(required = false,defaultValue = "0" ) @Min(value = 0) Integer page,
                                                                                     @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false,defaultValue = "10")  Integer size,
                                                                                     @RequestParam(value = "filter",required = false,defaultValue="0") Integer filter) {
        PageResponse<List<ProductRes.ProductList>> productList = productService.getProductList(filter,page,size,user.getId());
        return CommonResponse.onSuccess(productList);
    }

    @GetMapping("/search")
    @ApiOperation(value = "03-02 상품 검색 조회. 아직 구현 안했어요🏬 API Response #FRAME PRODUCT 01", notes = "")
    public CommonResponse<PageResponse<List<ProductRes.ProductList>>> getProductListByContent(@AuthenticationPrincipal User user,
                                                                                     @Parameter(description = "페이지", example = "0") @RequestParam(required = false,defaultValue = "0" ) @Min(value = 0) Integer page,
                                                                                     @Parameter(description = "페이지 사이즈", example = "10") @RequestParam(required = false,defaultValue = "10")  Integer size,
                                                                                     @RequestParam(value = "content",required = true) String content) {
        PageResponse<List<ProductRes.ProductList>> productList = productService.getProductListByContent(content,page,size,user.getId());
        return CommonResponse.onSuccess(productList);
    }

    @GetMapping("/{productId}}")
    @ApiOperation(value = "03-03 상품 상세조회 API Response #FRAME PRODUCT 01", notes = "")
    public CommonResponse<ProductRes.ProductDetail> getProductDetail(@AuthenticationPrincipal User user,
                                                                     @PathVariable("productId") Long productId) {
        ProductRes.ProductDetail productDetail = productService.getProductDetail(user.getId(),productId);
        return CommonResponse.onSuccess(productDetail);
    }


}
