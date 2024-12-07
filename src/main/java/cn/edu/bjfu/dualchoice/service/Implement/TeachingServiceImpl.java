package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.TeachingMapper;
import cn.edu.bjfu.dualchoice.pojo.Teaching;
import cn.edu.bjfu.dualchoice.service.TeachingService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;

@Service
public class TeachingServiceImpl implements TeachingService {
    @Autowired
    TeachingMapper teachingMapper;
    @Override
    public void updateInfo(int disciplineId, int teacherId, int academicQuota, int professionalQuota, int phdQuota) {
        UpdateWrapper<Teaching> wrapper = new UpdateWrapper<>();
        wrapper.eq("discipline_id", disciplineId).eq("teacher_id", teacherId);
        wrapper.set("academic_quota", academicQuota);
        wrapper.set("professional_quota", professionalQuota);
        wrapper.set("direct_phd_quota", phdQuota);
        teachingMapper.update(null, wrapper);
    }
}
