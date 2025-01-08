package com.example.demo.service;

import com.example.demo.model.UserTableBean;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@Service
public class CouponTaskService {

    private Logger logger = Logger.getLogger(CouponTaskService.class.getName());

    private final CouponService couponService;
    private final UserRepository userRepository;

    public CouponTaskService(CouponService couponService, UserRepository userRepository) {
        this.couponService = couponService;
        this.userRepository = userRepository;
    }

    //秒（0-59） 分鐘（0-59） 小時（0-23） 日（1-31） 月（1-12） 星期（0-6，星期天是0）
    @Scheduled(cron = "0 0 10 1 * *")
    public void sendAnnualCoupons() {
        logger.info("執行發送周年優惠券");
        LocalDateTime currentDateStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime currentDateEnd = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);

        List<UserTableBean> users = userRepository.findUsersByCreateTime(currentDateStart, currentDateEnd);
        for(UserTableBean user : users) {
            boolean result = couponService.addOneCoupon(user.getUserId());
            if(result) {
                logger.info("成功發送優惠券給 user: " + user.getUserId());
            }
        }

        userRepository.saveAll(users);
    }
}
