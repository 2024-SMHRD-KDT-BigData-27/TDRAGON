package com.smart.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.entity.meetings;
import com.smart.repository.MeetingsRepository;
import com.smart.repository.RecordsRepository;

@Service
public class MeetingsService {

    @Autowired
    private MeetingsRepository meetingsRepository;
    
    @Autowired
    private RecordsRepository recordsRepository;

    public List<meetings> getAllMeetings() {
        return meetingsRepository.findByIsActive(1);
    }
    
    public List<meetings> getActiveMeetingsByEmail(String hostEmail) {
        return meetingsRepository.findByHostEmailAndIsActive(hostEmail, 1);
    }
    
    public List<meetings> getDeletedMeetings(String hostEmail) {
        return meetingsRepository.findByHostEmailAndIsActive(hostEmail, 0);
    }
    
    // 수정된 메서드 호출
    public List<meetings> getMeetingsByUserEmail(String user_email) {
        // user_email을 host_email로 가진 미팅을 반환
        return meetingsRepository.findByHostEmail(user_email); // 수정된 메서드
    }

    public meetings getNoteByMeetingIdx(Long meetingIdx) {
        return meetingsRepository.findByMeetingIdx(meetingIdx);  // 쿼리문에 의한 조회
    }
    
    public int insertMeetings(String meeting_title, String host_email) {
    	return meetingsRepository.insertMeeting(meeting_title, host_email);
    }
    
    public void moveToTrash(List<Long> meetingIds) {
        meetingsRepository.moveToTrash(meetingIds);
    }

    public void restoreFromTrash(List<Long> meetingIds) {
        meetingsRepository.restoreFromTrash(meetingIds);
    }

    public void deletePermanently(List<Long> meetingIds) {
        meetingsRepository.deletePermanently(meetingIds);
    }

	public int getLastMeetingIdx() {
		Integer lastIdx = meetingsRepository.getLastMeetingIdx();
		return (lastIdx != null) ? lastIdx : 0;
	}


    
    
}
