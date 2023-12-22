# サンプルプロジェクト - Web

## 概要

Spring Web MVCとThymeleafを使用したWebのプロジェクトです。

## 起動方法

Spring Boot Maven Pluginを使用して起動します。

```bash
mvn spring-boot:run
```

次のURLで起動します。

- http://localhost:8080

なお、一部の機能で`api`のエンドポイントを参照しています。
[apiのREADME](../api/README.md)に従い、`api`も起動しておいてください。

予め用意しているユーザは次の通りです。

|ログインID|パスワード|プロジェクトマネージャ|
|---|---|---|
|`10000001`|`pass123-`|○|
|`10000002`|`pass123-`|○|
|`10000003`|`pass123-`||
|`10000004`|`pass123-`||
|`10000005`|`pass123-`||

プロジェクトマネージャは登録・更新が行えます。
それ以外のユーザは参照のみです。

## コンテナイメージの生成方法

Spring Boot Maven Pluginを使用してコンテナイメージを生成します。

```bash
mvn spring-boot:build-image -DskipTests
```

※プロキシ環境下では[imageパラメータ](https://docs.spring.io/spring-boot/docs/2.7.x/maven-plugin/reference/htmlsingle/#goals-build-image-parameters-details-image)の`env`でプロキシの設定(`HTTP_PROXY`、`HTTPS_PROXY`)を行う必要があります。

プラグインの詳細は次のウェブサイトを参照してください。

- https://docs.spring.io/spring-boot/docs/3.2.x/maven-plugin/reference/htmlsingle/#build-image
- https://spring.pleiades.io/spring-boot/docs/3.2.x/maven-plugin/reference/htmlsingle/#build-image ※非公式・有志による日本語訳

