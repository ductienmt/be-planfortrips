package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.FeedbackDto;
import com.be_planfortrips.dto.response.FeedbackResponse;
import com.be_planfortrips.entity.Feedback;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.impl.FeedbackMapper;
import com.be_planfortrips.repositories.FeedbackRepository;
import com.be_planfortrips.services.interfaces.IFeedbackService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeedbackServiceImpl implements IFeedbackService {

    FeedbackRepository feedbackRepository;
    FeedbackMapper feedbackMapper;

    @Override
    public List<FeedbackResponse> getAllFeedbacks() {
        List<Feedback> feedbacks = feedbackRepository.findAll();
        return feedbacks.stream()
                .map(feedbackMapper::toResponse)
                .toList();
    }

    @Override
    public FeedbackResponse getFeedbackById(UUID id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorType.notFound,""));
        return feedbackMapper.toResponse(feedback);
    }

    @Override
    public FeedbackResponse createFeedback(FeedbackDto feedbackDto) {
        Feedback feedback = feedbackMapper.toEntity(feedbackDto);
        feedback = feedbackRepository.save(feedback);
        return feedbackMapper.toResponse(feedback);
    }

    @Override
    public FeedbackResponse updateFeedback(UUID id, FeedbackDto feedbackDto) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorType.notFound, feedbackDto));
        feedbackMapper.updateEntityFromDto(feedbackDto, feedback);
        feedback = feedbackRepository.save(feedback);
        return feedbackMapper.toResponse(feedback);
    }

    @Override
    public void deleteFeedbackById(UUID id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorType.notFound, id));
        feedbackRepository.delete(feedback);
    }
}
