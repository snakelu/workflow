package com.matrix.workflow.util;

import com.matrix.workflow.behavior.HelloBehavior;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.ServiceTask;
import org.activiti.bpmn.model.StartEvent;

public class BpmnGenerator {

  public static BpmnModel generateModel() {
    // 创建BPMN流程模型
    BpmnModel model = new BpmnModel();
    Process process = new Process();
    process.setId("my-process");
    model.addProcess(process);

    StartEvent start = new StartEvent();
    start.setId("start");
    process.addFlowElement(start);

    // 添加用户任务节点
    ServiceTask task = new ServiceTask();
    task.setId("hello");
    task.setName("User Task");
    task.setImplementation(HelloBehavior.class.getCanonicalName());
    task.setResultVariableName("result");
    process.addFlowElement(task);

    // 添加结束事件节点
    EndEvent endEvent = new EndEvent();
    endEvent.setId("end-event");
    process.addFlowElement(endEvent);

    // 添加流程顺序流和任务分配关系
    SequenceFlow flow1 = new SequenceFlow();
    flow1.setId("flow1");
    flow1.setSourceRef("start");
    flow1.setTargetRef("hello");
    process.addFlowElement(flow1);

    SequenceFlow flow2 = new SequenceFlow();
    flow2.setId("flow2");
    flow2.setSourceRef("hello");
    flow2.setTargetRef("end");
    process.addFlowElement(flow2);

    return model;
  }
}
