package net.hexj.comet.service.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.NestedServletException;

import net.hexj.comet.service.common.LocaleMessageService;
import net.hexj.comet.service.common.MessageWrapper;
import net.hexj.comet.service.common.ServiceException;

/**
 * Created by Eric on 13/10/2016.
 */
@RestController
public class CommonController implements ErrorController {
  private static final String ERROR_PATH = "/error";
  @Autowired
  private LocaleMessageService localeMessageService;

  @RequestMapping(value = ERROR_PATH, method = {RequestMethod.GET, RequestMethod.POST,
      RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
  public MessageWrapper error(HttpServletRequest request, HttpServletResponse response) {
    int statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
    Object exceptionObject = request.getAttribute("javax.servlet.error.exception");

    if (exceptionObject != null && exceptionObject instanceof NestedServletException) {
      NestedServletException nestedServletException = (NestedServletException) exceptionObject;
      Throwable cause = nestedServletException.getCause();
      if (cause != null && cause instanceof ServiceException) {
        response.setStatus(MessageWrapper.SC_OK);
        ServiceException serviceException = (ServiceException) cause;
        return new MessageWrapper(serviceException.getCode(), serviceException.getMessage(),
            serviceException.getData());
      }
    }

    response.setStatus(MessageWrapper.SC_OK);
    return new MessageWrapper(statusCode, localeMessageService.getMessage("server.internal.error"),
        null);
  }

  @Override
  public String getErrorPath() {
    return ERROR_PATH;
  }
}
