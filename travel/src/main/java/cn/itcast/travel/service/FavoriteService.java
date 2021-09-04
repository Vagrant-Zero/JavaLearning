package cn.itcast.travel.service;

public interface FavoriteService {

    /**
     * 根据rid以及uid查询uid的用户是否收藏rid的线路
     * @param rid
     * @param uid
     * @return
     */
    public boolean isFavorite(String rid, int uid);

    /**
     * 根据rid查询rid线路的收藏次数
     * @param rid
     * @return
     */
    public int findCountByRid(int rid);

    /**
     * 当前用户添加rid线路到收藏列表
     * @param rid
     * @param uid
     */
    void add(String rid, int uid);
}
