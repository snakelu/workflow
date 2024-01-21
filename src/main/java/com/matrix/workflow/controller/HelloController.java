package com.matrix.workflow.controller;

import com.matrix.workflow.util.BpmnGenerator;
import javax.annotation.Resource;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

  @Resource RepositoryService repositoryService;

  @Resource RuntimeService runtimeService;

  @Resource TaskService taskService;

  @Resource HistoryService historyService;

  @GetMapping("/hello")
  public String hello() {
    repositoryService
        .createDeployment()
        .addBpmnModel("test", BpmnGenerator.generateModel())
        .deploy();
    return "";
  }
}
