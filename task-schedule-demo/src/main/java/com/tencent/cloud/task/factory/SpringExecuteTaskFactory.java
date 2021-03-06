package com.tencent.cloud.task.factory;

import com.tencent.cloud.task.worker.DefaultTaskFactory;
import com.tencent.cloud.task.worker.exception.InstancingException;
import com.tencent.cloud.task.worker.model.ExecutableTaskData;
import com.tencent.cloud.task.worker.spi.ExecutableTask;
import com.tencent.cloud.task.worker.spi.ExecutableTaskFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class SpringExecuteTaskFactory implements ExecutableTaskFactory, ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private ApplicationContext applicationContext;

    private final ExecutableTaskFactory defaultFactory = new DefaultTaskFactory(Thread.currentThread().getContextClassLoader());

    @Override
    public ExecutableTask newExecutableTask(ExecutableTaskData executableTaskData) throws InstancingException {
        try {
            ExecutableTask executableTask = applicationContext.getBean(executableTaskData.getTaskContent(),ExecutableTask.class);
            LOG.info("generate executableTask bean SpringExecutableTaskFactory. taskName: {}", executableTaskData.getTaskContent());
            return executableTask;
        } catch (Throwable t) {
            return defaultFactory.newExecutableTask(executableTaskData);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
