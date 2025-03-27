package com.smart.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart.entity.records;
import com.smart.repository.RecordsRepository;

@Service
public class RecordsService {

    @Autowired
    private RecordsRepository recordsRepository;

    public records getRecordsByMeetingIdx(Long meetingIdx) {
        return recordsRepository.findByMeetingIdx(meetingIdx);
    }
}
