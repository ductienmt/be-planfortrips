package com.be_planfortrips.mappers.impl;

public interface MapperInterface<R,E,P> {
    E mapFromRequest(R R);

    P mapToResponse(E e);

}
