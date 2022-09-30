package com.example.batch.archunit;

import static com.example.batch.archunit.ArchUnitConfiguration.*;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

import org.junit.jupiter.api.Test;

/**
 * 命名規約をチェックするテストクラス。
 */
public class NamingConventionTest {

    /**
     * configurationパッケージにあるクラス名はxxxConfigであること。
     */
    @Test
    void testConfigurationNamingConvention() {
        classes()
                .that().resideInAPackage("..configuration")
                .and().resideOutsideOfPackage("..common.configuration") // common配下は除外
                .should().haveSimpleNameEndingWith("Properties")
                .orShould().haveSimpleNameEndingWith("Config")
                .check(appJavaClasses);
    }

    /**
     * itemパッケージにあるクラス名はxxxItemであること。
     */
    @Test
    void testItemNamingConvention() {
        classes()
                .that().resideInAPackage("..item")
                .should().haveSimpleNameEndingWith("Item")
                .check(appJavaClasses);
    }

    /**
     * mapperパッケージにあるクラス名はxxxMapperであること。
     */
    @Test
    void testMapperNamingConvention() {
        classes()
                .that().resideInAPackage("..mapper")
                .should().haveSimpleNameEndingWith("Mapper")
                .check(appJavaClasses);
    }

    /**
     * processorパッケージにあるクラス名はxxxItemProcessorであること。
     */
    @Test
    void testProcessorResponseNamingConvention() {
        classes()
                .that().resideInAPackage("..processor")
                .should().haveSimpleNameEndingWith("ItemProcessor")
                .check(appJavaClasses);
    }

    /**
     * readerパッケージにあるクラス名はxxxItemReaderまたはxxxFieldSetMapperであること。
     *
     * {@link com.example.batch.common.reader.LineNumberMapper}のような
     * {@link org.springframework.batch.item.file.LineMapper}実装クラスもreaderパッケージに配置する可能性があるが、
     * 通常、基板部品としてcommonパッケージとして配置され、業務アプリケーションの一部として実装するケースは想定していない。
     * よってcommonパッケージ以外ではItemReader、FieldSetMapper以外の接尾辞は許容していない。
     */
    @Test
    void testReaderNamingConvention() {
        classes()
                .that().resideInAPackage("..reader")
                .and().resideOutsideOfPackage("..common.reader") // common配下は除外
                .should().haveSimpleNameEndingWith("ItemReader")
                .orShould().haveSimpleNameEndingWith("FieldSetMapper")
                .allowEmptyShould(true)
                .check(appJavaClasses);
    }

    /**
     * writerパッケージにあるクラス名はxxxItemWriterまたはxxxFieldExtractorであること。
     */
    @Test
    void testWriterNamingConvention() {
        classes()
                .that().resideInAPackage("..writer")
                .should().haveSimpleNameEndingWith("ItemWriter")
                .orShould().haveSimpleNameEndingWith("FieldExtractor")
                .check(appJavaClasses);
    }
}
