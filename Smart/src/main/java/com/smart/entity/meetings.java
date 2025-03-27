package com.smart.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "meetings")
public class meetings {
	
	@Id
    private long meeting_idx;

    private String meeting_title;
    
    @Column(name= "host_email")
    private String hostEmail;

    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime created_at;

    @Column(name = "is_active") 
    private int isActive =1;


	

}
