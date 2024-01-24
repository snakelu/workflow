package com.matrix.workflow.util;

import com.matrix.workflow.listener.SayTaskListener;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.builder.ServiceTaskBuilder;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperties;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperty;

public class BpmnGenerator {

  public static BpmnModelInstance generateModel(String id, String name) {
    BpmnModelInstance modelInstance =
        Bpmn.createExecutableProcess()
            .id(id)
            .name(name)
            // start event
            .startEvent("start")
            // user task: say, delegate execute after task complete
            .userTask("say")
            .camundaTaskListenerDelegateExpression(
                SayTaskListener.EVENTNAME_COMPLETE, "${sayTaskListener}")
            // gateway, say count < 10 -> service task: hello, say count >= 10 -> end
            .exclusiveGateway("gateway")
            .name("gateway")
            .condition("yes", "#{count < 10}")
            // say hello service task with delegate
            .serviceTask("hello")
            .camundaDelegateExpression("${helloDelegate}")
            .connectTo("say")
            .moveToNode("gateway")
            .condition("no", "#{count >= 10}")
            .endEvent()
            .done();

    ExtensionElements extensionElements = modelInstance.newInstance(ExtensionElements.class);
    CamundaProperties camundaProperties =
        extensionElements.addExtensionElement(CamundaProperties.class);
    CamundaProperty camundaProperty = extensionElements.addExtensionElement(CamundaProperty.class);
    camundaProperty.setCamundaName("color");
    camundaProperty.setCamundaValue("red");
    camundaProperties.getCamundaProperties().add(camundaProperty);
    ServiceTask hello = modelInstance.getModelElementById("hello");
    hello.setExtensionElements(extensionElements);

    return modelInstance;
  }

  public static BpmnModelInstance generateTestModel(String id, String name, int count) {
    ServiceTaskBuilder taskBuilder =
        Bpmn.createExecutableProcess()
            .id(id)
            .name(name)
            // start event
            .startEvent("start")
            // user task: say, delegate execute after task complete
            .userTask("say")
            .camundaTaskListenerDelegateExpression(
                SayTaskListener.EVENTNAME_COMPLETE, "${sayTaskListener}")
            .serviceTask("test0")
            .camundaDelegateExpression("${testDelegate}");
    for (int i = 1; i < count; i++) {
      taskBuilder =
          taskBuilder.serviceTask("test" + i).camundaDelegateExpression("${testDelegate}");
    }
    BpmnModelInstance modelInstance = taskBuilder
        .serviceTask("hello")
        .camundaDelegateExpression("${helloDelegate}")
        .endEvent()
        .done();

    ExtensionElements extensionElements = modelInstance.newInstance(ExtensionElements.class);
    CamundaProperties camundaProperties =
        extensionElements.addExtensionElement(CamundaProperties.class);
    CamundaProperty camundaProperty = extensionElements.addExtensionElement(CamundaProperty.class);
    camundaProperty.setCamundaName("color");
    camundaProperty.setCamundaValue("red");
    camundaProperties.getCamundaProperties().add(camundaProperty);
    ServiceTask hello = modelInstance.getModelElementById("hello");
    hello.setExtensionElements(extensionElements);

    return modelInstance;
  }
}
