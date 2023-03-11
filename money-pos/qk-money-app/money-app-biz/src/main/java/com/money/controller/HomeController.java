package com.money.controller;

import com.money.dto.Home.HomeCountVO;
import com.money.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 主页控制器
 * @createTime : 2022-04-28 21:37:53
 */
@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/count")
    public HomeCountVO homeCountVO() {
        return homeService.homeCount();
    }

}
