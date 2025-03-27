package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.smart.entity.Memos;
import com.smart.service.MemoService;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
public class MemoController {

    @Autowired
    private MemoService memoService;

    // ✅ 메모 저장 API
    @PostMapping("/save-memo")
    public ResponseEntity<?> saveMemo(@RequestBody Map<String, String> request, HttpSession session) {
        String memoText = request.get("memo_text");
        Long meetingIdx = Long.parseLong(request.get("meeting_idx"));

        if (memoText == null || memoText.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "메모 내용을 입력하세요!"));
        }

        Memos newMemo = new Memos();
        newMemo.setMeetingIdx(meetingIdx);
        newMemo.setMemoText(memoText);
        newMemo.setCreatedAt(LocalDateTime.now());

        memoService.saveMemo(newMemo);
        return ResponseEntity.ok(Map.of("success", true));
    }

    // ✅ 특정 회의의 메모 조회 API
    @GetMapping("/get-memos/{meetingIdx}")
    public ResponseEntity<List<Memos>> getMemos(@PathVariable Long meetingIdx) {
    	  System.out.println("🔍 getMemos 요청: meetingIdx = " + meetingIdx);
        List<Memos> memos = memoService.getMemosByMeetingIdx(meetingIdx);
        return ResponseEntity.ok(memos);
    }
    
    
}
