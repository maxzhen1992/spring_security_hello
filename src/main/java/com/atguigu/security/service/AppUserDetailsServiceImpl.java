package com.atguigu.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service    //做认证操作的实现类
public class AppUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String loginacct) throws UsernameNotFoundException {
        String queryUser = "SELECT * FROM `t_admin` WHERE loginacct=?";
        //1.查询指定用户的信息
        Map<String, Object> map = jdbcTemplate.queryForMap(queryUser, loginacct);
        //2.将查询到的用户封装到框架使用的 UserDetails里面
        return new User(map.get("loginacct").toString(),map.get("userpswd").toString(),
                AuthorityUtils.createAuthorityList("罗汉拳","葵花宝典"));//暂时写死，过后数据库查
    }
}
