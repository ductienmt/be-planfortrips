package com.be_planfortrips.services.impl;

import com.be_planfortrips.dto.FeedbackDto;
import com.be_planfortrips.dto.response.FeedbackResponse;
import com.be_planfortrips.entity.AccountEnterprise;
import com.be_planfortrips.entity.Feedback;
import com.be_planfortrips.entity.Plan;
import com.be_planfortrips.entity.User;
import com.be_planfortrips.exceptions.AppException;
import com.be_planfortrips.exceptions.ErrorType;
import com.be_planfortrips.mappers.impl.FeedbackMapper;
import com.be_planfortrips.mappers.impl.TokenMapperImpl;
import com.be_planfortrips.repositories.AccountEnterpriseRepository;
import com.be_planfortrips.repositories.FeedbackRepository;
import com.be_planfortrips.repositories.PlanRepository;
import com.be_planfortrips.repositories.UserRepository;
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
    AccountEnterpriseRepository accountEnterpriseRepository;
    FeedbackMapper feedbackMapper;
    UserRepository userRepository;
    TokenMapperImpl tokenMapper;
    PlanRepository planRepository;


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
        AccountEnterprise accountEnterprise = accountEnterpriseRepository.findById(feedbackDto.getAccountEnterpriseId())
                .orElseThrow(()-> new AppException(ErrorType.notFound));
        User user = userRepository.findByUsername(feedbackDto.getUsername());
        feedback.setUser(user);
        feedback.setAccountEnterprise(accountEnterprise);
        Plan plan = planRepository.findById(feedbackDto.getPlanId()).orElseThrow(
                () -> new AppException(ErrorType.notFound)
        );

        System.out.println(accountEnterprise.getTypeEnterpriseDetail().getId());
        //Nha` xe
        if (accountEnterprise.getTypeEnterpriseDetail().getId() == 1) {
            plan.setIsFbVehicle(true);
        }

        // noi o?
        if (accountEnterprise.getTypeEnterpriseDetail().getId() >= 3) {
               plan.setIsFbHotel(true);
        }

        planRepository.save(plan);

        feedbackRepository.save(feedback);
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

    @Override
    public List<FeedbackResponse> getFeedBackByEnterpriseId() {
        AccountEnterprise accountEnterprise = this.accountEnterpriseRepository.findById(tokenMapper.getIdEnterpriseByToken())
                .orElseThrow(() -> new AppException(ErrorType.notFound, "Không tìm thấy tài khoản doanh nghiệp"));
        return feedbackRepository.getFeedbackByAccountEnterprise(accountEnterprise)
                .stream().map(feedbackMapper::toResponse).toList();
    }

    @Override
    public List<FeedbackResponse> getFeedBackByEnterpriseId(Long id) {
        AccountEnterprise accountEnterprise = this.accountEnterpriseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorType.notFound, "Không tìm thấy tài khoản doanh nghiệp"));
        return feedbackRepository.getFeedbackByAccountEnterprise(accountEnterprise)
                .stream().map(feedbackMapper::toResponse).toList();
    }
}
