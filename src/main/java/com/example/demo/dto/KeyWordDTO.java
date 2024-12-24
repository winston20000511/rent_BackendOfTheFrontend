package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class KeyWordDTO {
    private String origin;
    private int minPrice;
    private int maxPrice;
    private boolean washingMachine;
    private boolean airConditioner;
    private boolean netWork;
    private boolean bedStead;
    private boolean matTress;
    private boolean refRigerator;
    private boolean ewaterHeater;
    private boolean gwaterHeater;
    private boolean teleVision;
    private boolean channelFour;
    private boolean sofa;
    private boolean tables;
    private boolean pet;
    private boolean parkingSpace;
    private boolean elevator;
    private boolean balcony;
    private boolean shortTerm;
    private boolean cooking;
    private boolean waterDispenser;
    private boolean fee;
    private int gender;
    private String houseType;
    private String priority;

    @Override
    public boolean equals(Object o) {
        // 1) 先判斷是否相同參考
        if (this == o) return true;
        // 2) 判斷是否為空、型別是否相同
        if (o == null || getClass() != o.getClass()) return false;
        // 3) cast 成同型別
        KeyWordDTO that = (KeyWordDTO) o;

        //使用者 / 對象
        // 4) 依序比對每個欄位
        if (minPrice < that.minPrice) return false;
        if (maxPrice < that.maxPrice) return false;
        if (washingMachine != that.washingMachine) return false;
        if (airConditioner != that.airConditioner) return false;
        if (netWork != that.netWork) return false;
        if (bedStead != that.bedStead) return false;
        if (matTress != that.matTress) return false;
        if (refRigerator != that.refRigerator) return false;
        if (ewaterHeater != that.ewaterHeater) return false;
        if (gwaterHeater != that.gwaterHeater) return false;
        if (teleVision != that.teleVision) return false;
        if (channelFour != that.channelFour) return false;
        if (sofa != that.sofa) return false;
        if (tables != that.tables) return false;
        if (pet != that.pet) return false;
        if (parkingSpace != that.parkingSpace) return false;
        if (elevator != that.elevator) return false;
        if (balcony != that.balcony) return false;
        if (shortTerm != that.shortTerm) return false;
        if (cooking != that.cooking) return false;
        if (waterDispenser != that.waterDispenser) return false;
        if (fee != that.fee) return false;
        if (gender != that.gender && gender != 2) {
            return false;
        }
        // String 物件或其他物件類型要用 equals() 來比對
//        if (origin != null ? !origin.equals(that.origin) : that.origin != null) return false;

        if (houseType.indexOf(that.houseType) == -1) return false;

//        if (priority != null ? !priority.equals(that.priority) : that.priority != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = origin != null ? origin.hashCode() : 0;
        result = 31 * result + minPrice;
        result = 31 * result + maxPrice;
        result = 31 * result + (washingMachine ? 1 : 0);
        result = 31 * result + (airConditioner ? 1 : 0);
        result = 31 * result + (netWork ? 1 : 0);
        result = 31 * result + (bedStead ? 1 : 0);
        result = 31 * result + (matTress ? 1 : 0);
        result = 31 * result + (refRigerator ? 1 : 0);
        result = 31 * result + (ewaterHeater ? 1 : 0);
        result = 31 * result + (gwaterHeater ? 1 : 0);
        result = 31 * result + (teleVision ? 1 : 0);
        result = 31 * result + (channelFour ? 1 : 0);
        result = 31 * result + (sofa ? 1 : 0);
        result = 31 * result + (tables ? 1 : 0);
        result = 31 * result + (pet ? 1 : 0);
        result = 31 * result + (parkingSpace ? 1 : 0);
        result = 31 * result + (elevator ? 1 : 0);
        result = 31 * result + (balcony ? 1 : 0);
        result = 31 * result + (shortTerm ? 1 : 0);
        result = 31 * result + (cooking ? 1 : 0);
        result = 31 * result + (waterDispenser ? 1 : 0);
        result = 31 * result + (fee ? 1 : 0);
        result = 31 * result + gender;
        result = 31 * result + (houseType != null ? houseType.hashCode() : 0);
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        return result;
    }
}


