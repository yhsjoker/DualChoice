package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.ApplicationMapper;
import cn.edu.bjfu.dualchoice.pojo.Application;
import cn.edu.bjfu.dualchoice.pojo.User;
import cn.edu.bjfu.dualchoice.service.ApplicationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    @Autowired
    ApplicationMapper applicationMapper;
    @Override
    public List<Application> findApplicationByStuIdPreOrd(int stuId, int preOrd){
        QueryWrapper<Application> wrapper = new QueryWrapper<>();
        wrapper.eq("student_id", stuId).eq("preference_order", preOrd);
        return applicationMapper.selectList(wrapper);
    }

    @Override
    public void insertApplication(int student_id, int discipline_id, int preference_order){
        applicationMapper.insertApplication(student_id, discipline_id, preference_order);
    }
}
