package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.DisciplineInfoMapper;
import cn.edu.bjfu.dualchoice.pojo.DisciplineInfo;
import cn.edu.bjfu.dualchoice.service.DisciplineInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

    @Override
    public List<Integer> findSecIdByPriId(int disciplineId) {
        QueryWrapper<DisciplineInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("d1_id", disciplineId);
        wrapper.select("d2_id");
        return disciplineInfoMapper.selectObjs(wrapper);
    }
}
