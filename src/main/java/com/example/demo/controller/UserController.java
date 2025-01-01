package com.example.demo.controller;

import com.example.demo.dto.UserCenterDTO;
import com.example.demo.dto.UserRegisterDTO;
import com.example.demo.dto.UserUpdateDTO;
import com.example.demo.helper.JwtUtil;
import com.example.demo.model.UserTableBean;
import com.example.demo.service.RecaptchaService;
import com.example.demo.service.EmailService;
import com.example.demo.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UserCenterDTO;
import com.example.demo.dto.UserRegisterDTO;
import com.example.demo.dto.UserSimpleInfoDTO;
import com.example.demo.helper.JwtUtil;
import com.example.demo.model.UserTableBean;
import com.example.demo.service.EmailService;
import com.example.demo.service.RecaptchaService;
import com.example.demo.service.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * RESTful API 控制層，處理使用者相關的 HTTP 請求
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173") // 允許跨域
@Slf4j
public class UserController {

	private Logger logger = Logger.getLogger(UserController.class.getName());

	@Autowired
	private UserService userService;

	@Autowired
	private RecaptchaService recaptchaService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * 登入邏輯，檢查會員是否已停權
	 *
	 * @param loginRequest 登入請求的 JSON 格式
	 * @return 登入成功的 Token 或錯誤訊息
	 */
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
		String email = loginRequest.get("email");
		String password = loginRequest.get("password");
		String recaptchaToken = loginRequest.get("recaptchaToken");

		// 驗證 reCAPTCHA token
		boolean isCaptchaValid = recaptchaService.verifyRecaptcha(recaptchaToken);

		// TODO
		isCaptchaValid = true;

		if (!isCaptchaValid) {
			return ResponseEntity.status(400).body("reCAPTCHA 驗證失敗");
		}

		try {
			log.info("loginRequest {}", loginRequest);
			log.info("email:{}", email);
			UserTableBean user = userService.getUserByEmail(email);
			log.info("user {}", user);
			if (user != null) {
				// 驗證密碼是否匹配
				boolean isPasswordValid = userService.verifyPassword(password, user.getPassword());
				// TODO DEBUG
				isPasswordValid = true;

				if (isPasswordValid) {
					// 檢查帳號狀態

					if (user.getStatus() == 6) {
						// 重新生成 Email 驗證 Token
						String verificationToken = JwtUtil.generateEmailVerificationToken(user.getEmail(),
								user.getUserId());

						// 發送 Email 驗證信
						emailService.sendVerificationEmail(user.getEmail(), user.getName(), verificationToken);

						return ResponseEntity.status(403).body("帳號尚未驗證，已重新發送驗證信，請檢查您的 Email。");
					} else if (user.getStatus() != 1) {
						throw new RuntimeException("帳號已停用或停權，無法登入");
					}

					// 密碼匹配且狀態正常，生成登入用 JWT
					String token = JwtUtil.sign(user.getEmail(), user.getUserId(), user.getName());

					// 返回 Token 和用戶基本資料
					Map<String, Object> response = new HashMap<>();
					response.put("token", token);
					response.put("userId", user.getUserId());
					response.put("email", user.getEmail());
					response.put("name", user.getName());
					log.info("登入成功");

					return ResponseEntity.ok(response);
				}
				log.info("密碼輸入錯誤");
			}
			log.error("登入失敗qq");
			// 密碼錯誤或帳號不存在
			return ResponseEntity.status(401).body("帳號或密碼錯誤");
		} catch (RuntimeException e) {
			log.error("登入失敗，原因：{}", e.getMessage());
			return ResponseEntity.status(401).body(e.getMessage());
		}
	}

	/**
	 * 使用者註冊 API
	 *
	 * @param userRegisterDTO 註冊資料
	 * @return 註冊結果
	 */
	@PostMapping("/register")
	public ResponseEntity<Map<String, Object>> registerUser(@RequestBody UserRegisterDTO userRegisterDTO) {
		log.info("收到註冊請求：{}", userRegisterDTO);
		String result = userService.registerUser(userRegisterDTO);

		// 構建響應對象
		Map<String, Object> response = new HashMap<>();
		if ("註冊成功".equals(result)) {
			response.put("status", "success");
			response.put("message", result);
			return ResponseEntity.ok(response);
		} else {
			response.put("status", "error");
			response.put("message", result);
			return ResponseEntity.badRequest().body(response);
		}
	}

	/**
	 * 獲取會員中心資料
	 *
	 * @param authorization HTTP 標頭中的 JWT Token
	 * @return 會員中心資料的 JSON
	 */
	@PostMapping("/userCenter")
	public ResponseEntity<UserCenterDTO> getUserCenterData(@RequestHeader("Authorization") String authorization) {
		log.info("收到獲取會員中心資料的請求");

		try {
			UserCenterDTO userCenterDTO = userService.getUserCenterData(authorization);
			return ResponseEntity.ok(userCenterDTO);
		} catch (RuntimeException e) {
			log.error("獲取會員中心資料失敗，原因: {}", e.getMessage());
			return ResponseEntity.status(401).body(null);
		}
	}

	/**
	 * 會員簡歷查詢 API (POST 方法)
	 *
	 * @param authorization HTTP 標頭中的 JWT Token
	 * @return 會員基本資料的 JSON 格式
	 */
	@PostMapping("/userSimpleInfo")
	public ResponseEntity<UserSimpleInfoDTO> getUserSimpleInfo(@RequestHeader("Authorization") String authorization) {
		log.info("收到會員簡歷查詢請求");

		try {
			// 從 Service 層取得會員基本資料
			UserSimpleInfoDTO userSimpleInfo = userService.getUserSimpleInfo(authorization);
			return ResponseEntity.ok(userSimpleInfo); // 返回成功結果
		} catch (RuntimeException e) {
			log.error("會員簡歷查詢失敗，原因: {}", e.getMessage());
			return ResponseEntity.status(401).body(null); // 返回失敗結果
		}
	}

	// @PostMapping("/userPicture")
	// public ResponseEntity<byte[]> downloadPhotos(@RequestHeader("Authorization")
	// String authorization) {
	//
	// UserSimpleInfoDTO userSimpleInfo =
	// userService.getUserSimpleInfo(authorization);
	// byte[] photoByte = userSimpleInfo.getPicture();
	// HttpHeaders headers = new HttpHeaders();
	// headers.setContentType(MediaType.IMAGE_JPEG);
	// return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
	// }

	/**
	 * 更新會員資料的 API
	 *
	 * @param authorization JWT Token 用於驗證身份
	 * @param updateRequest 更新資料的 JSON 格式
	 * @return 更新後的會員中心資料
	 */
	@PutMapping("/update")
	public ResponseEntity<String> updateProfile(
			@RequestHeader("Authorization") String authorization,
			@RequestBody UserTableBean updateRequest) {
		try {
			System.out.println("收到更新請求：" + updateRequest);
			userService.updateUserProfile(authorization, updateRequest);
			System.out.println("會員資料更新成功，圖片更新成功：" + (updateRequest.getPicture() != null));
			return ResponseEntity.ok("會員資料更新成功");
		} catch (RuntimeException e) {
			System.err.println("會員資料更新失敗：" + e.getMessage());
			return ResponseEntity.badRequest().body("會員資料更新失敗：" + e.getMessage());
		}
	}

	/**
	 * 停用帳號（會員自行停權）的 API
	 *
	 * @param authorization JWT Token 用於驗證身份
	 * @return 停用結果
	 */
	@PutMapping("/deactivate")
	public ResponseEntity<String> deactivateAccount(@RequestHeader("Authorization") String authorization) {
		log.info("收到停用帳號請求");

		try {
			// 停用帳號，並設置狀態為 0
			userService.deactivateAccount(authorization);
			return ResponseEntity.ok("帳號已成功停用，狀態設為 0");
		} catch (RuntimeException e) {
			log.error("停用帳號失敗，原因：{}", e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/verifyEmail")
	public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
		try {
			// 驗證 Token 並獲取 Email
			String[] decodedToken = JwtUtil.verifyEmailToken(token);
			String email = decodedToken[0];

			// 更新用戶狀態
			userService.verifyEmail(email);

			return ResponseEntity.ok("驗證成功！請返回首頁重新登入。");
		} catch (RuntimeException e) {
			log.error("Email 驗證失敗，原因：{}", e.getMessage());
			return ResponseEntity.status(400).body("驗證失敗：" + e.getMessage());
		}
	}

	@PostMapping("/updatePassword")
	public ResponseEntity<String> updatePassword(@RequestHeader("Authorization") String authorization,
			@RequestBody Map<String, String> request) {
		String[] decodedToken = JwtUtil.verify(authorization);
		String email = decodedToken[0];
		String oldPassword = request.get("oldPassword");
		String newPassword = request.get("newPassword");
		boolean success = userService.updatePassword(email, oldPassword, newPassword);
		if (success) {
			return ResponseEntity.ok("Password updated successfully.");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email or old password.");
		}
	}

	/**
	 * 會員簡歷查詢 API (POST 方法)
	 *
	 * @param authorization HTTP 標頭中的 JWT Token
	 * @return 會員基本資料的 JSON 格式
	 */

	/**
	 * 更新會員資料的 API
	 *
	 * @param authorization JWT Token 用於驗證身份
	 * @param updateRequest 更新資料的 JSON 格式
	 * @return 更新後的會員中心資料
	 */
	@PostMapping("/update")
	public ResponseEntity<String> updateUserProfile(
			@RequestHeader("Authorization") String authorization,
			@RequestBody UserUpdateDTO updateRequest) {
		log.info("收到會員資料更新請求：{}", updateRequest);

		try {
			userService.updateUserProfile(authorization, updateRequest);
			return ResponseEntity.ok("會員資料更新成功！");
		} catch (RuntimeException e) {
			log.error("會員資料更新失敗，原因：{}", e.getMessage());
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}