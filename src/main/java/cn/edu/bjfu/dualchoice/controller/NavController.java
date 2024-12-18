package cn.edu.bjfu.dualchoice.controller;

import cn.edu.bjfu.dualchoice.pojo.College;
import cn.edu.bjfu.dualchoice.pojo.Discipline;
import cn.edu.bjfu.dualchoice.pojo.Result;
import cn.edu.bjfu.dualchoice.pojo.Teacher;
import cn.edu.bjfu.dualchoice.service.CollegeService;
import cn.edu.bjfu.dualchoice.service.DisciplineService;
import cn.edu.bjfu.dualchoice.service.TeacherService;
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
@RequestMapping("/api/nav")
public class NavController {
    @Autowired
    CollegeService collegeService;
    @Autowired
    DisciplineService disciplineService;
    @Autowired
    TeacherService teacherService;
    @GetMapping("subjects")
    public Result subjects(){
        Map<String, Object> map = ThreadLocalUtil.get();
        int user_id = (Integer) map.get("id");
        String user_identity = (String) map.get("user_identity");
        Logger.log(user_identity, user_id, "", Logger.LogType.INFO, "/api/nav/subjects");
        if(!user_identity.equals("InterviewGroup")){
            Logger.log(user_identity, user_id, "", Logger.LogType.ERROR, "/api/nav/subjects");
            return Result.error("permission denied");
        }

        JSONObject result = new JSONObject();
        JSONArray allData = new JSONArray();

        List<College> collegeInfos = collegeService.selectAll();
        for(College collegeInfo : collegeInfos){
            JSONObject college = new JSONObject();
            college.put("collegeName", collegeInfo.getName());

            List<Discipline> primaryDisciplineInfos = disciplineService.selectPrimaryDisciplineByCollegeId(collegeInfo.getId());
            JSONArray subjects = new JSONArray();
            for(Discipline primaryDisciplineInfo : primaryDisciplineInfos){
                JSONObject priDiscipline = new JSONObject();
                priDiscipline.put("name", primaryDisciplineInfo.getName());
                priDiscipline.put("intro", primaryDisciplineInfo.getOverview());

                JSONArray children = new JSONArray();
                List<Discipline> secDisciplineInfos = disciplineService.findSecondDisByFistId(primaryDisciplineInfo.getId());
                for(Discipline secDisciplineInfo : secDisciplineInfos){
                    JSONObject secDiscipline = new JSONObject();
                    secDiscipline.put("name", secDisciplineInfo.getName());
                    secDiscipline.put("intro", secDisciplineInfo.getOverview());
                    children.add(secDiscipline);
                }

                priDiscipline.put("children", children);
                subjects.add(priDiscipline);
            }
            college.put("subjects", subjects);

            allData.add(college);
        }

        result.put("allData", allData);
        Logger.log(user_identity, user_id, result, Logger.LogType.SUCCESS, "/api/nav/subjects");
        return Result.success(result);
    }

    @GetMapping("teachers")
    public Result teachers(){
        Map<String, Object> map = ThreadLocalUtil.get();
        int user_id = (Integer) map.get("id");
        String user_identity = (String) map.get("user_identity");
        Logger.log(user_identity, user_id, "", Logger.LogType.INFO, "/api/nav/teachers");
        if(!user_identity.equals("InterviewGroup")){
            Logger.log(user_identity, user_id, "", Logger.LogType.ERROR, "/api/nav/teachers");
            return Result.error("permission denied");
        }

        JSONObject result = new JSONObject();
        JSONArray allData = new JSONArray();

        List<College> collegeInfos = collegeService.selectAll();
        for(College collegeInfo : collegeInfos){
            JSONObject college = new JSONObject();
            college.put("collegeName", collegeInfo.getName());

            List<Teacher> teacherInfos = teacherService.selectTeacherByCollegeId(collegeInfo.getId());
            JSONArray teachers = new JSONArray();
            for(Teacher teacherInfo : teacherInfos){
                JSONObject teacher = new JSONObject();
                teacher.put("name", teacherInfo.getName());
                teacher.put("position", teacherInfo.getTitle());
                teacher.put("phone", teacherInfo.getPhone());
                teacher.put("email", teacherInfo.getEmail());
                teacher.put("hasAdmissionQualification", teacherInfo.getQualification() != null && teacherInfo.getQualification().equals("是"));
                teacher.put("intro", teacherInfo.getProfile());
                teacher.put("imageUrl", teacherInfo.getImage());

                teachers.add(teacher);
            }
            college.put("teachers", teachers);
            allData.add(college);
        }

        result.put("allData", allData);
        Logger.log(user_identity, user_id, result, Logger.LogType.SUCCESS, "/api/nav/teachers");
        return Result.success(result);
    }
}
