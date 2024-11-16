package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.TagDTO;
import com.be_planfortrips.dto.TagDTO;
import com.be_planfortrips.dto.response.ApiResponse;
import com.be_planfortrips.dto.response.TListResponse;
import com.be_planfortrips.dto.response.TagResponse;
import com.be_planfortrips.dto.response.TagResponse;
import com.be_planfortrips.services.interfaces.ITagService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequestMapping("${api.prefix}/tags")
@RequiredArgsConstructor
public class TagController {
    ITagService iTagService;
    @GetMapping("/all")
    public ResponseEntity<?> getAllTag(@RequestParam("page") int page,
                                       @RequestParam("limit") int limit){
        try {
            PageRequest request = PageRequest.of(page,limit);
            int totalPage= 0;
            Page<TagResponse> tagResponseList = iTagService.getTags(request);
            totalPage = tagResponseList.getTotalPages();
            TListResponse<TagResponse> listResponse = new TListResponse<>();
            listResponse.setListResponse(tagResponseList.toList());
            listResponse.setTotalPage(totalPage);
            return ResponseEntity.ok(listResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/create")
    public ResponseEntity<?> createTag(@RequestBody @Valid TagDTO TagDTO,
                                          BindingResult result){
        try {
            if(result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            TagResponse TagResponse = iTagService.createTag(TagDTO);
            return ResponseEntity.ok(TagResponse);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
