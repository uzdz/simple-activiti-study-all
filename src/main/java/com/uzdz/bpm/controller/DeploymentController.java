package com.uzdz.bpm.controller;

import com.uzdz.bpm.base.Response;
import com.uzdz.bpm.base.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

@Slf4j
@RestController
@RequestMapping("/deploy")
public class DeploymentController {

    @Autowired
    private RepositoryService repositoryService;

    /**
     * 部署
     * @param file  ZIP压缩包文件
     * @param processName   流程名称
     * @return
     */
    @PostMapping("/upload")
    public Response upload(@RequestParam("zipFile") MultipartFile file, @RequestParam("processName") String processName) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && !originalFilename.endsWith("zip")) {
            throw new BizException("文件格式错误");
        }

        try {
            ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream());
            repositoryService.createDeployment().addZipInputStream(zipInputStream).name(processName).deploy();
            return Response.ok();
        } catch (IOException e) {
            throw new BizException("流程部署失败！原因: " + e.getMessage());
        }
    }

    /**
     * 查看流程图
     * @param deploymentId  部署ID
     * @param response
     * @return
     */
    @GetMapping("/getDiagram")
    public void getDiagram(@RequestParam("deploymentId") String deploymentId, HttpServletResponse response) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
        InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, processDefinition.getResourceName());
        try {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            IOUtils.copy(inputStream, response.getOutputStream());
        } catch (IOException e) {
            throw new BizException("流程图查看失败！原因: " + e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
}