package com.laotabu.activiti.service.impl;

import com.laotabu.common.utils.file.FileUploadUtils;
import com.github.pagehelper.Page;
import com.laotabu.activiti.domain.dto.DefinitionIdDTO;
import com.laotabu.activiti.domain.vo.ActReDeploymentVO;
import com.laotabu.activiti.domain.dto.ProcessDefinitionDTO;
import com.laotabu.activiti.mapper.ActReDeploymentMapper;
import com.laotabu.activiti.service.IProcessDefinitionService;
import com.laotabu.common.config.LaotabuConfig;
import com.laotabu.common.core.page.PageDomain;
import com.laotabu.common.utils.StringUtils;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

/**
 * laotabu
 * 创建日期:2023/11/7
 * 版本   开发者     日期
 * 1.0    lsd    2023/11/7
 */

@Service
public class ProcessDefinitionServiceImpl implements IProcessDefinitionService {
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ActReDeploymentMapper actReDeploymentMapper;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private RuntimeService runtimeService;

    /**
     * 流程实例挂起与激活状态
     */
    public final static int SUSPEND_STATE = 1;
    public final static int ACTIVE_STATE = 2;


    @Override
    public Page<ProcessDefinitionDTO> selectProcessDefinitionList(ProcessDefinitionDTO processDefinition, PageDomain pageDomain) {
        Page<ProcessDefinitionDTO> list = new Page<>();
        // 创建查询流程定义的变量
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionId().orderByProcessDefinitionVersion().desc();
        // 模糊查询，根据流程定义的名字，key
        if (StringUtils.isNotBlank(processDefinition.getName())) {
            processDefinitionQuery.processDefinitionNameLike("%" + processDefinition.getName() + "%");
        }
        if (StringUtils.isNotBlank(processDefinition.getKey())) {
            processDefinitionQuery.processDefinitionKeyLike("%" + processDefinition.getKey() + "%");
        }
        List<ProcessDefinition> processDefinitions = processDefinitionQuery.listPage((pageDomain.getPageNum() - 1) * pageDomain.getPageSize(), pageDomain.getPageSize());
        long count = processDefinitionQuery.count();
        list.setTotal(count);
        if (count!=0) {
            /**
             * 收集已经部署的流程的id
             */
            Set<String> ids = processDefinitions.parallelStream().map(pdl -> pdl.getDeploymentId()).collect(Collectors.toSet());
            /**
             * 这里就是去获取已经部署的流程的时间
             */
            List<ActReDeploymentVO> actReDeploymentVOS = actReDeploymentMapper.selectActReDeploymentByIds(ids);

            List<ProcessDefinitionDTO> processDefinitionDTOS = processDefinitions.stream()
                    .map(pd -> new ProcessDefinitionDTO((ProcessDefinitionEntityImpl) pd,
                            actReDeploymentVOS.parallelStream().filter(ard -> pd.getDeploymentId().equals(ard.getId())).findAny().orElse(new ActReDeploymentVO())))
                    .collect(Collectors.toList());
            list.addAll(processDefinitionDTOS);
        }
        return list;
    }

    @Override
    public DefinitionIdDTO getDefinitionsByInstanceId(String instanceId) {
        /**
         * 根据流程实例id去查询流程实例【操作的是act_ru_execution,act_re_procdef 这两个表】
         * 返回流程实例对象
         *
         * <sql id="selectProcessInstanceWithVariablesByQueryCriteriaSql">
         *     from ${prefix}ACT_RU_EXECUTION RES
         *     inner join ${prefix}ACT_RE_PROCDEF P on RES.PROC_DEF_ID_ = P.ID_
         *     left outer join ${prefix}ACT_RU_EXECUTION S on RES.SUPER_EXEC_ = S.ID_
         *     <if test="includeProcessVariables">
         *       left outer join ${prefix}ACT_RU_VARIABLE VAR ON RES.PROC_INST_ID_ = VAR.EXECUTION_ID_ and VAR.TASK_ID_ is null
         *     </if>
         *     <include refid="commonSelectExecutionsByQueryCriteriaSql"/>
         *   </sql>
         *
         */
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
        String deploymentId = processInstance.getDeploymentId();
        /**
         * 根据deploymentId获取流程定义信息
         */
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
        /**
         * 只封装想要的字段
         */
        return new DefinitionIdDTO(processDefinition);
    }

    @Override
    public int deleteProcessDefinitionById(String id) {
        repositoryService.deleteDeployment(id, false);
        return 1;
    }

    @Override
    public void uploadStreamAndDeployment(MultipartFile file) throws IOException {
        // 获取上传的文件名
        String fileName = file.getOriginalFilename();
        // 得到输入流（字节流）对象
        InputStream fileInputStream = file.getInputStream();
        // 文件的扩展名
        String extension = FilenameUtils.getExtension(fileName);

        if (extension.equals("zip")) {
            ZipInputStream zip = new ZipInputStream(fileInputStream);
            repositoryService.createDeployment()//初始化流程
                    .addZipInputStream(zip)
                    .deploy();
        } else {
            repositoryService.createDeployment()//初始化流程
                    .addInputStream(fileName, fileInputStream)

                    .deploy();
        }
    }

    @Override
    public void suspendOrActiveApply(String id, Integer state) {
        switch (state.intValue()){
            case SUSPEND_STATE:
                // 当流程定义被挂起时，已经发起的该流程定义的流程实例不受影响
                // （如果选择级联挂起则流程实例也会被挂起）。
                // 当流程定义被挂起时，无法发起新的该流程定义的流程实例。
                // 直观变化：act_re_procdef 的 SUSPENSION_STATE_ 为 2
                repositoryService.suspendProcessDefinitionById(id);
                break;
            case ACTIVE_STATE:
                repositoryService.activateProcessDefinitionById(id);
                break;
        }
    }

    @Override
    public String upload(MultipartFile multipartFile) throws IOException {
       return FileUploadUtils.upload(LaotabuConfig.getUploadPath()+"/processDefinition" , multipartFile);
    }

    @Override
    public void addDeploymentByString(String stringBPMN) {
        repositoryService.createDeployment()
                .addString("CreateWithBPMNJS.bpmn", stringBPMN)
                .deploy();
    }

    @Override
    public void getProcessDefineXML(HttpServletResponse response, String deploymentId, String resourceName) throws IOException {
        InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
        int count = inputStream.available();
        byte[] bytes = new byte[count];
        response.setContentType("text/xml");
        OutputStream outputStream = response.getOutputStream();
        while (inputStream.read(bytes) != -1) {
            outputStream.write(bytes);
        }
        inputStream.close();
    }

    @Override
    public DefinitionIdDTO getDefinitionsByBusinesskey(String businesskey) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceBusinessKey(businesskey)
                .singleResult();
        String deploymentId = processInstance.getDeploymentId();
        /**
         * 根据deploymentId获取流程定义信息
         */
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
        /**
         * 只封装想要的字段
         */
        return new DefinitionIdDTO(processDefinition);
    }
}
