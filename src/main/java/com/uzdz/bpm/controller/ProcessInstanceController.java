//package com.uzdz.bpm.controller;
//
//import com.cjs.example.domain.RespResult;
//import com.cjs.example.util.ResultUtils;
//import org.activiti.api.process.model.ProcessInstance;
//import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
//import org.activiti.api.process.runtime.ProcessRuntime;
//import org.activiti.engine.RuntimeService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @Author ChengJianSheng
// * @Date 2021/7/12
// */
//@RestController
//@RequestMapping("/processInstance")
//public class ProcessInstanceController {
//    @Autowired
//    private ProcessRuntime processRuntime;
//
//    @Autowired
//    private RuntimeService runtimeService;
//
//    @GetMapping("/start")
//    public RespResult start(@RequestParam("processDefinitionId") String processDefinitionId) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        org.activiti.engine.runtime.ProcessInstance processInstance = null;
//        try {
////            ProcessInstance processInstance = processRuntime.start(ProcessPayloadBuilder
////                    .start()
////                    .withProcessDefinitionId(processDefinitionId)
////                    .withVariable("sponsor", authentication.getName())
////                    .build());
//
//            Map<String, Object> variables = new HashMap<>();
//            variables.put("sponsor", authentication.getName());
//            processInstance = runtimeService.startProcessInstanceById(processDefinitionId, variables);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return ResultUtils.success(processInstance);
//    }
//}