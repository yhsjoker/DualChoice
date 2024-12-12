package cn.edu.bjfu.dualchoice.service;

import cn.edu.bjfu.dualchoice.pojo.TeacherBaseInfo;

import java.util.List;

public interface TeacherBaseInfoService {
    TeacherBaseInfo selectAllById(int teacherId);

    int selectMinimumVolunteerRoundByCollegeId(int collegeId);
}
