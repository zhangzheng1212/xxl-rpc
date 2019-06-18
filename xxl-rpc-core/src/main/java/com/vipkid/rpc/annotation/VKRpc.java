package com.vipkid.rpc.annotation;

import com.vipkid.rpc.remote.HttpMethod;
import com.vipkid.rpc.route.LoadBalance;

import java.lang.annotation.*;

/**
 * 创建时间：2019年/06月/19日
 *
 * @author 张政[zhangzheng6@vipkid.com.cn]
 * 类名：VKRpc
 * 描述：
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface VKRpc {

    /**
     * http请求方式
     */
    HttpMethod httpMethod() default HttpMethod.GET;

    /**
     * 接口域名.或者server名称
     */
    String[] apiHost() default {};

    /**
     * 接口路径
     */
    String apiPath() default "";

    /**
     * 接口认证地址header中author字段
     */
    String accessToken() default "";

    /**
     * 默认超时时间
     */
    long timeout() default 1000;

    /**
     * 接口版本,header中体现version
     */
    String version() default "";

    /**
     * 负载策略
     */
    LoadBalance loadBalance() default LoadBalance.ROUND;
}
