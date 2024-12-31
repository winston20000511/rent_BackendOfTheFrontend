package com.example.demo.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AdCreationRequestDTO;
import com.example.demo.dto.AdDetailsResponseDTO;
import com.example.demo.model.AdBean;
import com.example.demo.model.AdtypeBean;
import com.example.demo.model.OrderBean;
import com.example.demo.repository.AdRepository;
import com.example.demo.repository.AdSpecification;
import com.example.demo.repository.AdtypeRepository;
import com.example.demo.repository.HouseRepository;

@Service
public class AdService {

	private Logger logger = Logger.getLogger(AdService.class.getName());

	private AdRepository adRepository;
	private AdtypeRepository adtypeRepository;
	private HouseRepository houseRepository;

	public AdService(AdRepository adRepository, AdtypeRepository adtypeRepository, HouseRepository houseRepository) {
		this.adRepository = adRepository;
		this.adtypeRepository = adtypeRepository;
		this.houseRepository = houseRepository;
	}

	/**
	 * 接收請求資料以在資料庫中建立新的推播服務資料
	 * 
	 * @param userId
	 * @param requestDTO
	 * @return
	 */
	public boolean createAd(Long userId, AdCreationRequestDTO requestDTO) {

		AdBean adBean = new AdBean();
		adBean.setUserId(userId);
		adBean.setHouseId(requestDTO.getHouseId());

		adtypeRepository.findById(requestDTO.getAdtypeId()).map(adtypeBean -> {
			adBean.setAdtypeId(adtypeBean.getAdtypeId());
			adBean.setAdPrice(adtypeBean.getAdPrice());
			return adBean;
		}).orElse(adBean);

		adBean.setIsPaid(false);
		adBean.setIsCouponUsed(0);

		AdBean savedBean = adRepository.save(adBean);
		logger.info("新增的ad id: " + savedBean.getAdId().toString());
		
		return true;
	}

	/**
	 * 更新未付款之推播服務的服務項目
	 * 
	 * @param userId
	 * @param adId
	 * @param newAdtypeId
	 * @return
	 */
	public AdDetailsResponseDTO updateAdtypeByAdId(Long userId, Long adId, Integer newAdtypeId) {

		AdDetailsResponseDTO responseDTO = adRepository.findById(adId).map(ad -> {
			AdtypeBean adtypeBean = adtypeRepository.findById(newAdtypeId)
					.orElseThrow(() -> new RuntimeException("Adtype not found"));
			
			ad.setAdtypeId(newAdtypeId);
			ad.setAdPrice(adtypeBean.getAdPrice());
			ad.setAdtype(adtypeBean);
			AdBean savedAd = adRepository.save(ad);
			
			return setAdDetailResponseDTO(savedAd);
			
		}).orElse(null);

		return responseDTO;
	}

	/**
	 * 以推播服務編號(adId)刪除服務
	 * 
	 * @param userId
	 * @param adId
	 * @return
	 */
	public boolean deleteAdById(Long userId, Long adId) {
		boolean result = adRepository.findById(adId).map(adBean -> {
			adRepository.deleteById(adId);
			return true;
		}).orElse(false);

		return result;
	}

	/**
	 * 取得資料庫中Adtype的資料
	 * 
	 * @return
	 */
	public List<AdtypeBean> findAllAdType() {
		return adtypeRepository.findAll();
	}

	/**
	 * 取得推播服務詳細內容，並驗證是否為該用戶所有
	 * 
	 * @param adId
	 * @return
	 */
	public AdDetailsResponseDTO findAdDetailsByAdId(Long userId, Long adId) {
		AdDetailsResponseDTO responseDTO = adRepository.findById(adId).filter(ad -> ad.getUserId().equals(userId))
				.map(this::setAdDetailResponseDTO).orElse(null);

		return responseDTO;
	}

	/**
	 * 依條件(conditions)篩選出使用者擁有的推播服務：
	 * 
	 * @param userId
	 * @param conditions
	 * @return
	 */
	public Page<AdDetailsResponseDTO> findAdsByConditions(Long userId, Integer pageNumber, String dateRange,
			String paymentStatus, String houseTitle) {
		
		// 建立篩選條件
		Specification<AdBean> spec = AdSpecification.filter(userId, pageNumber, dateRange, paymentStatus, houseTitle);
		
		Pageable pageable = PageRequest.of(pageNumber - 1, 10, Sort.Direction.DESC, "adId");
		Page<AdBean> page = adRepository.findAll(spec, pageable);
		logger.info("find all 找到的資料: " + page.getContent().toString());

		List<AdDetailsResponseDTO> responseDTOs = setAdDetailsResponseDTO(page.getContent());

		logger.info("取到的廣告資料: " + responseDTOs);
		return new PageImpl<>(responseDTOs, pageable, page.getTotalElements());
	}

	/**
	 * 篩選使用者當前擁有的物件中，無申請或無使用推播服務者
	 * 
	 * @param userId, pageNumber
	 * @return
	 */
	public Page<Map<String, Object>> findHousesWithoutAds(Long userId, Integer pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber - 1, 10, Sort.Direction.DESC, "houseId");
		Page<Map<String, Object>> housesInfo = houseRepository.findHousesWithoutAds(userId, pageable);
		return housesInfo;
	}
	
	public List<AdBean> updateAdBeansAfterPaymentVerified(List<AdBean> ads, OrderBean order){
		for(AdBean ad : ads) {
			ad.setPaidDate(LocalDateTime.now());
			ad.setIsPaid(true);
			ad.setOrder(order);
		}
		return ads;
	}
	
	/**
	 * 建立多筆提供給使用者看的 AdDetailsResponseDTO 資料
	 * 
	 * @param ads
	 * @return
	 */
	private List<AdDetailsResponseDTO> setAdDetailsResponseDTO(List<AdBean> ads) {
		List<AdDetailsResponseDTO> responseDTOs = new ArrayList<>();
		for (AdBean ad : ads) {
			AdDetailsResponseDTO responseDTO = new AdDetailsResponseDTO();
			responseDTO.setAdId(ad.getAdId());
			responseDTO.setUserId(ad.getUserId());
			responseDTO.setHouseId(ad.getHouseId());
			responseDTO.setHouseTitle(ad.getHouse().getTitle());
			responseDTO.setAdName(ad.getAdtype().getAdName());
			responseDTO.setAdPrice(ad.getAdPrice());
			responseDTO.setIsPaid(ad.getIsPaid());
			responseDTO.setOrderId(ad.getOrderId());
			responseDTO.setPaidDate(ad.getPaidDate());

			LocalDateTime now = LocalDateTime.now();
			LocalDateTime paidDate = ad.getPaidDate();
			String adName = ad.getAdtype().getAdName();
			String numericPart  = adName.replaceAll("\\D+","");
			
			int days = 0;
			if(!numericPart.isEmpty()) {
				days = Integer.parseInt(numericPart);
				logger.info("天數: " + days);
			}else {
				logger.info("無法解析出數字");
			}
			
			if (ad.getPaidDate() != null) {
				LocalDateTime expiryDate = paidDate.plusDays(days);
				Duration duration = Duration.between(now, expiryDate);
				responseDTO.setRemainingDays(duration.toDays());
			}

			responseDTOs.add(responseDTO);
			logger.severe("廣告詳細 response: " + responseDTO.toString());
		}

		return responseDTOs;
	}

	/**
	 * 建立單筆提供給使用者看的 AdDetailsResponseDTO 資料
	 * 
	 * @param ad
	 * @return
	 */
	private AdDetailsResponseDTO setAdDetailResponseDTO(AdBean ad) {
		AdDetailsResponseDTO responseDTO = new AdDetailsResponseDTO();
		responseDTO.setAdId(ad.getAdId());
		responseDTO.setUserId(ad.getUserId());
		responseDTO.setHouseTitle(ad.getHouse().getTitle());
		responseDTO.setAdName(ad.getAdtype().getAdName());
		responseDTO.setAdPrice(ad.getAdPrice());
		responseDTO.setIsPaid(ad.getIsPaid());
		responseDTO.setOrderId(ad.getOrderId());
		responseDTO.setPaidDate(ad.getPaidDate());
		
		// 計算廣到剩餘時間
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime paidDate = ad.getPaidDate();
		if (ad.getPaidDate() != null) {
			Duration duration = Duration.between(now, paidDate);
			responseDTO.setRemainingDays(duration.toDays());
			responseDTO.setRemainingDays(duration.toDays());
		}

		logger.severe("廣告詳細 response: " + responseDTO.toString());
		
		return responseDTO;
	}

}
