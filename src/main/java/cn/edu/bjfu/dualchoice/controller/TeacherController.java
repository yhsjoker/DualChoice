package cn.edu.bjfu.dualchoice.controller;

import cn.edu.bjfu.dualchoice.pojo.*;
import cn.edu.bjfu.dualchoice.service.*;
import cn.edu.bjfu.dualchoice.service.Implement.StudentApplicationInfoServiceImpl;
import cn.edu.bjfu.dualchoice.utils.Logger;
import cn.edu.bjfu.dualchoice.utils.ThreadLocalUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
    @Autowired
    TeacherBaseInfoService teacherBaseInfoService;
    @Autowired
    TeacherQuotaInfoService teacherQuotaInfoService;
    @Autowired
    StudentChoiceInfoService studentChoiceInfoService;
    @Autowired
    StudentService studentService;
    @Autowired
    TeachingService teachingService;
    @Autowired
    ChoiceService choiceService;
    @Autowired
    DisciplineService disciplineService;
    @Autowired
    TeacherService teacherService;
    @Autowired
    StudentApplicationInfoService studentApplicationInfoService;
    @Autowired
    private StudentApplicationInfoServiceImpl studentApplicationInfoServiceImpl;
    @Autowired
    AdmissionService admissionService;

    @GetMapping("/info")
    public Result info(){
        Map<String, Object> map = ThreadLocalUtil.get();
        int teacherId = (Integer) map.get("id");
        String user_identity = (String) map.get("user_identity");
        Logger.log(user_identity, teacherId, "", Logger.LogType.INFO, "/api/teacher/info");
        if(!user_identity.equals("Teacher")){
            Logger.log(user_identity, teacherId, "", Logger.LogType.ERROR, "/api/teacher/info");
            return Result.error("permission denied");
        }

        JSONObject result = new JSONObject();

        TeacherBaseInfo teacherBaseInfo = teacherBaseInfoService.selectAllById(teacherId);
        int current_volunteer_round = teacherBaseInfoService.selectMinimumVolunteerRoundByCollegeId(teacherBaseInfo.getCollegeId());
        result.put("name", teacherBaseInfo.getTeacherName());
        result.put("college", teacherBaseInfo.getCollegeName());
        result.put("email", teacherBaseInfo.getEmail());
        result.put("phone", teacherBaseInfo.getPhone());
        result.put("title", teacherBaseInfo.getTitle());
        result.put("round", current_volunteer_round);
        result.put("imageUrl", teacherBaseInfo.getImage());

        JSONArray secondarySubjects = new JSONArray();
        List<TeacherQuotaInfo> teacherQuotaInfos = teacherQuotaInfoService.findByTeacherId(teacherId);

        for(TeacherQuotaInfo teacherQuotaInfo : teacherQuotaInfos){
            JSONObject teacherInfo = new JSONObject();
            teacherInfo.put("name", teacherQuotaInfo.getDisciplineName());
            teacherInfo.put("academicQuota", teacherQuotaInfo.getAcademicQuota());
            teacherInfo.put("professionalQuota", teacherQuotaInfo.getProfessionalQuota());
            teacherInfo.put("phdQuota", teacherQuotaInfo.getPhdQuota());
            JSONArray students = new JSONArray();

            if(teacherBaseInfo.getVolunteerRound()==4){
                List<Admission> admissions = admissionService.selectByDisciplineId(teacherQuotaInfo.getDisciplineId());
                for(Admission admission : admissions){
                    if(admission.getTeacherId()!=teacherId)
                        continue;
                    JSONObject student = new JSONObject();
                    Student stu = studentService.selectById(admission.getStudentId());
                    student.put("studentId", stu.getId());
                    student.put("studentName", stu.getName());
                    student.put("graduateType", stu.getGraduateType());
                    student.put("studentPhone", stu.getPhone());
                    student.put("isSelected", true);
                    students.add(student);
                }
            }
            else if(current_volunteer_round == teacherBaseInfo.getVolunteerRound()){
                List<StudentChoiceInfo> studentChoiceInfos = studentChoiceInfoService.selectStudentsByTeacherIdDisIdVolRound(teacherId, teacherQuotaInfo.getDisciplineId(), current_volunteer_round);
                for(StudentChoiceInfo studentChoiceInfo : studentChoiceInfos){
                    JSONObject student = new JSONObject();
                    student.put("studentId", studentChoiceInfo.getStudentId());
                    student.put("studentName", studentChoiceInfo.getStudentName());
                    student.put("graduateType", studentChoiceInfo.getGraduateType());
                    student.put("studentPhone", studentChoiceInfo.getStudentPhone());
                    student.put("isSelected", false);
                    students.add(student);
                }
            }
            teacherInfo.put("students", students);
            secondarySubjects.add(teacherInfo);
        }
        result.put("secondarySubjects", secondarySubjects);

        Logger.log(user_identity, teacherId, result, Logger.LogType.SUCCESS, "/api/teacher/info");
        return Result.success(result);
    }

    @PutMapping("/submitSelections")
    public Result submitSelections(@RequestBody SelectionsDTO selectionsDTO){
        Map<String, Object> map = ThreadLocalUtil.get();
        int teacherId = (Integer) map.get("id");
        String user_identity = (String) map.get("user_identity");
        Logger.log(user_identity, teacherId, selectionsDTO, Logger.LogType.INFO, "/api/teacher/submitSelections");
        if(!user_identity.equals("Teacher")){
            return Result.error("permission denied");
        }

        for(int studentId : selectionsDTO.getSelectedStudents()){
            int disciplineId = studentApplicationInfoService.selectSecondDisciplineId(studentId);
            choiceService.lockChoice(teacherId, studentId, disciplineId);
        }

        for(TeachingQuotaUpdateDTO teachingQuotaUpdateInfo : selectionsDTO.getSecondarySubjects()){
            int discipline_id = disciplineService.selectIdByName(teachingQuotaUpdateInfo.getDisciplineName());
            teachingService.updateInfo(discipline_id, teacherId, teachingQuotaUpdateInfo.getAcademicQuota(), teachingQuotaUpdateInfo.getProfessionalQuota(), teachingQuotaUpdateInfo.getPhdQuota());
        }

        teacherService.increaseVolRound(teacherId);

        Logger.log(user_identity, teacherId, "", Logger.LogType.SUCCESS, "/api/teacher/submitSelections");
        return Result.success();
    }

}
