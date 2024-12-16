package com.example.demo.helper;

//import org.apache.commons.codec.binary.Base64;

import java.util.Base64;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

public final class HMACUtil {

	    private HMACUtil(){}

	    public static String encrypt(final String channelSecret, final String requestUri, final String queryOrBody, final String nonce) {
	        String data = channelSecret + requestUri + queryOrBody + nonce;
	        byte[] bytes = HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_256, channelSecret.getBytes()).doFinal(data.getBytes());
	        return Base64.getEncoder().encodeToString(bytes);
	    }
	    
}
