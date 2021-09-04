package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {
    UserDao userDao = new UserDaoImpl();

    /**
     * 用户注册
     * @param user
     * @return 成功返回true，失败返回false
     */
    @Override
    public boolean regist(User user) {
        User checkedUser = userDao.findUserByUsername(user.getUsername());
        if(checkedUser != null) {
            return false;
        }
        // 设置唯一编码、校验状态
        user.setCode(UuidUtil.getUuid());
        user.setStatus("N");
        // 在数据库中存入新用户的信息
        userDao.save(user);
        String content="<a href='http://localhost:8080/travel/user/active?code="+user.getCode()+"'>点击激活【三清旅游网】</a>";
        MailUtils.sendMail(user.getEmail(),content,"激活邮件");
        return true;
    }

    /**
     * 邮箱激活
     * @param code
     * @return
     */
    @Override
    public boolean active(String code) {
        User user = userDao.findUserByCode(code);
        if(user != null) {
            userDao.updateUserStatus(user);
            return true;
        }else {
            return false;
        }
    }

    /**
     * 登录校验
     * @param loginUser
     * @return 成功返回查找到的用户，否则返回null
     */
    @Override
    public User login(User loginUser) {
        return userDao.findByUsernameAndPassword(loginUser.getUsername(), loginUser.getPassword());
    }
}
