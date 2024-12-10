package cn.edu.bjfu.dualchoice.service;

import cn.edu.bjfu.dualchoice.pojo.Choice;

public interface ChoiceService {
    public Choice findChoiceByStuIdPri(int id, int pri);
    void insertChoice(int teacher_id, int student_id, int priority);
    void lockChoice(int teacher_id, int student_id);
}
