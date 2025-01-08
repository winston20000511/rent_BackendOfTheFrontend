package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Logger;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.helper.SerialOrderNoUtil;
import com.example.demo.repository.OrderRepository;

@Service
public class SerialOrderNoService {

	private Logger logger = Logger.getLogger(SerialOrderNoService.class.getName());
	
	private final SerialOrderNoUtil serialNoUtil;
	private final OrderRepository orderRepository;
//    private final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    private ScheduledFuture<?> scheduledFuture;

    public SerialOrderNoService(OrderRepository orderRepository) {
    	this.serialNoUtil = SerialOrderNoUtil.getInstance();
    	this.orderRepository = orderRepository;
		loadLatestCounterFromDatabase();
//    	scheduler.initialize();
//    	scheduleMidnightReset();
    	logger.info("SerialOrderNoService 實體化成功！");
    }
    
    /**
     * 與資料庫資料同步
     */
    private void loadLatestCounterFromDatabase() {
    	String todayStr = serialNoUtil.getCurrentDateStr();
		String latestPrefixAndDate = SerialOrderNoUtil.PREFIX + todayStr;

		LocalDateTime startDateTime = LocalDateTime.now().toLocalDate().atStartOfDay();
		LocalDateTime endDateTime = LocalDateTime.now().toLocalDate().atTime(23,59,59,999999);


		Optional<String> optional = orderRepository.findLatestMerchantTradNoByDate(startDateTime, endDateTime);
		if(optional.isPresent()) {
			String latestMerchantTradNo = optional.get();
			logger.info("資料庫中最新的訂單號碼: " + latestMerchantTradNo);

			if(latestMerchantTradNo.startsWith(latestPrefixAndDate)) {
				try{
					int counterValue = Integer.parseInt(latestMerchantTradNo.substring(latestPrefixAndDate.length()));
					serialNoUtil.resetCounter(counterValue);
				}catch(NumberFormatException exception){
					logger.severe("計數器轉換錯誤: " + exception.getMessage());
				}
			}
		}else{
			logger.info("今天第一筆，重置訂單號碼");
			serialNoUtil.resetCounter();
		}
	}
    
    /**
     * 生成訂單流水號
     */
    public CompletableFuture<String> generateSerialNumberAsync() {
		String serialNumber = serialNoUtil.generateSerialNumber();
    	return  CompletableFuture.completedFuture(serialNumber);
    }

    /**
     * Spring Annotation 的實現方法，效果同手動設置的 scheduleMidnightReset()
     * 需要在 main 方法的類別上加上 @EnableScheduling 以啟用定時任務
     */
    @Scheduled(cron="0 0 0 * * *")
    public void resetCounter() {
        serialNoUtil.resetCounter();
        logger.info("計數器已重置，當前值: " + serialNoUtil.getCurrentCounter());
    }
    
    /**
     * 安排每天00:00:00重置計時器
     */
//	@PostConstruct
//    private void scheduleMidnightReset() {
//		String cronExpression = "0 0 0 * * ?";
//		scheduler.schedule(() -> {
//			serialNoUtil.resetCounter();
//			logger.info("計數器已重置，當前值: " + serialNoUtil.getCurrentCounter());
//		}, new CronTrigger(cronExpression));
//    }

    /**
     * 計算當前到零時的毫秒數
     */
//    private long calculateDelayToMidnight() {
//    	LocalDateTime now = LocalDateTime.now();
//    	LocalDateTime midnight = now.toLocalDate().atStartOfDay();
//
//    	if(now.isAfter(midnight)) {
//    		midnight = midnight.plusDays(1);
//    	}
//
//    	return java.time.Duration.between(now, midnight).toMillis();
//    }
    
}
