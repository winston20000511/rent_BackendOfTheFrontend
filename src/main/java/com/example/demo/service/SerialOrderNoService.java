package com.example.demo.service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Logger;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import com.example.demo.helper.SerialOrderNoUtil;

@Service
public class SerialOrderNoService {

	private Logger logger = Logger.getLogger(SerialOrderNoService.class.getName());
	
	private final SerialOrderNoUtil serialNoUtil = new SerialOrderNoUtil();
    private final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    private ScheduledFuture<?> scheduledFuture;

    public SerialOrderNoService() {
    	scheduler.initialize();
    	scheduleMidnightReset();
    }
    
    /**
     * 安排每天00:00:00重置計時器
     */
    private void scheduleMidnightReset() {
    	long delay = calculateDelayToMidnight();
    	Instant initialDelay = Instant.now().plusMillis(delay); // 取得首次執行 Instant 的時間
    	
    	scheduledFuture = scheduler.scheduleAtFixedRate(() -> {
    		serialNoUtil.resetCounter();  // 重置計數器
            System.out.println("計數器已重置，當前值: " + serialNoUtil.getCounter());
        }, initialDelay, Duration.ofDays(1));  // 每 24 小時確保重置一次，從零點開始
    }
    
    /**
     * Spring Annotation 的實現方法，效果同手動設置的 scheduleMidnightReset()
     * 需要在 main 方法的類別上加上 @EnableScheduling 以啟用定時任務
     */
    @Scheduled(cron="0 0 0 * * *")
    public void resetCounter() {
    	serialNoUtil.resetCounter();
    	logger.severe("計數器已重置，當前值: " + serialNoUtil.getCounter());
    }
    
    /**
     * 生成訂單流水號
     */
    public String generateSerialNumber() {
    	return serialNoUtil.generateSerialNumber();
    }
    
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
