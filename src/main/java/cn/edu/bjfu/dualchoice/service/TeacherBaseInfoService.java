package cn.edu.bjfu.dualchoice.service;

import cn.edu.bjfu.dualchoice.pojo.TeacherBaseInfo;

public interface TeacherBaseInfoService {
    TeacherBaseInfo selectAllById(int teacherId);

    int selectMinimumVolunteerRoundByCollegeId(int collegeId);
}
