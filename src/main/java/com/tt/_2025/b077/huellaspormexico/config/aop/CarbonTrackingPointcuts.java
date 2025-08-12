package com.tt._2025.b077.huellaspormexico.config.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CarbonTrackingPointcuts {

    @Pointcut("execution(* com.tt._2025.b077.huellaspormexico.modules..*(..))")
    public void allowModuleMethods() {}

    @Pointcut("execution(* com.tt._2025.b077.huellaspormexico.modules.auth..*(..)) || " +
            "execution(* com.tt._2025.b077.huellaspormexico.modules.carbon..*(..)) || " +
            "execution(* com.tt._2025.b077.huellaspormexico.config..*(..)) || " +
            "execution(* com.tt._2025.b077.huellaspormexico.modules.users.repositories.UserRepository.findByUsername(..))")
    public void excludedModules() {}

    @Pointcut("allowModuleMethods() && !excludedModules()")
    public void trackableExecution() {}
}
