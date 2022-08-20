package com.money.web.orderlog;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* <p>
* 订单操作日志
* </p>
*
* @author money
* @since 2022-04-26
*/
@Data
public class OrderLogVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
    * 订单id
    */
    private Long orderId;

    /**
    * 描述
    */
    private String description;


    private String createBy;

    private LocalDateTime createTime;


}
