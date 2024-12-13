package cn.edu.bjfu.dualchoice.service;

import cn.edu.bjfu.dualchoice.pojo.Teacher;
import java.util.List;

public interface TeacherService {
    public Teacher findTeacherById(int id);

    int getTeacherIdByName(String name);

    void increaseVolRound(int teacherId);

    List<Teacher> getTeacherList();

    void setQualificationById(int id, String qualification);

    List<Teacher> getTeacherListById(int id);

    List<Teacher> selectTeacherByCollegeId(int collegeId);
}
