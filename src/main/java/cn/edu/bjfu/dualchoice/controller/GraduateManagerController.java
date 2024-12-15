package cn.edu.bjfu.dualchoice.controller;

import cn.edu.bjfu.dualchoice.pojo.Discipline;
import cn.edu.bjfu.dualchoice.pojo.GraduateManagerInfo;
import cn.edu.bjfu.dualchoice.pojo.Result;
import cn.edu.bjfu.dualchoice.pojo.StudentChoiceInfo;
import cn.edu.bjfu.dualchoice.service.DisciplineService;
import cn.edu.bjfu.dualchoice.service.GraduateManagerInfoService;
import cn.edu.bjfu.dualchoice.service.StudentChoiceInfoService;
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
    @GetMapping("admission")
    public Result admission(){
        Map<String, Object> map = ThreadLocalUtil.get();
        int graduateManagerId = (Integer) map.get("id");

        JSONObject result = new JSONObject();

        GraduateManagerInfo graduateManagerInfo = graduateManagerInfoService.selectById(graduateManagerId);
        int college_id = graduateManagerInfo.getCollegeId();
        String college_name = graduateManagerInfo.getCollegeName();
        result.put("collegeName", college_name);

        List<Discipline> disciplines = disciplineService.selectSecDisciplineByCollegeId(college_id);
        JSONArray admissionList = new JSONArray();

        for(Discipline discipline : disciplines){
            List<StudentChoiceInfo> studentChoiceInfos = studentChoiceInfoService.selectAcceptedStuByDisId(discipline.getId());
            for(StudentChoiceInfo studentChoiceInfo : studentChoiceInfos){
                JSONObject admission = new JSONObject();
                admission.put("name", studentChoiceInfo.getStudentName());
                admission.put("phone", studentChoiceInfo.getStudentPhone());
                admission.put("discipline", studentChoiceInfo.getDisciplineName());
                admission.put("graduateType", studentChoiceInfo.getGraduateType());
                admission.put("teacherName", studentChoiceInfo.getTeacherName());
                admission.put("teacherPhone", studentChoiceInfo.getTeacherPhone());
                admissionList.add(admission);
            }
        }

        result.put("admissionList", admissionList);
        return Result.success(result);

    }
}
