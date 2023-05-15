package com.atguigu.system.service.impl;/**
 * 2  *                             _ooOoo_
 * 3  *                            o8888888o
 * 4  *                            88" . "88
 * 5  *                            (| -_- |)
 * 6  *                            O\  =  /O
 * 7  *                         ____/`---'\____
 * 8  *                       .'  \\|     |//  `.
 * 9  *                      /  \\|||  :  |||//  \
 * 10  *                     /  _||||| -:- |||||-  \
 * 11  *                     |   | \\\  -  /// |   |
 * 12  *                     | \_|  ''\---/''  |   |
 * 13  *                     \  .-\__  `-`  ___/-. /
 * 14  *                   ___`. .'  /--.--\  `. . __
 * 15  *                ."" '<  `.___\_<|>_/___.'  >'"".
 * 16  *               | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 * 17  *               \  \ `-.   \_ __\ /__ _/   .-` /  /
 * 18  *          ======`-.____`-.___\_____/___.-`____.-'======
 * 19  *                             `=---='
 * 20  *          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * 21  *                     佛祖保佑        永无BUG
 * 22  *            佛曰:
 * 23  *                   写字楼里写字间，写字间里程序员；
 * 24  *                   程序人员写程序，又拿程序换酒钱。
 * 25  *                   酒醒只在网上坐，酒醉还来网下眠；
 * 26  *                   酒醉酒醒日复日，网上网下年复年。
 * 27  *                   但愿老死电脑间，不愿鞠躬老板前；
 * 28  *                   奔驰宝马贵者趣，公交自行程序员。
 * 29  *                   别人笑我忒疯癫，我笑自己命太贱；
 * 30  *                   不见满街漂亮妹，哪个归得程序员？
 * 31
 */

import com.atguigu.common.result.ResultCodeEnum;
import com.atguigu.model.system.SysUser;
import com.atguigu.system.custom.CustomUser;
import com.atguigu.system.exception.CustomException;
import com.atguigu.system.mapper.SysUserMapper;
import com.atguigu.system.service.SysMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * ClassName: UserDetailsServiceImpl
 * Package: com.atguigu.system.service.impl
 * Description:
 * @Author 张 文 强
 * @Create 2023/5/14 14:47
 * @Version 1.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysMenuService sysMenuService;
    //根据用户名查询用户信息
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper();
        wrapper.eq(SysUser::getUsername,username);

        SysUser sysUser = sysUserMapper.selectOne(wrapper);
        if (sysUser == null){
            throw new CustomException(ResultCodeEnum.ACCOUNT_ERROR);
        }
        if (sysUser.getStatus() == 0){
            throw new CustomException(ResultCodeEnum.ACCOUNT_STOP);
        }
        List<String> permsList = sysMenuService.findUserPermsList(sysUser.getId());
        sysUser.setUserPermsList(permsList);

        return new CustomUser(sysUser, Collections.emptyList());
    }
}
