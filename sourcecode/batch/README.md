# サンプルプロジェクト - バッチ

## 概要

Spring Batchを使用したバッチのプロジェクトです。

## 起動方法

### プロジェクト一括登録

プロジェクト一括登録はCSVファイルからワークテーブルに登録するバッチとワークテーブルから本テーブルに登録するバッチの2つから構成されています。

#### CSVファイルからワークテーブルに登録するバッチ

まず`work/BA1060201/input`というディレクトリを作成し、以下の内容でCSVファイルを作成します(文字コードはUTF-8にする)。
ファイル名は`N21AA001.csv`としてください。

```csv
1,プロジェクト名１,01,04,2020/01/01,2020/12/31,4,7,プロジェクトマネージャ１,プロジェクトリーダー１,備考１,1234
2,プロジェクト名２,02,05,2021/01/01,2021/12/31,5,8,プロジェクトマネージャ２,プロジェクトリーダー２,備考２,2345
3,プロジェクト名３,03,06,2022/01/01,2022/12/31,6,9,プロジェクトマネージャ３,プロジェクトリーダー３,備考３,3456
```

[自動テストで使用するこちらのCSVファイル](src/test/resources/com/example/batch/project/ImportProjectsToWorkTest/testMultiRecord/input.csv)をコピーしても構いません。

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

