package cn.edu.bjfu.dualchoice.mapper;

import cn.edu.bjfu.dualchoice.pojo.Teacher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TeacherMapper extends BaseMapper<Teacher> {
    @Select("SELECT id FROM Teacher WHERE name = #{name}")
    int getTeacherIdByName(String name);

    @Select("SELECT id, name, title, phone, qualification FROM Teacher")
    List<Teacher> getTeacherList();

    @Select("SELECT id, name, title, phone, qualification FROM Teacher WHERE college_id = #{id}")
    List<Teacher> getTeacherListById(int id);

    @Update("UPDATE Teacher SET qualification = #{qualification} WHERE id = #{id}")
    void setQualificationById(int id, String qualification);


}
