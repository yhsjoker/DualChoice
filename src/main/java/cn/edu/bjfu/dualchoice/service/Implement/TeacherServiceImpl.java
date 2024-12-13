package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.TeacherMapper;
import cn.edu.bjfu.dualchoice.pojo.Teacher;
import cn.edu.bjfu.dualchoice.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<Teacher> getTeacherList(){
        return teacherMapper.getTeacherList();
    }

    @Override
    public void setQualificationById(int id, String qualification){
        teacherMapper.setQualificationById(id, qualification);
    }

    @Override
    public List<Teacher> getTeacherListById(int id){
        return teacherMapper.getTeacherListById(id);
    }

    @Override
    public List<Teacher> selectTeacherByCollegeId(int collegeId) {
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        wrapper.eq("college_id", collegeId);
        return teacherMapper.selectList(wrapper);
    }
}
