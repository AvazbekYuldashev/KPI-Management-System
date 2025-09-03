package api.v1.KPI.Management.System.exception.exps;

public class ConfirmCodeExpiredException extends RuntimeException {
  public ConfirmCodeExpiredException(String message) {
    super(message);
  }
}
