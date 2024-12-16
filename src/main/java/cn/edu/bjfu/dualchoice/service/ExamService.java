package cn.edu.bjfu.dualchoice.service;


public interface ExamService {
    void insertExamInfo(int studentId, int subjectId, String type, float score, java.sql.Timestamp time, String location, String comment, String url);

    float getScore(int id);


}
