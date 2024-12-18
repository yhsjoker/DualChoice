package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.DisciplineSecretaryInfoMapper;
import cn.edu.bjfu.dualchoice.pojo.DisciplineSecretaryInfo;
import cn.edu.bjfu.dualchoice.service.DisciplineSecretaryInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DisciplineSecretaryInfoServiceImpl implements DisciplineSecretaryInfoService {
    @Autowired
    DisciplineSecretaryInfoMapper disciplineSecretaryInfoMapper;
    @Override
    public DisciplineSecretaryInfo selectById(int id) {
        QueryWrapper<DisciplineSecretaryInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("secretary_id", id);
        return disciplineSecretaryInfoMapper.selectOne(wrapper);
    }
}
