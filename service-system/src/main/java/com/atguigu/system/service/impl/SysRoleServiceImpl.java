package com.atguigu.system.service.impl;

import com.atguigu.model.system.SysRole;
import com.atguigu.model.system.SysUserRole;
import com.atguigu.model.vo.AssignRoleVo;
import com.atguigu.system.mapper.SysRoleMapper;
import com.atguigu.system.mapper.SysUserRoleMapper;
import com.atguigu.system.service.SysRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author zhang
 * @since 2023-05-05
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    /**
     * 根据用户获取角色数据
     * @param userId
     * @return
     */
    @Override
    public Map<String, Object> getRolesByUserId(Long userId) {
        //获取所有角色信息
        List<SysRole> sysRoleList = baseMapper.selectList(null);
        //根据用户id查询，获取已分配的角色
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId,userId);

        List<SysUserRole> userRoleList = sysUserRoleMapper.selectList(wrapper);
        //获取用户已分配的角色id
        List<Long> idList = new ArrayList<>();
        for (SysUserRole userRole : userRoleList) {
            Long roleId = userRole.getRoleId();
            idList.add(roleId);
        }
        //创建返回的map
        Map<String,Object> map = new HashMap<>();
        map.put("allRoles",sysRoleList);
        map.put("userRoleIds",idList);
        return map;
    }
    /**
     * 分配角色
     * @param assginRoleVo
     */
    @Override
    public void doAssign(AssignRoleVo assginRoleVo) {
        //根据用户id删除原来分配的角色
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId,assginRoleVo.getUserId());
        sysUserRoleMapper.delete(wrapper);
        //获取所有的角色id
        List<Long> roleIdList = assginRoleVo.getRoleIdList();
        for (Long roleId : roleIdList) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(assginRoleVo.getUserId());
            sysUserRole.setRoleId(roleId);
            sysUserRoleMapper.insert(sysUserRole);
        }
    }
}
