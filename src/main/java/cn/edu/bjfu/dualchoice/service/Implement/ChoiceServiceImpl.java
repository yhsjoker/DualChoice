package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.ChoiceMapper;
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
        QueryWrapper<Choice> updateWrapper = new QueryWrapper<>();
        updateWrapper.eq("teacher_id", teacher_id).eq("student_id", student_id);
        Choice choiceToUpdate = new Choice();
        choiceToUpdate.setPriority(4);
        choiceMapper.update(choiceToUpdate, updateWrapper);

        QueryWrapper<Choice> updateOtherWrapper = new QueryWrapper<>();
        updateOtherWrapper.eq("student_id", student_id).ne("teacher_id", teacher_id);
        Choice otherChoices = new Choice();
        otherChoices.setPriority(-1);
        choiceMapper.update(otherChoices, updateOtherWrapper);
    }
}
