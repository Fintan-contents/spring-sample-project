# サンプルプロジェクト - parent

## 概要

Web、バッチ、API、commonの親となるプロジェクトです。

各プロジェクトで使用する依存ライブラリやプラグインのバージョンはparentの`dependencyManagement`で定義されています。

## ローカルのMavenリポジトリへインストールする

次のコマンドでローカルのMavenリポジトリ(通常はホームディレクトリ以下の`.m2/repository`)へインストールします。

```bash
mvn install
```
