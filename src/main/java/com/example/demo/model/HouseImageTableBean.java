package com.example.demo.model;

import java.sql.Date;
import java.sql.Time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "image_table")
public class HouseImageTableBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url")
    private byte[] imageUrl;

    @ManyToOne
    @JoinColumn(name = "house_id", nullable = false) // 房屋ID作為外鍵
    private HouseTableBean house;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // 用戶ID作為外鍵
    private UserTableBean user;


}
