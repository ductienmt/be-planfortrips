package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.AccountEnterprise;
import com.be_planfortrips.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {

    List<Feedback> getFeedbackByAccountEnterprise(AccountEnterprise accountEnterprise);
}
