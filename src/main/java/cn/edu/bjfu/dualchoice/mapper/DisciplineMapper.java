package cn.edu.bjfu.dualchoice.mapper;

import cn.edu.bjfu.dualchoice.pojo.Discipline;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DisciplineMapper extends BaseMapper<Discipline> {
    @Select("SELECT id FROM Discipline WHERE name = #{name}")
    int selectIdByName(String name);
}
