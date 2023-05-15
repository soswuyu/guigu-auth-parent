package com.atguigu.system.controller;/**
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

import com.atguigu.common.result.Result;
import com.atguigu.common.result.ResultCodeEnum;
import com.atguigu.model.system.SysUser;
import com.atguigu.model.vo.LoginVo;
import com.atguigu.system.exception.CustomException;
import com.atguigu.system.service.SysUserService;
import com.atguigu.common.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: IndexController
 * Package: com.atguigu.system.controller
 * Description:
 * @Author 张 文 强
 * @Create 2023/5/8 18:57
 * @Version 1.0
 */
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {
    @Autowired
    private SysUserService service;
    @Autowired
    private RedisTemplate redisTemplate;
    //登录
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo){
        //验证用户名密码是否正确
        SysUser sysUser = service.getByUsername(loginVo);
        if (sysUser == null){
            throw new CustomException(ResultCodeEnum.ACCOUNT_ERROR);
        }
        String password = loginVo.getPassword();
        //对上传的密码进行加密
        String encrypt = MD5.encrypt(password);
        if (!sysUser.getPassword().equals(encrypt)){
            throw new CustomException(ResultCodeEnum.PASSWORD_ERROR);
        }
        if (sysUser.getStatus() == 0){
            throw new CustomException(ResultCodeEnum.ACCOUNT_STOP);
        }

        Map<String,String> map = new HashMap<>();
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        redisTemplate.boundValueOps(token).set(sysUser,2, TimeUnit.HOURS);
        map.put("token",token);
        return Result.ok(map);
    }
    //获取用户信息
    @GetMapping("/info")
    public Result info(HttpServletRequest request){
        //获取请求头中的token
        String token = request.getHeader("token");
        //根据token获取Redis中的用户信息
        SysUser sysUser = (SysUser) redisTemplate.boundValueOps(token).get();
        //调用SysUserService中根据用户id获取用户信息的方法
        Map<String,Object> map = service.getUserInfoById(sysUser.getId());
        return Result.ok(map);
    }
    //退出
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request){
        //获取请求头中的token
        String token = request.getHeader("token");
        redisTemplate.delete(token);
        return Result.ok();
    }
}
