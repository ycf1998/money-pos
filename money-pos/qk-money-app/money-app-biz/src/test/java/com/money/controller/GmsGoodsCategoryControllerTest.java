package com.money.controller;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.money.base.ControllerTestBase;
import com.money.dto.GmsGoodsCategory.GmsGoodsCategoryDTO;
import com.money.dto.SelectVO;
import com.money.dto.TreeNodeVO;
import com.money.entity.GmsGoodsCategory;
import com.money.mapper.GmsGoodsCategoryMapper;
import com.money.testdata.CategoryDataService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 商品分类接口测试
 */
@DisplayName("商品分类接口测试")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GmsGoodsCategoryControllerTest extends ControllerTestBase {

    @Autowired
    private CategoryDataService categoryData;

    @Autowired
    private GmsGoodsCategoryMapper categoryMapper;

    @AfterEach
    void tearDown() {
        categoryData.cleanup();
    }

    // ============================================================
    // 分类下拉选择
    // ============================================================

    @Nested
    @DisplayName("分类下拉选择")
    class CategorySelectTests {

        @BeforeEach
        void setUp() {
            // 创建根分类
            categoryData.createRootCategory("select_root");
        }

        @Test
        @DisplayName("成功获取分类下拉列表")
        void testCategorySelect() throws Exception {
            var result = get("/gms/goodsCategory/select");
            assertSuccess(result);

            var selectList = getListData(result, SelectVO.class);
            assertTrue(!selectList.isEmpty());
        }
    }

    // ============================================================
    // 分类树形结构
    // ============================================================

    @Nested
    @DisplayName("分类树形结构")
    class CategoryTreeTests {

        @BeforeEach
        void setUp() {
            // 创建根分类
            GmsGoodsCategory root = categoryData.createRootCategory("tree_root");
            // 创建子分类
            categoryData.createCategory(root.getId(), "tree_child1");
            categoryData.createCategory(root.getId(), "tree_child2");
        }

        @Test
        @DisplayName("成功获取分类树")
        void testCategoryTree() throws Exception {
            var result = get("/gms/goodsCategory/tree");
            assertSuccess(result);

            TreeNodeVO tree = getData(result, TreeNodeVO.class);
            assertNotNull(tree);
            // 树中应包含测试分类
            assertTrue(hasNodeWithName(tree, "测试分类_tree_root"));
        }

        private boolean hasNodeWithName(TreeNodeVO node, String name) {
            if (name.equals(node.getName())) {
                return true;
            }
            if (node.getChildren() != null) {
                for (TreeNodeVO child : node.getChildren()) {
                    if (hasNodeWithName(child, name)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    // ============================================================
    // 添加分类
    // ============================================================

    @Nested
    @DisplayName("添加分类")
    class AddCategoryTests {

        @Test
        @DisplayName("成功添加根分类（无 icon）")
        void testAddRootCategoryWithoutIcon() throws Exception {
            GmsGoodsCategoryDTO dto = new GmsGoodsCategoryDTO();
            dto.setPid(0L);
            dto.setName("测试分类_add_root");

            var result = multipart(HttpMethod.POST, "/gms/goodsCategory", "goodsCategory", dto, null, null);
            assertSuccess(result);

            GmsGoodsCategory created = new LambdaQueryChainWrapper<>(categoryMapper)
                    .eq(GmsGoodsCategory::getName, dto.getName())
                    .one();
            assertNotNull(created);
            assertEquals(0L, created.getPid());
        }

        @Test
        @DisplayName("成功添加子分类（带 icon）")
        void testAddChildCategoryWithIcon() throws Exception {
            // 先创建父分类
            GmsGoodsCategory parent = categoryData.createRootCategory("parent_for_child");

            GmsGoodsCategoryDTO dto = new GmsGoodsCategoryDTO();
            dto.setPid(parent.getId());
            dto.setName("测试分类_add_child");

            var result = multipart(HttpMethod.POST, "/gms/goodsCategory", "goodsCategory", dto, "icon", "fake icon".getBytes());
            assertSuccess(result);

            GmsGoodsCategory created = new LambdaQueryChainWrapper<>(categoryMapper)
                    .eq(GmsGoodsCategory::getName, dto.getName())
                    .one();
            assertNotNull(created);
            assertEquals(parent.getId(), created.getPid());
            assertNotNull(created.getIcon());
        }
    }

    // ============================================================
    // 修改分类
    // ============================================================

    @Nested
    @DisplayName("修改分类")
    class UpdateCategoryTests {

        private GmsGoodsCategory testCategory;

        @BeforeEach
        void setUp() {
            testCategory = categoryData.createRootCategory("update");
        }

        @Test
        @DisplayName("成功修改分类名称")
        void testUpdateCategoryName() throws Exception {
            GmsGoodsCategoryDTO dto = new GmsGoodsCategoryDTO();
            dto.setId(testCategory.getId());
            dto.setPid(testCategory.getPid());
            dto.setName("测试分类_updated");

            var result = multipart(HttpMethod.PUT, "/gms/goodsCategory", "goodsCategory", dto, null, null);
            assertSuccess(result);

            GmsGoodsCategory updated = categoryMapper.selectById(testCategory.getId());
            assertEquals("测试分类_updated", updated.getName());
        }

        @Test
        @DisplayName("成功修改分类并更新 icon")
        void testUpdateCategoryWithIcon() throws Exception {
            GmsGoodsCategoryDTO dto = new GmsGoodsCategoryDTO();
            dto.setId(testCategory.getId());
            dto.setPid(testCategory.getPid());
            dto.setName("测试分类_upd_icon");

            var result = multipart(HttpMethod.PUT, "/gms/goodsCategory", "goodsCategory", dto, "icon", "new icon".getBytes());
            assertSuccess(result);

            GmsGoodsCategory updated = categoryMapper.selectById(testCategory.getId());
            assertNotNull(updated.getIcon());
        }
    }

    // ============================================================
    // 删除分类
    // ============================================================

    @Nested
    @DisplayName("删除分类")
    class DeleteCategoryTests {

        @Test
        @DisplayName("成功删除分类")
        void testDeleteCategorySuccess() throws Exception {
            GmsGoodsCategory category = categoryData.createRootCategory("delete");

            var result = delete("/gms/goodsCategory", Set.of(category.getId()));
            assertSuccess(result);

            assertNull(categoryMapper.selectById(category.getId()));
        }

        @Test
        @DisplayName("成功批量删除分类")
        void testDeleteCategoriesBatch() throws Exception {
            GmsGoodsCategory cat1 = categoryData.createRootCategory("batch1");
            GmsGoodsCategory cat2 = categoryData.createRootCategory("batch2");

            var result = delete("/gms/goodsCategory", Set.of(cat1.getId(), cat2.getId()));
            assertSuccess(result);

            assertNull(categoryMapper.selectById(cat1.getId()));
            assertNull(categoryMapper.selectById(cat2.getId()));
        }
    }
}