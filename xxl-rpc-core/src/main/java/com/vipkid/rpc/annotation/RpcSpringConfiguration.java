package com.vipkid.rpc.annotation;

import com.vipkid.rpc.reference.RpcReferenceBean;
import com.xxl.rpc.remoting.provider.XxlRpcProviderFactory;
import com.xxl.rpc.util.XxlRpcException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * 创建时间：2019年/06月/19日
 *
 * @author 张政[zhangzheng6@vipkid.com.cn]
 * 类名：RpcSpringConfiguration
 * 描述：扫描配置RpcBean
 */
@Slf4j
public class RpcSpringConfiguration extends InstantiationAwareBeanPostProcessorAdapter {

    @Override
    public boolean postProcessAfterInstantiation(final Object bean, final String beanName) throws BeansException {
        ReflectionUtils.doWithFields(bean.getClass(), new DealWithVKRpc(bean, beanName));
        return super.postProcessAfterInstantiation(bean, beanName);
    }

    public class DealWithVKRpc implements ReflectionUtils.FieldCallback {
        private Object bean;
        private String beanName;

        private DealWithVKRpc(Object bean, String beanName) {
            this.bean = bean;
            this.beanName = beanName;
        }

        @Override
        public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
            if (!field.isAnnotationPresent(VKRpc.class)) {
                return;
            }
            // 必须定义的是在接口上
            Class fieldClassType = field.getType();
            if (!fieldClassType.isInterface()) {
                throw new XxlRpcException("vk-rpc, reference(VKRpc) must be interface.");
            }

            VKRpc rpcReference = field.getAnnotation(VKRpc.class);


            // init reference bean
            RpcReferenceBean rpcReferenceBean = new RpcReferenceBean(rpcReference.loadBalance(),
                    fieldClassType,
                    rpcReference.version(),
                    rpcReference.timeout(),
                    rpcReference.apiPath(),
                    rpcReference.apiPath(),
                    rpcReference.accessToken(),
                    xxlRpcInvokerFactory);

            Object serviceProxy = rpcReferenceBean.getObject();

            // 注入Bean到属性上
            field.setAccessible(true);
            field.set(bean, serviceProxy);

            log.info("vk-rpc, invoker factory init reference bean success. serviceKey = {}, bean.field = {}.{}", XxlRpcProviderFactory.makeServiceKey(fieldClassType.getName(), rpcReference.version()), beanName, field.getName());
        }

    }
}
