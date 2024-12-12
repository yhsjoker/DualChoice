package cn.edu.bjfu.dualchoice.controller;

import cn.edu.bjfu.dualchoice.pojo.*;
import cn.edu.bjfu.dualchoice.service.Implement.TeacherBaseInfoServiceImpl;
import cn.edu.bjfu.dualchoice.service.StudentApplicationInfoService;
import cn.edu.bjfu.dualchoice.service.TeacherBaseInfoService;
import cn.edu.bjfu.dualchoice.service.TeacherPrimaryDisciplineInfoService;
import cn.edu.bjfu.dualchoice.service.TeacherQuotaInfoService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/drawLottery")
public class DrawLotteryController {
    @Autowired
    TeacherPrimaryDisciplineInfoService teacherPrimaryDisciplineInfoService;
    @Autowired
    TeacherQuotaInfoService teacherQuotaInfoService;
    @Autowired
    StudentApplicationInfoService studentApplicationInfoService;
    @GetMapping("teacher")
    public Result teacher(){
        JSONObject result = new JSONObject();
        JSONArray teachers = new JSONArray();
        List<TeacherPrimaryDisciplineInfo> teacherInfos = teacherPrimaryDisciplineInfoService.selectAllEleIdName();
        for(TeacherPrimaryDisciplineInfo teacherInfo : teacherInfos){
            JSONObject teacher = new JSONObject();
            teacher.put("id", teacherInfo.getTeacherId());
            teacher.put("name", teacherInfo.getTeacherName());
            JSONArray subjects = new JSONArray();
            List<TeacherQuotaInfo> teacherQuotaInfos = teacherQuotaInfoService.findByTeacherId(teacherInfo.getTeacherId());
            for(TeacherQuotaInfo quotaInfo : teacherQuotaInfos){
                JSONObject subject = new JSONObject();
                subject.put("name", quotaInfo.getDisciplineName());
                subject.put("academicQuota", quotaInfo.getAcademicQuota());
                subject.put("professionalQuota", quotaInfo.getProfessionalQuota());
                subject.put("phdQuota", quotaInfo.getPhdQuota());
                subjects.add(subject);
            }
            teacher.put("subjects", subjects);
            teachers.add(teacher);
        }
        result.put("teachers", teachers);
        return Result.success(result);
    }
    @GetMapping("/student")
    public Result student(){
        JSONObject result = new JSONObject();
        List<StudentApplicationInfo> studentInfos = studentApplicationInfoService.selectByDisciplineId(6);
        JSONArray students = new JSONArray();

        for(StudentApplicationInfo studentInfo : studentInfos){
            JSONObject student = new JSONObject();
            student.put("id", studentInfo.getStudentId());
            student.put("name", studentInfo.getStudentName());
            student.put("type", studentInfo.getGraduateType());
            student.put("subjects", studentApplicationInfoService.selectDisByStuIdParentDis(studentInfo.getStudentId(), 6));
            students.add(student);
        }

        result.put("students", students);
        return Result.success(result);
    }
}
