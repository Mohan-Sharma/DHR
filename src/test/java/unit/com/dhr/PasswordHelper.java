package com.dhr;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: Nthdimenzion
 * Date: 26/10/12
 * Time: 1:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class PasswordHelper {

    static ShaPasswordEncoder encoder = new ShaPasswordEncoder();
    public static void main(String a[]){
        System.out.println("DHR password " + encoder.encodePassword("h","dhr"));
    }

}
