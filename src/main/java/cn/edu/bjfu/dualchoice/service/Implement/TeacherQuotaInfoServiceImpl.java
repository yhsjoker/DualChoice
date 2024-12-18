package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.TeacherQuotaInfoMapper;
import cn.edu.bjfu.dualchoice.pojo.TeacherQuotaInfo;
import cn.edu.bjfu.dualchoice.pojo.User;
import cn.edu.bjfu.dualchoice.service.TeacherQuotaInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TeacherQuotaInfoServiceImpl implements TeacherQuotaInfoService {
    @Autowired
    TeacherQuotaInfoMapper teacherQuotaInfoMapper;
    @Override
    public List<TeacherQuotaInfo> findTeacherQuotaInfoByDisciplineId(int disciplineId){
        QueryWrapper<TeacherQuotaInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("discipline_id", disciplineId);
        return teacherQuotaInfoMapper.selectList(wrapper);
    }

    @Override
    public List<TeacherQuotaInfo> findByTeacherId(int teacherId) {
        QueryWrapper<TeacherQuotaInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id", teacherId);
        return teacherQuotaInfoMapper.selectList(wrapper);
    }

    @Override
    public List<TeacherQuotaInfo> selectAllIdNTeaNameByParentDIsId(int disciplineId) {
        QueryWrapper<TeacherQuotaInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_discipline", disciplineId)
                .select("teacher_id", "teacher_name");
        List<TeacherQuotaInfo> resultList = teacherQuotaInfoMapper.selectList(wrapper);

        // 使用 Stream 对 teacher_name 去重
        List<TeacherQuotaInfo> distinctList = resultList.stream()
                .filter(Objects::nonNull) // 防止 null 值
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                TeacherQuotaInfo::getTeacherName, // 按 teacher_name 去重
                                Function.identity(),             // 保留第一条完整数据
                                (existing, replacement) -> existing // 如果有重复，保留现有的
                        ),
                        map -> new ArrayList<>(map.values()) // 转换为 List
                ));

        return distinctList;
    }


}