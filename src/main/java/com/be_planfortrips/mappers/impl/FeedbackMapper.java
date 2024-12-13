package com.be_planfortrips.mappers.impl;

import com.be_planfortrips.dto.FeedbackDto;
import com.be_planfortrips.dto.response.FeedbackResponse;
import com.be_planfortrips.entity.Feedback;
import com.be_planfortrips.mappers.MapperInterface;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
@Component
public class FeedbackMapper implements MapperInterface<FeedbackResponse, Feedback, FeedbackDto> {

    ModelMapper modelMapper;

    @Override
    public Feedback toEntity(FeedbackDto feedbackDto) {
        Feedback feedback = modelMapper.map(feedbackDto, Feedback.class);
        return feedback;
    }

    @Override
    public FeedbackResponse toResponse(Feedback feedback) {
        FeedbackResponse feedbackResponse = modelMapper.map(feedback, FeedbackResponse.class);
        feedbackResponse.setUserName(feedback.getUser().getUserName());
        feedbackResponse.setTypeEnterpriseName(feedback.getAccountEnterprise().getEnterpriseName());
        feedbackResponse.setCreatedAt(feedback.getCreateAt().toString());
        return feedbackResponse;
    }

    @Override
    public void updateEntityFromDto(FeedbackDto feedbackDto, Feedback feedback) {
    }
}
