package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImpl implements UserDao {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public User findUserByUsername(String userName) {
        try {
            String sql = "select * from tab_user where username = ?";
            User user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), userName);
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void save(User user) {
        String sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code) values (?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql,
                            user.getUsername(),
                            user.getPassword(),
                            user.getName(),
                            user.getBirthday(),
                            user.getSex(),
                            user.getTelephone(),
                            user.getEmail(),
                            user.getStatus(),
                            user.getCode()
        );
    }

    @Override
    public User findUserByCode(String code) {
        try {
            String sql = "select * from tab_user where code = ?";
            User user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), code);
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void updateUserStatus(User user) {
        String sql = "update tab_user set status = 'Y' where uid = ?";
        jdbcTemplate.update(sql, user.getUid());
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        try {
            String sql = "select * from tab_user where username = ? and password = ?";
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username, password);
        } catch (DataAccessException e) {
            return null;
        }
    }
}
