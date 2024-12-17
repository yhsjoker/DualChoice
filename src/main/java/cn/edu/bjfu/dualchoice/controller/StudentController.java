package cn.edu.bjfu.dualchoice.controller;

import cn.edu.bjfu.dualchoice.pojo.*;
import cn.edu.bjfu.dualchoice.service.*;
import cn.edu.bjfu.dualchoice.utils.AliOssUtil;
import cn.edu.bjfu.dualchoice.utils.ThreadLocalUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @Autowired
    ApplicationService applicationService;

    @Autowired
    DisciplineService disciplineService;

    @Autowired
    ChoiceService choiceService;

    @Autowired
    TeacherService teacherService;

    @Autowired
    StuBaseInfoService stuBaseInfoService;

    @Autowired
    DisciplineInfoService disciplineInfoService;

    @Autowired
    AdmissionService admissionService;

    @GetMapping("/info")
    public Result info(){
        Map<String, Object> map = ThreadLocalUtil.get();
        int studentId = (Integer) map.get("id");
        String user_identity = (String) map.get("user_identity");
        if(!user_identity.equals("Student")){
            return Result.error("permission denied");
        }

        //查找学生基本信息
        StuBaseInfo stuBaseInfo = stuBaseInfoService.getStuBaseInfoById(studentId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("subject", stuBaseInfo.getDiscipline());
        jsonObject.put("examNumber", stuBaseInfo.getExamNumber());
        jsonObject.put("name", stuBaseInfo.getName());
        jsonObject.put("studentType", stuBaseInfo.getStudentType());
        jsonObject.put("graduationSchool", stuBaseInfo.getGraduationSchool());
        jsonObject.put("graduationTime", stuBaseInfo.getGraduationTime());
        jsonObject.put("graduatedMajor", stuBaseInfo.getGraduatedMajor());
        jsonObject.put("email", stuBaseInfo.getEmail());
        jsonObject.put("contact", stuBaseInfo.getContact());
        jsonObject.put("emergencyContact", stuBaseInfo.getEmergencyContact());
        jsonObject.put("origin", stuBaseInfo.getOrigin());
        jsonObject.put("idNumber", stuBaseInfo.getIdNumber());
        jsonObject.put("graduateType", stuBaseInfo.getGraduateType());

        //查找学生选择老师情况
        String teacher1 = studentService.selectTeacherName(studentId, 1);
        String teacher2 = studentService.selectTeacherName(studentId, 2);
        String teacher3 = studentService.selectTeacherName(studentId, 3);
        jsonObject.put("supervisor1", teacher1);
        jsonObject.put("supervisor2", teacher2);
        jsonObject.put("supervisor3", teacher3);

        //查找学生志愿情况
        List<String> applications = studentService.selectDisciplineName(studentId);
        if(applications.size() >= 2){
            jsonObject.put("acceptAdjustment", "是");
            jsonObject.put("preferredSubjects", applications);
            jsonObject.put("singleSubSubject", null);
            jsonObject.put("researchDirection", null);
        }
        else if(applications.size() == 1){
            if(stuBaseInfo.getDiscipline().equals("电子信息（全日制）")){
                jsonObject.put("acceptAdjustment", "否");
                jsonObject.put("preferredSubjects", null);
                jsonObject.put("singleSubSubject", applications.get(0));
                jsonObject.put("researchDirection", null);
            }
            else {
                jsonObject.put("acceptAdjustment", null);
                jsonObject.put("preferredSubjects", null);
                jsonObject.put("singleSubSubject", null);
                jsonObject.put("researchDirection", applications.get(0));
            }
        }
        else {
            jsonObject.put("acceptAdjustment", null);
            jsonObject.put("preferredSubjects", null);
            jsonObject.put("singleSubSubject", null);
            jsonObject.put("researchDirection", null);
        }

        //查找二级学科
        List<String> disciplines = disciplineInfoService.findNameByDiscipline(stuBaseInfo.getDiscipline());
        jsonObject.put("subSubjectOptions", disciplines);

        //查找url
        String url = studentService.selectResumeById(studentId);
        jsonObject.put("personalStatementUrl", url);

        return Result.success(jsonObject);
    }

    @PostMapping("/submitForm")
    public Result submitForm( StuDetailDTO stuDetailDTO, @RequestParam(value = "personalStatementFile") MultipartFile file) throws Exception {
        Map<String, Object> map = ThreadLocalUtil.get();
        int studentId = (Integer) map.get("id");
        String user_identity = (String) map.get("user_identity");
        if(!user_identity.equals("Student")){
            return Result.error("permission denied");
        }

        //插入学生基本信息
        studentService.updateStudent(stuDetailDTO.getContact(), stuDetailDTO.getEmergencyContact(), stuDetailDTO.getEmail(), stuDetailDTO.getGraduatedMajor(), stuDetailDTO.getGraduationSchool(), stuDetailDTO.getOrigin(), stuDetailDTO.getGraduationTime(), stuDetailDTO.getExamNumber(), stuDetailDTO.getStudentType(), stuDetailDTO.getGraduateType(), studentId);

        //插入学生选择导师情况
        String supervisor1 = stuDetailDTO.getSupervisor1();
        String supervisor2 = stuDetailDTO.getSupervisor2();
        String supervisor3 = stuDetailDTO.getSupervisor3();
        int supervisor1Id = teacherService.getTeacherIdByName(supervisor1);
        int supervisor2Id = teacherService.getTeacherIdByName(supervisor2);
        int supervisor3Id = teacherService.getTeacherIdByName(supervisor3);
        choiceService.insertChoice(supervisor1Id, studentId, 1);
        choiceService.insertChoice(supervisor2Id, studentId, 2);
        choiceService.insertChoice(supervisor3Id, studentId, 3);

        //插入学生选择学科情况
        String acceptAdjustment = stuDetailDTO.getAcceptAdjustment();
        if(acceptAdjustment != null){
            if(acceptAdjustment.equals("是")){
                List<String> preferredSubjects = stuDetailDTO.getPreferredSubjects();
                int preference_order = 1;
                for(String preferredSubject : preferredSubjects) {
                    int discipline_id = disciplineService.selectIdByName(preferredSubject);
                    applicationService.insertApplication(studentId, discipline_id, preference_order);
                    preference_order++;
                }
            }
            else if(acceptAdjustment.equals("否")){
                String singleSubSubject = stuDetailDTO.getSingleSubSubject();
                int discipline_id = disciplineService.selectIdByName(singleSubSubject);
                applicationService.insertApplication(studentId, discipline_id, 1);
            }
        }
        else{
            String researchDirection = stuDetailDTO.getResearchDirection();
            int discipline_id = disciplineService.selectIdByName(researchDirection);
            applicationService.insertApplication(studentId, discipline_id, 1);
        }

        //文件上传OSS
        String originalFilename = file.getOriginalFilename();
        String filename = studentId + "-" + UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
        String url = AliOssUtil.upload(filename, file.getInputStream());
        studentService.updateResume(url, studentId);
        studentService.updateVolunteerStatus(studentId, "审查阶段");
        return Result.success("信息提交成功");
    }

    @GetMapping("/currentStatus")
    public Result currentStatus(){
        Map<String, Object> map = ThreadLocalUtil.get();
        int studentId = (Integer) map.get("id");
        String user_identity = (String) map.get("user_identity");
        if(!user_identity.equals("Student")){
            return Result.error("permission denied");
        }

        JSONObject result = new JSONObject();

        String status = studentService.selectVolunteerStatus(studentId);
        result.put("status", status);

        if(status.equals("已录取")){
            Admission admission = admissionService.selectByStuId(studentId);
            String teacher_name = teacherService.selectNameById(admission.getTeacherId());
            String discipline = disciplineService.selectNameById(admission.getDisciplineId());
            result.put("admissionTeacherName", teacher_name);
            result.put("admissionMajor", discipline);
        }
        else{
            result.put("admissionTeacherName", null);
            result.put("admissionMajor", null);
        }
        return Result.success(result);
    }

}
