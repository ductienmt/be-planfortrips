package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.UserDto;
import com.be_planfortrips.entity.User;
import com.be_planfortrips.mappers.MapperInterface;
import com.be_planfortrips.responses.AccountUserResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserMapper implements MapperInterface<AccountUserResponse, User, UserDto> {

    ModelMapper modelMapper = new ModelMapper();

    @Override
    public User toEntity(UserDto userDto) {
        TypeMap<UserDto, User> typeMap = modelMapper.createTypeMap(UserDto.class, User.class);
        typeMap.addMappings(mapper -> mapper.skip(User::setId));
        User user = typeMap.map(userDto);
        return user;
    }

    @Override
    public AccountUserResponse toResponse(User user) {
        return modelMapper.map(user, AccountUserResponse.class);
    }

    @Override
    public void updateEntityFromDto(UserDto userDto, User user) {
        modelMapper.map(userDto, user);
    }
}
