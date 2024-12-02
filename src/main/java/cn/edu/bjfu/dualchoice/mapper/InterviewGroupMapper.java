package cn.edu.bjfu.dualchoice.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;



@Mapper
public interface InterviewGroupMapper {
    @Select("SELECT d.name FROM Discipline d, InterviewGroup i WHERE d.id = i.discipline_id AND i.id = #{id}")
    String selectDisNameById(int id);

}
