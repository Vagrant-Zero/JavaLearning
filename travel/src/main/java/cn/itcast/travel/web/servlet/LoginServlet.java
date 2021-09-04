package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 验证码校验
        String check = req.getParameter("check");
        HttpSession session = req.getSession();
        String checkCode = (String)session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");
        if(checkCode == null || !checkCode.equalsIgnoreCase(check)) {
            // 验证码错误
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("验证码错误！");
            ObjectMapper mapper = new ObjectMapper();
            resp.setContentType("application/json;charset=utf-8");
            mapper.writeValue(resp.getOutputStream(), resultInfo);
            return;
        }
        Map<String, String[]> map = req.getParameterMap();
        User loginUser = new User();
        try {
            BeanUtils.populate(loginUser, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        UserService userService = new UserServiceImpl();
        User user = userService.login(loginUser);
        ResultInfo resultInfo = new ResultInfo();
        ObjectMapper mapper = new ObjectMapper();
        if(user == null) {
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("用户名或密码错误！");
        }
        if(user != null && !"Y".equalsIgnoreCase(user.getStatus())) {
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("您尚未激活，请激活！");
        }
        if(user != null && "Y".equalsIgnoreCase(user.getStatus())) {
            session.setAttribute("user", user);
            resultInfo.setFlag(true);
        }
        resp.setContentType("application/json;charset=utf-8");
        mapper.writeValue(resp.getOutputStream(), resultInfo);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
