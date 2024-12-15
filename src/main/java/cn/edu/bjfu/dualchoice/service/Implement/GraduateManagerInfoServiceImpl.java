package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.GraduateManagerInfoMapper;
import cn.edu.bjfu.dualchoice.pojo.GraduateManagerInfo;
import cn.edu.bjfu.dualchoice.service.GraduateManagerInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GraduateManagerInfoServiceImpl implements GraduateManagerInfoService {
    @Autowired
    GraduateManagerInfoMapper graduateManagerInfoMapper;
    @Override
    public GraduateManagerInfo selectById(int graduateManagerId) {
        QueryWrapper<GraduateManagerInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("graduate_manager_id", graduateManagerId);
        return graduateManagerInfoMapper.selectOne(wrapper);
    }
}
