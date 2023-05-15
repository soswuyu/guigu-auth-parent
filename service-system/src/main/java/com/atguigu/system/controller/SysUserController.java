package com.atguigu.system.controller;


import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysUser;
import com.atguigu.model.vo.SysUserQueryVo;
import com.atguigu.system.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.protobuf.RpcUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author zhang
 * @since 2023-05-09
 */
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {
    @Autowired
    private SysUserService service;
    //条件分页查询
    @GetMapping("/{page}/{limit}")
    public Result ConditionPage(@PathVariable Long page,
                                @PathVariable Long limit,
                                SysUserQueryVo userQueryVo){
        //创建page对象，封装分页参数
        Page<SysUser> userPage = new Page<>(page,limit);
        //封装条件
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        String keyword = userQueryVo.getKeyword();
        //条件非空判断
        if (!StringUtils.isEmpty(keyword)){
            wrapper.like(SysUser::getUsername,keyword).or()
                    .like(SysUser::getName,keyword).or()
                    .like(SysUser::getPhone,keyword);
        }
        //调用service方法实现条件分页查询
        Page<SysUser> pageModel = service.page(userPage, wrapper);
        return Result.ok(pageModel);
    }
    //根据id获取用户
    @GetMapping("/get/{id}")
    public Result getUserById(@PathVariable Long id){
        SysUser sysUser = service.getById(id);
        if (!StringUtils.isEmpty(sysUser)){
            return Result.ok(sysUser);
        }else {
            return Result.fail();
        }
    }
    //新增用户
    @PostMapping("/save")
    public Result saveUser(@RequestBody SysUser sysUser){
        boolean isSuccess = service.save(sysUser);
        if (isSuccess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
    //删除用户
    @DeleteMapping("remove/{id}")
    public Result removeUserById(@PathVariable Long id){
        boolean isSuccess = service.removeById(id);
        if (isSuccess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
    //修改用户
    @PutMapping("update")
    public Result modifierUserInfo(@RequestBody SysUser sysUser){
        boolean isSuccess = service.updateById(sysUser);
        if (isSuccess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
    //状态
    @GetMapping("updateStatus/{id}/{status}")
    public Result updateState(@PathVariable Long id,@PathVariable Integer status){
        SysUser sysUser = service.getById(id);
        sysUser.setStatus(status);
        boolean isSuccess = service.updateById(sysUser);
        if (isSuccess){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }


}

