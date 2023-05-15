package com.atguigu.system.service;

import com.atguigu.model.system.SysUser;
import com.atguigu.model.vo.LoginVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author zhang
 * @since 2023-05-09
 */
public interface SysUserService extends IService<SysUser> {

    SysUser getByUsername(LoginVo loginVo);

    Map<String, Object> getUserInfoById(Long id);
}
