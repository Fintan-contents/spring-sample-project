# サンプルプロジェクト - API

## 概要

Spring Web MVCを使用したAPIのプロジェクトです。

## 起動方法

Spring Boot Maven Pluginを使用して起動します。

```bash
mvn spring-boot:run
```

次のURLで起動します。※ルートURLにはエンドポイントがなく、ブラウザで開いても何も表示されないのでご注意ください

- http://localhost:8090

`curl`コマンドでの動作確認例を記載します。

```bash
curl http://localhost:8090/clients
```

## コンテナイメージの生成方法

Spring Boot Maven Pluginを使用してコンテナイメージを生成します。

```bash
mvn spring-boot:build-image -DskipTests
```

※プロキシ環境下では[imageパラメータ](https://docs.spring.io/spring-boot/docs/2.7.x/maven-plugin/reference/htmlsingle/#goals-build-image-parameters-details-image)の`env`でプロキシの設定(`HTTP_PROXY`、`HTTPS_PROXY`)を行う必要があります。

プラグインの詳細は次のウェブサイトを参照してください。

- https://docs.spring.io/spring-boot/docs/3.2.x/maven-plugin/reference/htmlsingle/#build-image
- https://spring.pleiades.io/spring-boot/docs/3.2.x/maven-plugin/reference/htmlsingle/#build-image ※非公式・有志による日本語訳

