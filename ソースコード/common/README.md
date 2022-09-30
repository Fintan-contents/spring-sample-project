# サンプルプロジェクト - common

## 概要

Web、API、バッチそれぞれで共通的に使われるものを格納しています。

NablarchのライブラリをSpring Bootで使用するための設定も格納しています(`com.example.common.nablarch`パッケージ以下のクラス群を参照)。

## DBをセットアップする

[gsp-dba-maven-plugin](https://github.com/coastland/gsp-dba-maven-plugin)を使用してDBをセットアップします。

```bash
mvn -P gsp generate-resources
```

## MyBatisで使用するModelを自動生成する

[MyBatis Generator](https://mybatis.org/generator/)を使用してテーブルに対応するModelを自動生成します。

```bash
mvn mybatis-generator:generate
```

なお、MyBatis GeneratorはMapperインターフェースも生成できますが、生成される`UPDATE`文が楽観排他制御に対応しておらず誤って使用することを避けるため、本プロジェクトではModelのみを生成対象としています。

## ローカルのMavenリポジトリへインストールする

次のコマンドでローカルのMavenリポジトリ(通常はホームディレクトリ以下の`.m2/repository`)へインストールします。

```bash
mvn install -DskipTests
```
