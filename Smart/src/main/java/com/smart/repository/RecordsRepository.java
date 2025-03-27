package com.smart.repository;

import com.smart.entity.records;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.transaction.Transactional;

@Repository
public interface RecordsRepository extends JpaRepository<records, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM records r WHERE r.meeting.meeting_idx IN :meetingIds")
    void deleteByMeetingIdxIn(@Param("meetingIds") List<Long> meetingIds);

    @Query("SELECT r FROM records r WHERE r.meeting.meeting_idx = :meetingIdx")
    records findByMeetingIdx(@Param("meetingIdx") Long meetingIdx);
}
