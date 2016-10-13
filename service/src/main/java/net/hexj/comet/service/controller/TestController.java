package net.hexj.comet.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.hexj.comet.service.common.MessageWrapper;
import net.hexj.comet.service.mapper.TestMapper;

/**
 * Created by Eric on 13/10/2016.
 */
@RestController
public class TestController {
  @Autowired
  private TestMapper testMapper;

  @RequestMapping(value = "/test", method = RequestMethod.GET)
  public MessageWrapper test() {
    return new MessageWrapper(MessageWrapper.SC_OK, "hi", null);
  }
}
