package cn.edu.bjfu.dualchoice.service;

import cn.edu.bjfu.dualchoice.pojo.StudentApplicationInfo;

import java.util.List;

public interface StudentApplicationInfoService {
    List<StudentApplicationInfo> selectByDisciplineIdStatus(int disciplineId,String status);

    List<String> selectDisByStuIdParentDis(int studentId, int parentDisciplineId);

    int selectSecondDisciplineId(int studentId);
}
