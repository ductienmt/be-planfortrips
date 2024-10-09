package com.be_planfortrips.dto.response;

import java.util.List;

public class PageResponse<T> {
    List<T> data;
    int total;
}
