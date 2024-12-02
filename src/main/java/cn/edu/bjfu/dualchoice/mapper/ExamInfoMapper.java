package cn.edu.bjfu.dualchoice.mapper;

import cn.edu.bjfu.dualchoice.pojo.ExamInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ExamInfoMapper
{
    @Select("SELECT * FROM ExamInfo WHERE ExamInfo.id = #{id} AND ExamInfo.name = #{name}")
    ExamInfo selectExamInfoById(int id, String name);
}
