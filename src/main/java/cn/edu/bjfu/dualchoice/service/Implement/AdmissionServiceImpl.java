package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.AdmissionMapper;
import cn.edu.bjfu.dualchoice.pojo.Admission;
import cn.edu.bjfu.dualchoice.service.AdmissionService;
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
}
