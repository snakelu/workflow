package com.matrix.workflow.listener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class SayTaskListener implements TaskListener {

  private static final String QUESTIONS = "questions";

  @Override
  public void notify(DelegateTask delegateTask) {
    log.info("say at: {}", Instant.now().toString());
    if (delegateTask.hasVariable("name")) {
      Object name = delegateTask.getVariable("name");
      DelegateExecution execution = delegateTask.getExecution();
      if (!execution.hasVariable(QUESTIONS)) {
        execution.setVariable(QUESTIONS, new ArrayList<>());
      }
      List<String> questions = (List<String>) execution.getVariable(QUESTIONS);
      questions.add(name.toString());
      execution.setVariable(QUESTIONS, questions);
      execution.setVariable("count", questions.size());
      execution.setVariable("name", name);
    }
  }
}
