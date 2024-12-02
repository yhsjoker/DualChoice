package cn.edu.bjfu.dualchoice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DisciplineInfoMapper {
    @Select("SELECT d2_name FROM DisciplineInfo WHERE d1_name = #{name}")
    List<String> findNameByDiscipline(String name);
}
