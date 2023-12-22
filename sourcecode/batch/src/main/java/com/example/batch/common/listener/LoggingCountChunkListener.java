package com.example.batch.common.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.ChunkListenerSupport;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

/**
 * 入力件数とスキップ件数、コミット回数の中間状態をログに出力するリスナー。
 * 
 * @author sample
 *
 */
@Component
public class LoggingCountChunkListener extends ChunkListenerSupport {

    private final Logger logger = LoggerFactory.getLogger(LoggingCountChunkListener.class);

    @Override
    public void afterChunk(ChunkContext context) {
        StepExecution stepExecution = context.getStepContext().getStepExecution();
        long inputCount = stepExecution.getReadCount() + stepExecution.getReadSkipCount();
        long skipCount = stepExecution.getSkipCount();
        long commitCount = stepExecution.getCommitCount();
        logger.info("入力件数={}, スキップ件数={}, コミット回数={}", inputCount, skipCount, commitCount);
    }
}
