package com.atguigu.system.controller;


import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysMenu;
import com.atguigu.model.vo.AssignMenuVo;
import com.atguigu.system.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author zhang
 * @since 2023-05-10
 */
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {
    @Autowired
    private SysMenuService service;
    //根据角色获取菜单
    @GetMapping("toAssign/{roleId}")
    public Result toAssign(@PathVariable Long roleId){
        List<SysMenu> list = service.findSysMenuByRoleId(roleId);
        return Result.ok(list);
    }
    //给角色分配权限
    @PostMapping("doAssign")
    public Result doAssign(@RequestBody AssignMenuVo assignMenuVo){
        service.doAssign(assignMenuVo);
        return Result.ok();
    }
    //获取菜单
    @GetMapping("findNodes")
    public Result findNodes(){
        List<SysMenu> menuList = service.findNodes();
        return Result.ok(menuList);
    }
    //新增菜单
    @PostMapping("/save")
    public Result saveMenu(@RequestBody SysMenu sysMenu){
        boolean isSuccess = service.save(sysMenu);
        if (isSuccess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
    //根据id查询菜单
    @GetMapping("/getById/{id}")
    public Result getMenuById(@PathVariable Long id){
        SysMenu sysMenu = service.getById(id);
        if (StringUtils.isEmpty(sysMenu)){
            return Result.fail();
        }else {
            return Result.ok(sysMenu);
        }
    }
    //修改菜单
    @PutMapping("/update")
    public Result modifierMenu(@RequestBody SysMenu sysMenu){
        boolean isSuccess = service.updateById(sysMenu);
        if (isSuccess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
    //删除菜单
    @DeleteMapping("/remove/{id}")
    public Result removeMenu(@PathVariable Long id){
        service.removeMenu(id);
        return Result.ok();
    }
}

