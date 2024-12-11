package cn.edu.bjfu.dualchoice.controller;

import cn.edu.bjfu.dualchoice.pojo.Result;
import cn.edu.bjfu.dualchoice.pojo.TeacherBaseInfo;
import cn.edu.bjfu.dualchoice.pojo.TeacherQuotaInfo;
import cn.edu.bjfu.dualchoice.service.Implement.TeacherBaseInfoServiceImpl;
import cn.edu.bjfu.dualchoice.service.TeacherBaseInfoService;
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
    TeacherBaseInfoService teacherBaseInfoService;
    @Autowired
    TeacherQuotaInfoService teacherQuotaInfoService;
    @GetMapping("teacher")
    public Result teacher(){
        JSONObject result = new JSONObject();
        JSONArray teachers = new JSONArray();
        List<TeacherBaseInfo> teacherBaseInfos = teacherBaseInfoService.selectAllIdName();
        for(TeacherBaseInfo teacherInfo : teacherBaseInfos){
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
        return Result.success();
    }
}
