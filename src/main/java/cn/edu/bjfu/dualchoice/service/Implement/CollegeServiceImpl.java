package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.CollegeMapper;
import cn.edu.bjfu.dualchoice.pojo.College;
import cn.edu.bjfu.dualchoice.service.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollegeServiceImpl implements CollegeService {
    @Autowired
    CollegeMapper collegeMapper;
    @Override
    public List<College> selectAll() {
        return collegeMapper.selectList(null);
    }
}
