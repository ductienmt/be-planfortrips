package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.CarCompanyDTO;
import com.be_planfortrips.dto.TagDTO;
import com.be_planfortrips.dto.VehicleDTO;
import com.be_planfortrips.dto.response.TagResponse;
import com.be_planfortrips.dto.response.VehicleResponse;
import com.be_planfortrips.entity.CarCompany;
import com.be_planfortrips.entity.Tag;
import com.be_planfortrips.entity.Vehicle;
import com.be_planfortrips.mappers.MapperInterface;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class TagMapper implements MapperInterface<TagResponse, Tag, TagDTO> {
    ModelMapper modelMapper;

    @Override
    public Tag toEntity(TagDTO TagDTO) {
        TypeMap<TagDTO, Tag> typeMap = modelMapper.getTypeMap(TagDTO.class, Tag.class);
        if (typeMap == null) {
            typeMap = modelMapper.createTypeMap(TagDTO.class, Tag.class);
        }
        Tag Tag = modelMapper.map(TagDTO, Tag.class);
        return Tag;
    }

    @Override
    public TagResponse toResponse(Tag Tag) {
        modelMapper.typeMap(Tag.class,TagResponse.class).addMappings(mapper -> {
//             mapper.map(src -> src.getVehicle(), TagResponse::setVehicle);
//             mapper.map(src -> src.getStatus(), TagResponse::setStatusTag);

        });return modelMapper.map(Tag, TagResponse.class);
    }

    @Override
    public void updateEntityFromDto(TagDTO TagDTO, Tag Tag) {

    }
}
