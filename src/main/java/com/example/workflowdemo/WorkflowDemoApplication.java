package com.example.workflowdemo;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WorkflowDemoApplication {

    private RuntimeService runtimeService;

    public static void main(String[] args) {
        SpringApplication.run(WorkflowDemoApplication.class, args);
    }

    @Bean
    CommandLineRunner init(
            final ProcessEngine processEngine
    ) {

        return new CommandLineRunner() {

            public void run(String... strings) throws Exception {

                String pName = processEngine.getName();
                String ver = ProcessEngine.VERSION;
                System.out.println("ProcessEngine [" + pName + "] Version: [" + ver + "]");

                runtimeService = processEngine.getRuntimeService();
                //final TaskService taskService

//                Map<String, Object> variables = new HashMap<String, Object>();
//                variables.put("applicantName", "John Doe");
//                variables.put("email", "john.doe@activiti.com");
//                variables.put("phoneNumber", "123456789");
//                runtimeService.startProcessInstanceByKey("onboarding", variables);
                RepositoryService repositoryService = processEngine.getRepositoryService();
                Deployment deployment = repositoryService.createDeployment()
                        .addClasspathResource("processes/onboarding.bpmn20.xml").deploy();

                ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                        .deploymentId(deployment.getId()).singleResult();

                System.out.println(
                        "Found process definition ["
                        + processDefinition.getName() + "] with id ["
                        + processDefinition.getId() + "]");
//                ProcessInstance pr = runtimeService.startProcessInstanceById("onboarding");
//                System.out.println(
//                        "Started process definition ["
//                        + pr.getName() + "] with id ["
//                        + pr.getId() + "]");
            }
        };
    }

//    @Bean
//    public CommandLineRunner run() throws Exception {
//        return args -> {
//            ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
//                    .setJdbcUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=1000")
//                    .setJdbcUsername("sa")
//                    .setJdbcPassword("password")
//                    .setJdbcDriver("org.h2.Driver")
//                    .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
//            ProcessEngine processEngine = cfg.buildProcessEngine();
//            String pName = processEngine.getName();
//            String ver = ProcessEngine.VERSION;
//            System.out.println("ProcessEngine [" + pName + "] Version: [" + ver + "]");
//
//            RepositoryService repositoryService = processEngine.getRepositoryService();
//            Deployment deployment = repositoryService.createDeployment()
//                    .addClasspathResource("processes/onboarding.bpmn20.xml").deploy();
//            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
//                    .deploymentId(deployment.getId()).singleResult();
//            System.out.println(
//                    "Found process definition ["
//                    + processDefinition.getName() + "] with id ["
//                    + processDefinition.getId() + "]");
//        };
//    }
}
