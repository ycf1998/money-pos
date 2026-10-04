package com.money.controller;

import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.money.base.ControllerTestBase;
import com.money.dto.UmsMember.UmsMemberDTO;
import com.money.dto.UmsMember.UmsMemberVO;
import com.money.entity.UmsMember;
import com.money.mapper.UmsMemberMapper;
import com.money.testdata.MemberDataService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 会员管理接口测试
 */
@DisplayName("会员管理接口测试")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UmsMemberControllerTest extends ControllerTestBase {

    @Autowired
    private MemberDataService memberData;

    @Autowired
    private UmsMemberMapper umsMemberMapper;

    /**
     * 每个测试方法执行后，清理测试数据
     */
    @AfterEach
    void tearDown() {
        memberData.cleanup();
    }

    // ============================================================
    // 分页查询会员
    // ============================================================

    @Nested
    @DisplayName("分页查询会员")
    class ListMemberTests {

        @BeforeEach
        void setUp() {
            // 创建 5 个测试会员
            for (int i = 0; i < 5; i++) {
                memberData.createMember("page_" + i);
            }
        }

        @Test
        @DisplayName("成功分页查询会员列表")
        void testListMembers() throws Exception {
            var result = get("/ums/member", Map.of("page", "1", "size", "2"));
            assertSuccess(result);

            var pageData = getPageData(result, UmsMemberVO.class);
            assertNotNull(pageData);
            assertEquals(2, pageData.getRecords().size());
            assertTrue(pageData.getTotal() >= 5);
        }

        @Test
        @DisplayName("成功按会员名称搜索会员")
        void testListMembersByName() throws Exception {
            UmsMember member = memberData.createMember("search");

            var result = get("/ums/member", Map.of(
                    "page", "1",
                    "size", "10",
                    "name", member.getName()
            ));
            assertSuccess(result);

            var pageData = getPageData(result, UmsMemberVO.class);
            assertTrue(pageData.getRecords().stream()
                    .anyMatch(m -> m.getName().equals(member.getName())));
        }

        @Test
        @DisplayName("成功按会员类型筛选会员")
        void testListMembersByType() throws Exception {
            var result = get("/ums/member", Map.of(
                    "page", "1",
                    "size", "10",
                    "type", "VIP"
            ));
            assertSuccess(result);

            var pageData = getPageData(result, UmsMemberVO.class);
            assertTrue(pageData.getRecords().stream()
                    .allMatch(m -> "VIP".equals(m.getType())));
        }

        @Test
        @DisplayName("成功按消费金额排序")
        void testListMembersOrderByConsumeAmount() throws Exception {
            var result = get("/ums/member", Map.of(
                    "page", "1",
                    "size", "10",
                    "orderBy", "consumeAmount,desc"
            ));
            assertSuccess(result);

            var pageData = getPageData(result, UmsMemberVO.class);
            assertFalse(pageData.getRecords().isEmpty());
        }
    }

    // ============================================================
    // 添加会员
    // ============================================================

    @Nested
    @DisplayName("添加会员")
    class AddMemberTests {

        @AfterEach
        void tearDown() {
            // 清理通过 API 创建的测试会员（按名称前缀清理）
            new LambdaUpdateChainWrapper<>(umsMemberMapper)
                    .likeRight(UmsMember::getName, "新会员_")
                    .remove();
        }

        @Test
        @DisplayName("成功添加会员")
        void testAddMemberSuccess() throws Exception {
            UmsMemberDTO dto = new UmsMemberDTO();
            dto.setName("新会员_" + System.currentTimeMillis());
            dto.setType("SVIP");
            dto.setPhone("13800138000");
            dto.setProvince("北京市");
            dto.setCity("北京市");
            dto.setDistrict("朝阳区");
            dto.setAddress("建国路100号");
            dto.setCoupon(new BigDecimal("200.00"));
            dto.setRemark("新会员备注");

            var result = post("/ums/member", dto);
            assertSuccess(result);
        }
    }

    // ============================================================
    // 修改会员
    // ============================================================

    @Nested
    @DisplayName("修改会员")
    class UpdateMemberTests {

        private UmsMember testMember;

        @BeforeEach
        void setUp() {
            testMember = memberData.createMember("update");
        }

        @Test
        @DisplayName("成功修改会员")
        void testUpdateMemberSuccess() throws Exception {
            UmsMemberDTO updateDto = new UmsMemberDTO();
            updateDto.setId(testMember.getId());
            updateDto.setName("修改后的会员名称");
            updateDto.setType("SVIP");
            updateDto.setPhone("13800138001");
            updateDto.setProvince("上海市");
            updateDto.setCity("上海市");
            updateDto.setDistrict("浦东新区");
            updateDto.setAddress("陆家嘴环路1000号");
            updateDto.setCoupon(new BigDecimal("500.00"));
            updateDto.setRemark("修改后的备注");

            var result = put("/ums/member", updateDto);
            assertSuccess(result);

            // 验证修改结果
            UmsMember updated = umsMemberMapper.selectById(testMember.getId());
            assertNotNull(updated);
            assertEquals("修改后的会员名称", updated.getName());
            assertEquals("SVIP", updated.getType());
            assertEquals(new BigDecimal("500.00"), updated.getCoupon());
        }
    }

    // ============================================================
    // 删除会员
    // ============================================================

    @Nested
    @DisplayName("删除会员")
    class DeleteMemberTests {

        @Test
        @DisplayName("成功删除会员")
        void testDeleteMemberSuccess() throws Exception {
            UmsMember member = memberData.createMember("delete");

            var result = delete("/ums/member", Set.of(member.getId()));
            assertSuccess(result);

            // 验证逻辑删除：记录仍存在，但 deleted 字段为 true
            UmsMember deleted = umsMemberMapper.selectById(member.getId());
            assertNotNull(deleted);
            assertTrue(deleted.getDeleted());
        }

        @Test
        @DisplayName("成功批量删除会员")
        void testDeleteMembersBatch() throws Exception {
            UmsMember member1 = memberData.createMember("batch1");
            UmsMember member2 = memberData.createMember("batch2");

            var result = delete("/ums/member", Set.of(member1.getId(), member2.getId()));
            assertSuccess(result);

            // 验证逻辑删除：记录仍存在，但 deleted 字段为 true
            UmsMember deleted1 = umsMemberMapper.selectById(member1.getId());
            assertNotNull(deleted1);
            assertTrue(deleted1.getDeleted());

            UmsMember deleted2 = umsMemberMapper.selectById(member2.getId());
            assertNotNull(deleted2);
            assertTrue(deleted2.getDeleted());
        }
    }
}
