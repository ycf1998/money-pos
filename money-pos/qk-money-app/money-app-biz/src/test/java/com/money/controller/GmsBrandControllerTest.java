package com.money.controller;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.money.base.ControllerTestBase;
import com.money.dto.GmsBrand.GmsBrandDTO;
import com.money.dto.GmsBrand.GmsBrandVO;
import com.money.dto.SelectVO;
import com.money.entity.GmsBrand;
import com.money.mapper.GmsBrandMapper;
import com.money.testdata.BrandDataService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 商品品牌接口测试
 */
@DisplayName("商品品牌接口测试")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GmsBrandControllerTest extends ControllerTestBase {

    @Autowired
    private BrandDataService brandData;

    @Autowired
    private GmsBrandMapper gmsBrandMapper;

    @AfterEach
    void tearDown() {
        brandData.cleanup();
    }

    // ============================================================
    // 品牌下拉选择
    // ============================================================

    @Nested
    @DisplayName("品牌下拉选择")
    class BrandSelectTests {

        @BeforeEach
        void setUp() {
            for (int i = 0; i < 3; i++) {
                brandData.createBrand("select_" + i);
            }
        }

        @Test
        @DisplayName("成功获取品牌下拉列表")
        void testBrandSelect() throws Exception {
            var result = get("/gms/brand/select");
            assertSuccess(result);

            var selectList = getListData(result, SelectVO.class);
            assertTrue(selectList.size() >= 3);
        }
    }

    // ============================================================
    // 分页查询品牌
    // ============================================================

    @Nested
    @DisplayName("分页查询品牌")
    class ListBrandTests {

        @BeforeEach
        void setUp() {
            for (int i = 0; i < 5; i++) {
                brandData.createBrand("page_" + i);
            }
        }

        @Test
        @DisplayName("成功分页查询品牌列表")
        void testListBrands() throws Exception {
            var result = get("/gms/brand", Map.of("page", "1", "size", "2"));
            assertSuccess(result);

            var pageData = getPageData(result, GmsBrandVO.class);
            assertEquals(2, pageData.getRecords().size());
            assertTrue(pageData.getTotal() >= 5);
        }

        @Test
        @DisplayName("成功按品牌名称搜索")
        void testListBrandsByName() throws Exception {
            GmsBrand brand = brandData.createBrand("search");

            var result = get("/gms/brand", Map.of(
                    "page", "1", "size", "10", "name", brand.getName()
            ));
            assertSuccess(result);

            var pageData = getPageData(result, GmsBrandVO.class);
            assertTrue(pageData.getRecords().stream()
                    .anyMatch(b -> b.getName().equals(brand.getName())));
        }
    }

    // ============================================================
    // 添加品牌
    // ============================================================

    @Nested
    @DisplayName("添加品牌")
    class AddBrandTests {

        @AfterEach
        void tearDown() {
            new LambdaUpdateChainWrapper<>(gmsBrandMapper)
                    .likeRight(GmsBrand::getName, "test_")
                    .remove();
        }

        @Test
        @DisplayName("成功添加品牌（无 logo）")
        void testAddBrandWithoutLogo() throws Exception {
            GmsBrandDTO dto = new GmsBrandDTO();
            dto.setName("test_" + System.currentTimeMillis() % 10000);
            dto.setDescription("新品牌描述");

            var result = multipart(HttpMethod.POST, "/gms/brand", "brand", dto, null, null);
            assertSuccess(result);

            GmsBrand created = new LambdaQueryChainWrapper<>(gmsBrandMapper)
                    .eq(GmsBrand::getName, dto.getName())
                    .one();
            assertNotNull(created);
        }

        @Test
        @DisplayName("成功添加品牌（带 logo）")
        void testAddBrandWithLogo() throws Exception {
            GmsBrandDTO dto = new GmsBrandDTO();
            dto.setName("test_logo_" + System.currentTimeMillis() % 10000);
            dto.setDescription("带Logo品牌");

            var result = multipart(HttpMethod.POST, "/gms/brand", "brand", dto, "logo", "fake image".getBytes());
            assertSuccess(result);

            GmsBrand created = new LambdaQueryChainWrapper<>(gmsBrandMapper)
                    .eq(GmsBrand::getName, dto.getName())
                    .one();
            assertNotNull(created.getLogo());
        }
    }

    // ============================================================
    // 修改品牌
    // ============================================================

    @Nested
    @DisplayName("修改品牌")
    class UpdateBrandTests {

        private GmsBrand testBrand;

        @BeforeEach
        void setUp() {
            testBrand = brandData.createBrand("update");
        }

        @Test
        @DisplayName("成功修改品牌")
        void testUpdateBrandSuccess() throws Exception {
            GmsBrandDTO dto = new GmsBrandDTO();
            dto.setId(testBrand.getId());
            dto.setName("test_upd_" + System.currentTimeMillis() % 10000);
            dto.setDescription("修改后的描述");

            var result = multipart(HttpMethod.PUT, "/gms/brand", "brand", dto, null, null);
            assertSuccess(result);

            GmsBrand updated = gmsBrandMapper.selectById(testBrand.getId());
            assertEquals(dto.getName(), updated.getName());
        }

        @Test
        @DisplayName("成功修改品牌并更新 logo")
        void testUpdateBrandWithLogo() throws Exception {
            GmsBrandDTO dto = new GmsBrandDTO();
            dto.setId(testBrand.getId());
            dto.setName("test_updl_" + System.currentTimeMillis() % 10000);
            dto.setDescription("修改后描述");

            var result = multipart(HttpMethod.PUT, "/gms/brand", "brand", dto, "logo", "new logo".getBytes());
            assertSuccess(result);

            GmsBrand updated = gmsBrandMapper.selectById(testBrand.getId());
            assertNotNull(updated.getLogo());
        }
    }

    // ============================================================
    // 删除品牌
    // ============================================================

    @Nested
    @DisplayName("删除品牌")
    class DeleteBrandTests {

        @Test
        @DisplayName("成功删除品牌")
        void testDeleteBrandSuccess() throws Exception {
            GmsBrand brand = brandData.createBrand("delete");

            var result = delete("/gms/brand", Set.of(brand.getId()));
            assertSuccess(result);

            assertNull(gmsBrandMapper.selectById(brand.getId()));
        }

        @Test
        @DisplayName("成功批量删除品牌")
        void testDeleteBrandsBatch() throws Exception {
            GmsBrand brand1 = brandData.createBrand("batch1");
            GmsBrand brand2 = brandData.createBrand("batch2");

            var result = delete("/gms/brand", Set.of(brand1.getId(), brand2.getId()));
            assertSuccess(result);

            assertNull(gmsBrandMapper.selectById(brand1.getId()));
            assertNull(gmsBrandMapper.selectById(brand2.getId()));
        }
    }
}