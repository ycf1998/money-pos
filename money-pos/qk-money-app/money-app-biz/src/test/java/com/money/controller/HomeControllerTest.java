package com.money.controller;

import com.money.base.ControllerTestBase;
import com.money.dto.Home.HomeCountVO;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 主页接口测试
 */
@DisplayName("主页接口测试")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HomeControllerTest extends ControllerTestBase {

    // ============================================================
    // 主页统计
    // ============================================================

    @Nested
    @DisplayName("主页统计")
    class HomeCountTests {

        @Test
        @DisplayName("成功获取主页统计数据")
        void testHomeCount() throws Exception {
            var result = get("/home/count");
            assertSuccess(result);

            HomeCountVO homeCountVO = getData(result, HomeCountVO.class);
            assertNotNull(homeCountVO);
            // 验证包含 today, month, year, total, inventoryValue 等字段
            assertNotNull(homeCountVO.getToday());
            assertNotNull(homeCountVO.getMonth());
            assertNotNull(homeCountVO.getYear());
            assertNotNull(homeCountVO.getTotal());
            assertNotNull(homeCountVO.getInventoryValue());
        }
    }
}
