package cn.edu.bjfu.dualchoice.mapper;

import cn.edu.bjfu.dualchoice.pojo.StuBaseInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StuBaseInfoMapper {
    @Select("SELECT * FROM StuBaseInfo WHERE studentId = #{id}")
    StuBaseInfo getStuBaseInfoById(int id);
}
