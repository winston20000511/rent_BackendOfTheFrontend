package demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.dto.HouseDetailsDTO;
import demo.model.HouseTableBean;
import demo.repository.HouseRepository;

@Service
public class HouseService {

    @Autowired
    private HouseRepository houseRepository;

    public HouseDetailsDTO getHouseDetails(Long houseId) {
        HouseTableBean house = houseRepository.findById(houseId)
                                     .orElseThrow();

        HouseDetailsDTO houseDetailsDTO = new HouseDetailsDTO();
        houseDetailsDTO.setHouseId(house.getHouse_id());
        houseDetailsDTO.setFurnitureList(house.getFurnitureList());
        houseDetailsDTO.setHouseRestrictionsList(house.getHouseRestrictionsList());
        houseDetailsDTO.setAddress(house.getAddress());

        return houseDetailsDTO;
    }
}
