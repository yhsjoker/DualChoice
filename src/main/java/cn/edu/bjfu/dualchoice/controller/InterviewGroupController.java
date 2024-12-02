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
@RequestMapping("/api/interviewGroup")
public class InterviewGroupController {
    @Autowired
    private InterviewGroupService InterviewGroup;

    @Autowired
    private StudentService StudentService;

    @Autowired
    private SubjectService SubjectService;

    @Autowired
    private ExamInfoService ExamInfoService;

    @Autowired
    private ExamService ExamService;

    @GetMapping("/list")
    public Result list(){
        Map<String, Object> map = ThreadLocalUtil.get();
        int InterviewGroupId = (Integer) map.get("id");
        System.out.println(InterviewGroupId);

        JSONObject info = new JSONObject();
        String primarySubject = InterviewGroup.selectDisNameById(InterviewGroupId);
        info.put("primarySubject", primarySubject);
        List<Student> studentList = StudentService.selectAllStudents();
        List<JSONObject> stuInfoList = new ArrayList<>();
        for (Student student : studentList) {
            JSONObject stuInfo = new JSONObject();
            stuInfo.put("id", student.getId());
            stuInfo.put("name", student.getName());
            stuInfo.put("examNumber", student.getExamTicketNumber());
            stuInfo.put("studentType", student.getCategory());

            stuInfoList.add(stuInfo);
        }

        info.put("studentList", stuInfoList);

        return Result.success(info);
    }

    @GetMapping("/reExamInfo/{studentId}")
    public Result reExamInfo(@PathVariable("studentId") int studentId){
        System.out.println(studentId);
        JSONObject info = new JSONObject();

        ExamInfo examInfos_know = ExamInfoService.selectExamInfoById(studentId, "外语听力及口语");
        if(examInfos_know == null) {
            info.put("englishScore", null);
            info.put("reExamTime", null);
            info.put("reExamLocation", null);
            info.put("overallEvaluation", null);
        }
        else{
            info.put("englishScore", examInfos_know.getScore());
            info.put("reExamTime", examInfos_know.getTime());
            info.put("reExamLocation", examInfos_know.getLocation());
            info.put("overallEvaluation", examInfos_know.getComment());
        }

        ExamInfo examInfos_eng = ExamInfoService.selectExamInfoById(studentId, "专业知识测试");
        if(examInfos_eng == null){
            info.put("professionalScore", null);
        }
        else info.put("professionalScore", examInfos_eng.getScore());

        ExamInfo examInfos_interview = ExamInfoService.selectExamInfoById(studentId, "综合素质面试");
        if(examInfos_interview == null){
            info.put("interviewScore", null);
        }
        else info.put("interviewScore", examInfos_interview.getScore());

        return Result.success(info);
    }

    @PostMapping("/updateReExamInfo")
    public Result insertReExamInfo(@RequestBody StuExamInfoDTO stuExamInfoDTO){
        System.out.println(stuExamInfoDTO);
        int eng_id = SubjectService.selectSubjectIdByName("外语听力及口语");
        int pro_id = SubjectService.selectSubjectIdByName("专业知识测试");
        int inter_id = SubjectService.selectSubjectIdByName("综合素质面试");
        ExamService.insertExamInfo(stuExamInfoDTO.getId(), eng_id, "复试", stuExamInfoDTO.getEnglishScore(), stuExamInfoDTO.getReExamTime(), stuExamInfoDTO.getReExamLocation(), stuExamInfoDTO.getOverallEvaluation());
        ExamService.insertExamInfo(stuExamInfoDTO.getId(), pro_id, "复试", stuExamInfoDTO.getProfessionalScore(), stuExamInfoDTO.getReExamTime(), stuExamInfoDTO.getReExamLocation(), stuExamInfoDTO.getOverallEvaluation());
        ExamService.insertExamInfo(stuExamInfoDTO.getId(), inter_id, "复试", stuExamInfoDTO.getInterviewScore(), stuExamInfoDTO.getReExamTime(), stuExamInfoDTO.getReExamLocation(), stuExamInfoDTO.getOverallEvaluation());
        return Result.success("复试信息增加成功");
    }

}
