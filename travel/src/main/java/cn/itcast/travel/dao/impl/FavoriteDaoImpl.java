package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

public class FavoriteDaoImpl implements FavoriteDao {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public Favorite findByRidAndUid(int rid, int uid) {
        try {
            String sql = "select * from tab_favorite where rid = ? and uid = ?";
            Favorite favorite = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), rid, uid);
            return favorite;
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public int findCountByRid(int rid) {
        String sql = "SELECT COUNT(*) FROM tab_favorite WHERE rid = ?";
        return jdbcTemplate.queryForObject(sql,Integer.class,rid);
    }

    @Override
    public void add(int rid, int uid) {
        String sql = "insert into tab_favorite values(?, ?, ?)";
        jdbcTemplate.update(sql, rid, new Date(), uid);
    }
}
