package cn.edu.bjfu.dualchoice.service;

import cn.edu.bjfu.dualchoice.pojo.Admission;

import java.util.List;

public interface AdmissionService {

    List<Admission> selectAll();

    List<Admission> selectByDisciplineId(int disciplineId);

    Admission selectByStuId(int studentId);
}
