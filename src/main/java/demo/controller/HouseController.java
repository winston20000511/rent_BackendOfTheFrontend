package demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.dto.HouseDetailsDTO;
import demo.service.HouseService;

@RestController
@RequestMapping("/api/houses")
public class HouseController {

    @Autowired
    private HouseService houseService;

    @GetMapping("/{houseId}/details")
    public ResponseEntity<HouseDetailsDTO> getHouseDetails(@PathVariable Long houseId) {
        HouseDetailsDTO houseDetails = houseService.getHouseDetails(houseId);
        return ResponseEntity.ok(houseDetails);
    }
}