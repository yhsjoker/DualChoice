package cn.edu.bjfu.dualchoice.controller;

import cn.edu.bjfu.dualchoice.pojo.College;
import cn.edu.bjfu.dualchoice.pojo.Discipline;
import cn.edu.bjfu.dualchoice.pojo.Result;
import cn.edu.bjfu.dualchoice.service.CollegeService;
import cn.edu.bjfu.dualchoice.service.DisciplineService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/nav")
public class NavController {
    @Autowired
    CollegeService collegeService;
    @Autowired
    DisciplineService disciplineService;
    @GetMapping("subjects")
    public Result subjects(){
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
        return Result.success(result);
    }
}
