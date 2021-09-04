package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Seller;

public interface SellerDao {

    /**
     * 根据sid查找旅游路线对应的商家
     * @param sid
     * @return
     */
    public Seller findBySid(int sid);
}
