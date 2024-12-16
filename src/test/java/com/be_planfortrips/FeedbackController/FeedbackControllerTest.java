package com.be_planfortrips.FeedbackController;

import com.be_planfortrips.controllers.FeedbackController;
import com.be_planfortrips.dto.FeedbackDto;
import com.be_planfortrips.dto.response.FeedbackResponse;
import com.be_planfortrips.services.interfaces.IFeedbackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedbackControllerTest {

    @Mock
    private IFeedbackService feedbackService;

    @InjectMocks
    private FeedbackController feedbackController;

    private FeedbackDto feedbackDto;
    private FeedbackResponse feedbackResponse;

    @BeforeEach
    void setUp() {
        feedbackDto = mockFeedbackDto();
        feedbackResponse = mockFeedbackResponse();
    }

    private FeedbackDto mockFeedbackDto() {
        return new FeedbackDto();
    }

    private FeedbackResponse mockFeedbackResponse() {
        return new FeedbackResponse(UUID.randomUUID(), "This is a feedback.", 5, "User Name", "Enterprise Name", "2023-12-16T12:00:00");
    }

    @Test
    void getAllFeedbacks_ShouldReturnListOfFeedbacks() {
        // Arrange
        List<FeedbackResponse> mockFeedbacks = List.of(feedbackResponse);
        when(feedbackService.getAllFeedbacks()).thenReturn(mockFeedbacks);

        // Act
        ResponseEntity<List<FeedbackResponse>> response = feedbackController.getAllFeedbacks();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockFeedbacks, response.getBody());
        verify(feedbackService, times(1)).getAllFeedbacks();
    }

    @Test
    void getFeedbackById_ShouldReturnFeedback() {
        // Arrange
        UUID id = feedbackResponse.getId();
        when(feedbackService.getFeedbackById(id)).thenReturn(feedbackResponse);

        // Act
        ResponseEntity<FeedbackResponse> response = feedbackController.getFeedbackById(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(feedbackResponse, response.getBody());
        verify(feedbackService, times(1)).getFeedbackById(id);
    }

    @Test
    void getFeedBackByEnterpriseId_ShouldReturnListOfFeedbacks() {
        // Arrange
        List<FeedbackResponse> mockResponses = List.of(feedbackResponse);
        when(feedbackService.getFeedBackByEnterpriseId()).thenReturn(mockResponses);

        // Act
        ResponseEntity<List<FeedbackResponse>> response = feedbackController.getFeedBackByEnterpriseId();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponses, response.getBody());
        verify(feedbackService, times(1)).getFeedBackByEnterpriseId();
    }

    @Test
    void createFeedback_ShouldReturnCreatedFeedback() {
        // Arrange
        when(feedbackService.createFeedback(feedbackDto)).thenReturn(feedbackResponse);

        // Act
        ResponseEntity<FeedbackResponse> response = feedbackController.createFeedback(feedbackDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(feedbackResponse, response.getBody());
        verify(feedbackService, times(1)).createFeedback(feedbackDto);
    }

    @Test
    void updateFeedback_ShouldReturnUpdatedFeedback() {
        // Arrange
        UUID id = feedbackResponse.getId();
        when(feedbackService.updateFeedback(id, feedbackDto)).thenReturn(feedbackResponse);

        // Act
        ResponseEntity<FeedbackResponse> response = feedbackController.updateFeedback(id, feedbackDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(feedbackResponse, response.getBody());
        verify(feedbackService, times(1)).updateFeedback(id, feedbackDto);
    }

    @Test
    void deleteFeedback_ShouldReturnNoContent() {
        // Arrange
        UUID id = feedbackResponse.getId();

        // Act
        ResponseEntity<Void> response = feedbackController.deleteFeedback(id);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(feedbackService, times(1)).deleteFeedbackById(id);
    }
}