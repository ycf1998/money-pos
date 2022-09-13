package com.money.controller;


import com.money.common.dto.ValidGroup;
import com.money.common.vo.PageVO;
import com.money.service.UmsMemberService;
import com.money.dto.member.MemberDTO;
import com.money.dto.member.MemberQueryDTO;
import com.money.dto.member.MemberVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class UmsMemberController {

    private final UmsMemberService umsMemberService;

    @GetMapping
    @PreAuthorize("@rbac.hasPermission('member:list')")
    public PageVO<MemberVO> list(@Validated MemberQueryDTO queryDTO) {
        return umsMemberService.list(queryDTO);
    }

    @PostMapping
    @PreAuthorize("@rbac.hasPermission('member:add')")
    public Long addMember(@Validated(ValidGroup.Save.class) @RequestBody MemberDTO memberDTO) {
        return umsMemberService.add(memberDTO);
    }

    @PutMapping
    @PreAuthorize("@rbac.hasPermission('member:edit')")
    public void updateMember(@Validated(ValidGroup.Update.class) @RequestBody MemberDTO memberDTO) {
        umsMemberService.update(memberDTO);
    }

    @DeleteMapping
    @PreAuthorize("@rbac.hasPermission('member:del')")
    public void deleteMember(@RequestBody Set<Long> ids) {
        umsMemberService.delete(ids);
    }

}
