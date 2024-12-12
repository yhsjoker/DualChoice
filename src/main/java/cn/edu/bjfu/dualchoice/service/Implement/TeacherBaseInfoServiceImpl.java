package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.TeacherBaseInfoMapper;
import cn.edu.bjfu.dualchoice.pojo.TeacherBaseInfo;
import cn.edu.bjfu.dualchoice.service.TeacherBaseInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherBaseInfoServiceImpl implements TeacherBaseInfoService {
    @Autowired
    TeacherBaseInfoMapper teacherBaseInfoMapper;
    @Override
    public TeacherBaseInfo selectAllById(int teacherId) {
        QueryWrapper<TeacherBaseInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id", teacherId);
        return teacherBaseInfoMapper.selectOne(wrapper);
    }

    @Override
    public int selectMinimumVolunteerRoundByCollegeId(int collegeId) {
        QueryWrapper<TeacherBaseInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("college_id", collegeId);
        List<Object> results = teacherBaseInfoMapper.selectObjs(wrapper.select("MIN(volunteer_round)"));
        if (results != null && !results.isEmpty()) {
            return (Integer) results.get(0);
        }
        return -1;
    }
}
