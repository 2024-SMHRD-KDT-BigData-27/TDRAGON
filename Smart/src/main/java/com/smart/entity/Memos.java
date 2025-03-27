package com.smart.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "memos") // ✅ 테이블명 확인
public class Memos {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memoIdx; // ✅ 기본 키 (Primary Key)

    @Column(name = "meeting_idx", nullable = false) // ✅ 실제 DB 컬럼명과 맞춤
    private Long meetingIdx; // ✅ `meeting_Idx`로 되어 있다면 반드시 `meetingIdx`로 변경

    @Column(nullable = false, columnDefinition = "TEXT")
    private String memoText; // ✅ 메모 내용 저장

    @Column(nullable = true)
    private LocalDateTime createdAt; // ✅ 생성 시간 저장
}
