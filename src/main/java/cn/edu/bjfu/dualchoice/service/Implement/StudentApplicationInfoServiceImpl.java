package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.StudentApplicationInfoMapper;
import cn.edu.bjfu.dualchoice.pojo.StudentApplicationInfo;
import cn.edu.bjfu.dualchoice.service.StudentApplicationInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentApplicationInfoServiceImpl implements StudentApplicationInfoService {
    @Autowired
    StudentApplicationInfoMapper studentApplicationInfoMapper;
    @Override
    public List<StudentApplicationInfo> selectByDisciplineIdStatus(int disciplineId, String status) {
        QueryWrapper<StudentApplicationInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("discipline_id", disciplineId).eq("preference_order", 0).eq("volunteer_status", status);
        return studentApplicationInfoMapper.selectList(wrapper);
    }

    @Override
    public List<String> selectDisByStuIdParentDis(int studentId, int parentDisciplineId) {
        QueryWrapper<StudentApplicationInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("student_id", studentId)
                .eq("parent_discipline", parentDisciplineId)
                .orderByAsc("discipline_name");

        wrapper.select("discipline_name");

        return studentApplicationInfoMapper.selectObjs(wrapper)
                .stream()
                .map(obj -> (String) obj)
                .toList();
    }

    @Override
    public int selectSecondDisciplineId(int studentId) {
        QueryWrapper<StudentApplicationInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("student_id", studentId).eq("preference_order", 1).select("discipline_id");
        return studentApplicationInfoMapper.selectOne(wrapper).getDisciplineId();
    }
}
