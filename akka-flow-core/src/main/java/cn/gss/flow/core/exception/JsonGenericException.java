package cn.gss.flow.core.exception;

/**
 * Created by JackieGao on 2020/7/29.
 */
public class JsonGenericException extends RuntimeException {
  public JsonGenericException(String message) {
    super(message);
  }

  public JsonGenericException(String message, Throwable cause) {
    super(message, cause);
  }

  public JsonGenericException(Throwable cause) {
    super(cause);
  }
}
