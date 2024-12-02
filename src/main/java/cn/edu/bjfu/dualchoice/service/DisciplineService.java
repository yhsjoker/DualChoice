package cn.edu.bjfu.dualchoice.service;

import cn.edu.bjfu.dualchoice.pojo.Discipline;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DisciplineService {
    public Discipline findDisciplineById(int id);
    public List<Discipline> findSecondDisByFistId(int id);


    int selectIdByName(String name);
}
