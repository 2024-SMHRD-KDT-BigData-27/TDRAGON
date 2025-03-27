package com.smart.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class users {


	@Id
	private String user_email;
	
	@Column(length = 2000) // 길이 조절 2000크기 -> 지정 안할시 255
	private String user_pw;
	
	
	private String user_name;
	
	@Column(insertable = false, updatable = false, columnDefinition = "datetime default now()")
	private Date created_at; 
	
	private int user_idx;

}
