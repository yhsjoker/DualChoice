package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.InterviewGroupMapper;

import cn.edu.bjfu.dualchoice.service.InterviewGroupService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class InterviewGroupServiceImpl implements InterviewGroupService {

    @Autowired
    private InterviewGroupMapper InterviewGroupMapper;

    @Override
    public String selectDisNameById(int id){
        return InterviewGroupMapper.selectDisNameById(id);
    }









}
