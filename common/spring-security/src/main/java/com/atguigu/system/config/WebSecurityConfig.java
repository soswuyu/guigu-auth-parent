package com.atguigu.system.config;/**
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

import com.atguigu.system.custom.CustomMd5PasswordEncoder;
import com.atguigu.system.filter.TokenAuthenticationFilter;
import com.atguigu.system.filter.TokenLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * ClassName: WebSecurityConfig
 * Package: com.atguigu.system.config
 * Description:
 * @Author 张 文 强
 * @Create 2023/5/14 16:52
 * @Version 1.0
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CustomMd5PasswordEncoder customMd5PasswordEncoder;

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 这是配置的关键，决定哪些接口开启防护，哪些接口绕过防护
        http
                //关闭csrf跨站请求伪造
                .csrf().disable()
                // 开启跨域以便前端调用接口
                .cors().and()
                .authorizeRequests()
                // 指定某些接口不需要通过验证即可访问。登陆接口肯定是不需要认证的
                .antMatchers("/admin/system/index/login").permitAll()
                // 这里意思是其它所有接口需要认证才能访问
                .anyRequest().authenticated()
                .and()
                //TokenAuthenticationFilter放到UsernamePasswordAuthenticationFilter的前面，这样做就是为了除了登录的时候去查询数据库外，其他时候都用token进行认证。
                .addFilterBefore(new TokenAuthenticationFilter(redisTemplate), UsernamePasswordAuthenticationFilter.class)
                .addFilter(new TokenLoginFilter(authenticationManager(),redisTemplate));

        //禁用session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 指定UserDetailService和加密器
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(customMd5PasswordEncoder);
    }

    /**
     * 配置哪些请求不拦截
     * 排除swagger相关请求
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/favicon.ico","/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**", "/doc.html");
    }

}
