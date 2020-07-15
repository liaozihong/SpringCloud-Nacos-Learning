package com.zoey.learning.seataorder.aop;

import com.zoey.learning.commons.result.ApiResult;
import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.tm.api.GlobalTransaction;
import io.seata.tm.api.GlobalTransactionContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class WorkAspect {

    @Before("execution(* com.zoey.learning.seataorder.service.*.*(..))")
    public void before(JoinPoint joinPoint) throws TransactionException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        log.info("拦截到需要分布式事务的方法," + method.getName());
        // 此处可用redis或者定时任务来获取一个key判断是否需要关闭分布式事务
        // 模拟动态关闭分布式事务
        GlobalTransaction tx = GlobalTransactionContext.getCurrentOrCreate();
        tx.begin(300000, "test-client");
        log.info("创建分布式事务id:{}", tx.getXid());
    }

    @AfterThrowing(throwing = "e", pointcut = "execution(* com.zoey.learning.seataorder.service.*.*(..))")
    public void doRecoveryActions(Throwable e) throws TransactionException {
        log.info("方法执行异常", e);
        if (!StringUtils.isBlank(RootContext.getXID())) {
            log.info("分布式事务Id:{}, 手动回滚!", RootContext.getXID());
            GlobalTransactionContext.reload(RootContext.getXID()).rollback();
        }
    }

    @AfterReturning(value = "execution(* com.zoey.learning.seataorder.service.*.*(..))", returning = "result")
    public void afterReturning(JoinPoint point, Object result) throws TransactionException {
        log.info("方法执行结束:{}", result);
        // 方法返回值 RespData是自定义的统一返回类
        ApiResult respData = (ApiResult) result;
        if (respData.getCode() != 200) {
            if (!StringUtils.isBlank(RootContext.getXID())) {
                log.info("分布式事务Id:{}, 手动回滚!", RootContext.getXID());
                GlobalTransactionContext.reload(RootContext.getXID()).rollback();
            }
        }
    }

}
