package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategroyDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {
    private CategroyDao categroyDao = new CategoryDaoImpl();

    /**
     * 查找并返回所有旅游线路
     * @return
     */
    @Override
    public List<Category> findAll() {
        Jedis jedis = new Jedis();
        Set<Tuple> categorys = jedis.zrangeWithScores("category", 0, -1);
        List<Category> categoryList = null;
        if(categorys == null || categorys.size() == 0) {
            // 没有缓存
            categoryList = categroyDao.findAll();
            for (int i = 0; i < categoryList.size(); i++) {
                jedis.zadd("category", categoryList.get(i).getCid(),
                            categoryList.get(i).getCname());
            }
            return categroyDao.findAll();
        }else {
            // 从缓存中查询
            categoryList = new ArrayList<Category>();
            for (Tuple item : categorys) {
                Category category = new Category();
                category.setCname(item.getElement());
                category.setCid((int)item.getScore());
                categoryList.add(category);
            }
            return categoryList;
        }
    }
}
