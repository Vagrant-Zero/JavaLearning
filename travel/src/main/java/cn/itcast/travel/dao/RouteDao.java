package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteDao {

    /**
     * 根据cid查询总的线路条数
     * @param cid
     * @param rname
     * @return
     */
    public int findTotalCount(int cid, String rname);

    /**
     * 根据cid查询线路集合
     * @param cid
     * @param start
     * @param pageSize
     * @param rname
     * @return
     */
    public List<Route> findByPage(int cid, int start, int pageSize, String rname);

    /**
     * 根据rid查询对应一条旅游线路
     * @param rid
     * @return
     */
    public Route findOne(int rid);
}
