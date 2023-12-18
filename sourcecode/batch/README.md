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
mvn spring-boot:run -Dspring-boot.run.arguments=--spring.batch.job.name=BA1060201
```

#### ワークテーブルから本テーブルに登録するバッチ

Spring Boot Maven Pluginを使用して起動します。
起動時にジョブ名を指定します。

```
mvn spring-boot:run -Dspring-boot.run.arguments=--spring.batch.job.name=BA1060202
```

### 期間内プロジェクト一括出力

Spring Boot Maven Pluginを使用して起動します。
起動時にジョブ名を指定します。

```
mvn spring-boot:run -Dspring-boot.run.arguments=--spring.batch.job.name=BA1060101
```

デフォルトでは`work/BA1060101/output`の下に`N21AA002.csv`という名前で出力されます。

### ユーザ別従事プロジェクト抽出

自動テストを実行していると常駐バッチの状態を管理するテーブルが単体テスト用のデータに書き換えられるため、
ユーザ別従事プロジェクト抽出バッチの動作確認ができなくなります。
確実に動作確認をできるようにするため、事前にcommonプロジェクトで`mvn -P gsp generate-resources`をしてDBを再作成してから実行してください。

Spring Boot Maven Pluginを使用して起動します。
起動時にジョブID(`resident-batch.job-id`)とジョブ名(`resident-batch.spring-batch-job-name`)、
常駐バッチ起動を有効にするオプション(`resident-batch.enabled`)を指定します。
また、Spring Bootによる自動バッチ起動を無効化するオプション(`spring.batch.job.enabled=false`)も指定します。

```
mvn spring-boot:run -Dspring-boot.run.arguments="--resident-batch.enabled=true --resident-batch.job-id=BA1060301 --resident-batch.spring-batch-job-name=BA1060301 --spring.batch.job.enabled=false"
```

バッチが起動し、プロセスが常駐します。

この状態でWebを起動し、ユーザ別従事プロジェクト画面でファイル作成の処理要求を登録します。
しばらく待つと、登録したファイル作成要求が処理されてファイルが出力されます。
デフォルトでは`work/BA1060301/output`の下に`N21AA003_【要求ID】.csv`という名前で出力されます。

バッチのプロセスを終了させる場合は、Ctrl + Cと入力してください。

### 業務日付更新バッチ

Spring Boot Maven Pluginを使用して起動します。
起動時にジョブ名と更新する業務日付の区分を指定します。

```
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.batch.job.name=BA1070101 --project.update-business-date.segment-id=00"
```

デフォルトではシステム日付で指定された区分の業務日付が更新されます。
明示的に日付を指定する場合は、`businessDate=yyyyMMdd`を引数に追加して実行してください。

## 実行可能JARファイルの生成方法

Spring Boot Maven Pluginを使用して実行可能JARファイルを生成します。

```bash
mvn package -DskipTests
```

プラグインの詳細は次のウェブサイトを参照してください。

- https://docs.spring.io/spring-boot/docs/3.2.x/maven-plugin/reference/htmlsingle/#packaging
- https://spring.pleiades.io/spring-boot/docs/3.2.x/maven-plugin/reference/htmlsingle/#packaging ※非公式・有志による日本語訳

