package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.TagDto;
import com.be_planfortrips.dto.response.TagResponse;
import com.be_planfortrips.entity.Tag;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.repositories.TagRepository;
import com.be_planfortrips.services.interfaces.ITagService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Service
public class TagServiceImpl implements ITagService {

    TagRepository tagRepository;


    @Override
    public List<TagResponse> getAllTag(Integer page, Integer size) {
        if (page == null || size == null || page < 0 || size < 0) {
            // Lấy tất cả
            return tagRepository.findAll()
                    .stream()
                    .map(this::buildTagResponse)
                    .toList();
        } else {
            // Áp dụng phân trang
            PageRequest request = PageRequest.of(page, size);
            return tagRepository.findAll(request)
                    .stream()
                    .map(this::buildTagResponse)
                    .toList();
        }
    }


    @Override
    public TagResponse getTagById(UUID tagId) {
        Tag tag = tagRepository.findById(tagId).orElseThrow(
                () -> new AppException(ErrorType.tagIdNotFound)
        );
        return  buildTagResponse(tag);
    }

    @Override
    public TagResponse createTag(TagDto tagDto) {
        boolean isExist = tagRepository.existsByTagName(tagDto.getTagName());
        if (isExist) {
            throw new AppException(ErrorType.tagNameExisted);
        }
        Tag tag = tagRepository.save(
                Tag.builder().tagName(
                        tagDto.getTagName()).tagDes(tagDto.getDescription()).build()
        );
        return buildTagResponse(tag);
    }

    private TagResponse buildTagResponse(Tag tag) {
        return TagResponse.builder()
                .tagDes(tag.getTagDes())
                .tagName(tag.getTagName())
                .tagId(tag.getTagId())
                .build();
    }


    @Override
    public void removeTagById(UUID tagId) {
        Tag tag = tagRepository.findById(tagId).orElseThrow(
                () -> new AppException(ErrorType.tagIdNotFound)
        );
        tagRepository.deleteById(tagId);
    }

    @Override
    public TagResponse updateTag(UUID tagId, TagDto tagDto) {
        Tag tag = tagRepository.findById(tagId).orElseThrow(
                () -> new AppException(ErrorType.tagIdNotFound)
        );
        Tag tagNew = tagRepository.save(Tag.builder()
                .tagDes(tagDto.getDescription())
                .tagName(tag.getTagName())
                        .tagId(tagId)
                .build());
        return buildTagResponse(tagNew);
    }
}
