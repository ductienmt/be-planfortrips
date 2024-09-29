package com.be_planfortrips.mappers;

public interface MapperInterface<R,E,D> {

    E toEntity(D d);

    R toResponse(E e);


    void updateEntityFromDto(D d, E e);
}
