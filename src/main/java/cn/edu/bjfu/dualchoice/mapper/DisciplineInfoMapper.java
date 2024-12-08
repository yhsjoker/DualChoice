package cn.edu.bjfu.dualchoice.mapper;

import cn.edu.bjfu.dualchoice.pojo.DisciplineInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DisciplineInfoMapper extends BaseMapper<DisciplineInfo> {
    @Select("SELECT d2_name FROM DisciplineInfo WHERE d1_name = #{name}")
    List<String> findNameByDiscipline(String name);
}
