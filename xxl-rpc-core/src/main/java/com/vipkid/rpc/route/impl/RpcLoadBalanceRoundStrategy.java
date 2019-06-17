package com.vipkid.rpc.route.impl;

import com.xxl.rpc.remoting.invoker.route.XxlRpcLoadBalance;

import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 创建时间：2019年/06月/18日
 *
 * @author 张政[zhangzheng6@vipkid.com.cn]
 * 类名：RpcLoadBalanceRoundStrategy
 * 描述：轮训方式使用
 */
public class RpcLoadBalanceRoundStrategy extends XxlRpcLoadBalance {

    private ConcurrentHashMap<String, Integer> routeCountEachJob = new ConcurrentHashMap<String, Integer>();
    private long CACHE_VALID_TIME = 0;

    private int count(String serviceKey) {
        // cache clear
        if (System.currentTimeMillis() > CACHE_VALID_TIME) {
            routeCountEachJob.clear();
            CACHE_VALID_TIME = System.currentTimeMillis() + 24 * 60 * 60 * 1000;
        }

        // count++
        Integer count = routeCountEachJob.get(serviceKey);
        count = (count == null || count > 1000000) ? (new Random().nextInt(100)) : ++count;  // 初始化时主动Random一次，缓解首次压力
        routeCountEachJob.put(serviceKey, count);
        return count;
    }

    @Override
    public String route(String serviceKey, TreeSet<String> addressSet) {
        // arr
        String[] addressArr = addressSet.toArray(new String[addressSet.size()]);

        // round
        String finalAddress = addressArr[count(serviceKey) % addressArr.length];
        return finalAddress;
    }

}
