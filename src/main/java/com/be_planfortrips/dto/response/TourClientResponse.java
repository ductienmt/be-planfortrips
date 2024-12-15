package com.be_planfortrips.dto.response;

import com.be_planfortrips.entity.Admin;
import com.be_planfortrips.entity.Tag;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TourClientResponse {

    Integer tourId;
    String tourTitle;
    String tourDes;
    String urlImage;

    LocalDateTime timeCreate;
    LocalDateTime timeUpdate;
    Admin admin;
    List<Tag> tags;

    List<Long> listUserUsed;


}
