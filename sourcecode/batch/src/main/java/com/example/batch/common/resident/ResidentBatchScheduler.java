package com.example.batch.common.resident;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定期的にResidentBatchProcessorを呼び出すクラス。
 * 
 * アプリケーション終了の実装はShutdownEndpointを参考にしている。
 *
 * @see org.springframework.boot.actuate.context.ShutdownEndpoint
 *
 */
@Component
@ConditionalOnProperty(name = "resident-batch.enabled", havingValue = "true")
@EnableScheduling
public class ResidentBatchScheduler implements InitializingBean, ApplicationContextAware {

    @Autowired
    private ResidentBatchProcessor residentBatchProcessor;

    private ConfigurableApplicationContext context;

    private final AtomicBoolean shuttingDown = new AtomicBoolean();

    @Override
    public void afterPropertiesSet() throws Exception {
        residentBatchProcessor.initialize();
    }

    /**
     * 定期処理を実行する。
     * 
     */
    @Scheduled(initialDelayString = "${resident-batch.initial-delay:0}",
            fixedDelayString = "${resident-batch.run-interval:10000}")
    public void schedule() {
        if (shuttingDown.get()) {
            return;
        }
        boolean toBeContinue = residentBatchProcessor.process();
        if (toBeContinue == false) {
            shuttingDown.set(true);
            Thread thread = new Thread(this::performShutdown);
            thread.setContextClassLoader(getClass().getClassLoader());
            thread.start();
        }
    }

    /**
     * アプリケーションをシャットダウンする。
     * 
     */
    private void performShutdown() {
        try {
            Thread.sleep(500L);
        } catch (@SuppressWarnings("unused") InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        this.context.close();
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        if (context instanceof ConfigurableApplicationContext) {
            this.context = (ConfigurableApplicationContext) context;
        }
    }
}
