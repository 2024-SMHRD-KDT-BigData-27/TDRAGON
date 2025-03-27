package com.smart.entity;


import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Chunks {

    @Id
    @Column(name = "chunk_idx")
    private Long chunkIdx;

    @Column(name = "meeting_idx")
    private Long meetingIdx;

    @Column(name = "user_idx")
    private Long userIdx;

    @Column(name = "chunk_text")
    private String chunkText;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // ✅ 회의 제목 및 회의 생성일 출력용 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_idx", insertable = false, updatable = false)
    private meetings meetings;
    
}