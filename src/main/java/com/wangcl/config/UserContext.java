package com.wangcl.config;

import com.wangcl.dao.entity.User;

/**
 * 用户上下文工具类，用于存储和获取当前登录用户信息
 */
public class UserContext {
    private static final ThreadLocal<User> CURRENT_USER = new ThreadLocal<>();

    /**
     * 获取当前登录用户信息
     * @return 当前登录用户
     */
    public static User getCurrentUser() {
        return CURRENT_USER.get();
    }

    /**
     * 设置当前登录用户信息
     * @param user 用户信息
     */
    public static void setCurrentUser(User user) {
        CURRENT_USER.set(user);
    }

    /**
     * 清理当前线程的用户信息
     */
    public static void clearCurrentUser() {
        CURRENT_USER.remove();
    }

    /**
     * 获取当前用户ID
     * @return 当前用户ID
     */
    public static Long getCurrentUserId() {
        User user = getCurrentUser();
        return user != null ? user.getId().longValue() : null;
    }

    /**
     * 获取当前用户名
     * @return 当前用户名
     */
    public static String getCurrentUsername() {
        User user = getCurrentUser();
        return user != null ? user.getUsername() : null;
    }
}
