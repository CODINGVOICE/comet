package net.hexj.comet.service.common;

/**
 * Created by Eric on 13/10/2016.
 */
public class ServiceException extends RuntimeException {
  private static final long serialVersionUID = 916163809455877983L;
  private final int code;
  private final String message;
  private String data;

  public ServiceException(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public ServiceException(int code, String message, Exception cause) {
    super(cause);
    this.code = code;
    this.message = message;
  }

  public String getMessage() {
    return this.message;
  }

  public int getCode() {
    return this.code;
  }

  public String getData() {
    return this.data;
  }

  public void setData(String data) {
    this.data = data;
  }
}
