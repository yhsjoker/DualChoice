package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.AdmissionMapper;
import cn.edu.bjfu.dualchoice.pojo.Admission;
import cn.edu.bjfu.dualchoice.service.AdmissionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdmissionServiceImpl implements AdmissionService {
    @Autowired
    private AdmissionMapper admissionMapper;

    @Override
    public List<Admission> selectAll() {
        return admissionMapper.selectList(null);
    }

    @Override
    public Admission selectByStuId(int studentId) {
        QueryWrapper<Admission> wrapper = new QueryWrapper<>();
        wrapper.eq("student_id", studentId);
        return admissionMapper.selectOne(wrapper);
    }
}
