package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_table")
public class UserTableBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "phone")
    private String phone;
    @Column(name = "picture")
    private byte[] picture;
    @Column(name = "createtime")
    private LocalDateTime createTime;
    @Column(name = "gender")
    private Byte gender;
    @Column(name = "coupon")
    private Byte coupon;
    @Column(name = "status")
    private Byte status;
    
    
    @PrePersist
    protected void onCreate() {
        if (this.createTime == null) {
            this.createTime = LocalDateTime.now(); 
        }
        if (this.coupon == null) {
            this.coupon = 3; 
        }
        if (this.status == null) {
            this.status = 1; 
        }
    }
    
    
//    house
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<HouseTableBean> houses;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<HouseImageTableBean> images;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CollectTableBean> collect;
    
//    bookings
    @OneToMany(mappedBy = "rentUser", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BookingBean> bookings;
    
//    ad
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore 
    private List<AdBean> ads;
    
//    order
     @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
     @JsonIgnore
     private List<OrderBean> orders;
     
 
}
