package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.AdmissionMapper;
import cn.edu.bjfu.dualchoice.mapper.ChoiceMapper;
import cn.edu.bjfu.dualchoice.pojo.Admission;
import cn.edu.bjfu.dualchoice.pojo.Application;
import cn.edu.bjfu.dualchoice.pojo.Choice;
import cn.edu.bjfu.dualchoice.service.ChoiceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChoiceServiceImpl implements ChoiceService {
    @Autowired
    ChoiceMapper choiceMapper;
    @Autowired
    private AdmissionMapper admissionMapper;

    @Override
    public Choice findChoiceByStuIdPri(int id, int pri){
        QueryWrapper<Choice> wrapper = new QueryWrapper<>();
        wrapper.eq("student_id", id).eq("priority", pri);
        return choiceMapper.selectOne(wrapper);
    }

    @Override
    public void insertChoice(int teacher_id, int student_id, int priority){
        choiceMapper.insertChoice(teacher_id, student_id, priority);
    }

    @Override
    public void lockChoice(int teacher_id, int student_id) {
        Admission admission = new Admission();
        admission.setTeacherId(teacher_id);
        admission.setStudentId(student_id);
        admissionMapper.insert(admission);
    }
}
