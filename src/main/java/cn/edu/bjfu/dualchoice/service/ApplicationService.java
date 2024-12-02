package cn.edu.bjfu.dualchoice.service;

import cn.edu.bjfu.dualchoice.pojo.Application;

import java.util.List;

public interface ApplicationService {
    public List<Application> findApplicationByStuIdPreOrd(int stuId, int preOrd);

    void insertApplication(int student_id, int discipline_id, int preference_order);
}
