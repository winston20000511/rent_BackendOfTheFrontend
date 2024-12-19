package com.example.demo.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class SerialOrderNoUtil {
	
	private static AtomicInteger counter = new AtomicInteger(0);  // 計數器
	private static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor();
	private static String currentDate; // 紀錄當前時間
	private static String PREFIX = "ORDEE189"; 

	public SerialOrderNoUtil() {
		this.currentDate = getCurrentDate();
	}
	
	/**
	 *生成流水號：前綴 + 當前日期 + 當日計數器
	 */
	public String generateSerialNumber() {
		String currentDateTime = getCurrentDateStr();
		int currentCount = counter.incrementAndGet(); // 遞增並返回新值
		return PREFIX + currentDateTime + String.format("%04d", currentCount);
	}
	
	/**
	 * 取得當日日期，格式：年月日
	 */
	private String getCurrentDate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		return simpleDateFormat.format(Calendar.getInstance().getTime());
	}
	
	/**
	 * 取得訂單格式所需的當日日期，格式：年月日時分
	 */
	private String getCurrentDateStr() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
		return simpleDateFormat.format(Calendar.getInstance().getTime());
	}
	
	/**
	 * 重置計數器
	 */
	public void resetCounter() {
		counter.set(0);
		currentDate = getCurrentDate();
	}
	
    public int getCounter() {
        return counter.get();
    }


//	private Logger logger = Logger.getLogger(SerialOrderNoUtil.class.getName());
//	public void checkDelayToMidnight() {
//		// 計算下一次零點的時間
//		Calendar nextRun = Calendar.getInstance(TimeZone.getTimeZone("Asia/Taipei"));
//		nextRun.set(Calendar.HOUR_OF_DAY, 0);
//		nextRun.set(Calendar.MINUTE, 0);
//		nextRun.set(Calendar.SECOND, 0);
//		nextRun.set(Calendar.MILLISECOND, 0);
//
//		// 如果當前時間已經過了零點，則設置為明天的零點
//		if (nextRun.before(Calendar.getInstance())) {
//			nextRun.add(Calendar.DATE, 1);
//		}
//
//		// 計算到下一次零點的剩餘時間（毫秒）
//		long remainingTime = nextRun.getTimeInMillis() - System.currentTimeMillis();
//		logger.severe("距離零點還有 " + remainingTime / 1000 + " 秒");
//
//		// 使用ScheduledExecutorService安排在零點執行重置計數器
//		SCHEDULER.schedule(() -> {
//			resetCounter(); // 重置計數器
//			checkDelayToMidnight();
//		}, remainingTime, TimeUnit.MILLISECONDS); // 在剩餘時間後執行
//	}
//
//
//	// 計數器重置方法
//	private void resetCounter() {
//		counter.set(0);
//		logger.severe("計數器已重置，當前值為: " + counter);
//	}
//
//	// 模擬計數器增長
//	public void incrementCounter() {
//		counter.incrementAndGet();
//		logger.severe("計數器增長，當前值為: " + counter);
//	}
}
