package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.ExamInfoMapper;
import cn.edu.bjfu.dualchoice.pojo.ExamInfo;
import cn.edu.bjfu.dualchoice.service.ExamInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamInfoServiceImpl implements ExamInfoService {
    @Autowired
    private ExamInfoMapper examInfoMapper;

    @Override
    public ExamInfo selectExamInfoById(int id, String name){
        return examInfoMapper.selectExamInfoById(id, name);
    }
}
