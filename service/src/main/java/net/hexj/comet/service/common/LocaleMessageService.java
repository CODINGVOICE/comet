package net.hexj.comet.service.common;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * Created by Eric on 13/10/2016.
 */
@Component
public class LocaleMessageService {
  @Autowired
  private MessageSource messageSource;

  public String getMessage(String key) {
    return getMessage(key, null);
  }

  public String getMessage(String key, Object[] objects) {
    Locale locale = LocaleContextHolder.getLocale();
    return messageSource.getMessage(key, objects, locale);
  }
}
