package cn.edu.bjfu.dualchoice.controller;

import cn.edu.bjfu.dualchoice.pojo.*;
import cn.edu.bjfu.dualchoice.service.*;
import cn.edu.bjfu.dualchoice.utils.ThreadLocalUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/drawLottery")
public class DrawLotteryController {
    @Autowired
    TeacherPrimaryDisciplineInfoService teacherPrimaryDisciplineInfoService;
    @Autowired
    TeacherQuotaInfoService teacherQuotaInfoService;
    @Autowired
    StudentApplicationInfoService studentApplicationInfoService;
    @Autowired
    ChoiceService choiceService;
    @Autowired
    DisciplineService disciplineService;
    @Autowired
    DisciplineSecretaryService disciplineSecretaryService;
    @GetMapping("teacher")
    public Result teacher(){
        Map<String, Object> map = ThreadLocalUtil.get();
        int securityId = (Integer) map.get("id");
        DisciplineSecretary disciplineSecretaryInfo = disciplineSecretaryService.selectById(securityId);
        int collegeId = disciplineSecretaryInfo.getDisciplineId();

        JSONObject result = new JSONObject();
        JSONArray teachers = new JSONArray();
        List<TeacherPrimaryDisciplineInfo> teacherInfos = teacherPrimaryDisciplineInfoService.selectAllIdNameByCollegeId(collegeId);
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
        Map<String, Object> map = ThreadLocalUtil.get();
        int securityId = (Integer) map.get("id");
        DisciplineSecretary disciplineSecretaryInfo = disciplineSecretaryService.selectById(securityId);
        int collegeId = disciplineSecretaryInfo.getDisciplineId();

        JSONObject result = new JSONObject();
        List<StudentApplicationInfo> studentInfos = studentApplicationInfoService.selectByDisciplineId(collegeId);
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
    @PostMapping("submit")
    public Result submit(@RequestBody StudentChoiceDTO studentChoiceDTO){
        for(StudentChoiceBaseInfoDTO baseInfo : studentChoiceDTO.getSelections()){
            int disciplineId = disciplineService.selectIdByName(baseInfo.getSubject());
            choiceService.lockChoice(baseInfo.getTeacherId(), baseInfo.getStudentId(), disciplineId);
        }
        return Result.success("提交成功");
    }
}
