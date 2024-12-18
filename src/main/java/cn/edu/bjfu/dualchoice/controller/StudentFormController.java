package cn.edu.bjfu.dualchoice.controller;

import cn.edu.bjfu.dualchoice.pojo.ExamInfo;
import cn.edu.bjfu.dualchoice.pojo.Result;
import cn.edu.bjfu.dualchoice.pojo.StuBaseInfo;
import cn.edu.bjfu.dualchoice.service.*;
import cn.edu.bjfu.dualchoice.utils.Logger;
import cn.edu.bjfu.dualchoice.utils.ThreadLocalUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/studentForm")
public class StudentFormController {
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
    ExamService examService;

    @Autowired
    ExamInfoService ExamInfoService;

    @GetMapping("/{studentId}")
    public Result getStudentForm(@PathVariable("studentId") int studentId) {
        Map<String, Object> map = ThreadLocalUtil.get();
        int user_id = (Integer) map.get("id");
        String user_identity = (String) map.get("user_identity");
        Logger.log(user_identity, user_id, studentId, Logger.LogType.INFO, "/api/studentForm/{studentId}");
        if(!user_identity.equals("InterviewGroup")){
            Logger.log(user_identity, user_id, studentId, Logger.LogType.ERROR, "/api/studentForm/{studentId}");
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

        String url = studentService.selectResumeById(studentId);
        jsonObject.put("personalStatementUrl", url);

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

        //查找学生初试总分
        float score = examService.getScore(studentId);
        jsonObject.put("examScore", score);

        //查找学生复试成绩
        ExamInfo examInfos_know = ExamInfoService.selectExamInfoById(studentId, "外语听力及口语");
        if(examInfos_know == null) {
            jsonObject.put("englishScore", null);
            jsonObject.put("reExamTime", null);
            jsonObject.put("reExamLocation", null);
            jsonObject.put("overallEvaluation", null);
        }
        else{
            jsonObject.put("englishScore", examInfos_know.getScore());
            jsonObject.put("reExamTime", examInfos_know.getTime());
            jsonObject.put("reExamLocation", examInfos_know.getLocation());
            jsonObject.put("overallEvaluation", examInfos_know.getComment());
            jsonObject.put("photoUrl", examInfos_know.getSignature());
        }

        ExamInfo examInfos_eng = ExamInfoService.selectExamInfoById(studentId, "专业知识测试");
        if(examInfos_eng == null){
            jsonObject.put("professionalScore", null);
        }
        else {
            jsonObject.put("professionalScore", examInfos_eng.getScore());
            jsonObject.put("photoUrl", examInfos_eng.getSignature());
        }

        ExamInfo examInfos_interview = ExamInfoService.selectExamInfoById(studentId, "综合素质面试");
        if(examInfos_interview == null){
            jsonObject.put("interviewScore", null);
        }
        else {
            jsonObject.put("interviewScore", examInfos_interview.getScore());
            jsonObject.put("photoUrl", examInfos_interview.getSignature());
        }

        Logger.log(user_identity, user_id, jsonObject, Logger.LogType.SUCCESS, "/api/studentForm/{studentId}");
        return Result.success(jsonObject);

    }

}
