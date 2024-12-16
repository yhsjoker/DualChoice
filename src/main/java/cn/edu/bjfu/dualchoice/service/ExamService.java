package cn.edu.bjfu.dualchoice.service;


import java.util.Date;

public interface ExamService {
    void insertExamInfo(int studentId, int subjectId, String type, float score, Date time, String location, String comment, String url);

    float getScore(int id);


}
