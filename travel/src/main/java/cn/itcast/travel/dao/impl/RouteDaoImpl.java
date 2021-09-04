package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 根据cid查询线路总数
     * @param cid
     * @return
     */
    @Override
    public int findTotalCount(int cid, String rname) {
        String sql = "select count(*) from tab_route where 1 = 1 ";
        StringBuilder stringBuilder = new StringBuilder(sql);
        ArrayList<Object> values = new ArrayList<>();
        if(cid != 0) {
            stringBuilder.append(" and cid = ? ");
            values.add(cid);
        }
        if(rname != null && rname.length() > 0 && !"null".equalsIgnoreCase(rname)) {
            stringBuilder.append(" and rname like ? ");
            values.add("%" + rname + "%");
        }
        sql = stringBuilder.toString();
        return jdbcTemplate.queryForObject(sql, Integer.class, values.toArray());
    }

    /**
     * 根据cid查询线路集合
     * @param cid
     * @param start
     * @param pageSize
     * @return
     */
    @Override
    public List<Route> findByPage(int cid, int start, int pageSize, String rname) {
        String sql = "select * from tab_route where 1 = 1 ";
        StringBuilder stringBuilder = new StringBuilder(sql);
        ArrayList<Object> values = new ArrayList<>();
        if(cid != 0) {
            stringBuilder.append(" and cid = ?");
            values.add(cid);
        }
        if(rname != null && rname.length() > 0 && !"null".equalsIgnoreCase(rname)) {
            stringBuilder.append(" and rname like ? ");
            values.add("%" + rname + "%");
        }
        stringBuilder.append(" limit ?, ?");
        values.add(start);
        values.add(pageSize);
        sql = stringBuilder.toString();
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Route>(Route.class), values.toArray());
    }

    /**
     * 根据rid查询对应的一条旅游线路
     * @param rid
     * @return
     */
    @Override
    public Route findOne(int rid) {
        String sql = "select * from tab_route where rid = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Route>(Route.class), rid);
    }
}
