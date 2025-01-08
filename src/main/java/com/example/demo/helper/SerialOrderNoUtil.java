package com.example.demo.helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class SerialOrderNoUtil {

	private static final SerialOrderNoUtil instance = new SerialOrderNoUtil();

	public static String PREFIX = "EE189"; 
	private static String currentDateStr; // 紀錄當前時間
	private final AtomicInteger counter = new AtomicInteger(0);

	private SerialOrderNoUtil() {
		currentDateStr = getCurrentDateTimeStr();
	}

	public static SerialOrderNoUtil getInstance() {
		return instance;
	}

	/**
	 *生成流水號：前綴 + 當前日期 + 當日計數器
	 */
	public String generateSerialNumber() {
		String today = getCurrentDateTimeStr();
		if(!today.startsWith(currentDateStr)){
			resetCounter();
		}

		int currentCount = counter.incrementAndGet();
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
	public void resetCounter() {
		counter.set(0);
	}

	/**
	 * 重置計數器為當前最新值
	 */
	public void resetCounter(int counterValue) {
		counter.set(counterValue);
	}

	public AtomicInteger getCurrentCounter() {
		return this.counter;
	}

}
