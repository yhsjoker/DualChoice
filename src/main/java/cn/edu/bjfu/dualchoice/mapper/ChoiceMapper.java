package cn.edu.bjfu.dualchoice.mapper;

import cn.edu.bjfu.dualchoice.pojo.Choice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface ChoiceMapper extends BaseMapper<Choice> {
    @Insert("INSERT INTO Choice(teacher_id, student_id, priority) VALUES (#{teacher_id}, #{student_id}, #{priority})")
    void insertChoice(int teacher_id, int student_id, int priority);


}
