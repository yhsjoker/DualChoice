package cn.edu.bjfu.dualchoice.controller;

import cn.edu.bjfu.dualchoice.pojo.*;
import cn.edu.bjfu.dualchoice.service.*;
import cn.edu.bjfu.dualchoice.utils.ThreadLocalUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    TeacherService teacherService;
    @Autowired
    TeachingService teachingService;
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
    @PostMapping("/submitQuota")
    public Result submitQuota(@RequestBody DisHeadSubmitQuotaDTO disHeadSubmitQuotaDTO){
        int disciplineId = disciplineService.selectIdByName(disHeadSubmitQuotaDTO.getPrimarySubject());

        List<TeacherQuotaInfo> teacherQuotaInfos = disHeadSubmitQuotaDTO.getTeacherQuota();
        for(TeacherQuotaInfo info : teacherQuotaInfos){
            int teacherId = teacherService.getTeacherIdByName(info.getName());
            teachingService.updateInfo(
                    disciplineId, teacherId, info.getAcademicQuota(), info.getProfessionalQuota(), info.getPhdQuota()
            );
        }
        return Result.success();
    }
}
