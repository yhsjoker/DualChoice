package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.StudentMapper;
import cn.edu.bjfu.dualchoice.pojo.Student;
import cn.edu.bjfu.dualchoice.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public List<Student> selectAllStudents(){
        return studentMapper.selectAllStudents();
    }

    @Override
    public void updateStudent(String phone, String emergency_phone,String email, String undergraduate_major, String undergraduate_school, String origin, java.sql.Date graduation_date, String exam_ticket_number, String category, String graduate_type,int id){
        studentMapper.updateStudent(phone, emergency_phone,email, undergraduate_major, undergraduate_school, origin, graduation_date, exam_ticket_number, category, graduate_type, id);
    }

    @Override
    public String selectTeacherName(int id, int priority){
        return studentMapper.selectTeacherName(id, priority);
    }

    @Override
    public List<String> selectDisciplineName(int id){
        return studentMapper.selectDisciplineName(id);
    }

    @Override
    public void updateResume(String url, int id){
        studentMapper.updateResume(url, id);
    }

    @Override
    public String selectResumeById(int id){
        return studentMapper.selectResumeById(id);
    }


}
