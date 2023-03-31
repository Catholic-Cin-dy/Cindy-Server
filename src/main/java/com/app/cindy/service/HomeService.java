package com.app.cindy.service;

import com.app.cindy.domain.Banner;
import com.app.cindy.dto.home.HomeRes;
import com.app.cindy.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final BannerRepository bannerRepository;

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
}
