package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.CategroyDao;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

public class CategoryDaoImpl implements CategroyDao {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public List<Category> findAll() {
        try {
            String sql = "select * from tab_category";
            List<Category> categoryList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Category>(Category.class));
            return categoryList;
        } catch (DataAccessException e) {
            return null;
        }
    }
}
