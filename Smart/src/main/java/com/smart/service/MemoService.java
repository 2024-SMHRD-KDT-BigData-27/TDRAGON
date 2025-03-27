package com.smart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.smart.entity.Memos;
import com.smart.repository.MemoRepository;
import java.util.List;

@Service
public class MemoService {

    @Autowired
    private MemoRepository memoRepository; // ✅ 올바르게 주입 확인

    // ✅ 메모 저장
    public void saveMemo(Memos memo) {
        memoRepository.save(memo); // ✅ 오류 발생 시 memoRepository의 @Repository 어노테이션 확인
    }

    // ✅ 특정 회의의 메모 목록 조회
    public List<Memos> getMemosByMeetingIdx(Long meetingIdx) {
    	System.out.println("🔍 getMemosByMeetingIdx 호출됨, meetingIdx: " + meetingIdx);
        return memoRepository.findByMeetingIdx(meetingIdx); // ✅ 올바른 메서드명인지 확인
    }
}
