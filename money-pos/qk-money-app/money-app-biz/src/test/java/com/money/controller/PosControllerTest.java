package com.money.controller;

import com.money.base.ControllerTestBase;
import com.money.dto.OmsOrder.OmsOrderVO;
import com.money.dto.OmsOrderDetail.OmsOrderDetailDTO;
import com.money.dto.Pos.PosGoodsVO;
import com.money.dto.Pos.PosMemberVO;
import com.money.dto.Pos.SettleAccountsDTO;
import com.money.entity.GmsGoods;
import com.money.entity.UmsMember;
import com.money.testdata.GoodsDataService;
import com.money.testdata.MemberDataService;
import com.money.testdata.PosDataService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 收银接口测试
 */
@DisplayName("收银接口测试")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PosControllerTest extends ControllerTestBase {

    @Autowired
    private GoodsDataService goodsData;

    @Autowired
    private MemberDataService memberData;

    @Autowired
    private PosDataService posData;

    @AfterEach
    void tearDown() {
        goodsData.cleanup();
        memberData.cleanup();
        posData.cleanup();
    }

    // ============================================================
    // 商品列表
    // ============================================================

    @Nested
    @DisplayName("商品列表")
    class ListGoodsTests {

        private GmsGoods testGoods;

        @BeforeEach
        void setUp() {
            testGoods = goodsData.createGoods("pos_test");
        }

        @Test
        @DisplayName("成功按条码查询商品")
        void testListGoodsByBarcode() throws Exception {
            var result = get("/pos/goods", Map.of("barcode", testGoods.getBarcode()));
            assertSuccess(result);

            var goodsList = getListData(result, PosGoodsVO.class);
            assertNotNull(goodsList);
            assertEquals(1, goodsList.size());
            assertEquals(testGoods.getBarcode(), goodsList.get(0).getBarcode());
        }

        @Test
        @DisplayName("成功获取所有商品列表")
        void testListGoodsAll() throws Exception {
            var result = get("/pos/goods");
            assertSuccess(result);

            var goodsList = getListData(result, PosGoodsVO.class);
            assertNotNull(goodsList);
            assertTrue(goodsList.size() >= 1);
        }
    }

    // ============================================================
    // 会员列表
    // ============================================================

    @Nested
    @DisplayName("会员列表")
    class ListMemberTests {

        private UmsMember testMember;

        @BeforeEach
        void setUp() {
            testMember = memberData.createMember("pos_test");
        }

        @Test
        @DisplayName("成功按会员名称搜索会员")
        void testListMemberByName() throws Exception {
            var result = get("/pos/members", Map.of("member", testMember.getName()));
            assertSuccess(result);

            var memberList = getListData(result, PosMemberVO.class);
            assertNotNull(memberList);
            assertTrue(memberList.stream().anyMatch(m -> m.getName().equals(testMember.getName())));
        }

        @Test
        @DisplayName("成功获取所有会员列表")
        void testListMemberAll() throws Exception {
            var result = get("/pos/members");
            assertSuccess(result);

            var memberList = getListData(result, PosMemberVO.class);
            assertNotNull(memberList);
            assertTrue(!memberList.isEmpty());
        }
    }

    // ============================================================
    // 收款结算
    // ============================================================

    @Nested
    @DisplayName("收款结算")
    class SettleAccountsTests {

        private GmsGoods testGoods;
        private UmsMember testMember;

        @BeforeEach
        void setUp() {
            testGoods = goodsData.createGoods("settle_test");
            testMember = memberData.createMember("settle_test");
        }

        @Test
        @DisplayName("成功收款（普通订单）")
        void testSettleAccountsSuccess() throws Exception {
            // 准备订单明细
            OmsOrderDetailDTO detailDTO = new OmsOrderDetailDTO();
            detailDTO.setGoodsId(testGoods.getId());
            detailDTO.setGoodsBarcode(testGoods.getBarcode());
            detailDTO.setGoodsName(testGoods.getName());
            detailDTO.setGoodsPrice(testGoods.getSalePrice());
            detailDTO.setQuantity(2);
            detailDTO.setSalePrice(testGoods.getSalePrice());
            detailDTO.setPurchasePrice(testGoods.getPurchasePrice());
            detailDTO.setVipPrice(testGoods.getVipPrice());
            detailDTO.setCoupon(BigDecimal.ZERO);

            SettleAccountsDTO dto = new SettleAccountsDTO();
            dto.setOrderDetail(List.of(detailDTO));

            var result = post("/pos/settleAccounts", dto);
            assertSuccess(result);

            OmsOrderVO order = getData(result, OmsOrderVO.class);
            assertNotNull(order);
            assertNotNull(order.getOrderNo());

            // 记录产生的订单数据用于清理
            posData.recordOrderData(order.getId());
        }

        @Test
        @DisplayName("成功收款（VIP订单）")
        void testSettleAccountsWithVip() throws Exception {
            // 准备订单明细
            OmsOrderDetailDTO detailDTO = new OmsOrderDetailDTO();
            detailDTO.setGoodsId(testGoods.getId());
            detailDTO.setGoodsBarcode(testGoods.getBarcode());
            detailDTO.setGoodsName(testGoods.getName());
            detailDTO.setGoodsPrice(testGoods.getVipPrice());
            detailDTO.setQuantity(1);
            detailDTO.setSalePrice(testGoods.getSalePrice());
            detailDTO.setPurchasePrice(testGoods.getPurchasePrice());
            detailDTO.setVipPrice(testGoods.getVipPrice());
            detailDTO.setCoupon(new BigDecimal("5.00"));

            SettleAccountsDTO dto = new SettleAccountsDTO();
            dto.setMember(testMember.getId());
            dto.setOrderDetail(List.of(detailDTO));

            var result = post("/pos/settleAccounts", dto);
            assertSuccess(result);

            OmsOrderVO order = getData(result, OmsOrderVO.class);
            assertNotNull(order);
            assertNotNull(order.getOrderNo());
            assertTrue(order.getVip());

            // 记录产生的订单数据和修改的会员数据用于清理
            posData.recordOrderData(order.getId());
            posData.recordModifiedMember(testMember.getId());
        }
    }
}
