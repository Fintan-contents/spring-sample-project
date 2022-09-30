package com.example.common.nablarch.mail;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.example.common.nablarch.date.SystemTimeConfiguration;
import com.example.common.nablarch.db.DbAccessConfiguration;

import nablarch.common.idgenerator.IdGenerator;
import nablarch.common.idgenerator.SequenceIdGenerator;
import nablarch.common.mail.MailAttachedFileTable;
import nablarch.common.mail.MailConfig;
import nablarch.common.mail.MailRecipientTable;
import nablarch.common.mail.MailRequestConfig;
import nablarch.common.mail.MailRequestTable;
import nablarch.common.mail.MailRequester;
import nablarch.common.mail.MailTemplateTable;
import nablarch.common.mail.TemplateEngineMailProcessor;
import nablarch.common.mail.TinyTemplateEngineMailProcessor;
import nablarch.core.db.connection.ConnectionFactory;
import nablarch.core.db.transaction.SimpleDbTransactionManager;

/**
 * Nablarchのメール送信機能を使用するための設定。
 *
 * @author sample
 *
 */
@Configuration(proxyBeanMethods = false)
@Import({
        DbAccessConfiguration.class,
        SystemTimeConfiguration.class
})
public class MailConfiguration {

    /**
     * MailRequestTableを構築する。
     *
     * @param mailConfig MailConfig
     * @return 構築されたインスタンス
     */
    @Bean(initMethod = "initialize")
    public MailRequestTable mailRequestTable(MailConfig mailConfig) {
        MailRequestTable table = new MailRequestTable();
        table.setTableName("mail_request");
        table.setMailRequestIdColumnName("mail_request_id");
        table.setSubjectColumnName("subject");
        table.setFromColumnName("mail_from");
        table.setReplyToColumnName("reply_to");
        table.setReturnPathColumnName("return_path");
        table.setCharsetColumnName("charset");
        table.setStatusColumnName("status");
        table.setRequestDateTimeColumnName("request_datetime");
        table.setSendDateTimeColumnName("send_datetime");
        table.setMailBodyColumnName("mail_body");
        table.setMailConfig(mailConfig);
        return table;
    }

    /**
     * MailRecipientTableを構築する。
     *
     * @return 構築されたインスタンス
     */
    @Bean(initMethod = "initialize")
    public MailRecipientTable mailRecipientTable() {
        MailRecipientTable table = new MailRecipientTable();
        table.setTableName("mail_recipient");
        table.setMailRequestIdColumnName("mail_request_id");
        table.setSerialNumberColumnName("serial_number");
        table.setRecipientTypeColumnName("recipient_type");
        table.setMailAddressColumnName("mail_address");
        return table;
    }

    /**
     * MailAttachedFileTableを構築する。
     *
     * @return 構築されたインスタンス
     */
    @Bean(initMethod = "initialize")
    public MailAttachedFileTable mailAttachedFileTable() {
        MailAttachedFileTable table = new MailAttachedFileTable();
        table.setTableName("mail_attached_file");
        table.setMailRequestIdColumnName("mail_request_id");
        table.setSerialNumberColumnName("serial_number");
        table.setFileNameColumnName("file_name");
        table.setContentTypeColumnName("content_type");
        table.setFileColumnName("attached_file");
        return table;
    }

    /**
     * MailTemplateTableを構築する。
     *
     * @return 構築されたインスタンス
     */
    @Bean(initMethod = "initialize")
    public MailTemplateTable mailTemplateTable() {
        MailTemplateTable table = new MailTemplateTable();
        table.setTableName("mail_template");
        table.setMailTemplateIdColumnName("mail_template_id");
        table.setLangColumnName("lang");
        table.setSubjectColumnName("subject");
        table.setCharsetColumnName("charset");
        table.setMailBodyColumnName("mail_body");
        return table;
    }

    /**
     * MailConfigを構築する。
     *
     * @return 構築されたインスタンス
     */
    @Bean
    @ConfigurationProperties(prefix = "nablarch.mail.mail-config")
    public MailConfig mailConfig() {
        MailConfig config = new MailConfig();
        config.setMailRequestSbnId("mail_request_id");
        config.setRecipientTypeTO("0");
        config.setRecipientTypeCC("1");
        config.setRecipientTypeBCC("2");
        config.setStatusUnsent("0");
        config.setStatusSent("1");
        config.setStatusFailure("2");
        config.setSendSuccessMessageId("mail.send.success");
        config.setSendFailureCode("mail.send.failure");
        config.setMailRequestCountMessageId("mail.request.count");
        config.setAbnormalEndExitCode(199);
        return config;
    }

    /**
     * MailRequesterを構築する。
     *
     * @param mailRequestConfig MailRequestConfig
     * @param mailRequestIdGenerator IdGenerator
     * @param mailRequestTable MailRequestTable
     * @param mailRecipientTable MailRecipientTable
     * @param mailAttachedFileTable MailAttachedFileTable
     * @param templateEngineMailProcessor TemplateEngineMailProcessor
     * @param mailConfig MailConfig
     * @param connectionFactory ConnectionFactory
     * @param transactionManager SimpleDbTransactionManager
     * @return 構築されたインスタンス
     */
    @Bean
    public MailRequester mailRequester(
            MailRequestConfig mailRequestConfig,
            IdGenerator mailRequestIdGenerator,
            MailRequestTable mailRequestTable,
            MailRecipientTable mailRecipientTable,
            MailAttachedFileTable mailAttachedFileTable,
            TemplateEngineMailProcessor templateEngineMailProcessor,
            MailConfig mailConfig,
            ConnectionFactory connectionFactory,
            SimpleDbTransactionManager transactionManager) {
        MailRequester mailRequester = new TransactionalMailRequester(transactionManager);
        mailRequester.setMailRequestConfig(mailRequestConfig);
        mailRequester.setMailRequestIdGenerator(mailRequestIdGenerator);
        mailRequester.setMailRequestTable(mailRequestTable);
        mailRequester.setMailRecipientTable(mailRecipientTable);
        mailRequester.setMailAttachedFileTable(mailAttachedFileTable);
        mailRequester.setTemplateEngineMailProcessor(templateEngineMailProcessor);
        mailRequester.setMailConfig(mailConfig);
        return mailRequester;
    }

    /**
     * MailRequestConfigを構築する。
     *
     * @return 構築されたインスタンス
     */
    @Bean
    @ConfigurationProperties(prefix = "nablarch.mail.mail-request-config")
    public MailRequestConfig mailRequestConfig() {
        MailRequestConfig config = new MailRequestConfig();
        config.setDefaultReplyTo("default.reply.to@nablarch.sample");
        config.setDefaultReturnPath("default.return.path@nablarch.sample");
        config.setDefaultCharset("ISO-2022-JP");
        config.setMaxRecipientCount(100);
        config.setMaxAttachedFileSize(2097152);
        return config;
    }

    /**
     * SequenceIdGeneratorを構築する。
     *
     * @return 構築されたインスタンス
     */
    @Bean
    public SequenceIdGenerator idGenerator() {
        return new SequenceIdGenerator();
    }

    /**
     * TinyTemplateEngineMailProcessorを構築する。
     *
     * @param mailTemplateTable MailTemplateTable
     * @return 構築されたインスタンス
     */
    @Bean
    public TinyTemplateEngineMailProcessor templateEngineMailProcessor(MailTemplateTable mailTemplateTable) {
        TinyTemplateEngineMailProcessor processor = new TinyTemplateEngineMailProcessor();
        processor.setMailTemplateTable(mailTemplateTable);
        return processor;
    }

    /**
     * MailSystemRepositoryLoaderを構築する。
     *
     * @param mailRequester MailRequester
     * @return 構築されたインスタンス
     */
    @Bean
    public MailSystemRepositoryLoader mailSystemRepositoryLoader(MailRequester mailRequester) {
        return new MailSystemRepositoryLoader(mailRequester);
    }
}
