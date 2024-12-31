package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.demo.helper.JwtUtil;
import com.example.demo.helper.UnTokenException;

import java.util.*;

@Component // 將此類別交由Spring容器管理
@Aspect    // 標示這是一個切面類別，用於AOP（面向切面編程）
@Slf4j
public class JwtAspect {

    // 定義不需要 JWT 驗證的路徑集合
    private static final Set<String> EXCLUDED_URIS = new HashSet<>();

    static {
        EXCLUDED_URIS.add("/api/user/login");    // 登入頁面
        EXCLUDED_URIS.add("/api/user/register"); // 註冊頁面
        EXCLUDED_URIS.add("/api/user/verifyEmail"); // 註冊認證
        EXCLUDED_URIS.add("/api/user/google/login"); // 第三方登入
        EXCLUDED_URIS.add("/api/user/google/callback"); // 第三方登入
        EXCLUDED_URIS.add("/"); // 首頁
        EXCLUDED_URIS.add("/public/api");        // 未來新增的公共頁面
        EXCLUDED_URIS.add("/api/test"); //SearchController
        EXCLUDED_URIS.add("/api/map"); //SearchController
        EXCLUDED_URIS.add("/api/ads"); //SearchController
        EXCLUDED_URIS.add("/api/draw"); //SearchController
        EXCLUDED_URIS.add("/fake"); //SearchController
        EXCLUDED_URIS.add("/api/keyword"); //SearchController
        EXCLUDED_URIS.add("/api/ecpay/verify/checkvalue"); //EcpayController
        EXCLUDED_URIS.add("/api/linepay/request"); //LinepayController
        EXCLUDED_URIS.add("/api/houses/getPhotos/{houseId}"); //houseController
    }


    //  除了排除在外的controller，其餘controller都需要進到JWT驗證
    @Pointcut("execution(public * com.example.demo.controller..*(..))")
    public void controllerMethods() {}

    /**
     * 環繞通知，對 Controller 方法進行攔截並添加 JWT 驗證邏輯
     * @param joinPoint 方法的執行點
     * @return 方法執行結果
     * @throws Throwable 當 JWT 驗證失敗時拋出異常
     */
    @Around("controllerMethods()")
    public Object doBefore(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("進入AOP");
        // 取得 HTTP 請求屬性
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 從 Header 中取得 Token
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String headerName = headers.nextElement();
            String headerValue = request.getHeader(headerName);
            log.info(headerName + ": " + headerValue);
        }
        List<String> x = new ArrayList<>();

        String token = request.getHeader(JwtUtil.TOKEN);
        // 取得當前請求的 URI
        String requestURI = request.getRequestURI();

        // 如果請求的 URI 在排除列表中，直接放行
        if (EXCLUDED_URIS.contains(requestURI)) {
            return joinPoint.proceed();
        }

        // 驗證 Token 是否存在
        if (token != null) {
            String[] jwt = JwtUtil.verify(token);
            String userEmail = jwt[0];
            Long userId = Long.parseLong(jwt[1]);
            String name = jwt[2];

            if (userEmail == null) {
                throw new UnTokenException("無效的 Token，請重新登入。");
            }
            // 檢查 Token 是否需要更新
            if (JwtUtil.isNeedUpdate(token)) {
                String newToken = JwtUtil.sign(userEmail,userId,name);
                attributes.getResponse().setHeader(JwtUtil.TOKEN, newToken);
            }
        } else {
            // 沒有 Token 時拋出未登入異常
            throw new UnTokenException("您尚未登入，請先登入後再操作。");
        }

        // JWT 驗證成功後，繼續執行原本的 Controller 方法
        return joinPoint.proceed();
    }


}