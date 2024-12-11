package cn.edu.bjfu.dualchoice.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface GraduateManagementSecretaryMapper {
    @Select("SELECT College.name FROM GraduateManagementSecretary, College WHERE GraduateManagementSecretary.college_id = College.id AND GraduateManagementSecretary.id= #{id}")
    String selectCollegeById(int id);

    @Select("SELECT college_id FROM GraduateManagementSecretary WHERE id = #{id}")
    int selectCollegeIdById(int id);
}
