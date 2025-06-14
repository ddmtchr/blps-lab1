package com.ddmtchr.blpslab1.repository;

import com.ddmtchr.blpslab1.entity.CheckMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CheckMessageRepository extends JpaRepository<CheckMessage, Long> {

    List<CheckMessage> findAllBySent(Boolean sent);

    @Modifying
    @Query("update CheckMessage m set m.sent = true where m.bookingId in :ids")
    Integer setSentByBookingIdIn(Collection<Long> ids);
}
