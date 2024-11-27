package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.ConditionTableBean;
import com.example.demo.model.FurnitureTableBean;
import com.example.demo.model.HouseTableBean;


@RestController
@RequestMapping("/api")
public class HouseController {

    @PostMapping("/houses")
    public ResponseEntity<?> addHouse(
            @RequestParam String title,
            @RequestParam Integer price,
            @RequestParam Integer size,
            @RequestParam String address,
            @RequestParam Byte room,
            @RequestParam Byte bathroom,
            @RequestParam Byte livingroom,
            @RequestParam Byte kitchen,
            @RequestParam Byte floor,
            @RequestParam boolean atticAddition,
            @RequestParam boolean washingMachine,
            @RequestParam boolean airConditioner,
            @RequestParam boolean network,
            @RequestParam boolean bedstead,
            @RequestParam boolean mattress,
            @RequestParam boolean refrigerator,
            @RequestParam boolean ewaterHeater,
            @RequestParam boolean gwaterHeater,
            @RequestParam boolean television,
            @RequestParam boolean channel4,
            @RequestParam boolean sofa,
            @RequestParam boolean tables,
            @RequestParam boolean pet,
            @RequestParam boolean parkingSpace,
            @RequestParam boolean elevator,
            @RequestParam boolean balcony,
            @RequestParam boolean shortTerm,
            @RequestParam boolean cooking,
            @RequestParam boolean waterDispenser,
            @RequestParam boolean fee,
            @RequestParam Byte genderRestrictions, // 性別限制
            @RequestParam(required = false) List<MultipartFile> images // 圖片上傳
    ) {
        // 在這裡處理接收到的數據，例如保存到資料庫
    	
        HouseTableBean house = new HouseTableBean();
        house.setTitle(title);
        house.setPrice(price);
        house.setSize(size);
        house.setAddress(address);
        house.setRoom(room);
        house.setBathroom(bathroom);
        house.setLivingroom(livingroom);
        house.setKitchen(kitchen);
        house.setFloor(floor);
        house.setAtticAddition(atticAddition);

        // 設置家具與服務
        FurnitureTableBean furnitureServices = new FurnitureTableBean();
        furnitureServices.setWashingMachine(washingMachine);
        furnitureServices.setAirConditioner(airConditioner);
        furnitureServices.setNetwork(network);
        furnitureServices.setBedstead(bedstead);
        furnitureServices.setMattress(mattress);
        furnitureServices.setRefrigerator(refrigerator);
        furnitureServices.setEwaterHeater(ewaterHeater);
        furnitureServices.setGwaterHeater(gwaterHeater);
        furnitureServices.setTelevision(television);
        furnitureServices.setChannel4(channel4);
        furnitureServices.setSofa(sofa);
        furnitureServices.setTables(tables);

        house.setFurniture(furnitureServices);

        // 設置房屋限制
        ConditionTableBean condition = new ConditionTableBean();
        condition.setPet(pet);
        condition.setParkingSpace(parkingSpace);
        condition.setElevator(elevator);
        condition.setBalcony(balcony);
        condition.setShortTerm(shortTerm);
        condition.setCooking(cooking);
        condition.setWaterDispenser(waterDispenser);
        condition.setManagementFee(fee);
        
        condition.setGenderRestrictions(genderRestrictions); // 性別限制設置
        
        house.setCondition(condition);

       // 處理圖片上傳邏輯（例如儲存圖片）

       return ResponseEntity.ok("房屋資訊已成功儲存");
   }
}
