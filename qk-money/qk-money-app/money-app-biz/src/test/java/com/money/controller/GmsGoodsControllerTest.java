package com.money.controller;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.money.base.ControllerTestBase;
import com.money.dto.GmsGoods.GmsGoodsDTO;
import com.money.dto.GmsGoods.GmsGoodsVO;
import com.money.entity.GmsGoods;
import com.money.mapper.GmsGoodsMapper;
import com.money.testdata.GoodsDataService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 商品接口测试
 */
@DisplayName("商品接口测试")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GmsGoodsControllerTest extends ControllerTestBase {

    @Autowired
    private GoodsDataService goodsData;

    @Autowired
    private GmsGoodsMapper gmsGoodsMapper;

    @AfterEach
    void tearDown() {
        goodsData.cleanup();
    }

    // ============================================================
    // 分页查询商品
    // ============================================================

    @Nested
    @DisplayName("分页查询商品")
    class ListGoodsTests {

        @BeforeEach
        void setUp() {
            for (int i = 0; i < 5; i++) {
                goodsData.createGoods("page_" + i);
            }
        }

        @Test
        @DisplayName("成功分页查询商品列表")
        void testListGoods() throws Exception {
            var result = get("/gms/goods", Map.of("page", "1", "size", "2"));
            assertSuccess(result);

            var pageData = getPageData(result, GmsGoodsVO.class);
            assertEquals(2, pageData.getRecords().size());
            assertTrue(pageData.getTotal() >= 5);
        }

        @Test
        @DisplayName("成功按商品名称搜索")
        void testListGoodsByName() throws Exception {
            GmsGoods goods = goodsData.createGoods("search");

            var result = get("/gms/goods", Map.of(
                    "page", "1", "size", "10", "name", goods.getName()
            ));
            assertSuccess(result);

            var pageData = getPageData(result, GmsGoodsVO.class);
            assertTrue(pageData.getRecords().stream()
                    .anyMatch(g -> g.getName().equals(goods.getName())));
        }
    }

    // ============================================================
    // 添加商品
    // ============================================================

    @Nested
    @DisplayName("添加商品")
    class AddGoodsTests {

        @AfterEach
        void tearDown() {
            new LambdaQueryChainWrapper<>(gmsGoodsMapper)
                    .likeRight(GmsGoods::getName, "test_")
                    .list()
                    .forEach(g -> gmsGoodsMapper.deleteById(g.getId()));
        }

        @Test
        @DisplayName("成功添加商品（无图片）")
        void testAddGoodsWithoutPic() throws Exception {
            GmsGoodsDTO dto = new GmsGoodsDTO();
            dto.setBrandId(1L);
            dto.setCategoryId(1L);
            dto.setBarcode("test_barcode_" + System.currentTimeMillis() % 10000);
            dto.setName("test_goods_" + System.currentTimeMillis() % 10000);
            dto.setUnit("个");
            dto.setSize("标准规格");
            dto.setDescription("新商品描述");
            dto.setPurchasePrice(new BigDecimal("10.00"));
            dto.setSalePrice(new BigDecimal("20.00"));
            dto.setVipPrice(new BigDecimal("18.00"));
            dto.setCoupon(new BigDecimal("2.00"));
            dto.setStock(100L);

            var result = multipart(HttpMethod.POST, "/gms/goods", "goods", dto, null, null);
            assertSuccess(result);

            GmsGoods created = new LambdaQueryChainWrapper<>(gmsGoodsMapper)
                    .eq(GmsGoods::getName, dto.getName())
                    .one();
            assertNotNull(created);
        }

        @Test
        @DisplayName("成功添加商品（带图片）")
        void testAddGoodsWithPic() throws Exception {
            GmsGoodsDTO dto = new GmsGoodsDTO();
            dto.setBrandId(1L);
            dto.setCategoryId(1L);
            dto.setBarcode("test_pic_" + System.currentTimeMillis() % 10000);
            dto.setName("test_pic_goods_" + System.currentTimeMillis() % 10000);
            dto.setUnit("个");
            dto.setSize("标准规格");
            dto.setDescription("带图片商品");
            dto.setPurchasePrice(new BigDecimal("15.00"));
            dto.setSalePrice(new BigDecimal("25.00"));
            dto.setVipPrice(new BigDecimal("22.00"));
            dto.setCoupon(new BigDecimal("3.00"));
            dto.setStock(50L);

            var result = multipart(HttpMethod.POST, "/gms/goods", "goods", dto, "pic", "fake image".getBytes());
            assertSuccess(result);

            GmsGoods created = new LambdaQueryChainWrapper<>(gmsGoodsMapper)
                    .eq(GmsGoods::getName, dto.getName())
                    .one();
            assertNotNull(created.getPic());
        }
    }

    // ============================================================
    // 修改商品
    // ============================================================

    @Nested
    @DisplayName("修改商品")
    class UpdateGoodsTests {

        private GmsGoods testGoods;

        @BeforeEach
        void setUp() {
            testGoods = goodsData.createGoods("update");
        }

        @Test
        @DisplayName("成功修改商品")
        void testUpdateGoodsSuccess() throws Exception {
            GmsGoodsDTO dto = new GmsGoodsDTO();
            dto.setId(testGoods.getId());
            dto.setBrandId(testGoods.getBrandId());
            dto.setCategoryId(testGoods.getCategoryId());
            dto.setBarcode(testGoods.getBarcode());
            dto.setName("test_upd_goods_" + System.currentTimeMillis() % 10000);
            dto.setUnit(testGoods.getUnit());
            dto.setSize(testGoods.getSize());
            dto.setDescription("修改后的描述");
            dto.setPurchasePrice(new BigDecimal("12.00"));
            dto.setSalePrice(new BigDecimal("22.00"));
            dto.setVipPrice(new BigDecimal("20.00"));
            dto.setCoupon(new BigDecimal("2.00"));
            dto.setStock(testGoods.getStock());

            var result = multipart(HttpMethod.PUT, "/gms/goods", "goods", dto, null, null);
            assertSuccess(result);

            GmsGoods updated = gmsGoodsMapper.selectById(testGoods.getId());
            assertEquals(dto.getName(), updated.getName());
            assertEquals("修改后的描述", updated.getDescription());
        }

        @Test
        @DisplayName("成功修改商品并更新图片")
        void testUpdateGoodsWithPic() throws Exception {
            GmsGoodsDTO dto = new GmsGoodsDTO();
            dto.setId(testGoods.getId());
            dto.setBrandId(testGoods.getBrandId());
            dto.setCategoryId(testGoods.getCategoryId());
            dto.setBarcode(testGoods.getBarcode());
            dto.setName("test_updpic_goods_" + System.currentTimeMillis() % 10000);
            dto.setUnit(testGoods.getUnit());
            dto.setSize(testGoods.getSize());
            dto.setDescription("带图片修改的描述");
            dto.setPurchasePrice(new BigDecimal("12.00"));
            dto.setSalePrice(new BigDecimal("22.00"));
            dto.setVipPrice(new BigDecimal("20.00"));
            dto.setCoupon(new BigDecimal("2.00"));
            dto.setStock(testGoods.getStock());

            var result = multipart(HttpMethod.PUT, "/gms/goods", "goods", dto, "pic", "new pic".getBytes());
            assertSuccess(result);

            GmsGoods updated = gmsGoodsMapper.selectById(testGoods.getId());
            assertNotNull(updated.getPic());
        }
    }

    // ============================================================
    // 删除商品
    // ============================================================

    @Nested
    @DisplayName("删除商品")
    class DeleteGoodsTests {

        @Test
        @DisplayName("成功删除商品")
        void testDeleteGoodsSuccess() throws Exception {
            GmsGoods goods = goodsData.createGoods("delete");

            var result = delete("/gms/goods", Set.of(goods.getId()));
            assertSuccess(result);

            assertNull(gmsGoodsMapper.selectById(goods.getId()));
        }

        @Test
        @DisplayName("成功批量删除商品")
        void testDeleteGoodsBatch() throws Exception {
            GmsGoods goods1 = goodsData.createGoods("batch1");
            GmsGoods goods2 = goodsData.createGoods("batch2");

            var result = delete("/gms/goods", Set.of(goods1.getId(), goods2.getId()));
            assertSuccess(result);

            assertNull(gmsGoodsMapper.selectById(goods1.getId()));
            assertNull(gmsGoodsMapper.selectById(goods2.getId()));
        }
    }
}
