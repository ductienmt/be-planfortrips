package com.be_planfortrips.controllers;

import com.be_planfortrips.dto.FeedbackDto;
import com.be_planfortrips.dto.response.FeedbackResponse;
import com.be_planfortrips.services.interfaces.IFeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/feedbacks")
public class FeedbackController {

    private final IFeedbackService feedbackService;

    @GetMapping("all")
    public ResponseEntity<List<FeedbackResponse>> getAllFeedbacks() {
        List<FeedbackResponse> feedbacks = feedbackService.getAllFeedbacks();
        return ResponseEntity.ok(feedbacks);
    }


    @GetMapping("getById/{id}")
    public ResponseEntity<FeedbackResponse> getFeedbackById(@PathVariable UUID id) {
        FeedbackResponse response = feedbackService.getFeedbackById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/enterprise/{enterpriseId}")
    public ResponseEntity<List<FeedbackResponse>> getFeedBackByEnterpriseId() {
        List<FeedbackResponse> responses = feedbackService.getFeedBackByEnterpriseId();
        return ResponseEntity.ok(responses);
    }
    @GetMapping("/enterpriseById/{enterpriseId}")
    public ResponseEntity<List<FeedbackResponse>> getFeedBacksByEnterpriseId(@PathVariable Long enterpriseId) {
        List<FeedbackResponse> responses = feedbackService.getFeedBackByEnterpriseId(enterpriseId);
        return ResponseEntity.ok(responses);
    }


    @PostMapping("/create")
    public ResponseEntity<FeedbackResponse> createFeedback(@Valid @RequestBody FeedbackDto dto) {
        FeedbackResponse response = feedbackService.createFeedback(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<FeedbackResponse> updateFeedback(
            @PathVariable UUID id,
            @RequestBody FeedbackDto dto
    ) {
        FeedbackResponse response = feedbackService.updateFeedback(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable UUID id) {
        feedbackService.deleteFeedbackById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
