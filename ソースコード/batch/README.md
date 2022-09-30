# サンプルプロジェクト - バッチ

## 概要

Spring Batchを使用したバッチのプロジェクトです。

## 起動方法

### プロジェクト一括登録

プロジェクト一括登録はCSVファイルからワークテーブルに登録するバッチとワークテーブルから本テーブルに登録するバッチの2つから構成されています。

#### CSVファイルからワークテーブルに登録するバッチ

まず`work/BA1060201/input`のディレクトリに、以下の内容で CSV ファイルを作成します(文字コードはUTF-8にする)。

```csv
,プロジェクト１,01,01,2019/01/01,2021/12/01,1,1,マネージャ１,リーダー１,テスト１,15000
,プロジェクト２,01,01,2019/01/01,2021/12/02,2,2,マネージャ２,リーダー２,テスト２,16000
,プロジェクト３,01,01,2019/01/01,2021/12/03,3,3,マネージャ３,リーダー３,テスト３,17000
```

Spring Boot Maven Pluginを使用して起動します。
起動時にジョブ名を指定します。

```
mvn spring-boot:run -Dspring-boot.run.arguments=--spring.batch.job.names=BA1060201
```

#### ワークテーブルから本テーブルに登録するバッチ

Spring Boot Maven Pluginを使用して起動します。
起動時にジョブ名を指定します。

```
mvn spring-boot:run -Dspring-boot.run.arguments=--spring.batch.job.names=BA1060202
```

### 期間内プロジェクト一括出力

Spring Boot Maven Pluginを使用して起動します。
起動時にジョブ名を指定します。

```
mvn spring-boot:run -Dspring-boot.run.arguments=--spring.batch.job.names=BA1060101
```

デフォルトでは`work/BA1060101/output`の下に`N21AA002.csv`という名前で出力されます。

## 実行可能JARファイルの生成方法

Spring Boot Maven Pluginを使用して実行可能JARファイルを生成します。

```bash
mvn package -DskipTests
```

プラグインの詳細は次のウェブサイトを参照してください。

- https://docs.spring.io/spring-boot/docs/2.7.x/maven-plugin/reference/htmlsingle/#packaging
- https://spring.pleiades.io/spring-boot/docs/2.7.x/maven-plugin/reference/htmlsingle/#packaging ※非公式・有志による日本語訳

