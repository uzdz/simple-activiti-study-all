package com.uzdz.bpm.activiti;


import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestForActivitiBusiness {


    @Test
    public void queryProcessInstance() {
        // 流程定义key
        String processDefinitionKey = "evection";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取RunTimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        List<ProcessInstance> list = runtimeService
                .createProcessInstanceQuery()
                .processDefinitionKey(processDefinitionKey)
                .list();

        for (ProcessInstance processInstance : list) {
            System.out.println("----------------------------");
            System.out.println("流程实例id："
                    + processInstance.getProcessInstanceId());
            System.out.println("所属流程定义id："
                    + processInstance.getProcessDefinitionId());
            System.out.println("是否执行完成：" + processInstance.isEnded());
            System.out.println("是否暂停：" + processInstance.isSuspended());
            System.out.println("当前活动标识：" + processInstance.getActivityId());
        }
    }


    /**
     * 全部流程实例挂起与激活
     */
    @Test
    public void SuspendAllProcessInstance(){
//        获取processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        获取repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
//        查询流程定义的对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().
                processDefinitionKey("myEvection").
                singleResult();
//        得到当前流程定义的实例是否都为暂停状态
        boolean suspended = processDefinition.isSuspended();
//        流程定义id
        String processDefinitionId = processDefinition.getId();
//        判断是否为暂停
        if(suspended){
//         如果是暂停，可以执行激活操作 ,参数1 ：流程定义id ，参数2：是否激活，参数3：激活时间
            repositoryService.activateProcessDefinitionById(processDefinitionId,
                    true,
                    null
            );
            System.out.println("流程定义："+processDefinitionId+",已激活");
        }else{
//          如果是激活状态，可以暂停，参数1 ：流程定义id ，参数2：是否暂停，参数3：暂停时间
            repositoryService.suspendProcessDefinitionById(processDefinitionId,
                    true,
                    null);
            System.out.println("流程定义："+processDefinitionId+",已挂起");
        }

    }

    /**
     * 单个流程实例挂起与激活
     */
    @Test
    public void SuspendSingleProcessInstance(){
//        获取processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
//        查询流程定义的对象
        ProcessInstance processInstance = runtimeService.
                createProcessInstanceQuery().
                processInstanceId("15001").
                singleResult();
//        得到当前流程定义的实例是否都为暂停状态
        boolean suspended = processInstance.isSuspended();
//        流程定义id
        String processDefinitionId = processInstance.getId();
//        判断是否为暂停
        if(suspended){
//         如果是暂停，可以执行激活操作 ,参数：流程定义id
            runtimeService.activateProcessInstanceById(processDefinitionId);
            System.out.println("流程定义："+processDefinitionId+",已激活");
        }else{
//          如果是激活状态，可以暂停，参数：流程定义id
            runtimeService.suspendProcessInstanceById( processDefinitionId);
            System.out.println("流程定义："+processDefinitionId+",已挂起");
        }

    }

    // 查询当前个人待执行的任务
    @Test
    public void findPersonalTaskList() {
        // 流程定义key
        String processDefinitionKey = "myEvection1";
        // 任务负责人
        String assignee = "张三";
        //        获取processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取TaskService
        TaskService taskService = processEngine.getTaskService();
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey(processDefinitionKey)
                .includeProcessVariables()
                .taskAssignee(assignee)
                .list();
        for (Task task : taskList) {
            System.out.println("----------------------------");
            System.out.println("流程实例id： " + task.getProcessInstanceId());
            System.out.println("任务id： " + task.getId());
            System.out.println("任务负责人： " + task.getAssignee());
            System.out.println("任务名称： " + task.getName());
            System.out.println("任务名称： " + task.getProcessVariables());
        }
    }


}
