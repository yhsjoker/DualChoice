package cn.edu.bjfu.dualchoice.controller;

import cn.edu.bjfu.dualchoice.pojo.*;
import cn.edu.bjfu.dualchoice.service.*;
import cn.edu.bjfu.dualchoice.utils.AliOssUtil;
import cn.edu.bjfu.dualchoice.utils.Logger;
import cn.edu.bjfu.dualchoice.utils.ThreadLocalUtil;
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
@RequestMapping("/api/interviewGroup")
public class InterviewGroupController {
    @Autowired
    private InterviewGroupService InterviewGroup;

    @Autowired
    private StuBaseInfoService stuBaseInfoService;

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
        String user_identity = (String) map.get("user_identity");
        Logger.log(user_identity, InterviewGroupId, "", Logger.LogType.INFO, "/api/interviewGroup/list");
        if(!user_identity.equals("InterviewGroup")){
            Logger.log(user_identity, InterviewGroupId, "", Logger.LogType.ERROR, "/api/interviewGroup/list");
            return Result.error("permission denied");
        }

        JSONObject info = new JSONObject();
        String primarySubject = InterviewGroup.selectDisNameById(InterviewGroupId);
        info.put("primarySubject", primarySubject);
        List<StuBaseInfo> studentList = stuBaseInfoService.getStuBaseInfoBydiscipline(primarySubject);
        List<JSONObject> stuInfoList = new ArrayList<>();
        for (StuBaseInfo student : studentList) {
            JSONObject stuInfo = new JSONObject();
            stuInfo.put("id", student.getStudentId());
            stuInfo.put("name", student.getName());
            stuInfo.put("examNumber", student.getExamNumber());
            stuInfo.put("studentType", student.getStudentType());

            stuInfoList.add(stuInfo);
        }

        info.put("studentList", stuInfoList);
        Logger.log(user_identity, InterviewGroupId, info, Logger.LogType.SUCCESS, "/api/interviewGroup/list");
        return Result.success(info);
    }

    @GetMapping("/reExamInfo/{studentId}")
    public Result reExamInfo(@PathVariable("studentId") int studentId){
        Map<String, Object> map = ThreadLocalUtil.get();
        int InterviewGroupId = (Integer) map.get("id");
        String user_identity = (String) map.get("user_identity");
        Logger.log(user_identity, InterviewGroupId, studentId, Logger.LogType.INFO, "/api/interviewGroup/reExamInfo/{studentId}");
        if(!user_identity.equals("InterviewGroup")){
            Logger.log(user_identity, InterviewGroupId, studentId, Logger.LogType.ERROR, "/api/interviewGroup/reExamInfo/{studentId}");
            return Result.error("permission denied");
        }

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
            info.put("signatureUrl", examInfos_know.getSignature());
        }

        ExamInfo examInfos_eng = ExamInfoService.selectExamInfoById(studentId, "专业知识测试");
        if(examInfos_eng == null){
            info.put("professionalScore", null);
        }
        else {
            info.put("professionalScore", examInfos_eng.getScore());
            info.put("signatureUrl", examInfos_eng.getSignature());
        }

        ExamInfo examInfos_interview = ExamInfoService.selectExamInfoById(studentId, "综合素质面试");
        if(examInfos_interview == null){
            info.put("interviewScore", null);
        }
        else {
            info.put("interviewScore", examInfos_interview.getScore());
            info.put("signatureUrl", examInfos_interview.getSignature());
        }

        Logger.log(user_identity, InterviewGroupId, info, Logger.LogType.SUCCESS, "/api/interviewGroup/reExamInfo/{studentId}");
        return Result.success(info);
    }

    @PostMapping("/updateReExamInfo")
    public Result insertReExamInfo(StuExamInfoDTO stuExamInfoDTO, @RequestParam(value = "signatureFile") MultipartFile file) throws Exception {
        Map<String, Object> map = ThreadLocalUtil.get();
        int InterviewGroupId = (Integer) map.get("id");
        String user_identity = (String) map.get("user_identity");
        Logger.log(user_identity, InterviewGroupId, stuExamInfoDTO, Logger.LogType.INFO, "/api/interviewGroup/updateReExamInfo");
        if(!user_identity.equals("InterviewGroup")){
            Logger.log(user_identity, InterviewGroupId, stuExamInfoDTO, Logger.LogType.ERROR, "/api/interviewGroup/updateReExamInfo");
            return Result.error("permission denied");
        }

        String originalFilename = file.getOriginalFilename();
        String filename = InterviewGroupId + "-" + UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
        String url = AliOssUtil.upload(filename, file.getInputStream());

        int eng_id = SubjectService.selectSubjectIdByName("外语听力及口语");
        int pro_id = SubjectService.selectSubjectIdByName("专业知识测试");
        int inter_id = SubjectService.selectSubjectIdByName("综合素质面试");
        ExamService.insertExamInfo(stuExamInfoDTO.getId(), eng_id, "复试", stuExamInfoDTO.getEnglishScore(), stuExamInfoDTO.getReExamTime(), stuExamInfoDTO.getReExamLocation(), stuExamInfoDTO.getOverallEvaluation(), url);
        ExamService.insertExamInfo(stuExamInfoDTO.getId(), pro_id, "复试", stuExamInfoDTO.getProfessionalScore(), stuExamInfoDTO.getReExamTime(), stuExamInfoDTO.getReExamLocation(), stuExamInfoDTO.getOverallEvaluation(), url);
        ExamService.insertExamInfo(stuExamInfoDTO.getId(), inter_id, "复试", stuExamInfoDTO.getInterviewScore(), stuExamInfoDTO.getReExamTime(), stuExamInfoDTO.getReExamLocation(), stuExamInfoDTO.getOverallEvaluation(), url);

        Logger.log(user_identity, InterviewGroupId, "复试信息增加成功", Logger.LogType.SUCCESS, "/api/interviewGroup/updateReExamInfo");
        return Result.success("复试信息增加成功");
    }

}
