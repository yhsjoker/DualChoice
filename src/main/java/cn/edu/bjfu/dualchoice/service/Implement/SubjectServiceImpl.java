package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.SubjectMapper;
import cn.edu.bjfu.dualchoice.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectServiceImpl implements SubjectService {
    @Autowired
    private SubjectMapper subjectMapper;

    @Override
    public int selectSubjectIdByName(String name){
        return subjectMapper.selectSubjectIdByName(name);
    }
}
