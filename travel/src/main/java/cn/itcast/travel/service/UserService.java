package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;

public interface UserService {
    /**
     * 用户注册
     * @param user
     * @return 成功返回true，失败返回false
     */
    boolean regist(User user);

    /**
     * 根据唯一验证码激活用户
     * @param code
     * @return 成功返回true，失败返回false
     */
    boolean active(String code);

    /**
     * 登录校验
     * @param loginUser
     * @return
     */
    User login(User loginUser);
}
