package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.TagDto;
import com.be_planfortrips.dto.response.TagResponse;

import java.util.List;
import java.util.UUID;

public interface ITagService {

    List<TagResponse> getAllTag(Integer page, Integer size);

    TagResponse getTagById(UUID tagId);
    TagResponse createTag(TagDto tagDto);

    void removeTagById(UUID tagId);

    TagResponse updateTag(UUID tagId, TagDto tagDto);

}
