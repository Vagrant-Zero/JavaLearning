package cn.itcast.travel.dao;

import cn.itcast.travel.domain.RouteImg;

import java.util.List;

public interface RouteImgDao {

    /**
     * 根据rid查找对应一条旅游路线的图片路径集合
     * @param rid
     * @return
     */
    public List<RouteImg> findByRid(int rid);
}
