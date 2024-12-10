package cn.edu.bjfu.dualchoice.service;

import cn.edu.bjfu.dualchoice.pojo.TeacherQuotaInfo;

import java.util.List;

public interface TeacherQuotaInfoService {
    List<TeacherQuotaInfo> findTeacherQuotaInfoByDisciplineId(int DisciplineId);

    List<TeacherQuotaInfo> findByTeacherId(int teacherId);
}