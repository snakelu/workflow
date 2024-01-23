package com.matrix.workflow.delegate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class HelloDelegate implements JavaDelegate {

  private static final String ANSWERS = "answers";

  @Override
  public void execute(DelegateExecution execution) {
    if (execution.hasVariable("name")) {
      if (!execution.hasVariable(ANSWERS)) {
        execution.setVariable(ANSWERS, new ArrayList<>());
      }
      List<String> questions = (List<String>) execution.getVariable(ANSWERS);
      questions.add("hello " + execution.getVariable("name"));
      execution.setVariable(ANSWERS, questions);
      execution.removeVariable("name");
    }
    log.info("hello at: {}", Instant.now().toString());
  }
}
