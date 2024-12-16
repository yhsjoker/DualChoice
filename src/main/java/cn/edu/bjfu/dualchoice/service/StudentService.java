package cn.edu.bjfu.dualchoice.service;

import cn.edu.bjfu.dualchoice.pojo.Student;

import java.util.Date;
import java.util.List;

public interface StudentService {

    List<Student> selectAllStudents();

    void updateStudent(String phone, String emergency_phone, String email, String undergraduate_major, String undergraduate_school, String origin, Date graduation_date, String exam_ticket_number, String category, String graduate_type, int id);

    String selectTeacherName(int id, int priority);

    List<String> selectDisciplineName(int id);

    String selectGraduateType(int id);

    void updateResume(String url, int id);

    String selectResumeById(int id);

    void updateVolunteerStatus(int id, String status);

    String selectVolunteerStatus(int id);
}
