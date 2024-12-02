package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.DisciplineInfoMapper;
import cn.edu.bjfu.dualchoice.service.DisciplineInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisciplineInfoServiceImpl implements DisciplineInfoService {
    @Autowired
    private DisciplineInfoMapper disciplineInfoMapper;

    @Override
    public List<String> findNameByDiscipline(String name){
        return disciplineInfoMapper.findNameByDiscipline(name);
    }
}
