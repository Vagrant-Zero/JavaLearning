package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Category;

import java.util.List;

/**
 * 路线查询Dao
 */
public interface CategroyDao {

    /**
     * 调用数据库或缓存查找所有的旅游线路
     * @return
     */
    List<Category> findAll();
}
