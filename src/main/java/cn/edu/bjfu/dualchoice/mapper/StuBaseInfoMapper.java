package cn.edu.bjfu.dualchoice.mapper;

import cn.edu.bjfu.dualchoice.pojo.StuBaseInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StuBaseInfoMapper {
    @Select("SELECT * FROM StuBaseInfo WHERE studentId = #{id}")
    StuBaseInfo getStuBaseInfoById(int id);

    @Select("SELECT studentId, name, examNumber, studentType FROM StuBaseInfo WHERE discipline = #{discipline}")
    List<StuBaseInfo> getStuBaseInfoBydiscipline(String discipline);

    @Select("SELECT studentId, name, examNumber, studentType FROM StuBaseInfo WHERE college_id = #{id}")
    List<StuBaseInfo> getStuBaseInfoByCollegeId(int id);
}
