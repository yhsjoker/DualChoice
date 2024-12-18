package cn.edu.bjfu.dualchoice.controller;

import cn.edu.bjfu.dualchoice.pojo.*;
import cn.edu.bjfu.dualchoice.service.*;
import cn.edu.bjfu.dualchoice.utils.Logger;
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
        String user_identity = (String) map.get("user_identity");
        Logger.log(user_identity, graduateManagementSecretaryId, "", Logger.LogType.INFO, "/api/graduateManagementSecretary/getCollege");
        if(!user_identity.equals("GraduateManagementSecretary")){
            Logger.log(user_identity, graduateManagementSecretaryId, "", Logger.LogType.ERROR, "/api/graduateManagementSecretary/getCollege");
            return Result.error("permission denied");
        }

        String collegeName = graduateManagementSecretaryService.selectCollegeById(graduateManagementSecretaryId);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("collegeName", collegeName);

        Logger.log(user_identity, graduateManagementSecretaryId, jsonObject, Logger.LogType.SUCCESS, "/api/graduateManagementSecretary/getCollege");
        return Result.success(jsonObject);
    }

    @GetMapping("/students")
    public Result getStudents(){
        Map<String, Object> map = ThreadLocalUtil.get();
        int graduateManagementSecretaryId = (Integer) map.get("id");
        String user_identity = (String) map.get("user_identity");
        Logger.log(user_identity, graduateManagementSecretaryId, "", Logger.LogType.INFO, "/api/graduateManagementSecretary/students");
        if(!user_identity.equals("GraduateManagementSecretary")){
            Logger.log(user_identity, graduateManagementSecretaryId, "", Logger.LogType.ERROR, "/api/graduateManagementSecretary/students");
            return Result.error("permission denied");
        }

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

        Logger.log(user_identity, graduateManagementSecretaryId, info, Logger.LogType.SUCCESS, "/api/graduateManagementSecretary/students");
        return Result.success(info);
    }

    @GetMapping("/teachers")
    public Result getTeachers(){
        Map<String, Object> map = ThreadLocalUtil.get();
        int graduateManagementSecretaryId = (Integer) map.get("id");
        String user_identity = (String) map.get("user_identity");
        Logger.log(user_identity, graduateManagementSecretaryId, "", Logger.LogType.INFO, "/api/graduateManagementSecretary/teachers");
        if(!user_identity.equals("GraduateManagementSecretary")){
            Logger.log(user_identity, graduateManagementSecretaryId, "", Logger.LogType.ERROR, "/api/graduateManagementSecretary/teachers");
            return Result.error("permission denied");
        }

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

        Logger.log(user_identity, graduateManagementSecretaryId, info, Logger.LogType.SUCCESS, "/api/graduateManagementSecretary/teachers");
        return Result.success(info);
    }

    @PutMapping("/teachers/setQualification")
    public Result setQualification(@RequestBody Teacher teacher){
        Map<String, Object> map = ThreadLocalUtil.get();
        int graduateManagementSecretaryId = (Integer) map.get("id");
        String user_identity = (String) map.get("user_identity");
        Logger.log(user_identity, graduateManagementSecretaryId, teacher, Logger.LogType.INFO, "/api/graduateManagementSecretary/teachers/setQualification");
        if(!user_identity.equals("GraduateManagementSecretary")){
            Logger.log(user_identity, graduateManagementSecretaryId, teacher, Logger.LogType.ERROR, "/api/graduateManagementSecretary/teachers/setQualification");
            return Result.error("permission denied");
        }

        int teacherId = teacher.getId();
        String qualification = teacher.getQualification();
        teacherService.setQualificationById(teacherId, qualification);

        Logger.log(user_identity, graduateManagementSecretaryId, "", Logger.LogType.SUCCESS, "/api/graduateManagementSecretary/teachers/setQualification");
        return Result.success();
    }

}
