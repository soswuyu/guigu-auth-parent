package com.atguigu.system.controller;


import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysRole;
import com.atguigu.model.vo.AssignRoleVo;
import com.atguigu.model.vo.SysRoleQueryVo;
import com.atguigu.system.exception.CustomException;
import com.atguigu.system.service.SysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author zhang
 * @since 2023-05-05
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {
    @Autowired
    private SysRoleService service;
    //根据用户获取角色数据
    @GetMapping("toAssign/{userId}")
    public Result toAssgin(@PathVariable Long userId){
        Map<String,Object> map = service.getRolesByUserId(userId);
        return Result.ok(map);
    }
    //根据用户分配角色
    @PostMapping("doAssign")
    public Result doAssign(@RequestBody AssignRoleVo assignRoleVo){
        service.doAssign(assignRoleVo);
        return Result.ok();
    }

    @ApiOperation("根据id查询")
    @GetMapping("/find/{id}")
    public Result findById(@PathVariable Long id){
        SysRole sysRole = service.getById(id);
        if (!StringUtils.isEmpty(sysRole)){
            return Result.ok(sysRole);
        }else {
            return Result.fail();
        }
    }
    @ApiOperation("修改角色")
    @PutMapping("/update")
    public Result modifier(@RequestBody SysRole sysRole){
        boolean isModifier = service.updateById(sysRole);
        if (isModifier){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
    @ApiOperation("批量删除角色")
    @DeleteMapping("batchRemove")
    public Result removeBatch(@RequestBody List<Long> id){
        boolean isSuccess = service.removeByIds(id);
        if (isSuccess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("删除角色")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id){
        boolean isRemove = service.removeById(id);
        if (isRemove){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
    @PreAuthorize("hasAuthority('bnt.sysRole.add')")
    @ApiOperation("新增角色")
    @PostMapping("save")
    public Result save(@RequestBody SysRole sysRole){
        boolean isSave = service.save(sysRole);
        if (isSave){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
    @ApiOperation("查询所有角色")
    @GetMapping("findAll")
    public Result findAll(){
        /*try {
            int i = 10/0;
        } catch (Exception e) {
            throw new CustomException("自定义异常",250);
        }*/
        List<SysRole> sysRoleList = service.list();
        return Result.ok(sysRoleList);
    }
    @ApiOperation("条件分页查询")
    @GetMapping("/show/{page}/{limit}")
    public Result index(@PathVariable Long page,
                        @PathVariable Long limit,
                        SysRoleQueryVo sysRoleQueryVo){
        //创建page对象，封装分页参数
        Page<SysRole> pageParam = new Page<>(page,limit);
        //封装条件
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper();
        String roleName = sysRoleQueryVo.getRoleName();
        //条件非空判断
        if (!StringUtils.isEmpty(roleName)){
            queryWrapper.like(SysRole::getRoleName,roleName);
        }
        //调用service方法实现条件分页查询
        Page<SysRole> pagsModel = service.page(pageParam, queryWrapper);

        return Result.ok(pagsModel);
    }


}

