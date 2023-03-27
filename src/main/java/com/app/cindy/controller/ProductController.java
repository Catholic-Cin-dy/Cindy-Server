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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        PageResponse<List<ProductRes.ProductList>> productList = productService.getProductList(filter,page,size,1L);
        return CommonResponse.onSuccess(productList);
    }
}
