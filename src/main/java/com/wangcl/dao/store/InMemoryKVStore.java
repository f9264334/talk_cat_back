package com.wangcl.dao.store;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 全局内存 KV 存储（类似 Redis 内存版）
 * 线程安全 + 支持过期时间 + 全局单例
 */
@Component
public class InMemoryKVStore {
    // 核心存储：ConcurrentHashMap 保证线程安全
    private final Map<String, ValueWrapper> storage = new ConcurrentHashMap<>();

    // 定时清理过期键的线程池（非阻塞）
    private final ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor();

    // 初始化：启动定时清理任务（每分钟检查一次过期键）
    public InMemoryKVStore() {
        cleaner.scheduleAtFixedRate(this::cleanExpiredKeys, 1, 1, TimeUnit.MINUTES);
    }

    // ========== 基础 KV 操作 ==========
    /**
     * 设置 KV（永久有效）
     */
    public void set(String key, Object value) {
        storage.put(key, new ValueWrapper(value, null));
    }

    /**
     * 设置 KV + 过期时间（单位：秒）
     */
    public void set(String key, Object value, long expireSeconds) {
        long expireTime = System.currentTimeMillis() + expireSeconds * 1000;
        storage.put(key, new ValueWrapper(value, expireTime));
    }

    /**
     * 获取值（自动过滤过期键）
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        ValueWrapper wrapper = storage.get(key);
        // 键不存在 / 已过期 → 返回 null
        if (wrapper == null || (wrapper.expireTime != null && System.currentTimeMillis() > wrapper.expireTime)) {
            storage.remove(key); // 主动清理过期键
            return null;
        }
        return (T) wrapper.value;
    }

    /**
     * 删除键
     */
    public void delete(String key) {
        storage.remove(key);
    }

    /**
     * 清空所有键
     */
    public void clear() {
        storage.clear();
    }

    /**
     * 获取当前存储大小
     */
    public int size() {
        return storage.size();
    }

    // ========== 内部辅助类：包装值 + 过期时间 ==========
    private static class ValueWrapper {
        Object value;       // 实际存储的值
        Long expireTime;    // 过期时间（毫秒时间戳，null 表示永久）

        public ValueWrapper(Object value, Long expireTime) {
            this.value = value;
            this.expireTime = expireTime;
        }
    }

    // ========== 定时清理过期键 ==========
    private void cleanExpiredKeys() {
        long now = System.currentTimeMillis();
        // 遍历并移除过期键（ConcurrentHashMap 支持安全遍历）
        storage.entrySet().removeIf(entry -> {
            ValueWrapper wrapper = entry.getValue();
            return wrapper.expireTime != null && now > wrapper.expireTime;
        });
    }

    // 销毁时关闭线程池（Spring 销毁 Bean 时调用）
    @Override
    protected void finalize() throws Throwable {
        cleaner.shutdown();
        super.finalize();
    }
}