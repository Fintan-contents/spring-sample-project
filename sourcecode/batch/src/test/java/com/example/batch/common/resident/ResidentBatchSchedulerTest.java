package com.example.batch.common.resident;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mock.env.MockEnvironment;

/**
 * {@link ResidentBatchScheduler}のテスト。
 *
 */
class ResidentBatchSchedulerTest {

    /**
     * {@link ResidentBatchScheduler}は終了時にアプリケーションコンテキストをcloseするため、
     * SpringBootTestアノテーションを使用せず、テストメソッド内でアプリケーションコンテキストを構築している。
     * 
     * @throws Exception
     */
    @Test
    void testSchedule() throws Exception {

        ResidentBatchProcessor residentBatchProcessorMock = mock(ResidentBatchProcessor.class);
        doNothing().when(residentBatchProcessorMock).initialize();
        when(residentBatchProcessorMock.process()).thenReturn(true, true, false);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        MockEnvironment environment = new MockEnvironment();
        // 定期実行を有効化
        environment.setProperty("resident-batch.enabled", "true");
        // ResidentBatchSchedulerが登録されていることを確認する時間を確保
        environment.setProperty("resident-batch.initial-delay", "100");
        // 定期実行のインターバルは1ミリ秒へ設定
        environment.setProperty("resident-batch.run-interval", "1");

        context.setEnvironment(environment);
        context.registerBean(ResidentBatchProcessor.class, () -> residentBatchProcessorMock);
        context.register(ResidentBatchScheduler.class);
        context.refresh();

        ResidentBatchScheduler bean = context.getBean(ResidentBatchScheduler.class);
        assertNotNull(bean);

        // 以下を考慮して少しsleepする
        // resident-batch.initial-delayが100ミリ秒
        // ResidentBatchScheduler#performShutdownが500ミリ秒
        // 処理自体の実行時間と定期処理間のインターバル(1つ1つはほぼ無視できるレベル)
        TimeUnit.MILLISECONDS.sleep(700);

        assertFalse(context.isActive());

        verify(residentBatchProcessorMock, times(3)).process();
    }
}
