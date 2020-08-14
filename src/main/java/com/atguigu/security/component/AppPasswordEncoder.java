package com.atguigu.security.component;

import com.atguigu.security.util.MD5Util;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AppPasswordEncoder implements PasswordEncoder {

    @Override   // 讲原文加密成密文返回
    public String encode(CharSequence charSequence) {
        //return charSequence.toString();
        return MD5Util.digest(charSequence.toString());
    }

    /**
     * 比较密码是否一致
     * @param charSequence 原文
     * @param encodedPassword 密文
     * @return
     */
    @Override
    public boolean matches(CharSequence charSequence, String encodedPassword) {
        //return encodedPassword.equals(charSequence.toString());
        return MD5Util.digest(charSequence.toString()).equals(encodedPassword);
    }
}
