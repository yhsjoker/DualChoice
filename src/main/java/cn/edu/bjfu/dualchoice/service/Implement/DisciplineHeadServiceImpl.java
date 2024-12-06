package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.DisciplineHeadMapper;
import cn.edu.bjfu.dualchoice.service.DisciplineHeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DisciplineHeadServiceImpl implements DisciplineHeadService {
    @Autowired
    DisciplineHeadMapper disciplineHeadMapper;
    @Override
    public int getDisciplineIdById(int disciplineHeadId) {
        return disciplineHeadMapper.selectById(disciplineHeadId).getDisciplineId();
    }
}
