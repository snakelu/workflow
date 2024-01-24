package com.matrix.workflow.delegate;

import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class TestDelegate implements JavaDelegate {

  @Override
  public void execute(DelegateExecution execution) {
    if (execution.hasVariable("name")) {
      log.info("name: {}", execution.getVariable("name"));
    }
  }
}
