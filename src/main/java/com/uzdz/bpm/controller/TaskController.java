//package com.uzdz.bpm.controller;
//
//import org.activiti.api.runtime.shared.query.Page;
//import org.activiti.api.runtime.shared.query.Pageable;
//import org.activiti.api.task.model.Task;
//import org.activiti.api.task.model.builders.TaskPayloadBuilder;
//import org.activiti.api.task.runtime.TaskRuntime;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * @Author ChengJianSheng
// * @Date 2021/7/12
// */
//@RestController
//@RequestMapping("/task")
//public class TaskController {
//
//    @Autowired
//    private TaskRuntime taskRuntime;
//
//    @GetMapping("/pageList")
//    public void pageList() {
//        //  查询待办任务（个人任务 + 组任务）
//        Page<Task> page = taskRuntime.tasks(Pageable.of(0, 10));
//
//        if (null != page && page.getTotalItems() > 0) {
//            for (Task task : page.getContent()) {
//                //  认领任务
//                taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
//                //  完成任务
//                taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId()).build());
//            }
//        }
//    }
//}