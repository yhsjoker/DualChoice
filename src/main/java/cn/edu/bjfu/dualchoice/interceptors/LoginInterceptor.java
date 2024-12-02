package cn.edu.bjfu.dualchoice.interceptors;

import cn.edu.bjfu.dualchoice.utils.JwtUtil;
import cn.edu.bjfu.dualchoice.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        //System.out.println(request.getMethod());
        //过滤OPTIONS试探请求
        if(request.getMethod().equals("OPTIONS")){
            return true;
        }
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        //System.out.println(token);
        try{
            Map<String, Object> claims = JwtUtil.parseToken(token);
            ThreadLocalUtil.set(claims);
            //System.out.println((Integer) claims.get("id"));
            return true;
        } catch (Exception e){
            response.setStatus(401);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove();
    }
}
