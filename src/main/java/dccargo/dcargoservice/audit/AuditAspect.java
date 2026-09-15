package dccargo.dcargoservice.audit;

import dccargo.dcargoservice.util.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Обвязывает методы с аннотацией {@link Audited}:
 * открывает операцию, наполняет контекст (кто/где/какая операция),
 * а при выходе закрывает и сохраняет накопленные записи.
 * При вложенных @Audited-вызовах (метод вызывает другой @Audited-метод)
 * операция остаётся одна — дочерние не открывают новую транзакцию.
 * <p>
 * Аудит полностью «best-effort»: любые его ошибки логируются в консоль и
 * НЕ влияют на выполнение бизнес-метода.
 */
@Slf4j
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuditAspect {

    private final AuditService auditService;
    private final SecurityUtils securityUtils;

    public AuditAspect(AuditService auditService, SecurityUtils securityUtils) {
        this.auditService = auditService;
        this.securityUtils = securityUtils;
    }

    @Around("@annotation(dccargo.dcargoservice.audit.Audited)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        boolean topLevel = AuditContext.current() == null;

        if (topLevel) {
            openOperation(joinPoint);
        }

        try {
            Object result = joinPoint.proceed();
            if (topLevel) {
                flushSafely();
            }
            return result;
        } finally {
            if (topLevel) {
                AuditContext.end();
            }
        }
    }

    /** Открывает операцию; если не вышло — работаем без аудита, но запрос живёт. */
    private void openOperation(ProceedingJoinPoint joinPoint) {
        try {
            Audited audited = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(Audited.class);
            AuditContext.Tx tx = AuditContext.begin();
            tx.setOperation(resolveOperation(audited, joinPoint));
            fillActorAndEndpoint(tx);
        } catch (Exception e) {
            log.error("Аудит: не удалось открыть контекст операции", e);
            AuditContext.end();
        }
    }

    /** Сохраняет накопленное; любые ошибки — только в консоль. */
    private void flushSafely() {
        try {
            AuditContext.Tx tx = AuditContext.current();
            if (tx != null) {
                auditService.flush(tx);
            }
        } catch (Exception e) {
            log.error("Аудит: не удалось сохранить записи", e);
        }
    }

    private String resolveOperation(Audited audited, ProceedingJoinPoint joinPoint) {
        if (audited.operation() != null && !audited.operation().isBlank()) {
            return audited.operation();
        }
        return joinPoint.getSignature().getName().toUpperCase();
    }

    private void fillActorAndEndpoint(AuditContext.Tx tx) {
        try {
            tx.setActorId(securityUtils.getCurrentUserId());
        } catch (Exception e) {
            tx.setActorId(null);
        }
        try {
            tx.setActorName(securityUtils.getCurrentUsername());
        } catch (Exception e) {
            tx.setActorName(null);
        }
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                tx.setEndpoint(request.getRequestURI());
            }
        } catch (Exception e) {
            tx.setEndpoint(null);
        }
    }
}