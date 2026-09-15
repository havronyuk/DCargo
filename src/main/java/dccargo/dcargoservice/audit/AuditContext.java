package dccargo.dcargoservice.audit;

import dccargo.dcargoservice.model.dcargo.AuditLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Контекст текущей аудит-операции в рамках одного потока (ThreadLocal).
 * Каждая открытая {@code @Audited} операция имеет один transaction_id;
 * перехватчик Hibernate складывает записи сюда по мере сохранения сущностей.
 */
public final class AuditContext {

    private static final ThreadLocal<Tx> CURRENT = new ThreadLocal<>();

    private AuditContext() {
    }

    /** Открывает новую операцию (вызывается аспектом при входе в @Audited-метод). */
    public static Tx begin() {
        Tx tx = new Tx();
        CURRENT.set(tx);
        return tx;
    }

    /** Текущая операция в потоке (вызывается перехватчиком Hibernate). */
    public static Tx current() {
        return CURRENT.get();
    }

    /** Закрывает операцию (вызывается аспектом при выходе из @Audited-метода). */
    public static void end() {
        CURRENT.remove();
    }

    /** Транзакция одной пользовательской операции. */
    public static final class Tx {

        private final String transactionId = UUID.randomUUID().toString();
        private String operation;
        private Long actorId;
        private String actorName;
        private String endpoint;
        private String comment;
        private int seq;
        private final List<AuditLog> entries = new ArrayList<>();
        private final List<PendingEntry> pending = new ArrayList<>();

        public String getTransactionId() {
            return transactionId;
        }

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
        }

        public Long getActorId() {
            return actorId;
        }

        public void setActorId(Long actorId) {
            this.actorId = actorId;
        }

        public String getActorName() {
            return actorName;
        }

        public void setActorName(String actorName) {
            this.actorName = actorName;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public int getSeq() {
            return seq;
        }

        /** Следующий порядковый номер записи в операции. */
        public int nextSeq() {
            return ++seq;
        }

        public List<AuditLog> getEntries() {
            return entries;
        }

        public void addEntry(AuditLog entry) {
            entries.add(entry);
        }

        /** Накопленная перехватчиком запись с сырыми снапшотами до финализации. */
        public void addPending(PendingEntry pendingEntry) {
            pending.add(pendingEntry);
        }

        public List<PendingEntry> getPending() {
            return pending;
        }
    }

    /** Промежуточная запись: держит сырые карты «до/после» и ссылку на сущность. */
    public static final class PendingEntry {

        private final AuditLog entry;
        private final Object entity;
        private final Object id;
        private final Map<String, Object> before;
        private final Map<String, Object> after;

        public PendingEntry(AuditLog entry, Object entity, Object id, Map<String, Object> before, Map<String, Object> after) {
            this.entry = entry;
            this.entity = entity;
            this.id = id;
            this.before = before;
            this.after = after;
        }

        public AuditLog getEntry() {
            return entry;
        }

        public Object getEntity() {
            return entity;
        }

        public Object getId() {
            return id;
        }

        public Map<String, Object> getBefore() {
            return before;
        }

        public Map<String, Object> getAfter() {
            return after;
        }
    }
}