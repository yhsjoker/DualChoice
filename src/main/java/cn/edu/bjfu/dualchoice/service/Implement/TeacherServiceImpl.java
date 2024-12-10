package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.TeacherMapper;
import cn.edu.bjfu.dualchoice.pojo.Teacher;
import cn.edu.bjfu.dualchoice.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    TeacherMapper teacherMapper;
    @Override
    public Teacher findTeacherById(int id){
        return teacherMapper.selectById(id);
    }

    @Override
    public int getTeacherIdByName(String name){
        return teacherMapper.getTeacherIdByName(name);
    }

    @Override
    public void increaseVolRound(int teacherId) {
        UpdateWrapper<Teacher> wrapper = new UpdateWrapper<>();
        wrapper.eq("teacher_id", teacherId);
        wrapper.setSql("volunteer_round = volunteer_round + 1");
        teacherMapper.update(null, wrapper);
    }
}
