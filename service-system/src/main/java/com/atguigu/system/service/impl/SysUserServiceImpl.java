package com.atguigu.system.service.impl;

import com.atguigu.model.system.SysMenu;
import com.atguigu.model.system.SysUser;
import com.atguigu.model.vo.LoginVo;
import com.atguigu.model.vo.RouterVo;
import com.atguigu.system.mapper.SysMenuMapper;
import com.atguigu.system.mapper.SysUserMapper;
import com.atguigu.system.service.SysUserService;
import com.atguigu.system.util.MenuHelper;
import com.atguigu.system.util.RouterHelper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author zhang
 * @since 2023-05-09
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public SysUser getByUsername(LoginVo loginVo) {
        //验证用户名密码是否正确
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername,loginVo.getUsername());
        SysUser sysUser = baseMapper.selectOne(wrapper);
        return sysUser;
    }

    /**
     * 根据用户id获取用户登录信息
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> getUserInfoById(Long id) {
        //获取菜单信息
        List<SysMenu> menuList = this.getUserMenusById(id);
        //将menuList转换为菜单树
        List<SysMenu> buildTree = MenuHelper.buildTree(menuList);
        List<RouterVo> routerVoList = RouterHelper.buildRouters(buildTree);
        //获取操作按钮数据
        List<String> permsList = this.getPermsById(menuList);
        SysUser sysUser = baseMapper.selectById(id);
        Map<String,Object> map = new HashMap<>();
        map.put("name",sysUser.getName());
        map.put("roles",new HashSet<>());
        //头像
        map.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        map.put("routers", routerVoList);//可以操作菜单数据
        map.put("buttons", permsList); //可以操作按钮数据
        return map;
    }
    
    //获取操作按钮数据
    private List<String> getPermsById(List<SysMenu> menuList) {
        List<String> permsList = new ArrayList<>();
        for (SysMenu sysMenu : menuList) {
            if (sysMenu.getType() == 2){
                permsList.add(sysMenu.getPerms());
            }
        }
        return permsList;
    }


    private List<SysMenu> getUserMenusById(Long id) {
        List<SysMenu> sysMenuList = null;
        //判断该用户是否是系统管理员
        if (id == 1L){
            sysMenuList = sysMenuMapper.selectList(null);
        }else {
            sysMenuList = sysMenuMapper.selectMenuListById(id);
        }
        return sysMenuList;
    }
}
