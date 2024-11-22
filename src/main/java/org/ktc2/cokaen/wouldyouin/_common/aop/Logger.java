package org.ktc2.cokaen.wouldyouin._common.aop;

import io.jsonwebtoken.lang.Objects;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
public class Logger {

    @Pointcut("execution(* org.ktc2.cokaen.wouldyouin.auth.application.JwtAuthFilter.*(..))")
    public void springFilterExecution() {
    }

    @Pointcut("execution(* org.ktc2.cokaen.wouldyouin..*.*(..)) && !springFilterExecution()")
    public void all() {
    }

    @Around("all()")
    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        String packageName = joinPoint.getSignature().getDeclaringTypeName()
            .replace("org.ktc2.cokaen.wouldyouin.", "")
            .replaceAll("\\.[^\\.]+$", "");
        Object[] args = joinPoint.getArgs();
        long start = System.currentTimeMillis();
        long timeInMs;
        Object result;
        log.debug("{} : CALL {}", packageName, methodName);
        log.debug("{} :     with param = {}", packageName, Arrays.stream(args)
            .map(obj -> {
                // byte[] 는 로깅하지 않음
                if (obj instanceof byte[]) {
                    return "'object of byte[]'";
                }
                return Objects.nullSafeToString(obj);
            })
            .toArray());

        try {
            result = joinPoint.proceed();
        } catch(Throwable t) {
            log.debug("{} :      {} throws {}", packageName, methodName, t.getClass().getSimpleName());
            throw t;
        } finally {
            timeInMs = System.currentTimeMillis() - start;
            log.debug("{} : EXIT {}", packageName, methodName);
            log.debug("{} :     with executeTime = {}ms", packageName, timeInMs);
        }

        // byte[] 는 로깅하지 않음
        String resultToString = Objects.nullSafeToString(result);
        if (result instanceof byte[]) {
            resultToString = "'object of byte[]'";
        }

        log.debug("{} :     with return      = {}", packageName, resultToString);

        return result;
    }
}
