package com.czj.config;

import lombok.Data;

@Data
public class InitCourseConfig {
    // 新用户自动添加课程序列
    public static Integer[] courseIds = new Integer[2];

    static{
        courseIds[0] = 1;   // 新手教程
        courseIds[1] = 66;   // 新手指引

    }
}
