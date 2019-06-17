package com.vipkid.rpc.exception;

/**
 * 创建时间：2019年/06月/18日
 *
 * @author 张政[zhangzheng6@vipkid.com.cn]
 * 类名：RpcException
 * 描述：远程调用异常
 */
public class RpcException extends RuntimeException {
    private static final long serialVersionUID = 42L;

    public RpcException(String msg) {
        super(msg);
    }

    public RpcException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RpcException(Throwable cause) {
        super(cause);
    }

}