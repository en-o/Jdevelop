package com.detabes.string.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * 字符串数字相关
 * @author tn
 * @version 1
 * @ClassName StringNumber
 * @date 2020/8/11 22:07
 */
public class StringNumber {


    /**
     * 返回 字符串 中的数字
     * @param s  字符串
     * @return int
     */
    public static Integer getNum(String s) {
        try {
            Pattern p = compile("\\d+");
            Matcher m = p.matcher(s);
            if(m.find()){
                //截取到数字
                return Integer.valueOf(m.group());
            }
            return 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
}
