package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.DisciplineMapper;
import cn.edu.bjfu.dualchoice.mapper.StudentMapper;
import cn.edu.bjfu.dualchoice.pojo.Choice;
import cn.edu.bjfu.dualchoice.pojo.Discipline;
import cn.edu.bjfu.dualchoice.service.DisciplineService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisciplineServiceImpl implements DisciplineService {
    @Autowired
    DisciplineMapper disciplineMapper;
    @Override
    public Discipline findDisciplineById(int id){
        return disciplineMapper.selectById(id);
    }
    @Override
    public List<Discipline> findSecondDisByFistId(int id){
        QueryWrapper<Discipline> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_discipline", id);
        return disciplineMapper.selectList(wrapper);
    }

    @Override
    public int selectIdByName(String name){
        return disciplineMapper.selectIdByName(name);
    }
}
