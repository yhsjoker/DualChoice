package cn.edu.bjfu.dualchoice.controller;

import cn.edu.bjfu.dualchoice.pojo.DisciplineSecretaryInfo;
import cn.edu.bjfu.dualchoice.pojo.Result;
import cn.edu.bjfu.dualchoice.service.DisciplineSecretaryInfoService;
import cn.edu.bjfu.dualchoice.utils.Logger;
import cn.edu.bjfu.dualchoice.utils.ThreadLocalUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/disciplineSecretary")
public class DisciplineSecretaryController {
    @Autowired
    DisciplineSecretaryInfoService disciplineSecretaryInfoService;
    @GetMapping("/info")
    public Result info(){
        Map<String, Object> map = ThreadLocalUtil.get();
        int secretaryId = (Integer) map.get("id");
        String user_identity = (String) map.get("user_identity");
        Logger.log(user_identity, secretaryId, "", Logger.LogType.INFO, "/api/disciplineSecretary/info");
        if(!user_identity.equals("DisciplineSecretary")){
            Logger.log(user_identity, secretaryId, "", Logger.LogType.ERROR, "/api/disciplineSecretary/info");
            return Result.error("permission denied");
        }

        DisciplineSecretaryInfo disciplineSecretaryInfo = disciplineSecretaryInfoService.selectById(secretaryId);
        JSONObject result = new JSONObject();
        result.put("primarySubject", disciplineSecretaryInfo.getDisciplineName());
        Logger.log(user_identity, secretaryId, result, Logger.LogType.SUCCESS, "/api/disciplineSecretary/info");
        return Result.success(result);
    }
}
