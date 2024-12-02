package cn.edu.bjfu.dualchoice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SubjectMapper {
    @Select("SELECT id FROM Subject WHERE name = #{name}")
    int selectSubjectIdByName(String name);
}
