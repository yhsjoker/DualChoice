package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.DisciplineSecretaryMapper;
import cn.edu.bjfu.dualchoice.pojo.DisciplineSecretary;
import cn.edu.bjfu.dualchoice.service.DisciplineSecretaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DisciplineSecretaryServiceImpl implements DisciplineSecretaryService {
    @Autowired
    DisciplineSecretaryMapper disciplineSecretaryMapper;
    @Override
    public DisciplineSecretary selectById(int securityId) {
        return disciplineSecretaryMapper.selectById(securityId);
    }
}
