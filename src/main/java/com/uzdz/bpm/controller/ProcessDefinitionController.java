//package com.uzdz.bpm.controller;
//
//import com.cjs.example.domain.RespResult;
//import com.cjs.example.util.ResultUtils;
//import org.activiti.api.process.model.ProcessDefinition;
//import org.activiti.api.process.runtime.ProcessAdminRuntime;
//import org.activiti.api.process.runtime.ProcessRuntime;
//import org.activiti.api.runtime.shared.query.Page;
//import org.activiti.api.runtime.shared.query.Pageable;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@RestController
//@RequestMapping("/processDefinition")
//public class ProcessDefinitionController {
//
//    @Autowired
//    private ProcessAdminRuntime processAdminRuntime;
//
//    @GetMapping("/list")
//    public RespResult<Page<ProcessDefinition>> getProcessDefinition(){
//        Page<ProcessDefinition> processDefinitionPage = processAdminRuntime.processDefinitions(Pageable.of(0, 10));
//        return ResultUtils.success(processDefinitionPage);
//    }
//}