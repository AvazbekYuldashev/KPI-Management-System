package api.v1.KPI.Management.System.exception.exps;

public class AttemptLimitException extends RuntimeException {
    public AttemptLimitException(String message) {
        super(message);
    }
}
