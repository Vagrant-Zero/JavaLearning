package cn.itcast.travel.service;


import cn.itcast.travel.domain.Category;

import java.util.List;

public interface CategoryService {
    /**
     * 在数据库或者缓存中查找所有的旅游线路
     * @return
     */
    List<Category> findAll();
}
