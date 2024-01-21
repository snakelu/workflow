package com.matrix.workflow.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

@Service
public class ActivitiService {

  @Resource RepositoryService repositoryService;

  @Resource RuntimeService runtimeService;

  @Resource TaskService taskService;

  @Resource HistoryService historyService;

  /** 部署流程服务 */
  public String deployProcess(BpmnModel model) {
    Deployment deployment =
        repositoryService.createDeployment().addBpmnModel("hello", model).name("hello").deploy();
    return deployment.getKey();
  }

  /**
   * 查询所有部署的流程定义
   *
   * @return
   */
  public List<ProcessDefinition> getAllProcessDefinition() {
    ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
    return query.orderByProcessDefinitionVersion().desc().list();
  }

  /**
   * 查询所有的部署
   *
   * @return
   */
  public List<Deployment> getAllDeployment() {
    DeploymentQuery query = repositoryService.createDeploymentQuery();
    return query.list();
  }

  /**
   * 查询所有流程实例
   *
   * @return
   */
  public List<ProcessInstance> getAllProcessInstance() {
    List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().list();
    return list;
  }

  public List<Task> getAllProcessTask() {
    List<Task> list = taskService.createTaskQuery().list();
    return list;
  }

  /**
   * 查询用户待完成和待认领的任务
   *
   * @param username
   * @return
   */
  public List<Task> getAllProcessTaskByCandidateOrAssigned(String username) {
    List<Task> list = taskService.createTaskQuery().taskCandidateOrAssigned(username).list();
    return list;
  }

  public List<Task> getAllProcessTaskByCandidate(String username) {
    List<Task> list = taskService.createTaskQuery().taskCandidateUser(username).list();
    return list;
  }

  /**
   * 查询用户的待完成任务
   *
   * @param username
   * @return
   */
  public List<Task> getAllProcessTaskByAssigned(String username) {
    List<Task> list = taskService.createTaskQuery().taskAssignee(username).list();
    return list;
  }

  public void complateTask(Task task, Map<String, Object> map) {
    taskService.complete(task.getId(), map);
  }

  public void complateTask(Task task) {
    taskService.complete(task.getId());
  }

  public void claimTask(Task task, String username) {
    taskService.claim(task.getId(), username);
  }

  public ProcessInstance startProcess(String processDefinitionKey, Map<String, Object> map) {
    return runtimeService.startProcessInstanceById(processDefinitionKey, map);
  }

  public String getProcessDefinitionXml(String processDefinitionId) {
    ProcessDefinition processDefinition =
        repositoryService.getProcessDefinition(processDefinitionId);
    String resourceName = processDefinition.getResourceName();
    InputStream resourceAsStream =
        repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
    try {
      return IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);
    } catch (IOException e) {
      // handle exception
    }
    return null;
  }

  public List<HistoricProcessInstance> getHistoryByBusinessKey(String businessKey) {
    List<HistoricProcessInstance> list =
        historyService
            .createHistoricProcessInstanceQuery()
            .processInstanceBusinessKey(businessKey)
            .list();
    return list;
  }
}
