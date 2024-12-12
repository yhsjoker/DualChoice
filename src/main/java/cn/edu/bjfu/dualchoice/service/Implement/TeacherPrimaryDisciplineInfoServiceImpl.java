package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.TeacherPrimaryDisciplineInfoMapper;
import cn.edu.bjfu.dualchoice.pojo.TeacherBaseInfo;
import cn.edu.bjfu.dualchoice.pojo.TeacherPrimaryDisciplineInfo;
import cn.edu.bjfu.dualchoice.service.TeacherPrimaryDisciplineInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherPrimaryDisciplineInfoServiceImpl implements TeacherPrimaryDisciplineInfoService {
    @Autowired
    TeacherPrimaryDisciplineInfoMapper teacherPrimaryDisciplineInfoMapper;
    @Override
    public List<TeacherPrimaryDisciplineInfo> selectAllEleIdName() {
        QueryWrapper<TeacherPrimaryDisciplineInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("discipline_id", 6);
        wrapper.select("teacher_id", "teacher_name");
        return teacherPrimaryDisciplineInfoMapper.selectList(wrapper);
    }
}
