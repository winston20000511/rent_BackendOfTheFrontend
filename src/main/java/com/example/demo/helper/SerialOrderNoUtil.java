package com.example.demo.helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

public class SerialOrderNoUtil {
	
	public static String PREFIX = "EE189"; 
	private static String currentDateStr; // 紀錄當前時間
	
	/**
	 *生成流水號：前綴 + 當前日期 + 當日計數器
	 */
	public String generateSerialNumber(AtomicInteger counter) {
		String today = getCurrentDateTimeStr();
		int currentCount = counter.incrementAndGet(); // 遞增並返回新值
		return PREFIX + today + String.format("%03d", currentCount);
	}
	
	/**
	 * 取得當日日期，格式：年月日時分
	 */
	public String getCurrentDateTimeStr() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
		return now.format(formatter);
	}
	
	/**
	 * 取得當日日期，格式：年月日
	 */
	public String getCurrentDateStr() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		return now.format(formatter);
	}
	
	/**
	 * 重置計數器
	 */
	public void resetCounter(AtomicInteger counter) {
		counter.set(0);
		currentDateStr = getCurrentDateTimeStr();
	}
}
