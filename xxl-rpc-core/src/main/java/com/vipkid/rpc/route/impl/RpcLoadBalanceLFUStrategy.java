package com.vipkid.rpc.route.impl;

import com.xxl.rpc.remoting.invoker.route.XxlRpcLoadBalance;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 创建时间：2019年/06月/18日
 *
 * @author 张政[zhangzheng6@vipkid.com.cn]
 * 类名：RpcLoadBalanceLFUStrategy
 * 描述：根据最近使用最少频率(CACHE_VALID_TIME)的负载
 */
public class RpcLoadBalanceLFUStrategy extends XxlRpcLoadBalance {

    private ConcurrentHashMap<String, HashMap<String, Integer>> jobLfuMap = new ConcurrentHashMap<String, HashMap<String, Integer>>();
    private long CACHE_VALID_TIME = 0;

    public String doRoute(String serviceKey, TreeSet<String> addressSet) {

        // cache clear
        if (System.currentTimeMillis() > CACHE_VALID_TIME) {
            jobLfuMap.clear();
            CACHE_VALID_TIME = System.currentTimeMillis() + 1000 * 60 * 60 * 24;
        }

        // lfu item init
        // Key排序可以用TreeMap+构造入参Compare；Value排序暂时只能通过ArrayList；
        HashMap<String, Integer> lfuItemMap = jobLfuMap.get(serviceKey);
        if (lfuItemMap == null) {
            lfuItemMap = new HashMap<String, Integer>();
            jobLfuMap.putIfAbsent(serviceKey, lfuItemMap);   // 避免重复覆盖
        }

        // put new
        for (String address : addressSet) {
            if (!lfuItemMap.containsKey(address) || lfuItemMap.get(address) > 1000000) {
                lfuItemMap.put(address, 0);
            }
        }

        // remove old
        List<String> delKeys = new ArrayList<>();
        for (String existKey : lfuItemMap.keySet()) {
            if (!addressSet.contains(existKey)) {
                delKeys.add(existKey);
            }
        }
        if (delKeys.size() > 0) {
            for (String delKey : delKeys) {
                lfuItemMap.remove(delKey);
            }
        }

        // load least userd count address
        List<Map.Entry<String, Integer>> lfuItemList = new ArrayList<Map.Entry<String, Integer>>(lfuItemMap.entrySet());
        Collections.sort(lfuItemList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        Map.Entry<String, Integer> addressItem = lfuItemList.get(0);
        String minAddress = addressItem.getKey();
        addressItem.setValue(addressItem.getValue() + 1);

        return minAddress;
    }

    @Override
    public String route(String serviceKey, TreeSet<String> addressSet) {
        String finalAddress = doRoute(serviceKey, addressSet);
        return finalAddress;
    }

}
