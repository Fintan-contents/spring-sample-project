package com.example.batch.project.configuration;

import java.util.Map;

import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.PathResource;
import org.springframework.format.support.DefaultFormattingConversionService;

import com.example.batch.common.configuration.BatchBaseConfig;
import com.example.batch.common.exception.BatchSkipItemException;
import com.example.batch.common.listener.TruncateTableListener;
import com.example.batch.common.reader.LineNumberMapper;
import com.example.batch.project.item.ImportProjectsToWorkItem;
import com.example.batch.project.mapper.ImportProjectsToWorkMapper;
import com.example.batch.project.processor.ImportProjectsToWorkItemProcessor;
import com.example.common.generated.model.ProjectWork;

/**
 * プロジェクト一括登録バッチ/ワークテーブル登録のConfig。
 */
@Configuration
@PropertySource(value = {
        "classpath:/properties/project/ImportProjectsToWork.properties",
        "classpath:/properties/project/ImportProjectsToWork-${spring.profiles.active}.properties",
}, encoding = "UTF-8", ignoreResourceNotFound = true)
public class ImportProjectsToWorkConfig extends BatchBaseConfig {
    @Autowired
    private BeanValidatingItemProcessor<ImportProjectsToWorkItem> beanValidatingItemProcessor;
    @Autowired
    private ImportProjectsToWorkItemProcessor importProjectsToWorkItemProcessor;

    /**
     * プロジェクト一括登録バッチ/ワークテーブル登録のPropertiesを構築する。
     * 
     * @return 構築されたインスタンス
     */
    @Bean
    @ConfigurationProperties(prefix = "project.import-projects-to-work")
    public ImportProjectsToWorkProperties importProjectsToWorkProperties() {
        return new ImportProjectsToWorkProperties();
    }

    /**
     * プロジェクト一括登録バッチ/ワークテーブル登録のItemReaderを構築する。
     * 
     * @return 構築されたインスタンス
     */
    @Bean
    @StepScope
    public FlatFileItemReader<ImportProjectsToWorkItem> importProjectsToWorkItemReader() {
        /*
         * ■FlatFileItemReaderBuilder を使用していない理由
         * LineMapper をカスタムの実装に置き換えようとすると、
         * Builder がほとんど利用できなくなるため。
         *
         * カスタムの LineMapper では、変換処理はオリジナルの LineMapper に委譲し、
         * 変換後の Item に対して行番号を設定する処理を行っている。
         * これは、バリデーションエラー時に原因となった入力ファイルの行を特定するために行っている。
         *
         * オリジナルの LineMapper は、 FlatFileItemReaderBuilder の内部で構築されるが、
         * このインスタンスを外部から取得する方法が存在しない。
         * このため、オリジナルの LineMapper は FlatFileItemReaderBuilder が内部で実施している
         * ことを再現する形で手動で構築する必要がある。
         *
         * このため、 FlatFileItemReaderBuilder がほとんど利用できなくなるため、
         * 初めから FlatFileItemReaderBuilder は使用せずに直接構築していく方法を採っている。
         */
        BeanWrapperFieldSetMapper<ImportProjectsToWorkItem> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(ImportProjectsToWorkItem.class);
        // 空文字の項目をnullとして取り込むようにするため、StringTrimmerEditorを設定
        fieldSetMapper.setCustomEditors(Map.of(String.class, new StringTrimmerEditor(true)));

        // JSR-310 の日付型のフィールドを @DateTimeFormat で注釈することでバインドできるようにするため、
        // DefaultFormattingConversionService を設定する
        fieldSetMapper.setConversionService(new DefaultFormattingConversionService());

        DefaultLineMapper<ImportProjectsToWorkItem> delegate = new DefaultLineMapper<>();
        delegate.setFieldSetMapper(fieldSetMapper);

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer
                .setNames(
                        "projectId",
                        "projectName",
                        "projectType",
                        "projectClass",
                        "projectStartDate",
                        "projectEndDate",
                        "organizationId",
                        "clientId",
                        "projectManager",
                        "projectLeader",
                        "note",
                        "sales");

        delegate.setLineTokenizer(tokenizer);

        LineMapper<ImportProjectsToWorkItem> lineMapper = new LineNumberMapper<>(delegate);

        FlatFileItemReader<ImportProjectsToWorkItem> reader = new FlatFileItemReader<>();
        reader.setEncoding("UTF-8");
        reader.setSaveState(false);
        reader.setLineMapper(lineMapper);
        reader.setResource(new PathResource(importProjectsToWorkProperties().getInputFilePath()));
        reader.setStrict(false); // 入力ファイルが存在しない場合でもエラーとしない

        return reader;
    }

    /**
     * プロジェクト一括登録バッチ/ワークテーブル登録のItemProcessorを構築する。
     * 
     * @return 構築されたインスタンス
     */
    @Bean
    @StepScope
    public CompositeItemProcessor<ImportProjectsToWorkItem, ProjectWork> importProjectsToWorkCompositeItemProcessor() {
        return new CompositeItemProcessorBuilder<ImportProjectsToWorkItem, ProjectWork>()
                .delegates(beanValidatingItemProcessor, importProjectsToWorkItemProcessor)
                .build();
    }

    /**
     * プロジェクト一括登録バッチ/ワークテーブル登録のItemWriterを構築する。
     * 
     * @return 構築されたインスタンス
     */
    @Bean
    @StepScope
    public MyBatisBatchItemWriter<ProjectWork> importProjectsToWorkItemWriter() {
        return new MyBatisBatchItemWriterBuilder<ProjectWork>()
                .statementId(ImportProjectsToWorkMapper.class.getName() + ".insertProjectWork")
                .sqlSessionFactory(sqlSessionFactory)
                .build();
    }

    /**
     * ワークテーブルをTRUNCATEするリスナーを構築する。
     * 
     * @return 構築されたインスタンス
     */
    @Bean
    @StepScope
    public TruncateTableListener importProjectsToWorkTruncateTableListener() {
        return new TruncateTableListener(batchCommonMapper, "project_work");
    }

    /**
     * プロジェクト一括登録バッチ/ワークテーブル登録のStepを構築する。
     * 
     * @return 構築されたインスタンス
     */
    @Bean
    public Step importProjectsToWorkStep() {
        int chunkSize = importProjectsToWorkProperties().getChunkSize();

        return new StepBuilder("BA1060201", jobRepository)
                .<ImportProjectsToWorkItem, ProjectWork> chunk(chunkSize, platformTransactionManager)

                .reader(importProjectsToWorkItemReader())
                .processor(importProjectsToWorkCompositeItemProcessor())
                .writer(importProjectsToWorkItemWriter())

                .faultTolerant()
                .skip(ValidationException.class)
                .skip(FlatFileParseException.class)
                .skip(BatchSkipItemException.class)
                .skipLimit(Integer.MAX_VALUE)

                .listener(loggingSkipItemListener)
                .listener(loggingCountChunkListener)
                .listener(importProjectsToWorkTruncateTableListener())
                .build();
    }

    /**
     * プロジェクト一括登録バッチ/ワークテーブル登録のJobを構築する。
     * 
     * @return 構築されたインスタンス
     */
    @Bean
    public Job importProjectsToWorkJob() {
        return new JobBuilder("BA1060201", jobRepository)
                .start(importProjectsToWorkStep())
                .listener(loggingCountJobListener)
                .build();
    }
}
