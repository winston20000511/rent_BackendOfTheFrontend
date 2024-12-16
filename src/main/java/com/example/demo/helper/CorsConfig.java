package com.example.demo.helper;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;




@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173") // 允許所有來源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}




















//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public CorsFilter corsFilter() {
//        CorsConfiguration config = new CorsConfiguration();
//        
//        // 允許的來源
//        config.addAllowedOrigin("http://localhost:5173"); // 允許你的前端域名
//        // 允許的標頭
//        config.addAllowedHeader("*"); // 允許所有 Header
//        // 允許的方法
//        config.addAllowedMethod("*"); // 允許所有 HTTP 方法
//        // 是否允許 cookie 驗證
//        config.setAllowCredentials(true);
//
//        // 設置路徑的 CORS 配置
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }
//	
	
	
	
	
//    @Value("${front.end.host}")
//    private String fronthost;
//
//    @Bean
//    CorsFilter corsFilter() {
//        CorsConfiguration config = new CorsConfiguration();
//        // 允許的來源
//        config.setAllowedOrigins(Arrays.asList(fronthost));
//        // 允許的方法
//        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
//        // 允許的請求 header
//        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
//        // 是否允許 cookie 驗證
//        config.setAllowCredentials(true);
//        
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }

//    @Configuration
//    public class GlobalCorsConfig {
//
//        @Bean
//        public WebMvcConfigurer corsConfigurer() {
//            return new WebMvcConfigurer() {
//                @Override
//                public void addCorsMappings(CorsRegistry registry) {
//                    registry.addMapping("/**") // 匹配所有路由
//                            .allowedOrigins("*") 
//                            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") 
//                            .allowedHeaders("*") // 允许的Header
//                            .allowCredentials(false); // 不允许携带凭据，如Cookie
//                }
//            };
//        }
//    }

    

