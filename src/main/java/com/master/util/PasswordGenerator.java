package com.master.util;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2/20/2015.
 */
public class PasswordGenerator {
    public static String generateNewPassword() {
        List integers = Lists.newLinkedList();
        List characters = Lists.newLinkedList();
        for(int i = 0; i<10;i++){
            integers.add(i);
        }
        for(int i = 97; i<123;i++){
            characters.add((char)i);
        }
        Collections.shuffle(integers);
        Collections.shuffle(characters);
        String newPass = String.valueOf(integers.get(0).toString()+characters.get(0)+integers.get(5)+integers.get(2)+integers.get(8)+characters.get(5)+integers.get(7)+characters.get(7)+integers.get(9)+characters.get(9)+characters.get(2));
        return newPass;
    }
}
