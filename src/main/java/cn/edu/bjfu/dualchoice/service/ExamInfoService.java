package cn.edu.bjfu.dualchoice.service;


import cn.edu.bjfu.dualchoice.pojo.ExamInfo;

public interface ExamInfoService {
    ExamInfo selectExamInfoById(int id, String name);
}
