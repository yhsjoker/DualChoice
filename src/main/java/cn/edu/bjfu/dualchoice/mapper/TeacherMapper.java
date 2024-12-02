package cn.edu.bjfu.dualchoice.mapper;

import cn.edu.bjfu.dualchoice.pojo.Teacher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TeacherMapper extends BaseMapper<Teacher> {
    @Select("SELECT id FROM Teacher WHERE name = #{name}")
    int getTeacherIdByName(String name);


}
