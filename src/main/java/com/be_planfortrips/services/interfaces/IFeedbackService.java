package com.be_planfortrips.services.interfaces;

import com.be_planfortrips.dto.FeedbackDto;
import com.be_planfortrips.dto.response.FeedbackResponse;

import java.util.List;
import java.util.UUID;

public interface IFeedbackService {

    // Lấy tất cả feedback
    List<FeedbackResponse> getAllFeedbacks();

    // Lấy feedback theo ID
    FeedbackResponse getFeedbackById(UUID id);

    // Tạo feedback mới
    FeedbackResponse createFeedback(FeedbackDto feedbackDto);

    // Cập nhật feedback
    FeedbackResponse updateFeedback(UUID id, FeedbackDto feedbackDto);

    // Xóa feedback theo ID
    void deleteFeedbackById(UUID id);

    // Lấy FeedBack By enterpriseId
    List<FeedbackResponse> getFeedBackByEnterpriseId();
    List<FeedbackResponse> getFeedBackByEnterpriseId(Long id);

}
