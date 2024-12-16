package cn.edu.bjfu.dualchoice.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ExamMapper {
    @Insert("INSERT INTO Exam(subject_id, student_id, type, score,time, location, comment, signature) VALUES(#{subjectId}, #{studentId}, #{type}, #{score}, #{time}, #{location}, #{comment}, #{url}) ")
    void insertExamInfo(int studentId, int subjectId, String type, float score, java.sql.Timestamp time, String location, String comment, String url);

    @Select("SELECT SUM(score) FROM Exam WHERE student_id = #{id}")
    float getScore(int id);
}
