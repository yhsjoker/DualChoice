package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.StuBaseInfoMapper;
import cn.edu.bjfu.dualchoice.pojo.StuBaseInfo;
import cn.edu.bjfu.dualchoice.service.StuBaseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StuBaseInfoServiceImpl implements StuBaseInfoService {
    @Autowired
    StuBaseInfoMapper stuBaseInfoMapper;

    @Override
    public StuBaseInfo getStuBaseInfoById(int id){
        return stuBaseInfoMapper.getStuBaseInfoById(id);
    }
}
