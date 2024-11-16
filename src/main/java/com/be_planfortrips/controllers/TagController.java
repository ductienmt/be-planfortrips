package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.TagDto;
import com.be_planfortrips.dto.response.TagResponse;
import com.be_planfortrips.services.interfaces.ITagService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/tags")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TagController {

    ITagService iTagService;

    @GetMapping("/all")
    public ResponseEntity<List<TagResponse>> getAllTagResponse(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        List<TagResponse> response = iTagService.getAllTag(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getById/{tagId}")
    public ResponseEntity<TagResponse> getTagById(
            @PathVariable UUID tagId
            ) {
        TagResponse response = iTagService.getTagById(tagId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<TagResponse> createTag(
            @RequestBody TagDto tagDto
    ) {
        TagResponse response = iTagService.createTag(tagDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{tagId}")
    public ResponseEntity<TagResponse> updateTag(
            @PathVariable UUID tagId,
            @RequestBody TagDto tagDto
    ) {
        TagResponse response = iTagService.updateTag(tagId, tagDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{tagId}")
    public ResponseEntity<Void> deleteTag(
            @PathVariable UUID tagId
    ) {
        iTagService.removeTagById(tagId);
        return ResponseEntity.noContent().build();
    }


}
