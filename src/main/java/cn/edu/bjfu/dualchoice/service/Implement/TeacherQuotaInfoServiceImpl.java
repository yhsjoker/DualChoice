package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.TeacherQuotaInfoMapper;
import cn.edu.bjfu.dualchoice.pojo.TeacherQuotaInfo;
import cn.edu.bjfu.dualchoice.pojo.User;
import cn.edu.bjfu.dualchoice.service.TeacherQuotaInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherQuotaInfoServiceImpl implements TeacherQuotaInfoService {
    @Autowired
    TeacherQuotaInfoMapper teacherQuotaInfoMapper;
    @Override
    public List<TeacherQuotaInfo> findTeacherQuotaInfoByDisciplineId(int disciplineId){
        QueryWrapper<TeacherQuotaInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("discipline_id", disciplineId);
        return teacherQuotaInfoMapper.selectList(wrapper);
    }
}
