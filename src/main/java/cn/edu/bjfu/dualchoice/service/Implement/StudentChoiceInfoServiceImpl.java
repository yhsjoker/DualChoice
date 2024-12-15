package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.StudentChoiceInfoMapper;
import cn.edu.bjfu.dualchoice.pojo.StudentChoiceInfo;
import cn.edu.bjfu.dualchoice.service.StudentChoiceInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentChoiceInfoServiceImpl implements StudentChoiceInfoService {
    @Autowired
    StudentChoiceInfoMapper studentChoiceInfoMapper;
    @Override
    public List<StudentChoiceInfo> selectStudentsByTeacherIdDisIdVolRound(int teacherId, int disciplineId, int currentVolunteerRound) {
        QueryWrapper<StudentChoiceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id", teacherId).eq("discipline_id", disciplineId).eq("priority", currentVolunteerRound);
        return studentChoiceInfoMapper.selectList(wrapper);
    }

    @Override
    public List<StudentChoiceInfo> selectAcceptedStuByDisId(int id) {
        QueryWrapper<StudentChoiceInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("discipline_id", id).eq("priority", 4);
        return studentChoiceInfoMapper.selectList(wrapper);
    }
}
