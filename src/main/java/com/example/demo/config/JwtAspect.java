package com.example.demo.config;

import java.util.HashSet;
import java.util.Set;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.demo.helper.JwtUtil;
import com.example.demo.helper.UnTokenException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

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
        EXCLUDED_URIS.add("/api/houses/getPhotos"); //houseController
    }

    @Around("controllerMethods()")
    public Object doBefore(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("進入AOP");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        
        // 取得 Header 中的 Token
        String token = request.getHeader(JwtUtil.TOKEN);
        String requestURI = request.getRequestURI();
        
        // 動態路徑白名單判斷
        if (isExcluded(requestURI)) {
            log.info("白名單請求，直接放行: " + requestURI);
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
                String newToken = JwtUtil.sign(userEmail, userId, name);
                attributes.getResponse().setHeader(JwtUtil.TOKEN, newToken);
            }
        } else {
            // 沒有 Token 時拋出未登入異常
            throw new UnTokenException("您尚未登入，請先登入後再操作。");
        }

        // JWT 驗證成功後，繼續執行原本的 Controller 方法
        return joinPoint.proceed();
    }

    // 動態路徑白名單判斷方法
    private boolean isExcluded(String uri) {
        // 靜態路徑判斷
        if (EXCLUDED_URIS.contains(uri)) {
            return true;
        }
        
        // 動態路徑處理: 判斷 /api/houses/getPhotos/{houseId}
        if (uri.startsWith("/api/houses/getPhotos/")) {
            try {
                String houseId = uri.substring("/api/houses/getPhotos/".length());
                Long.parseLong(houseId); // 驗證 houseId 是否為數字
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        
        return false; // 非白名單
    }
}