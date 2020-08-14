package com.atguigu.springsecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder bp = new BCryptPasswordEncoder();
        String encode = bp.encode("123456");
        System.out.println("encode = " + encode);
        //$2a$10$iuxBlrQU7Ju7GzGCnET6E.wSaPBmTUcxfZ8hsPjGLJ25j9xktpOv6
        //$2a$10$bGkPehHTxYHKJWRGoGlhOuIcGkDeS3KidCX5V5vjui9PNFgcwcKDq
        //$2a$10$.dDWeRb01wpzpcgtYqXqOepl4pE2fYfhDY3K8lLm3XfehNc15TY0W

        int length = "$2a$10$iuxBlrQU7Ju7GzGCnET6E.wSaPBmTUcxfZ8hsPjGLJ25j9xktpOv6".length();
        System.out.println("length = " + length); // 60个长度
    }
}
