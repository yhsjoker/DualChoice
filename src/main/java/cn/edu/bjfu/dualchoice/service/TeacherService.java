package cn.edu.bjfu.dualchoice.service;

import cn.edu.bjfu.dualchoice.pojo.Teacher;

public interface TeacherService {
    public Teacher findTeacherById(int id);

    int getTeacherIdByName(String name);
}
