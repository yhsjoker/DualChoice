package cn.edu.bjfu.dualchoice.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExamMapper {
    @Insert("INSERT INTO Exam(subject_id, student_id, type, score,time, location, comment) VALUES(#{subjectId}, #{studentId}, #{type}, #{score}, #{time}, #{location}, #{comment}) ")
    void insertExamInfo(int studentId, int subjectId, String type, float score, java.sql.Timestamp time, String location, String comment);
}
