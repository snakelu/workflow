package com.matrix.workflow.controller;

import com.matrix.workflow.util.BpmnGenerator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

  @Resource RepositoryService repositoryService;

  @Resource RuntimeService runtimeService;

  @Resource TaskService taskService;

  @Resource HistoryService historyService;

  @GetMapping("/start")
  public String start() {
    Deployment deployment =
        repositoryService
            .createDeployment()
            .addModelInstance("test.bpmn20.xml", BpmnGenerator.generateModel("test", "test"))
            .deploy();
    List<ProcessDefinition> processDefinitions =
        repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();
    ProcessInstance processInstance =
        runtimeService.startProcessInstanceById(processDefinitions.get(0).getId());
    return processInstance.getId();
  }

  @GetMapping("/delete")
  public String delete() {
    List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().list();
    for (ProcessInstance processInstance : processInstances) {
      runtimeService.deleteProcessInstance(processInstance.getId(), "");
    }
    List<Deployment> deployments = repositoryService.createDeploymentQuery().list();
    List<ProcessDefinition> processDefinitions =
        repositoryService.createProcessDefinitionQuery().list();
    for (ProcessDefinition processDefinition : processDefinitions) {
      repositoryService.deleteProcessDefinition(processDefinition.getId());
    }
    for (Deployment deployment : deployments) {
      repositoryService.deleteDeployment(deployment.getId());
    }
    return "success";
  }

  @GetMapping("/say/{id}/{name}")
  public String say(@PathVariable String id, @PathVariable String name) {
    Task say = taskService.createTaskQuery().processInstanceId(id).taskName("say").singleResult();
    if (say != null) {
      taskService.complete(say.getId(), Map.of("name", name));
      return "success";
    } else {
      return "over";
    }
  }

  @GetMapping("/result/{id}")
  public Object result(@PathVariable String id) {
    Execution execution =
        runtimeService.createExecutionQuery().processInstanceId(id).singleResult();
    if (execution != null) {
      return runtimeService.getVariables(execution.getId()).get("answers");
    } else {
      return historyService
          .createHistoricVariableInstanceQuery()
          .processInstanceId(id)
          .variableName("answers")
          .singleResult()
          .getValue();
    }
  }
}
