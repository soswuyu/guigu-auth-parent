package com.atguigu.system.service.impl;

import com.atguigu.common.result.ResultCodeEnum;
import com.atguigu.model.system.SysMenu;
import com.atguigu.model.system.SysRoleMenu;
import com.atguigu.model.vo.AssignMenuVo;
import com.atguigu.system.exception.CustomException;
import com.atguigu.system.mapper.SysMenuMapper;
import com.atguigu.system.mapper.SysRoleMenuMapper;
import com.atguigu.system.service.SysMenuService;
import com.atguigu.system.util.MenuHelper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author zhang
 * @since 2023-05-10
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    //给角色分配权限
    @Override
    public void doAssign(AssignMenuVo assignMenuVo) {
        //删除之前分配的权限
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper();
        wrapper.eq(SysRoleMenu::getRoleId, assignMenuVo.getRoleId());
        sysRoleMenuMapper.delete(wrapper);
        //遍历所有已选择的权限id
        for (Long menuId : assignMenuVo.getMenuIdList()) {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(assignMenuVo.getRoleId());
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenuMapper.insert(sysRoleMenu);
        }
    }

    /**
     * 获取用户菜单权限
     * @param userId
     * @return
     */
    @Override
    public List<String> findUserPermsList(Long userId) {
        List<SysMenu> sysMenuList = null;
        if (userId == 1L){
            sysMenuList = baseMapper.selectList(null);
        }else {
            sysMenuList = baseMapper.selectMenuListById(userId);
        }

        List<String> permsList = sysMenuList
                                .stream().filter(item -> item.getType() == 2)
                                .map(item -> item.getPerms())
                                .collect(Collectors.toList());
        return permsList;
    }

    //根据角色获取菜单
    @Override
    public List<SysMenu> findSysMenuByRoleId(Long roleId) {
        //获取所有菜单
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper();
        wrapper.eq(SysMenu::getStatus, 1);
        List<SysMenu> menuList = baseMapper.selectList(wrapper);
        //根据角色id获取角色权限
        LambdaQueryWrapper<SysRoleMenu> srmWrapper = new LambdaQueryWrapper<>();
        srmWrapper.eq(SysRoleMenu::getRoleId, roleId);
        List<SysRoleMenu> sysRoleMenuList = sysRoleMenuMapper.selectList(srmWrapper);
        //获取该角色已分配的所有权限id
        List<Long> idList = new ArrayList<>();
        for (SysRoleMenu sysRoleMenu : sysRoleMenuList) {
            idList.add(sysRoleMenu.getMenuId());
        }
        //遍历所有权限列表
        for (SysMenu sysMenu : menuList) {
            if (idList.contains(sysMenu.getId())) {
                sysMenu.setSelect(true);
            } else {
                sysMenu.setSelect(false);
            }
        }
        List<SysMenu> list = MenuHelper.buildTree(menuList);
        return list;
    }

    //删除菜单
    @Override
    public void removeMenu(Long id) {
        //封装条件
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getParentId, id);
        List<SysMenu> menuList = baseMapper.selectList(wrapper);
        if (!CollectionUtils.isEmpty(menuList)) {
            throw new CustomException(ResultCodeEnum.NODE_ERROR);
        } else {
            baseMapper.deleteById(id);
        }
    }

    //查询菜单
    @Override
    public List<SysMenu> findNodes() {
        //查询所有菜单
        List<SysMenu> menuList = baseMapper.selectList(null);
        if (CollectionUtils.isEmpty(menuList)) {
            return null;
        }
        //构建树形数据
        List<SysMenu> result = MenuHelper.buildTree(menuList);
        return result;
    }

}
