package com.atguigu.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

@Configuration  //声明当前类为一个配置类，相当于xml配置文件的作用
@EnableWebSecurity  //开启 springSecurity权限框架的功能，声明式配置
public class AppWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override   //此方法用于认证
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //super.configure(auth);    // 默认认证，无法认证，系统中没有任何账号
        /*auth.inMemoryAuthentication()
                .withUser("king").password("123456").roles("学徒","大师")
                .and()
                .withUser("rocket").password("123456").authorities("罗汉拳","葵花宝典");//基于权限*/
        //auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override   //重写父类方法用预授权
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);    //默认授权，所有请求都会被限制，不允许访问，包括首页和静态资源
        //实验一：授权首页和静态资源
        /*http.authorizeRequests()
                .antMatchers("/layui/**","/index.jsp").permitAll()  //授权首页和静态资源
                .anyRequest().authenticated();  //其它资源的访问仍需授权   */
        http.authorizeRequests()
                .antMatchers("/layui/**","/index.jsp").permitAll()  //授权首页和静态资源
                //.antMatchers("/level1/**").hasRole("学徒")
                .antMatchers("/level1/1").hasAuthority("罗汉拳")
                .antMatchers("/level2/**").hasRole("大师")
                //.antMatchers("/level3/**").hasRole("宗师")
                .antMatchers("/level3/1").hasAuthority("葵花宝典")
                .antMatchers("/level3/2").hasAuthority("龟派气功")
                .antMatchers("/level3/3").hasAuthority("独孤九剑")
                .anyRequest().authenticated();//放最后，以上未规定的都需要权限认证
        //实验二：授权默认登录页或自定义登录页
        // 默认登录页要求：
        // 请求 action="/login"
        // 请求method="post"
        // 请求参数必须：usernsme  password
        // 请求必须携带_csrf参数
        // http.formLogin();   //当没有权限访问资源时，会自动跳转到默认登录页
        //自定义登录页需要指定自己的登录页，请求 action，以及请求参数名称和登陆跳转路径
        http.formLogin()
                .loginPage("/index.jsp")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/main");

        //实验五：授权注销功能
        http.logout();  //默认请求地址：/logout    如果是 Get请求需要禁用 csrf，否则只能发起 post请求
        //http.logout().logoutUrl("/logout").logoutSuccessUrl("/index.jsp");  //自定义请求URL路径 ，以及注销后去哪儿
//        http.logout().addLogoutHandler(new LogoutHandler() {
//            @Override
//            public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//                request.getSession().invalidate();
//            }
//        });

        //实验七：无权访问异常错误统一处理页面
        // 7.1 直接指定错误页面
        http.exceptionHandling().accessDeniedPage("/error403");
        // 7.2 通过匿名内部类进行处理 (推荐使用)
        http.exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                request.getRequestDispatcher("/error403").forward(request, response);//处理同步请求
            }
        });

        // 实验八：记住我 cookie版 清理浏览器 cookie或重启服务器都会导致 cookie版记住我功能失效
        http.rememberMe();//请求必须携带 remember-me 参数名称
        //记住我数据库版
//        JdbcTokenRepositoryImpl ptr = new JdbcTokenRepositoryImpl();
//        ptr.setDataSource(dataSource);
//        http.rememberMe().tokenRepository(ptr);


        //禁用 csrf
        http.csrf().disable();
    }






    // 基于 xml配置文件声明一个 bean对象
    // <bean id="user" class="com.atguigu.bean.user"><bean>
    /**
     *  @bean   //基于注解声明一个 bean对象
     *  public User user () {
     *      return new User;
     *  }
     */
}
