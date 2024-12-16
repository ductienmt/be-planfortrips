package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.AccountEnterprise;
import com.be_planfortrips.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {

    List<Feedback> getFeedbackByAccountEnterprise(AccountEnterprise accountEnterprise);

    @Query(nativeQuery = true, value = "SELECT\n" +
            "    ROUND(AVG(f.rating)::NUMERIC, 1) AS average_rating, \n" +
            "    COUNT(*) AS total_reviews, \n" +
            "    COUNT(CASE WHEN f.rating = 1 THEN 1 END) AS count_1_star, \n" +
            "    COUNT(CASE WHEN f.rating = 2 THEN 1 END) AS count_2_star, \n" +
            "    COUNT(CASE WHEN f.rating = 3 THEN 1 END) AS count_3_star,\n" +
            "    COUNT(CASE WHEN f.rating = 4 THEN 1 END) AS count_4_star, \n" +
            "    COUNT(CASE WHEN f.rating = 5 THEN 1 END) AS count_5_star \n" +
            "FROM\n" +
            "    feedbacks f\n" +
            "WHERE\n" +
            "    f.enterprise_id = :enterpriseId;")
    Map<String, Object> compileFeedbackByEnterprise(@Param("enterpriseId") Long enterpriseId);
}
