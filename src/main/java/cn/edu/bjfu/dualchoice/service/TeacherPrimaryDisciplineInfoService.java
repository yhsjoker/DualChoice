package cn.edu.bjfu.dualchoice.service;

import cn.edu.bjfu.dualchoice.pojo.TeacherBaseInfo;
import cn.edu.bjfu.dualchoice.pojo.TeacherPrimaryDisciplineInfo;

import java.util.List;

public interface TeacherPrimaryDisciplineInfoService {
    List<TeacherPrimaryDisciplineInfo> selectAllEleIdName();
}
