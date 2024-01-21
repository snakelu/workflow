package com.matrix.workflow.behavior;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.delegate.ActivityBehavior;

public class HelloBehavior implements ActivityBehavior {

  @Override
  public void execute(DelegateExecution execution) {
    Object name = execution.getVariable("name");
    System.out.println("hello " + name);
    execution.setVariable("hello", "hello " + name);
  }
}
