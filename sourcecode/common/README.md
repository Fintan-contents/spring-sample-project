# サンプルプロジェクト - common

## 概要

Web、API、バッチそれぞれで共通的に使われるものを格納しています。

NablarchのライブラリをSpring Bootで使用するための設定も格納しています(`com.example.common.nablarch`パッケージ以下のクラス群を参照)。

## DBをセットアップする

[gsp-dba-maven-plugin](https://github.com/coastland/gsp-dba-maven-plugin)を使用してDBをセットアップします。

```bash
mvn -P gsp generate-resources
```

上記のコマンドはデータベースの内容を洗い替えるため、動作確認をしたり自動テストを行なうことでデータが変更された場合にデータベースをリセットするためにもご使用ください。

## MyBatisで使用するModelを自動生成する

[MyBatis Generator](https://mybatis.org/generator/)を使用してテーブルに対応するModelを自動生成します。

```bash
mvn mybatis-generator:generate
```

Modelは`src/main/java/com/example/common/generated/model`以下に出力され、すでにファイルが存在する場合は上書きされます。
生成されたModelを削除したい場合は、手動で削除してください。

なお、MyBatis GeneratorはMapperインターフェースも生成できますが、生成される`UPDATE`文が楽観排他制御に対応しておらず誤って使用することを避けるため、本プロジェクトではModelのみを生成対象としています。

## ローカルのMavenリポジトリへインストールする

次のコマンドでローカルのMavenリポジトリ(通常はホームディレクトリ以下の`.m2/repository`)へインストールします。

```bash
mvn install -DskipTests
```
