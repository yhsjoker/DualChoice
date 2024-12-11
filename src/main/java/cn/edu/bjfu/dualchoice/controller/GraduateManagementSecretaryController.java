package cn.edu.bjfu.dualchoice.controller;

import cn.edu.bjfu.dualchoice.pojo.*;
import cn.edu.bjfu.dualchoice.service.*;
import cn.edu.bjfu.dualchoice.utils.ThreadLocalUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/graduateManagementSecretary")
public class GraduateManagementSecretaryController {

    @Autowired
    GraduateManagementSecretaryService graduateManagementSecretaryService;

    @Autowired
    StudentService studentService;

    @Autowired
    TeacherService teacherService;

    @Autowired
    ApplicationService applicationService;

    @Autowired
    DisciplineService disciplineService;

    @Autowired
    ChoiceService choiceService;

    @Autowired
    StuBaseInfoService stuBaseInfoService;

    @Autowired
    DisciplineInfoService disciplineInfoService;

    @GetMapping("/getCollege")
    public Result getCollege(){
        Map<String, Object> map = ThreadLocalUtil.get();
        int graduateManagementSecretaryId = (Integer) map.get("id");
        String collegeName = graduateManagementSecretaryService.selectCollegeById(graduateManagementSecretaryId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("collegeName", collegeName);

        return Result.success(jsonObject);
    }

    @GetMapping("/students")
    public Result getStudents(){
        Map<String, Object> map = ThreadLocalUtil.get();
        int graduateManagementSecretaryId = (Integer) map.get("id");
        System.out.println(graduateManagementSecretaryId);
        int collegeId = graduateManagementSecretaryService.selectCollegeIdById(graduateManagementSecretaryId);
        JSONObject info = new JSONObject();
        List<StuBaseInfo> studentList = stuBaseInfoService.getStuBaseInfoByCollegeId(collegeId);
        List<JSONObject> stuInfoList = new ArrayList<>();
        for (StuBaseInfo student : studentList) {
            JSONObject stuInfo = new JSONObject();
            stuInfo.put("id", student.getStudentId());
            stuInfo.put("name", student.getName());
            stuInfo.put("examNumber", student.getExamNumber());
            stuInfo.put("category", student.getStudentType());

            stuInfoList.add(stuInfo);
        }

        info.put("students", stuInfoList);

        return Result.success(info);
    }

    @GetMapping("/teachers")
    public Result getTeachers(){
        Map<String, Object> map = ThreadLocalUtil.get();
        int graduateManagementSecretaryId = (Integer) map.get("id");
        int collegeId = graduateManagementSecretaryService.selectCollegeIdById(graduateManagementSecretaryId);
        JSONObject info = new JSONObject();
        List<Teacher> teacherList = teacherService.getTeacherListById(collegeId);
        List<JSONObject> teaInfoList = new ArrayList<>();
        for (Teacher teacher : teacherList) {
            JSONObject teaInfo = new JSONObject();
            teaInfo.put("id", teacher.getId());
            teaInfo.put("name", teacher.getName());
            teaInfo.put("title", teacher.getTitle());
            teaInfo.put("phone", teacher.getPhone());
            teaInfo.put("qualification", teacher.getQualification());

            teaInfoList.add(teaInfo);
        }

        info.put("teachers", teaInfoList);

        return Result.success(info);
    }

    @PutMapping("/teachers/setQualification")
    public Result setQualification(@RequestBody Teacher teacher){
        int teacherId = teacher.getId();
        System.out.println(teacherId);
        String qualification = teacher.getQualification();
        System.out.println(qualification);
        teacherService.setQualificationById(teacherId, qualification);
        return Result.success();
    }

}
