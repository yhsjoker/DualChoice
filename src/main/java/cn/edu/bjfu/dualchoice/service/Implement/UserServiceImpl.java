package cn.edu.bjfu.dualchoice.service.Implement;

import cn.edu.bjfu.dualchoice.mapper.UserMapper;
import cn.edu.bjfu.dualchoice.pojo.User;
import cn.edu.bjfu.dualchoice.service.UserService;
import cn.edu.bjfu.dualchoice.utils.AddSalt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUserName(String username){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", username);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public void updateAllPasswords() {
        List<User> users = userMapper.selectList(new QueryWrapper<>());

        for (User user : users) {
            // 生成新密码
            String newPassword = AddSalt.hashPasswordWithSalt(user.getPassword());
            user.setPassword(newPassword);

            // 根据唯一标识（如 user_name）更新记录
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("user_name", user.getUserName()).eq("id", user.getId()); // 假设 user_name 是唯一标识
            userMapper.update(user, wrapper);
        }
    }
}
