package com.atguigu.system.filter;/**
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
import com.atguigu.common.util.ResponseUtil;
import com.atguigu.model.vo.LoginVo;
import com.atguigu.system.custom.CustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: TokenLoginFilter
 * Package: com.atguigu.system.filter
 * Description:
 * @Author 张 文 强
 * @Create 2023/5/14 14:55
 * @Version 1.0
 */
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {
    private RedisTemplate redisTemplate;
    public TokenLoginFilter(AuthenticationManager manager, RedisTemplate redisTemplate) {
        this.setAuthenticationManager(manager);
        //指定登录接口及提交方式
        this.setRequiresAuthenticationRequestMatcher(
                new AntPathRequestMatcher("/admin/system/index/login","POST")
        );
        //给redisTemplate属性赋值
        this.redisTemplate = redisTemplate;
    }
    /**
     * 登录认证
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //获取表单提交的用户名密码，封装为LoginVo
        try {
            LoginVo loginVo = new ObjectMapper().readValue(request.getInputStream(),LoginVo.class);
            //将LoginVo数据封装为Security要求的对象UsernamePasswordAuthenticationToken
            Authentication authentication = new UsernamePasswordAuthenticationToken(loginVo.getUsername(), loginVo.getPassword());
            //调用方法进行认证 authentication
            return this.getAuthenticationManager().authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 登录成功
     * @param request
     * @param response
     * @param chain
     * @param authResult the object returned from the <tt>attemptAuthentication</tt>
     * method.
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        //通过Authentication获取用户信息
        CustomUser customUser = (CustomUser) authResult.getPrincipal();

        String token = UUID.randomUUID().toString().replaceAll("-","");
        redisTemplate.boundValueOps(token).set(customUser.getSysUser(),2, TimeUnit.HOURS);

        Map<String,Object> map = new HashMap<>();
        map.put("token",token);
        ResponseUtil.out(response, Result.ok(map));
    }
    /**
     * 登录失败
     * @param request
     * @param response
     * @param
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        ResponseUtil.out(response, Result.build(null, ResultCodeEnum.LOGIN_MOBLE_ERROR));
    }
}
