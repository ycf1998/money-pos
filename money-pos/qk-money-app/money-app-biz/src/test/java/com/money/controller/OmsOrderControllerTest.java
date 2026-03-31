package com.money.controller;

import com.money.base.ControllerTestBase;
import com.money.dto.OmsOrder.OmsOrderVO;
import com.money.dto.OmsOrder.OrderCountVO;
import com.money.dto.OmsOrder.OrderDetailVO;
import com.money.dto.OmsOrder.ReturnGoodsDTO;
import com.money.entity.GmsGoods;
import com.money.entity.OmsOrder;
import com.money.entity.OmsOrderDetail;
import com.money.entity.UmsMember;
import com.money.mapper.OmsOrderMapper;
import com.money.testdata.GoodsDataService;
import com.money.testdata.MemberDataService;
import com.money.testdata.OrderDataService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 订单接口测试
 */
@DisplayName("订单接口测试")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OmsOrderControllerTest extends ControllerTestBase {

    @Autowired
    private OrderDataService orderData;

    @Autowired
    private GoodsDataService goodsData;

    @Autowired
    private MemberDataService memberData;

    @Autowired
    private OmsOrderMapper omsOrderMapper;

    @AfterEach
    void tearDown() {
        orderData.cleanup();
        goodsData.cleanup();
        memberData.cleanup();
    }

    // ============================================================
    // 分页查询订单
    // ============================================================

    @Nested
    @DisplayName("分页查询订单")
    class ListOrdersTests {

        @BeforeEach
        void setUp() {
            for (int i = 0; i < 5; i++) {
                orderData.createOrder("page_" + i);
            }
        }

        @Test
        @DisplayName("成功分页查询订单列表")
        void testListOrders() throws Exception {
            var result = get("/oms/order", Map.of("page", "1", "size", "2"));
            assertSuccess(result);

            var pageData = getPageData(result, OmsOrderVO.class);
            assertEquals(2, pageData.getRecords().size());
            assertTrue(pageData.getTotal() >= 5);
        }

        @Test
        @DisplayName("成功按订单状态搜索")
        void testListOrdersByStatus() throws Exception {
            OmsOrder order = orderData.createOrder("search");
            order.setStatus("COMPLETED");
            omsOrderMapper.updateById(order);

            var result = get("/oms/order", Map.of(
                    "page", "1", "size", "10", "status", "COMPLETED"
            ));
            assertSuccess(result);

            var pageData = getPageData(result, OmsOrderVO.class);
            assertTrue(pageData.getRecords().stream()
                    .anyMatch(o -> o.getOrderNo().equals(order.getOrderNo())));
        }
    }

    // ============================================================
    // 订单统计
    // ============================================================

    @Nested
    @DisplayName("订单统计")
    class OrderCountTests {

        @BeforeEach
        void setUp() {
            for (int i = 0; i < 3; i++) {
                orderData.createOrder("count_" + i);
            }
        }

        @Test
        @DisplayName("成功获取订单统计信息")
        void testOrderCount() throws Exception {
            String startTime = "2026-04-01 00:00:00";
            String endTime = "2026-04-30 23:59:59";

            var result = get("/oms/order/count", Map.of(
                    "startTime", startTime,
                    "endTime", endTime
            ));
            assertSuccess(result);

            OrderCountVO countVO = getData(result, OrderCountVO.class);
            assertNotNull(countVO);
        }
    }

    // ============================================================
    // 订单详情
    // ============================================================

    @Nested
    @DisplayName("订单详情")
    class OrderDetailTests {

        private OmsOrder testOrder;

        @BeforeEach
        void setUp() {
            testOrder = orderData.createOrder("detail");
        }

        @Test
        @DisplayName("成功获取订单详情")
        void testOrderDetail() throws Exception {
            var result = get("/oms/order/detail", Map.of("id", testOrder.getId().toString()));
            assertSuccess(result);

            OrderDetailVO detailVO = getData(result, OrderDetailVO.class);
            assertNotNull(detailVO);
            assertNotNull(detailVO.getOrder());
            assertEquals(testOrder.getOrderNo(), detailVO.getOrder().getOrderNo());
        }
    }

    // ============================================================
    // 退单
    // ============================================================

    @Nested
    @DisplayName("退单")
    class ReturnOrderTests {

        @Test
        @DisplayName("成功退单")
        void testReturnOrderSuccess() throws Exception {
            OmsOrder order = orderData.createOrder("return");

            var result = delete("/oms/order/returnOrder", Set.of(order.getId()));
            assertSuccess(result);
        }
    }

    // ============================================================
    // 退货
    // ============================================================

    @Nested
    @DisplayName("退货")
    class ReturnGoodsTests {

        private OmsOrderDetail orderDetail;

        @BeforeEach
        void setUp() {
            // 通过 DataService 直接插表创建完整订单
            GmsGoods goods = goodsData.createGoods("return_goods");
            UmsMember member = memberData.createMember("return_goods");
            
            orderDetail = orderData.createOrderWithDetail("return_goods", goods, member, 2);
        }

        @Test
        @DisplayName("成功退货")
        void testReturnGoodsSuccess() throws Exception {
            ReturnGoodsDTO dto = new ReturnGoodsDTO();
            dto.setId(orderDetail.getId());
            dto.setQuantity(1);

            var result = delete("/oms/order/returnGoods", dto);
            assertSuccess(result);
        }
    }
}
