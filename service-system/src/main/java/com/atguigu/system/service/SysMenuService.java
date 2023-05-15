package com.atguigu.system.service;

import com.atguigu.model.system.SysMenu;
import com.atguigu.model.vo.AssignMenuVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author zhang
 * @since 2023-05-10
 */
public interface SysMenuService extends IService<SysMenu> {
    /**
     * 根据id删除菜单
     * @param id
     */
    void removeMenu(Long id);

    /**
     * 查询所有菜单
     * @return
     */
    List<SysMenu> findNodes();
    //根据角色id获取菜单
    List<SysMenu> findSysMenuByRoleId(Long roleId);
    //给角色分配权限
    void doAssign(AssignMenuVo assignMenuVo);

    /**
     * 获取用户菜单权限
     * @param id
     * @return
     */
    List<String> findUserPermsList(Long id);
}
