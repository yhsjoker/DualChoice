package cn.edu.bjfu.dualchoice.controller;

import cn.edu.bjfu.dualchoice.pojo.Result;
import cn.edu.bjfu.dualchoice.pojo.StuBaseInfo;
import cn.edu.bjfu.dualchoice.pojo.SupervisorInfo;
import cn.edu.bjfu.dualchoice.service.StuBaseInfoService;
import cn.edu.bjfu.dualchoice.service.StudentService;
import cn.edu.bjfu.dualchoice.service.SupervisorInfoService;
import cn.edu.bjfu.dualchoice.utils.ThreadLocalUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/supervisor")
public class SupervisorController {
    @Autowired
    SupervisorInfoService supervisorInfoService;
    @Autowired
    StuBaseInfoService stuBaseInfoService;
    @Autowired
    StudentService studentService;

    @GetMapping("/studentStatus")
    public Result studentStatus(){
        Map<String, Object> map = ThreadLocalUtil.get();
        int supervisorId = (Integer) map.get("id");
        String user_identity = (String) map.get("user_identity");
        if(!user_identity.equals("Supervisor")){
            return Result.error("permission denied");
        }

        SupervisorInfo supervisorInfo = supervisorInfoService.selectById(supervisorId);

        List<StuBaseInfo> studentInfos = stuBaseInfoService.getStuInfoByCollegeId(supervisorInfo.getCollegeId());
        JSONArray admissionList = new JSONArray();
        for(StuBaseInfo stuBaseInfo : studentInfos){
            JSONObject admission = new JSONObject();
            admission.put("name", stuBaseInfo.getName());
            admission.put("examNumber", stuBaseInfo.getExamNumber());
            admission.put("graduateType", stuBaseInfo.getGraduateType());
            admission.put("phone", stuBaseInfo.getContact());
            admission.put("volunteerStatus", studentService.selectVolunteerStatus(stuBaseInfo.getStudentId()));
            admissionList.add(admission);
        }

        JSONObject result = new JSONObject();
        result.put("collegeName", supervisorInfo.getCollegeName());
        result.put("admissionList", admissionList);
        return Result.success(result);
    }
}
