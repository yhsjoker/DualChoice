package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.CollegeStudentInfoMapper;
import cn.edu.bjfu.dualchoice.pojo.CollegeStudentInfo;
import cn.edu.bjfu.dualchoice.service.CollegeStudentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollegeStudentInfoServiceImpl implements CollegeStudentInfoService {

    @Autowired
    CollegeStudentInfoMapper collegeStudentInfoMapper;


    @Override
    public List<CollegeStudentInfo> getCollegeStudentInfoById(int id){
        return collegeStudentInfoMapper.getCollegeStudentInfoById(id);
    }
}
