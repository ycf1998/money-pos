package com.money.system.controller;

import com.money.base.ControllerTestBase;
import com.money.system.testdata.DictDataService;
import com.money.dto.SysDictDTO;
import com.money.dto.SysDictDetailDTO;
import com.money.entity.SysDict;
import com.money.entity.SysDictDetail;
import com.money.mapper.SysDictMapper;
import com.money.mapper.SysDictDetailMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 字典管理接口测试
 */
@DisplayName("字典管理接口测试")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SysDictControllerTest extends ControllerTestBase {

    @Autowired
    private DictDataService dictDataService;

    @Autowired
    private SysDictMapper dictMapper;

    @Autowired
    private SysDictDetailMapper dictDetailMapper;

    @AfterEach
    void tearDown() {
        dictDataService.cleanup();
    }

    // ============================================================
    // 分页查询字典
    // ============================================================

    @Nested
    @DisplayName("分页查询字典")
    class ListDictTests {

        @BeforeEach
        void setUp() {
            // 创建测试字典
            for (int i = 0; i < 3; i++) {
                dictDataService.createDict("test_dict_" + i, "测试字典_" + i);
            }
        }

        @Test
        @DisplayName("成功分页查询字典列表")
        void testListDicts() throws Exception {
            var result = get("/dict", Map.of("page", "1", "size", "2"));
            assertSuccess(result);

            var pageData = getPageData(result, SysDict.class);
            assertNotNull(pageData);
            assertEquals(2, pageData.getRecords().size());
            assertTrue(pageData.getTotal() >= 3);
        }

        @Test
        @DisplayName("成功按字典名称/描述搜索")
        void testListDictsByName() throws Exception {
            var result = get("/dict", Map.of("nameOrDesc", "test_dict_0"));
            assertSuccess(result);

            var pageData = getPageData(result, SysDict.class);
            assertTrue(pageData.getRecords().stream()
                    .anyMatch(d -> d.getDictName().contains("test_dict_0") ||
                                   d.getDictDesc().contains("test_dict_0")));
        }
    }

    // ============================================================
    // 添加字典
    // ============================================================

    @Nested
    @DisplayName("添加字典")
    class AddDictTests {

        @Test
        @DisplayName("成功添加字典")
        void testAddDictSuccess() throws Exception {
            SysDictDTO dto = new SysDictDTO();
            dto.setDictName("test_add_dict");
            dto.setDictDesc("测试添加字典");

            var result = post("/dict", dto);
            assertSuccess(result);
        }
    }

    // ============================================================
    // 修改字典
    // ============================================================

    @Nested
    @DisplayName("修改字典")
    class UpdateDictTests {

        private Long createdDictId = null;

        @BeforeEach
        void setUp() {
            // 创建测试字典
            SysDict dict = dictDataService.createDict("test_update_dict", "测试字典");
            createdDictId = dict.getId();
        }

        @AfterEach
        void tearDown() {
            dictDataService.cleanup();
        }

        @Test
        @DisplayName("成功修改字典")
        void testUpdateDictSuccess() throws Exception {
            SysDictDTO dto = new SysDictDTO();
            dto.setId(createdDictId);
            dto.setDictDesc("更新后的描述");

            var result = put("/dict", dto);
            assertSuccess(result);

            // 验证修改结果
            SysDict updated = dictMapper.selectById(createdDictId);
            assertNotNull(updated);
            assertEquals("更新后的描述", updated.getDictDesc());
        }
    }

    // ============================================================
    // 删除字典
    // ============================================================

    @Nested
    @DisplayName("删除字典")
    class DeleteDictTests {

        @Test
        @DisplayName("成功删除字典")
        void testDeleteDictSuccess() throws Exception {
            // 创建测试字典
            SysDict dict = dictDataService.createDict("test_delete_dict", "测试字典");

            var result = delete("/dict", Set.of(dict.getId()));
            assertSuccess(result);

            assertNull(dictMapper.selectById(dict.getId()));
        }

        @Test
        @DisplayName("成功批量删除字典")
        void testDeleteDictsBatch() throws Exception {
            // 创建测试字典
            SysDict dict1 = dictDataService.createDict("test_delete_dict_1", "测试字典 1");
            SysDict dict2 = dictDataService.createDict("test_delete_dict_2", "测试字典 2");

            var result = delete("/dict", Set.of(dict1.getId(), dict2.getId()));
            assertSuccess(result);

            assertNull(dictMapper.selectById(dict1.getId()));
            assertNull(dictMapper.selectById(dict2.getId()));
        }
    }

    // ============================================================
    // 获取字典详情
    // ============================================================

    @Nested
    @DisplayName("获取字典详情")
    class GetDictDetailTests {

        private String testDict = null;

        @BeforeEach
        void setUp() {
            // 创建测试字典
            SysDict dict = dictDataService.createDict("test_dict_detail", "测试字典详情");
            testDict = dict.getDictName();

            // 创建测试字典详情
            for (int i = 0; i < 3; i++) {
                dictDataService.createDictDetail(testDict, "value_" + i, "中文描述_" + i);
            }
        }

        @Test
        @DisplayName("成功获取字典详情")
        void testGetDictDetail() throws Exception {
            var result = get("/dict/detail", Map.of("dict", testDict));
            assertSuccess(result);

            var list = getListData(result, SysDictDetail.class);
            assertNotNull(list);
            assertEquals(3, list.size());
        }
    }

    // ============================================================
    // 添加字典详情
    // ============================================================

    @Nested
    @DisplayName("添加字典详情")
    class AddDictDetailTests {

        private String testDict = null;

        @BeforeEach
        void setUp() {
            // 创建测试字典
            SysDict dict = dictDataService.createDict("test_add_detail_dict", "测试字典");
            testDict = dict.getDictName();
        }

        @Test
        @DisplayName("成功添加字典详情")
        void testAddDictDetailSuccess() throws Exception {
            SysDictDetailDTO dto = new SysDictDetailDTO();
            dto.setDict(testDict);
            dto.setValue("test_value");
            dto.setCnDesc("测试中文描述");
            dto.setEnDesc("Test English Description");
            dto.setHidden(false);

            var result = post("/dict/detail", dto);
            assertSuccess(result);
        }
    }

    // ============================================================
    // 修改字典详情
    // ============================================================

    @Nested
    @DisplayName("修改字典详情")
    class UpdateDictDetailTests {

        private Long detailId = null;

        @BeforeEach
        void setUp() {
            // 创建测试字典
            SysDict dict = dictDataService.createDict("test_update_detail_dict", "测试字典");

            // 创建测试字典详情
            SysDictDetail detail = dictDataService.createDictDetail(dict.getDictName(), "original_value", "原始中文描述");
            detailId = detail.getId();
        }

        @AfterEach
        void tearDown() {
            dictDataService.cleanup();
        }

        @Test
        @DisplayName("成功修改字典详情")
        void testUpdateDictDetailSuccess() throws Exception {
            SysDictDetailDTO dto = new SysDictDetailDTO();
            dto.setId(detailId);
            dto.setValue("updated_value");
            dto.setCnDesc("更新后的中文描述");
            dto.setEnDesc("Updated English Description");
            dto.setHidden(false);

            var result = put("/dict/detail", dto);
            assertSuccess(result);

            // 验证修改结果
            SysDictDetail updated = dictDetailMapper.selectById(detailId);
            assertNotNull(updated);
            assertEquals("updated_value", updated.getValue());
            assertEquals("更新后的中文描述", updated.getCnDesc());
        }
    }

    // ============================================================
    // 删除字典详情
    // ============================================================

    @Nested
    @DisplayName("删除字典详情")
    class DeleteDictDetailTests {

        @Test
        @DisplayName("成功删除字典详情")
        void testDeleteDictDetailSuccess() throws Exception {
            // 创建测试字典
            SysDict dict = dictDataService.createDict("test_delete_detail_dict", "测试字典");

            // 创建测试字典详情
            SysDictDetail detail = dictDataService.createDictDetail(dict.getDictName(), "test_value", "测试中文描述");

            var result = delete("/dict/detail", Set.of(detail.getId()));
            assertSuccess(result);

            assertNull(dictDetailMapper.selectById(detail.getId()));
        }

        @Test
        @DisplayName("成功批量删除字典详情")
        void testDeleteDictDetailsBatch() throws Exception {
            // 创建测试字典
            SysDict dict = dictDataService.createDict("test_delete_batch_detail_dict", "测试字典");

            // 创建测试字典详情
            SysDictDetail detail1 = dictDataService.createDictDetail(dict.getDictName(), "value_1", "中文描述 1");
            SysDictDetail detail2 = dictDataService.createDictDetail(dict.getDictName(), "value_2", "中文描述 2");

            var result = delete("/dict/detail", Set.of(detail1.getId(), detail2.getId()));
            assertSuccess(result);

            assertNull(dictDetailMapper.selectById(detail1.getId()));
            assertNull(dictDetailMapper.selectById(detail2.getId()));
        }
    }
}
