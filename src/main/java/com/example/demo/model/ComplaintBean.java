package com.example.demo.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name ="complaints")
public class ComplaintBean {
			
	@Id @Column(name="complaints_id")
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	private int complaints_id ;
	
	@Column(name="user_id")
	private Long user_id;
	
	@Column(name="username")
	private String username ;
	
	@Column(name="category")
	private String category ;
	
	@Column(name="subject")
	private String subject ;
	
	@Column(name="content")
	private String content ;
	
	@Column(name="status")
	private String status ;

	@Column(name="note")
	private String note ;
	
	@Column(name="submission_date", insertable = false)
	private Timestamp  submission_date;
	


	public Timestamp getSubmission_date() {
		return submission_date;
	}

	public void setSubmission_date(Timestamp submission_date) {
		this.submission_date = submission_date;
	}

}
