package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.BannerDto;
import com.be_planfortrips.dto.response.BannerResponseAdmin;
import com.be_planfortrips.dto.response.BannerResponseUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IBannerService {
    BannerResponseAdmin createBanner(BannerDto bannerDto);
    BannerResponseAdmin updateBanner(Long id, BannerDto bannerDto);
    void deleteBanner(Long id);
    BannerResponseAdmin getBannerById(Long id);
    List<BannerResponseAdmin> getAllBanners();
    List<BannerResponseUser> getAllBannersUser();
    void changeStatusBanner(Long id, Integer status);
    void uploadBannerImage(Long id, MultipartFile file);
}
