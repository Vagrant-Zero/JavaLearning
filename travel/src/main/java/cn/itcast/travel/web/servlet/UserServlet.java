package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet{
    private UserService userService = new UserServiceImpl();

    /**
     * 注册方法
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

    /**
     * 登陆方法
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

    /**
     * 在session中查找一个用户
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object user = req.getSession().getAttribute("user");
        //将user写回客户端
        ObjectMapper mapper = new ObjectMapper();
        resp.setContentType("application/json;charset=utf-8");
        mapper.writeValue(resp.getOutputStream(),user);
    }

    /**
     * 用户退出
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 销毁session
        req.getSession().invalidate();
        // 跳转页面
        resp.sendRedirect(req.getContextPath() + "/login.html");
    }

    /**
     * 用户激活
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void active(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("code");
        if(code != null) {
            boolean flag = userService.active(code);// 激活成功返回true，否则返回失败
            String  msg = null;
            if(flag) {
                // 激活成功
                msg = "激活成功！请<a href='login.html'>登录</a>";
            }else {
                // 激活失败
                msg = "激活失败，请联系管理员";
            }
            resp.setContentType("text/html;charset=utf-8");
            resp.getWriter().write(msg);
        }
    }
}
