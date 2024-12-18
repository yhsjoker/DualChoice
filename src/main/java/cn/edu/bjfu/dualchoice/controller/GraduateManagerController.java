package cn.edu.bjfu.dualchoice.controller;

import cn.edu.bjfu.dualchoice.mapper.AdmissionMapper;
import cn.edu.bjfu.dualchoice.pojo.*;
import cn.edu.bjfu.dualchoice.service.*;
import cn.edu.bjfu.dualchoice.service.Implement.TeacherBaseInfoServiceImpl;
import cn.edu.bjfu.dualchoice.utils.Logger;
import cn.edu.bjfu.dualchoice.utils.ThreadLocalUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/graduateManager")
public class GraduateManagerController {
    @Autowired
    GraduateManagerInfoService graduateManagerInfoService;
    @Autowired
    DisciplineService disciplineService;
    @Autowired
    StudentChoiceInfoService studentChoiceInfoService;
    @Autowired
    AdmissionService admissionService;
    @Autowired
    StuBaseInfoService stuBaseInfoService;
    @Autowired
    TeacherBaseInfoService teacherBaseInfoService;

    @GetMapping("admission")
    public Result admission(){
        Map<String, Object> map = ThreadLocalUtil.get();
        int graduateManagerId = (Integer) map.get("id");
        String user_identity = (String) map.get("user_identity");
        Logger.log(user_identity, graduateManagerId, "", Logger.LogType.INFO, "/api/graduateManager/admission");
        if(!user_identity.equals("GraduateManager")){
            Logger.log(user_identity, graduateManagerId, "", Logger.LogType.ERROR, "/api/graduateManager/admission");
            return Result.error("permission denied");
        }

        JSONObject result = new JSONObject();

        GraduateManagerInfo graduateManagerInfo = graduateManagerInfoService.selectById(graduateManagerId);
        int college_id = graduateManagerInfo.getCollegeId();
        String college_name = graduateManagerInfo.getCollegeName();
        result.put("collegeName", college_name);

        List<Admission> admissions = admissionService.selectAll();
        JSONArray admissionList = new JSONArray();

        for(Admission admission : admissions){
            StuBaseInfo student = stuBaseInfoService.getStuBaseInfoById(admission.getStudentId());
            if(student != null && student.getCollegeId() == college_id){
                TeacherBaseInfo teacher = teacherBaseInfoService.selectAllById(admission.getTeacherId());
                JSONObject object = new JSONObject();
                object.put("name", student.getName());
                object.put("phone",student.getContact());
                object.put("discipline",student.getDiscipline());
                object.put("graduateType",student.getGraduateType());
                object.put("teacherName",teacher.getTeacherName());
                object.put("teacherPhone",teacher.getPhone());
                admissionList.add(object);
            }
        }

        result.put("admissionList", admissionList);
        Logger.log(user_identity, graduateManagerId, result, Logger.LogType.SUCCESS, "/api/graduateManager/admission");
        return Result.success(result);

    }
}
