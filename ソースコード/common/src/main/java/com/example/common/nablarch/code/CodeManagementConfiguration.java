package com.example.common.nablarch.code;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.example.common.nablarch.db.DbAccessConfiguration;

import nablarch.common.code.BasicCodeLoader;
import nablarch.common.code.BasicCodeManager;
import nablarch.common.code.Code;
import nablarch.common.code.CodeManager;
import nablarch.common.code.schema.CodeNameSchema;
import nablarch.common.code.schema.CodePatternSchema;
import nablarch.core.cache.BasicStaticDataCache;
import nablarch.core.db.transaction.SimpleDbTransactionManager;

/**
 * Nablarchのコード管理機能を使用するための設定。
 * 
 * @author sample
 *
 */
@Configuration(proxyBeanMethods = false)
@Import(DbAccessConfiguration.class)
public class CodeManagementConfiguration {

    /**
     * BasicCodeLoaderを構築する。
     * 
     * @param codePatternSchema CodePatternSchema
     * @param codeNameSchema CodeNameSchema
     * @param dbManager SimpleDbTransactionManager
     * @return 構築されたインスタンス
     */
    @Bean(initMethod = "initialize")
    public BasicCodeLoader codeLoader(
            CodePatternSchema codePatternSchema,
            CodeNameSchema codeNameSchema,
            SimpleDbTransactionManager dbManager) {
        BasicCodeLoader codeLoader = new BasicCodeLoader();
        codeLoader.setCodeNameSchema(codeNameSchema);
        codeLoader.setCodePatternSchema(codePatternSchema);
        codeLoader.setDbManager(dbManager);
        return codeLoader;
    }

    /**
     * CodePatternSchemaを構築する。
     * テーブル名やカラム名など、プロジェクトに合わせてカスタマイズすること。
     * 
     * @return 構築されたインスタンス。
     */
    @Bean
    public CodePatternSchema codePatternSchema() {
        CodePatternSchema codePatternSchema = new CodePatternSchema();
        codePatternSchema.setTableName("code_pattern");
        codePatternSchema.setIdColumnName("code_id");
        codePatternSchema.setValueColumnName("code_value");
        codePatternSchema
                .setPatternColumnNames(new String[] {
                        "pattern01", "pattern02", "pattern03", "pattern04", "pattern05", "pattern06", "pattern07",
                        "pattern08", "pattern09", "pattern10", "pattern11", "pattern12", "pattern13", "pattern14",
                        "pattern15", "pattern16", "pattern17", "pattern18", "pattern19", "pattern20"

                });
        return codePatternSchema;
    }

    /**
     * CodeNameSchemaを構築する。
     * テーブル名やカラム名など、プロジェクトに合わせてカスタマイズすること。
     * 
     * @return 構築されたインスタンス。
     */
    @Bean
    public CodeNameSchema codeNameSchema() {
        CodeNameSchema codeNameSchema = new CodeNameSchema();
        codeNameSchema.setTableName("code_name");
        codeNameSchema.setIdColumnName("code_id");
        codeNameSchema.setValueColumnName("code_value");
        codeNameSchema.setLangColumnName("lang");
        codeNameSchema.setSortOrderColumnName("sort_order");
        codeNameSchema.setNameColumnName("code_name");
        codeNameSchema.setShortNameColumnName("short_name");
        codeNameSchema
                .setOptionNameColumnNames(new String[] {
                        "option01", "option02", "option03", "option04", "option05", "option06", "option07", "option08",
                        "option09", "option10"
                });
        return codeNameSchema;
    }

    /**
     * BasicStaticDataCacheを構築する。
     * 
     * @param loader BasicCodeLoader
     * @return 構築されたインスタンス
     */
    @Bean(initMethod = "initialize")
    @ConfigurationProperties(prefix = "nablarch.code.code-definition-cache")
    public BasicStaticDataCache<Code> codeDefinitionCache(BasicCodeLoader loader) {
        BasicStaticDataCache<Code> basicStaticDataCache = new BasicStaticDataCache<>();
        basicStaticDataCache.setLoader(loader);
        return basicStaticDataCache;
    }

    /**
     * BasicCodeManagerを構築する。
     * 
     * @param codeDefinitionCache BasicStaticDataCache
     * @return 構築されたインスタンス
     */
    @Bean
    public BasicCodeManager codeManager(BasicStaticDataCache<Code> codeDefinitionCache) {
        BasicCodeManager codeManager = new BasicCodeManager();
        codeManager.setCodeDefinitionCache(codeDefinitionCache);
        return codeManager;
    }

    /**
     * CodeManagementSystemRepositoryLoaderを構築する。
     * 
     * @param codeManager BasicCodeManager
     * @return 構築されたインスタンス
     */
    @Bean
    public CodeManagementSystemRepositoryLoader codeManagementSystemRepositoryLoader(CodeManager codeManager) {
        return new CodeManagementSystemRepositoryLoader(codeManager);
    }
}