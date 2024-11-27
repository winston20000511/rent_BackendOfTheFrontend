package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.HouseDetailsDTO;
import com.example.demo.model.HouseTableBean;
import com.example.demo.repository.HouseRepository;

@Service
public class HouseService {

    @Autowired
    private HouseRepository houseRepository;

    public HouseDetailsDTO getHouseDetails(Long houseId) {
        HouseTableBean house = houseRepository.findById(houseId)
                                     .orElseThrow();

        HouseDetailsDTO houseDetailsDTO = new HouseDetailsDTO();
        houseDetailsDTO.setHouseId(house.getHouseId());


        return houseDetailsDTO;
    }



   public HouseTableBean addHouse(HouseTableBean house) {            

          return  houseRepository.save(house);
        }
    }


