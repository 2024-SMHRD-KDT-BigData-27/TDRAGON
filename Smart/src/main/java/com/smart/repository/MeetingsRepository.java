package com.smart.repository;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.smart.entity.meetings;

@Repository
public interface MeetingsRepository extends JpaRepository<meetings, Long> {
	

	List<meetings> findByHostEmailAndIsActive(String hostEmail, int isActive);
	
	
    List<meetings> findByIsActive(int isActive); // 활성 상태인 미팅 조회


    List<meetings> findByHostEmail(String hostEmail);
    
    
    @Query("SELECT m FROM meetings m WHERE m.meeting_idx = :meetingIdx")
    meetings findByMeetingIdx(@Param("meetingIdx") Long meetingIdx);
    
    
    
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO meetings (meeting_title, host_email, is_active, created_at) " +
                   "VALUES (:meeting_title, :host_email, DEFAULT, NOW())", nativeQuery = true)
    int insertMeeting(@Param("meeting_title") String meetingTitle, 
                      @Param("host_email") String hostEmail);

    @Modifying
    @Transactional
    @Query("UPDATE meetings m SET m.isActive = 0 WHERE m.meeting_idx IN :meetingIds")
    void moveToTrash(@Param("meetingIds") List<Long> meetingIds);

    @Modifying
    @Transactional
    @Query("UPDATE meetings m SET m.isActive = 1 WHERE m.meeting_idx IN :meetingIds")
    void restoreFromTrash(@Param("meetingIds") List<Long> meetingIds);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM meetings m WHERE m.meeting_idx IN :meetingIds")
    void deletePermanently(@Param("meetingIds") List<Long> meetingIds);

    @Query("SELECT MAX(m.meeting_idx) FROM meetings m")
	Integer getLastMeetingIdx();
    
}
