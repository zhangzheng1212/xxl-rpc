package com.vipkid.rpc.route;

import com.xxl.rpc.remoting.invoker.route.XxlRpcLoadBalance;
import com.xxl.rpc.remoting.invoker.route.impl.*;

/**
 * 创建时间：2019年/06月/18日
 *
 * @author 张政[zhangzheng6@vipkid.com.cn]
 * 类名：LoadBalance
 * 描述：路由处理策略
 */
public enum LoadBalance {

    RANDOM(new XxlRpcLoadBalanceRandomStrategy()),
    ROUND(new XxlRpcLoadBalanceRoundStrategy()),
    LRU(new XxlRpcLoadBalanceLRUStrategy()),
    LFU(new XxlRpcLoadBalanceLFUStrategy()),
    CONSISTENT_HASH(new XxlRpcLoadBalanceConsistentHashStrategy());


    public final XxlRpcLoadBalance xxlRpcInvokerRouter;

    private LoadBalance(XxlRpcLoadBalance xxlRpcInvokerRouter) {
        this.xxlRpcInvokerRouter = xxlRpcInvokerRouter;
    }


    public static LoadBalance match(String name, LoadBalance defaultRouter) {
        for (LoadBalance item : LoadBalance.values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return defaultRouter;
    }



    /*public static void main(String[] args) {
        String serviceKey = "service";
        TreeSet<String> addressSet = new TreeSet<String>(){{
            add("1");
            add("2");
            add("3");
            add("4");
            add("5");
        }};

        for (LoadBalance item : LoadBalance.values()) {
            long start = System.currentTimeMillis();
            for (int i = 0; i < 100000; i++) {
                String address = LoadBalance.LFU.xxlRpcInvokerRouter.route(serviceKey, addressSet);
                //System.out.println(address);;
            }
            long end = System.currentTimeMillis();
            System.out.println(item.name() + " --- " + (end-start));
        }

    }*/


}