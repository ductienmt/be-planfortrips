package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.TagDTO;
import com.be_planfortrips.dto.response.TagResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ITagService {
    TagResponse createTag(TagDTO TagDTO) throws Exception;
    TagResponse updateTag(Integer id,TagDTO TagDTO) throws Exception;
    Page<TagResponse> getTags(PageRequest request);
    TagResponse getByTagId(Integer id) throws Exception;
    void deleteTagById(Integer id);
}
