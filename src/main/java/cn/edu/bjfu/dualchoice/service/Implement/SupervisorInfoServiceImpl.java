package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.SupervisorInfoMapper;
import cn.edu.bjfu.dualchoice.pojo.SupervisorInfo;
import cn.edu.bjfu.dualchoice.service.SupervisorInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupervisorInfoServiceImpl implements SupervisorInfoService {
    @Autowired
    SupervisorInfoMapper supervisorInfoMapper;

    @Override
    public SupervisorInfo selectById(int id) {
        return supervisorInfoMapper.selectBySupervisorId(id);
    }
}
