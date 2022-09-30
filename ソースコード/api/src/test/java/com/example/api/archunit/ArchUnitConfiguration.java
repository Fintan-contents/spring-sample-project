package com.example.api.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;

/**
 * ArchUnitの共通設定を保持するクラス。
 * <p>
 * <p>
 * ArchUnitテストコードそのものの妥当性を確認したい場合は、
 * テスト起動時に-Darchunit.self-check=trueを指定する。
 * これによりセルフチェック用のクラス（com.example.web.archunit.selfcheck）がテスト対象となる。
 */
class ArchUnitConfiguration {

    /**
     * このアプリケーションを構成するclass群。
     * ここで定義されたクラスがArchUnitのテスト対象となる。
     */
    static JavaClasses appJavaClasses = createTargetJavaClasses();

    private static JavaClasses createTargetJavaClasses() {
        String selfCheck = System.getProperty("archunit.self-check");
        return Boolean.parseBoolean(selfCheck) ? createArchUnitSelfCheckClasses() : createWebAppJavaClasses();
    }

    /**
     * ArchUnitのテスト対象となるアプリケーションJavaクラスを生成する。
     *
     * commonモジュールへの依存をチェックできるようにするため、
     * パッケージ指定は"com.example.api"ではなく"com.example"としている。
     * こうすることで、例えば com.example.common 配下のmapperパッケージへの依存チェックができるようになる。
     *
     * @return テスト対象アプリケーションJavaクラス
     */
    private static JavaClasses createWebAppJavaClasses() {
        return new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.example");
    }

    /**
     * ArchUnitテストコードをチェックするためのJavaクラスを生成する。
     *
     * @return セルフチェック用Javaクラス
     */
    private static JavaClasses createArchUnitSelfCheckClasses() {
        return new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
                .withImportOption(ImportOption.Predefined.ONLY_INCLUDE_TESTS)
                .importPackages("com.example.api.archunit.selfcheck");
    }
}
