package com.uzdz.bpm.activiti;


import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class TestForActiviti {

    @Test
    public void create() {
        //使用classpath下的activiti.cfg.xml中的配置创建processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println(processEngine);

        RuntimeService runtimeService = processEngine.getRuntimeService();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        TaskService taskService = processEngine.getTaskService();

        Execution execution = runtimeService.createExecutionQuery()
                .messageEventSubscriptionName("Message_1hss4ii")
                .processInstanceId("xxx")
                .singleResult();

        runtimeService.messageEventReceived("Message_1hss4ii", execution.getId());
    }

    /**
     * 部署流程定义
     */
    @Test
    public void testDeployment(){
        // 1、创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、得到RepositoryService实例
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3、使用RepositoryService进行部署
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("bpmn/evection.bpmn") // 添加bpmn资源
                .addClasspathResource("bpmn/evection.png")  // 添加png资源
                .name("出差申请流程")
                .deploy();
        // 4、输出部署信息
        System.out.println("流程部署id：" + deployment.getId());
        System.out.println("流程部署名称：" + deployment.getName());
    }

    /**
     * 部署流程删除
     */
    @Test
    public void deleteDeployment() {
        // 流程部署id
        String deploymentId = "1";

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 通过流程引擎获取repositoryService
        RepositoryService repositoryService = processEngine
                .getRepositoryService();
        //删除流程定义，如果该流程定义已有流程实例启动则删除时出错
        //repositoryService.deleteDeployment(deploymentId);
        //设置true 级联删除流程定义，即使该流程有流程实例启动也可以删除，设置为false非级别删除方式，如果流程
        repositoryService.deleteDeployment(deploymentId, true);
    }

    @Test
    public void varLocal() {

        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        TaskService taskService = processEngine.getTaskService();
        Task jingli = taskService.createTaskQuery().taskAssignee("jingli").singleResult();
taskService.setVariableLocal(jingli.getId(), "vName", "uzdz");
        System.out.println(jingli.getName() + "aaa");
    }

    /**
     * 启动流程实例
     */
    @Test
    public void testStartProcess(){
        // 1、创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取RunTimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();

        Map<String,Object> assigneeMap = new HashMap<>();
        assigneeMap.put("assignee0","张三");
        assigneeMap.put("assignee1","李经理");
        assigneeMap.put("assignee2","王总经理");
        assigneeMap.put("assignee3","赵财务");

        // 3、根据流程定义Id启动流程
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("evection", "1001", assigneeMap);


        // 输出内容
        System.out.println("流程定义id：" + processInstance.getProcessDefinitionId());
        System.out.println("流程实例id：" + processInstance.getId());
        System.out.println("当前活动Id：" + processInstance.getActivityId());
    }

    /**
     * 查询当前个人待执行的任务
     */
    @Test
    public void testFindPersonalTaskList() {
        // 任务负责人
        String assignee = "dongzhen";
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 创建TaskService
        TaskService taskService = processEngine.getTaskService();
        // 根据流程key 和 任务负责人 查询任务
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey("evection") //流程Key
                .taskAssignee(assignee)//只查询该任务负责人的任务
                .list();

        for (Task task : list) {

            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());

        }
    }

    // 完成任务
    @Test
    public void completTask(){
        // 获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取taskService
        TaskService taskService = processEngine.getTaskService();

        // 根据流程key 和 任务的负责人 查询任务
        // 返回一个任务对象
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("evection") //流程Key
                .taskAssignee("jingli")  //要查询的负责人
                .singleResult();

        // 完成任务,参数：任务id
        taskService.complete("10005");
    }

    /**
     * 查询流程定义
     */
    @Test
    public void queryProcessDefinition(){
        // 获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // repositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 得到ProcessDefinitionQuery 对象
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        // 查询出当前所有的流程定义
        // 条件：processDefinitionKey =evection
        // orderByProcessDefinitionVersion 按照版本排序
        // desc倒叙
        // list 返回集合
        List<ProcessDefinition> definitionList = processDefinitionQuery.processDefinitionKey("evection")
                .orderByProcessDefinitionVersion()
                .desc()
                .list();
        // 输出流程定义信息
        for (ProcessDefinition processDefinition : definitionList) {
            System.out.println("流程定义 id="+processDefinition.getId());
            System.out.println("流程定义 name="+processDefinition.getName());
            System.out.println("流程定义 key="+processDefinition.getKey());
            System.out.println("流程定义 Version="+processDefinition.getVersion());
            System.out.println("流程部署ID ="+processDefinition.getDeploymentId());
        }
    }

    /**
     * 查看历史信息
     */
    @Test
    public void findHistoryInfo(){
        // 获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 获取HistoryService
        HistoryService historyService = processEngine.getHistoryService();
        // 获取 actinst表的查询对象
        HistoricActivityInstanceQuery instanceQuery = historyService.createHistoricActivityInstanceQuery();
        // 查询 actinst表，条件：根据 InstanceId 查询
        // instanceQuery.processInstanceId("2501");
        // 查询 actinst表，条件：根据 DefinitionId 查询
        instanceQuery.processDefinitionId("evection:1:7504");
        // 增加排序操作,orderByHistoricActivityInstanceStartTime 根据开始时间排序 asc 升序
        instanceQuery.orderByHistoricActivityInstanceStartTime().asc();
        // 查询所有内容
        List<HistoricActivityInstance> activityInstanceList = instanceQuery.list();
        // 输出
        for (HistoricActivityInstance hi : activityInstanceList) {
            System.out.println("activitiId" + hi.getActivityId());
            System.out.println(hi.getActivityName());
            System.out.println(hi.getProcessDefinitionId());
            System.out.println(hi.getProcessInstanceId());
            System.out.println("<==========================>");
        }
    }

    @Test
    public void getImg() throws IOException {

        String instanceId= "10001";
        // 获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        /*
         * 参数校验
         */
        System.out.println("查看完整流程图！流程实例ID:"+"10001");
        if(StringUtils.isBlank(instanceId)) return;

        HistoryService historyService = processEngine.getHistoryService();
        /*
         *  获取流程实例
         */
        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(instanceId).singleResult();
        if(processInstance == null) {
            System.out.println("流程实例ID:"+instanceId+"没查询到流程实例！");
            return;
        }

        RepositoryService repositoryService = processEngine.getRepositoryService();

        // 根据流程对象获取流程对象模型
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());


        /*
         *  查看已执行的节点集合
         *  获取流程历史中已执行节点，并按照节点在流程中执行先后顺序排序
         */
        // 构造历史流程查询
        HistoricActivityInstanceQuery historyInstanceQuery = historyService.createHistoricActivityInstanceQuery().processInstanceId(instanceId);
        // 查询历史节点
        List<HistoricActivityInstance> historicActivityInstanceList = historyInstanceQuery.orderByHistoricActivityInstanceStartTime().asc().list();
        if(historicActivityInstanceList == null || historicActivityInstanceList.size() == 0) {
            System.out.println("流程实例ID:"+instanceId+"没有历史节点信息！");
            return;
        }
        // 已执行的节点ID集合(将historicActivityInstanceList中元素的activityId字段取出封装到executedActivityIdList)
        List<String> executedActivityIdList = historicActivityInstanceList.stream().map(item -> item.getActivityId()).collect(Collectors.toList());

        /*
         *  获取流程走过的线
         */
        // 获取流程定义
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(processInstance.getProcessDefinitionId());
        List<String> flowIds = ActivitiUtils.getHighLightedFlows(bpmnModel, processDefinition, historicActivityInstanceList);


        /*
         * 输出图像，并设置高亮
         */
        InputStream inputStream = outputImg(bpmnModel, flowIds, executedActivityIdList);

//        InputStream in = this.getProcessDiagram("10001");
        String imageName = "/Users/uzdz/bpmn/image" + Instant.now().getEpochSecond()+".svg";
        writeToLocal(imageName, inputStream);
    }

    /**
     * <p>输出图像</p>
     * @param bpmnModel 图像对象
     * @param flowIds 已执行的线集合
     * @param executedActivityIdList void 已执行的节点ID集合
     * @author 悟纤【公众号SpringBoot】
     */
    private InputStream outputImg( BpmnModel bpmnModel, List<String> flowIds, List<String> executedActivityIdList) {
        InputStream imageStream = null;
        ProcessDiagramGenerator processDiagramGenerator = new DefaultProcessDiagramGenerator();
        try {
            return processDiagramGenerator.generateDiagram(bpmnModel, executedActivityIdList, flowIds, "宋体", "微软雅黑", "黑体", true, "png");
        }catch(Exception e) {
            e.printStackTrace();
        } finally { // 流关闭
            if(imageStream != null){
                try {
                    imageStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 将InputStream写入本地文件
     * @param destination 写入本地目录
     * @param input	输入流
     * @throws IOException
     */
    private static void writeToLocal(String destination, InputStream input)
            throws IOException {
        int index;
        byte[] bytes = new byte[1024];
        FileOutputStream downloadFile = new FileOutputStream(destination);
        while ((index = input.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
        }
        downloadFile.close();
        input.close();
    }
}
