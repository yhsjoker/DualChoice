package cn.edu.bjfu.dualchoice.service;

import cn.edu.bjfu.dualchoice.pojo.StuBaseInfo;

import java.util.List;


public interface StuBaseInfoService {
    StuBaseInfo getStuBaseInfoById(int id);

    List<StuBaseInfo> getStuBaseInfoBydiscipline(String discipline);

    List<StuBaseInfo> getStuBaseInfoByCollegeId(int id);
}
