package cn.edu.bjfu.dualchoice.mapper;

import cn.edu.bjfu.dualchoice.pojo.Application;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApplicationMapper extends BaseMapper<Application> {
    @Insert("INSERT INTO Application(student_id, discipline_id, preference_order) VALUES (#{student_id}, #{discipline_id}, #{preference_order})")
    void insertApplication(int student_id, int discipline_id, int preference_order);
}
