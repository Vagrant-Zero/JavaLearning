package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet{
    private RouteService routeService = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();

    /**
     * 分页查询
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 获取浏览器传过来的参数
        String currentPageStr = req.getParameter("currentPage"); //当前页码
        String pageSizeStr = req.getParameter("pageSize"); // 每页显示的条数
        String cidStr = req.getParameter("cid"); // 查询路线的cid
        String rname = req.getParameter("rname"); // 查找的景点名称
        if(rname != null && rname.length() > 0 && !"null".equalsIgnoreCase(rname)) {
            rname = new String(rname.getBytes("iso-8859-1"), "utf-8");
        }

        // 2. 处理参数
        int cid = 0;
        if(cidStr != null && cidStr.length() > 0 && !"null".equalsIgnoreCase(cidStr)) {
            cid = Integer.parseInt(cidStr);
        }
        int currentPage = 1;
        if(currentPageStr != null && currentPageStr.length() > 0 && !"null".equalsIgnoreCase(currentPageStr)) {
            currentPage = Integer.parseInt(currentPageStr);
        }
        int pageSize = 5;
        if(pageSizeStr != null && pageSizeStr.length() > 0 && !"null".equalsIgnoreCase(pageSizeStr)) {
            pageSize = Integer.parseInt(pageSizeStr);
        }

        // 3. 调用Service查询路线
        PageBean<Route> pb = routeService.pageQuery(cid, currentPage, pageSize, rname);

        // 4. 将数据写回到浏览器
        writeValue(pb, resp);
    }

    /**
     * 根据rid查询一条线路信息
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取浏览器端传来的rid
        String rid = req.getParameter("rid");
        // 调用service查询rid对应的旅游线路
        Route route = routeService.findOne(Integer.parseInt(rid));
        // 将route以json返回给浏览器
        writeValue(route, resp);
    }

    /**
     * 判断当前线路是否在当前用户的收藏列表中
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String rid = req.getParameter("rid");
        // 获取Session中的用户信息（登陆成功时已经将用户存入了session）
        User user = (User) req.getSession().getAttribute("user");
        int uid;  // 用户id
        if(user != null) {
            uid = user.getUid();
        }else {
            uid = 0;
        }
        // 调用FavoriteService查询
        boolean flag = favoriteService.isFavorite(rid, uid);
        // 将数据以json写回浏览器
        writeValue(flag, resp);
    }

    /**
     * 用户添加线路
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void addFavorite(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String rid = req.getParameter("rid");
        User user = (User) req.getSession().getAttribute("user");
        int uid;  // 用户id
        if(user != null) {
            uid = user.getUid();
        }else {
            return;
        }
        favoriteService.add(rid, uid);
    }

}
