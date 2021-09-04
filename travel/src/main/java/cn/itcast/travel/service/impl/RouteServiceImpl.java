package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImgDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImgDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao routeDao = new RouteDaoImpl();
    private RouteImgDao routeImgDao = new RouteImgDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();

    /**
     * 根据cid分页查询旅游线路
     * @param cid
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname) {
        PageBean<Route> pb = new PageBean<Route>();
        int totalCount = routeDao.findTotalCount(cid, rname);
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize :(totalCount / pageSize) + 1 ;
        if(currentPage <= 0) {
            currentPage = 1;
        }else if(currentPage > totalPage) {
            currentPage = totalPage;
        }
        if(totalPage == 0) {
            currentPage = 1;
        }
        int start = (currentPage - 1) * pageSize;//开始的记录数
        List<Route> routeList = routeDao.findByPage(cid, start, pageSize, rname);

        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);
        pb.setTotalCount(totalCount);
        pb.setTotalPage(totalPage);
        pb.setList(routeList);
        return pb;

    }

    /**
     * 根据rid查询对应的旅游线路
     * @param rid
     * @return
     */
    @Override
    public Route findOne(int rid) {
        Route route = routeDao.findOne(rid);
        List<RouteImg> routeImgList = routeImgDao.findByRid(route.getRid()); // 旅游线路的图片路径
        route.setRouteImgList(routeImgList);
        Seller seller = sellerDao.findBySid(route.getSid());
        route.setSeller(seller);
        // 查询收藏次数
        int count = favoriteService.findCountByRid(route.getRid());
        route.setCount(count);
        return route;
    }

}
