package cn.edu.bjfu.dualchoice.service;

import cn.edu.bjfu.dualchoice.pojo.Student;

import java.util.List;

public interface StudentService {

    List<Student> selectAllStudents();

    void updateStudent(String phone, String emergency_phone,String email, String undergraduate_major, String undergraduate_school, String origin, java.sql.Date graduation_date, String exam_ticket_number, String category, String graduate_type, int id);

    String selectTeacherName(int id, int priority);

    List<String> selectDisciplineName(int id);
}
