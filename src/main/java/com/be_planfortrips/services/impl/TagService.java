package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.TagDTO;
import com.be_planfortrips.dto.response.TagResponse;
import com.be_planfortrips.entity.Tag;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.impl.TagMapper;
import com.be_planfortrips.repositories.TagRepository;
import com.be_planfortrips.services.interfaces.ITagService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class TagService implements ITagService {
    TagRepository tagRepository;
    TagMapper tagMapper;
    @Override
    @Transactional
    public TagResponse createTag(TagDTO TagDTO) throws Exception {
        if(tagRepository.existsByName(TagDTO.getName())){
            throw new AppException(ErrorType.TagNameIsExist);
        }
        Tag tag = tagMapper.toEntity(TagDTO);
        tagRepository.save(tag);
        return tagMapper.toResponse(tag);
    }

    @Override
    @Transactional
    public TagResponse updateTag(Integer id, TagDTO TagDTO) throws Exception {
        return null;
    }

    @Override
    public Page<TagResponse> getTags(PageRequest request) {
        return tagRepository.findAll(request).map(tag -> tagMapper.toResponse(tag));
    }

    @Override
    public TagResponse getByTagId(Integer id) throws Exception {
        return null;
    }

    @Override
    @Transactional
    public void deleteTagById(Integer id) {

    }
}
