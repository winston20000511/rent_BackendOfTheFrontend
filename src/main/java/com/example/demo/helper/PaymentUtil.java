package com.example.demo.helper;

import java.util.Base64;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

public final class PaymentUtil {

	    private PaymentUtil(){}

	    // LINEPAY
	    public static String encryptLinepayRequest(final String channelSecret, final String requestUri, final String queryOrBody, final String nonce) {
	        String data = channelSecret + requestUri + queryOrBody + nonce;
	        byte[] bytes = HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_256, channelSecret.getBytes()).doFinal(data.getBytes());
	        return Base64.getEncoder().encodeToString(bytes);
	    }
}
