package com.example.batch.common.listener;

import java.util.List;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

import com.example.batch.common.mapper.BatchCommonMapper;

/**
 * ステップの最初に指定されたテーブルをTRUNCATEするリスナー。
 */
public class TruncateTableListener implements StepExecutionListener {
    private final BatchCommonMapper batchCommonMapper;
    private final List<String> tableNameList;

    /**
     * コンストラクタ。
     * @param batchCommonMapper バッチ共通のMapper
     * @param tableNames TRUNCATE対象のテーブル
     */
    public TruncateTableListener(BatchCommonMapper batchCommonMapper, String... tableNames) {
        this.batchCommonMapper = batchCommonMapper;
        this.tableNameList = List.of(tableNames);
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        tableNameList.forEach(batchCommonMapper::truncateTable);
    }
}
