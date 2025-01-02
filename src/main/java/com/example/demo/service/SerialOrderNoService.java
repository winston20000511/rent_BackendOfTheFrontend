package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import com.example.demo.helper.SerialOrderNoUtil;
import com.example.demo.repository.OrderRepository;

@Service
public class SerialOrderNoService {

	private Logger logger = Logger.getLogger(SerialOrderNoService.class.getName());
	
	private final SerialOrderNoUtil serialNoUtil;
	private OrderRepository orderRepository;
    private final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    private ScheduledFuture<?> scheduledFuture;
	private static AtomicInteger counter = new AtomicInteger(0);  // 計數器

    public SerialOrderNoService(OrderRepository orderRepository) {
    	this.serialNoUtil = new SerialOrderNoUtil();
    	this.orderRepository = orderRepository;
//		loadLatestCounterFromDatabase();
    	scheduler.initialize();
//    	scheduleMidnightReset();
    	resetCounter();
    	logger.info("SerialOrderNoService 實體化成功！");
    }
    
    /**
     * 與資料庫資料同步
     */
    private void loadLatestCounterFromDatabase() {
    	
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59, 999999);
    	
    	String todayStr = serialNoUtil.getCurrentDateStr();
    	String latestPredixAndDate = SerialOrderNoUtil.PREFIX + todayStr;
    	Optional<String> merchantTradNoList = orderRepository.findLatestMerchantTradNoByDate(startOfDay, endOfDay);
    	logger.severe("最新的訂單號碼LIST: " + merchantTradNoList);
    	
    	String latestMerchantTradNo;
    	if(merchantTradNoList.isEmpty()) {
    		latestMerchantTradNo = null;
    	}else {
    		latestMerchantTradNo = merchantTradNoList.get();
    		logger.severe("最新的訂單號碼: " + latestMerchantTradNo);
    	}
    	
    	if(latestMerchantTradNo != null && latestMerchantTradNo.startsWith(latestPredixAndDate)) {
        	try {
        		String counterStr = latestMerchantTradNo.substring((latestPredixAndDate).length());
        		counter = new AtomicInteger(Integer.parseInt(counterStr));
        	}catch(NumberFormatException exception) {
        		exception.getStackTrace();
        	}
    	}else {
    		counter = new AtomicInteger(0);
    	}
    }
    
    /**
     * 取得計數器
     */
    public int getCurrentCounter() {
    	return counter.get();
    }
    
    /**
     * 生成訂單流水號
     */
    public String generateSerialNumber() {
    	return serialNoUtil.generateSerialNumber(counter);
    }
    
    /**
     * Spring Annotation 的實現方法，效果同手動設置的 scheduleMidnightReset()
     * 需要在 main 方法的類別上加上 @EnableScheduling 以啟用定時任務
     */
    @Scheduled(cron="0 0 0 * * *")
    public void resetCounter() {
    	serialNoUtil.resetCounter(counter);
    	logger.info("計數器已重置，當前值: " + counter);
    }
    
    
    /**
     * 安排每天00:00:00重置計時器
     */
//    private void scheduleMidnightReset() {
//    	long delay = calculateDelayToMidnight();
//    	Instant initialDelay = Instant.now().plusMillis(delay); // 取得首次執行 Instant 的時間
//    	
//    	scheduledFuture = scheduler.scheduleAtFixedRate(() -> {
//    		serialNoUtil.resetCounter(counter);  // 重置計數器
//    		logger.info("計數器已重置，當前值: " + counter);
//        }, initialDelay, Duration.ofDays(1));  // 每 24 小時確保重置一次，從零點開始
//    }
    
    /**
     * 計算當前到零時的毫秒數
     */
    private long calculateDelayToMidnight() {
    	LocalDateTime now = LocalDateTime.now();
    	LocalDateTime midnight = now.toLocalDate().atStartOfDay();
    	
    	if(now.isAfter(midnight)) {
    		midnight = midnight.plusDays(1);
    	}
    	
    	return java.time.Duration.between(now, midnight).toMillis();
    }
    
}
