package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.GraduateManagementSecretaryMapper;
import cn.edu.bjfu.dualchoice.service.GraduateManagementSecretaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GraduateManagementSecretaryServiceImpl implements GraduateManagementSecretaryService {
    @Autowired
    GraduateManagementSecretaryMapper graduateManagementSecretaryMapper;

    @Override
    public String selectCollegeById(int id){
        return graduateManagementSecretaryMapper.selectCollegeById(id);
    }

    @Override
    public int selectCollegeIdById(int id){
        return graduateManagementSecretaryMapper.selectCollegeIdById(id);
    }
}
