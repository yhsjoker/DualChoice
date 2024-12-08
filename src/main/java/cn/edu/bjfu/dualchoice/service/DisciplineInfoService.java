package cn.edu.bjfu.dualchoice.service;

import java.util.List;

public interface DisciplineInfoService {
    List<String> findNameByDiscipline(String name);

    List<Integer> findSecIdByPriId(int disciplineId);
}
