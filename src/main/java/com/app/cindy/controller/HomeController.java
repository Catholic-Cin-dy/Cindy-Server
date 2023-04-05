package com.app.cindy.controller;

import com.app.cindy.common.CommonResponse;
import com.app.cindy.domain.user.User;
import com.app.cindy.dto.home.HomeRes;
import com.app.cindy.service.HomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "02-HOME 🏠 API")
@RequestMapping("/home")
public class HomeController {
    private final HomeService homeService;
    @GetMapping("/banner")
    @ApiOperation(value = "02-01 홈 화면 배너 조회 🏠", notes = "")
    public CommonResponse<List<HomeRes.HomeBanner>> getProductList(@AuthenticationPrincipal User user) {
        List<HomeRes.HomeBanner> homeBannerList = homeService.getHomeBannerList();
        return CommonResponse.onSuccess(homeBannerList);
    }

    @GetMapping("/new")
    @ApiOperation(value = "02-03 신상 상품 전체 조회 🏠", notes = "")
    public CommonResponse<List<HomeRes.HomeNewProduct>> getNewProductList(@AuthenticationPrincipal User user){
        List<HomeRes.HomeNewProduct> homeNewProductList = homeService.getNewProductList();
        return CommonResponse.onSuccess(homeNewProductList);
    }
}
