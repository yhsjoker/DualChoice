package cn.edu.bjfu.dualchoice.mapper;

import cn.edu.bjfu.dualchoice.pojo.CollegeStudentInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CollegeStudentInfoMapper {
    @Select("SELECT student_id, student_name, student_exam_ticket_number, student_category FROM CollegeStudentInfo WHERE college_id = #{id}")
    List<CollegeStudentInfo> getCollegeStudentInfoById(int id);
}
