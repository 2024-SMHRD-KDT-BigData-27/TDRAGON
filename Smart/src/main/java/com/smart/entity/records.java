package com.smart.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class records {

    @Id
    private Long file_idx;

    private String file_url;

    @ManyToOne
    @JoinColumn(name = "meeting_idx", referencedColumnName = "meeting_idx")
    private meetings meeting;  // meetings와의 관계를 설정

    private Integer record_time;
    private String record_text;
    @Column(name = "text_summary")
    private String text_summary;

    @Column(name = "uploaded_at")
    private java.sql.Timestamp uploaded_at;

    @Column(name = "text_keyword")
    private String text_keyword;
    
    
}
