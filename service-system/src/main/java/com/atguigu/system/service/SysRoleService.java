package com.atguigu.system.service;

import com.atguigu.model.system.SysRole;
import com.atguigu.model.vo.AssignRoleVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author zhang
 * @since 2023-05-05
 */
public interface SysRoleService extends IService<SysRole> {
    /**
     * 根据用户获取角色数据
     * @param userId
     * @return
     */
    Map<String, Object> getRolesByUserId(Long userId);
    /**
     * 分配角色
     * @param assignRoleVo
     */
    void doAssign(AssignRoleVo assignRoleVo);
}
