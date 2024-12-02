package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.TeacherMapper;
import cn.edu.bjfu.dualchoice.pojo.Teacher;
import cn.edu.bjfu.dualchoice.service.TeacherService;
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
}
