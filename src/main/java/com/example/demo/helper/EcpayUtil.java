package com.example.demo.helper;

import ecpay.payment.integration.ecpayOperator.EcpayFunction;

public class EcpayUtil {
	
	public static String getCheckMacValue(String hashKey, String hashIV, Object orderObject) {
		String checkMacValue = EcpayFunction.genCheckMacValue(hashKey, hashIV, orderObject);
		return checkMacValue;
	}
}
