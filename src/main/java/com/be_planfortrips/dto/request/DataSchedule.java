package com.be_planfortrips.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DataSchedule {
    private String originalLocation;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
}
