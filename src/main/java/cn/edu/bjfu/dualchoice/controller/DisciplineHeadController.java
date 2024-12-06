package cn.edu.bjfu.dualchoice.controller;

import cn.edu.bjfu.dualchoice.pojo.*;
import cn.edu.bjfu.dualchoice.service.DisciplineHeadService;
import cn.edu.bjfu.dualchoice.service.DisciplineInfoService;
import cn.edu.bjfu.dualchoice.service.DisciplineService;
import cn.edu.bjfu.dualchoice.service.TeacherQuotaInfoService;
import cn.edu.bjfu.dualchoice.utils.ThreadLocalUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/disciplineHead")
public class DisciplineHeadController {
    @Autowired
    DisciplineService disciplineService;
    @Autowired
    DisciplineHeadService disciplineHeadService;
    @Autowired
    DisciplineInfoService disciplineInfoService;
    @Autowired
    TeacherQuotaInfoService teacherQuotaInfoService;
    @GetMapping("/info")
    public Result info(){
        Map<String, Object> map = ThreadLocalUtil.get();
        int disciplineHeadId = (Integer) map.get("id");

        int disciplineId = disciplineHeadService.getDisciplineIdById(disciplineHeadId);
        int quotaIndicator = disciplineService.getQuotaIndicatorById(disciplineId);
        String disciplineName = disciplineService.getNameById(disciplineId);

        List<String> secondarySubjects = disciplineInfoService.findNameByDiscipline(disciplineName);

        List<TeacherQuotaInfo> teacherQuota = teacherQuotaInfoService.findTeacherQuotaInfoByDisciplineId(disciplineId);

        JSONObject result = new JSONObject();
        result.put("primarySubject", disciplineName);
        result.put("quotaIndicator", quotaIndicator);
        result.put("secondarySubjects", secondarySubjects);
        result.put("teacherQuota", teacherQuota);

        return Result.success(result);
    }
}
