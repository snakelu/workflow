package com.matrix.workflow.delegate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperties;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperty;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class HelloDelegate implements JavaDelegate {

  private static final String ANSWERS = "answers";

  @Override
  public void execute(DelegateExecution execution) {
    FlowElement element = execution.getBpmnModelElementInstance();
    ExtensionElements extensionElements = element.getExtensionElements();
    Collection<ModelElementInstance> elements = extensionElements.getElements();
    for (ModelElementInstance elementInstance : elements) {
      if (elementInstance instanceof CamundaProperties) {
        Collection<CamundaProperty> camundaProperties =
            ((CamundaProperties) elementInstance).getCamundaProperties();
        for (CamundaProperty camundaProperty : camundaProperties) {
          log.info(
              "property: {} - {}",
              camundaProperty.getCamundaName(),
              camundaProperty.getCamundaValue());
        }
      }
    }

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
