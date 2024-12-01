package com.be_planfortrips.dto.response;

import com.be_planfortrips.entity.Image;
import com.be_planfortrips.entity.Room;
import com.be_planfortrips.entity.ScheduleSeat;
import com.be_planfortrips.entity.Tag;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourDetailResponse {

    Integer id;
    String title;
    String description;
    Integer numberPeople;
    Double rating;
    Integer day;
    Integer night;

    CarCompanyResponse carCompanyResponse;

    HotelResponse hotelResponse;

    List<TourDataByDate> tourDataByDates;


    List<Tag> tags;
    List<Image> images;
    Double priceForOneTicket;


}
