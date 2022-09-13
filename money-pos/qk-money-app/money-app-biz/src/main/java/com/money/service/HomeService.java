package com.money.service;

import com.money.dto.home.HomeCountVO;

/**
 * @author : money
 * @version : 1.0.0
 * @description : 主页服务
 * @createTime : 2022-04-28 21:38:43
 */
public interface HomeService {

    /**
     * 主页统计数
     *
     * @return {@link HomeCountVO}
     */
    HomeCountVO homeCount();
}
