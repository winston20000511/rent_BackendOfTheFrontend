package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "house_table")
public class HouseTableBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "house_id")
    private Long houseId;
    
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private UserTableBean user;
    @Column(name = "title")
    private String title;
    @Column(name = "price")
    private Integer price;
    @Column(name = "description")
    private String description;
    @Column(name = "size")
    private Integer size;
    @Column(name = "address")
    private String address;
    @Column(name = "lat")
    private Double lat;
    @Column(name = "lng")
    private Double lng;
    @Column(name = "room")
    private Byte room;
    @Column(name = "bathroom")
    private Byte bathroom;
    @Column(name = "livingroom")
    private Byte livingroom;
    @Column(name = "kitchen")
    private Byte kitchen;
    @Column(name = "floor")
    private Byte floor;
    @Column(name = "atticAddition")
    private Boolean atticAddition;
    @Column(name = "status")
    private Byte status;
    @Column(name = "clickCount")
    private Integer clickCount;

    @OneToOne(mappedBy = "house", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private ConditionTableBean condition;
    
    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<CollectTableBean> collect;

    @OneToOne(mappedBy = "house", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private FurnitureTableBean furniture;

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<HouseImageTableBean> images = new ArrayList<>();

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BookingBean> bookings;

    @OneToOne(mappedBy = "house", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonManagedReference
    private HouseBookingTimeSlotBean bookingTimeSlots;
    
    @OneToMany(mappedBy = "house", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<AdBean> ads;

    

}
