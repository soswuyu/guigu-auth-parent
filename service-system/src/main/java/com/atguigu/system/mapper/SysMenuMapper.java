package com.atguigu.system.mapper;

import com.atguigu.model.system.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author zhang
 * @since 2023-05-10
 */
@Repository
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    List<SysMenu> selectMenuListById(Long id);
}
