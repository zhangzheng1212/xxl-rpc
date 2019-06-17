package com.vipkid.rpc.route;

import java.util.TreeSet;

/**
 * 创建时间：2019年/06月/18日
 *
 * @author 张政[zhangzheng6@vipkid.com.cn]
 * 类名：RpcLoadBalance
 * 描述：路由处理抽象类
 */
public abstract class RpcLoadBalance {

    public abstract String route(String serviceKey, TreeSet<String> addressSet);

}
