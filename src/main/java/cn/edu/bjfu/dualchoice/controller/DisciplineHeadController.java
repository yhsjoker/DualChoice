package cn.edu.bjfu.dualchoice.controller;

import cn.edu.bjfu.dualchoice.pojo.*;
import cn.edu.bjfu.dualchoice.service.*;
import cn.edu.bjfu.dualchoice.utils.ThreadLocalUtil;
import com.alibaba.fastjson.JSONArray;
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
    TeacherService teacherService;
    @Autowired
    TeachingService teachingService;
    @Autowired
    TeacherQuotaInfoService teacherQuotaInfoService;
    @GetMapping("/info")
    public Result info(){
        Map<String, Object> map = ThreadLocalUtil.get();
        int disciplineHeadId = (Integer) map.get("id");

        int disciplineId = disciplineHeadService.getDisciplineIdById(disciplineHeadId);
        int quotaIndicator = disciplineService.getQuotaIndicatorById(disciplineId);
        String disciplineName = disciplineService.getNameById(disciplineId);

        List<Integer> secondarySubjects = disciplineInfoService.findSecIdByPriId(disciplineId);
        JSONArray quota = new JSONArray();
        for(Integer secId : secondarySubjects){
            JSONObject info = new JSONObject();
            info.put("secondarySubjects", disciplineService.getNameById(secId));
            info.put("teacherQuota", teacherQuotaInfoService.findTeacherQuotaInfoByDisciplineId(secId));
            quota.add(info);
        }

        JSONObject result = new JSONObject();
        result.put("primarySubject", disciplineName);
        result.put("quotaIndicator", quotaIndicator);
        result.put("quota", quota);

        return Result.success(result);
    }
    @PutMapping("/submitQuota")
    public Result submitQuota(@RequestBody DisHeadSubmitQuotaDTO disHeadSubmitQuotaDTO){
        System.out.println(disHeadSubmitQuotaDTO);
        List<SecQuotaDTO> secInfos = disHeadSubmitQuotaDTO.getQuota();
        for(SecQuotaDTO secInfo : secInfos){
            int secondarySubjectsId = disciplineService.selectIdByName(secInfo.getSecondarySubjects());
            List<TeacherQuotaInfoDTO> teacherQuotaInfos = secInfo.getTeacherQuota();
            if(teacherQuotaInfos == null) continue;
            for(TeacherQuotaInfoDTO teacherQuotaInfo : teacherQuotaInfos){
                int teacherId = teacherService.getTeacherIdByName(teacherQuotaInfo.getTeacherName());
                System.out.println(teacherId);
                teachingService.updateInfo(
                        secondarySubjectsId, teacherId, teacherQuotaInfo.getAcademicQuota(), teacherQuotaInfo.getProfessionalQuota(), teacherQuotaInfo.getPhdQuota()
                );
            }
        }
        return Result.success();
    }
}
