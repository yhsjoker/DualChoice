package cn.edu.bjfu.dualchoice.service;

import cn.edu.bjfu.dualchoice.pojo.StudentChoiceInfo;

import java.util.List;

public interface StudentChoiceInfoService {
    List<StudentChoiceInfo> selectStudentsByTeacherIdDisIdVolRound(int teacherId, int disciplineId, int currentVolunteerRound);
}
