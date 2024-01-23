package com.matrix.workflow.util;

import com.matrix.workflow.listener.SayTaskListener;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

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

    return modelInstance;
  }
}
