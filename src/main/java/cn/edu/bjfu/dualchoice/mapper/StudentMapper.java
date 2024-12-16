package cn.edu.bjfu.dualchoice.mapper;

import cn.edu.bjfu.dualchoice.pojo.Student;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {

    @Select("SELECT id, name, exam_ticket_number, category FROM Student")
    List<Student> selectAllStudents();

    @Update("UPDATE Student SET phone = #{phone}, emergency_phone = #{emergency_phone}, email = #{email}, undergraduate_major = #{undergraduate_major}, undergraduate_school = #{undergraduate_school}, origin = #{origin}, graduation_date = #{graduation_date}, exam_ticket_number = #{exam_ticket_number}, category = #{category}, graduate_type = #{graduate_type}  WHERE id = #{id}")
    void updateStudent(String phone, String emergency_phone, String email, String undergraduate_major, String undergraduate_school, String origin, Date graduation_date, String exam_ticket_number, String category, String graduate_type, int id);

    @Select("SELECT t.name FROM Teacher t, Student s, Choice c WHERE t.id = c.teacher_id AND c.student_id = s.id AND s.id = #{id} AND c.priority = #{priority}")
    String selectTeacherName(int id, int priority);

    @Select("SELECT d.name FROM Discipline d, Student s, Application a WHERE d.id = a.discipline_id AND a.student_id = s.id AND s.id = #{id} AND a.preference_order <> 0")
    List<String> selectDisciplineName(int id);

    @Update("UPDATE Student SET resume = #{url} WHERE id = #{id}")
    void updateResume(String url, int id);

    @Select("SELECT resume FROM Student WHERE id = #{id}")
    String selectResumeById(int id);


}
