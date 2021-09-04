package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {

    /**
     * 根据用户名查找用户
     * @param Username
     * @return
     */
    User findUserByUsername(String Username);

    /**
     * 将注册的用户数据存储到数据库
     * @param user
     */
    void save(User user);

    /**
     * 根据唯一code查找用户
     * @param code
     * @return 找到返回对应的user对象，否则返回null
     */
    User findUserByCode(String code);

    /**
     * 更新用户的状态信息：'Y' or 'N'
     * @param user
     */
    void updateUserStatus(User user);

    /**
     * 根据用户名和密码查找用户
     * @param username
     * @param password
     * @return
     */
    User findByUsernameAndPassword(String username, String password);
}
