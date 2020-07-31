package cn.gss.flow.core.exception;

import org.springframework.dao.DataAccessException;

/**
 * Created by JackieGao on 2020/7/28.
 */
public class KeyGenerateException extends DataAccessException {
  public KeyGenerateException() {
    super("Can not get generated key from db");
  }
}
