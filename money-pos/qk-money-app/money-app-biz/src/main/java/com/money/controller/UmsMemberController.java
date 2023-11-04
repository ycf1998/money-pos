package com.money.controller;

import com.money.common.dto.ValidGroup;
import com.money.common.vo.PageVO;
import com.money.dto.UmsMember.UmsMemberDTO;
import com.money.dto.UmsMember.UmsMemberQueryDTO;
import com.money.dto.UmsMember.UmsMemberVO;
import com.money.service.UmsMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author money
 * @since 2023-02-27
 */
@Tag(name = "umsMember", description = "会员表")
@RestController
@RequestMapping("/ums/member")
@RequiredArgsConstructor
public class UmsMemberController {

    private final UmsMemberService umsMemberService;

    @Operation(summary = "分页查询")
    @GetMapping
    @PreAuthorize("@rbac.hasPermission('umsMember:list')")
    public PageVO<UmsMemberVO> list(@Validated UmsMemberQueryDTO queryDTO) {
        return umsMemberService.list(queryDTO);
    }

    @Operation(summary = "添加")
    @PostMapping
    @PreAuthorize("@rbac.hasPermission('umsMember:add')")
    public void add(@Validated(ValidGroup.Save.class) @RequestBody UmsMemberDTO addDTO) {
        umsMemberService.add(addDTO);
    }

    @Operation(summary = "修改")
    @PutMapping
    @PreAuthorize("@rbac.hasPermission('umsMember:edit')")
    public void update(@Validated(ValidGroup.Update.class) @RequestBody UmsMemberDTO updateDTO) {
        umsMemberService.update(updateDTO);
    }

    @Operation(summary = "删除")
    @DeleteMapping
    @PreAuthorize("@rbac.hasPermission('umsMember:del')")
    public void delete(@RequestBody Set<Long> ids) {
        umsMemberService.delete(ids);
    }
}
