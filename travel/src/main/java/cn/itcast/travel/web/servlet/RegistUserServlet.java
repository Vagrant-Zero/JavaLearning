package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/registUserServlet")
public class RegistUserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 先校验证码，验证码通过后再查询数据库
        HttpSession session = request.getSession();
        String checkCode = (String)session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");//为了保证验证码只能使用一次
        String check = request.getParameter("check");
        if(checkCode == null || !checkCode.equalsIgnoreCase(check)) {
            // 验证码错误
            ResultInfo resultInfo = new ResultInfo();
            ObjectMapper mapper = new ObjectMapper();
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("验证码错误！");
            response.setContentType("application/json;charset=utf-8");
            mapper.writeValue(response.getOutputStream(), resultInfo);
//            String json = mapper.writeValueAsString(resultInfo);
//            response.getWriter().write(json);
            // 将响应数据以json格式响应给浏览器
//            response.setContentType("application/json;charset=utf-8");
//            response.getWriter().write(json);
//            System.out.println(json);
            return;
        }
        // 验证码通过，校验后面的数据
        Map<String, String[]> map = request.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        UserService userService = new UserServiceImpl();
        boolean isExit = userService.regist(user);
        ResultInfo resultInfo = new ResultInfo();
        ObjectMapper mapper = new ObjectMapper();
        if(!isExit) {
            // 用户已经存在
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("用户名已存在，请尝试换一个用户名！");
        }else {
            // 用户不存在
            resultInfo.setFlag(true);
        }
        String json = mapper.writeValueAsString(resultInfo);
        // 将响应数据以json格式响应给浏览器
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

}
