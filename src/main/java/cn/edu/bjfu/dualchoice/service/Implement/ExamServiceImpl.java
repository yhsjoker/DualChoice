package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.ExamMapper;
import cn.edu.bjfu.dualchoice.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamServiceImpl implements ExamService {
    @Autowired
    private ExamMapper examMapper;

    @Override
    public void insertExamInfo(int studentId, int subjectId, String type, float score, java.sql.Timestamp time, String location, String comment, String url){
        examMapper.insertExamInfo(studentId, subjectId, type, score, time, location, comment, url);
    }

    @Override
    public float getScore(int id){
        return examMapper.getScore(id);
    }
}
