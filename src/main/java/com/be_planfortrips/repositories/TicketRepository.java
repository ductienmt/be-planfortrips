package com.be_planfortrips.repositories;

import com.be_planfortrips.entity.Schedule;
import com.be_planfortrips.entity.Status;
import com.be_planfortrips.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    @Query("select t from Ticket t where t.status = 'Pending' and t.createAt < :timeLimit")
    List<Ticket> findPendingTicketsBefore(LocalDateTime timeLimit);
    @Query("select t from Ticket t where t.status = 'Cancelled'")
    List<Ticket> findByStatusCancelled();
    List<Ticket> findByUser_Id(Long id);
    List<Ticket> findBySchedule_Id(Integer id);
    List<Ticket> findByUser_Id(Integer id);
}

