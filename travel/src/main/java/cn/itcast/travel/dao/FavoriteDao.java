package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;

public interface FavoriteDao {

    /**
     * 根据rid和uid在favorite查找数据
     * @param rid
     * @param uid
     * @return
     */
    public Favorite findByRidAndUid(int rid, int uid);

    /**
     * 根据rid查找rid线路的收藏次数
     * @param rid
     */
    public int findCountByRid(int rid);

    /**
     * 添加rid线路到uid用户的收藏列表
     * @param rid
     * @param uid
     */
    void add(int rid, int uid);
}
