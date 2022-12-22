# 常駐バッチの実装方法

- [概要](#概要)
- [要求データの主キー](#要求データの主キー)
- [常駐バッチの設定](#常駐バッチの設定)
- [動作確認方法](#動作確認方法)

## 概要

定期実行の制御とバッチ処理失敗時のリトライ制御はアプリ基盤部品である`ResidentBatchScheduler`が行う。
そのため、個々の機能の実装担当者は通常のバッチと同様に`Chunk`型、もしくは`Tasklet`型のバッチアプリケーションを実装すれば良い。

バッチアプリケーションの実行時に要求データの主キーが起動パラメータとして渡されるため、受け取って処理を行うこと。
起動パラメータの受け取り方は[起動パラメータの利用方法](./job-parameters.md)を参照すること。
なお、要求データの主キーは`request.id`という名前で渡される。

## 要求データの主キー

要求データの主キーは単一のカラムとする。
複合主キーには対応していない。

データ型は整数と文字列に対応している。

|主キーの型|Java上の型|必要な設定|
|---|---|---|
|整数|`Long`|なし（デフォルトで整数となる）|
|文字列|`String`|`string-primary-key`を`true`に設定する|

文字列型の主キーを扱う場合に必要となる設定については[常駐バッチの設定](#常駐バッチの設定)も参照すること。

## 常駐バッチの設定

常駐バッチで使用する要求テーブルに関する設定を行う必要がある。

設定ファイルの書き方や`Properties`の定義方法は[プロパティ管理](./property-management.md)を参照すること。

要求テーブルに関する設定は`resident-batch.requests.[ジョブID]`という接頭辞を持つキーに対して行う。
設定の例は次の通り（ここではジョブIDを仮に`BX0000000`としている）。

```properties
# 要求テーブルのテーブル名（必須）
resident-batch.requests.[BX0000000].table-name=xxx_request
# 要求テーブルの主キー名（必須）
resident-batch.requests.[BX0000000].primary-key-name=id
# 要求テーブルのステータスカラム名（必須）
resident-batch.requests.[BX0000000].status-column-name=status
# 要求テーブルの要求日時カラム名（必須）
resident-batch.requests.[BX0000000].requested-at-column-name=requested_datetime
# アプリ基盤部品が要求テーブルを検索する際、取得する最大件数（必須）
resident-batch.requests.[BX0000000].limit=100
# 要求テーブルの主キーの型が文字列の場合はtrueにする（オプション。デフォルトはfalse）
resident-batch.requests.[BX0000000].string-primary-key=true
```

また、必要に応じて要求データの各ステータスを表す値を設定する。
設定の例は次の通り。

```properties
# 「未処理」を表す値（オプション。デフォルトは01）
resident-batch.requests.[BX0000000].unprocessed-status=A
# 「処理中」を表す値（オプション。デフォルトは02）
resident-batch.requests.[BX0000000].processing-status=B
# 「処理成功」を表す値（オプション。デフォルトは03）
resident-batch.requests.[BX0000000].success-status=C
# 「処理失敗」を表す値（オプション。デフォルトは04）
resident-batch.requests.[BX0000000].failure-status=D
```

## 動作確認方法

常駐バッチを開発中にローカルで起動して動作確認したい場合はMavenを使用する。
その際、次のプロパティを指定すること。

|プロパティ名|説明|
|---|---|
|`resident-batch.enabled`|常駐バッチを有効化するフラグ。必ず`true`を設定すること|
|`resident-batch.job-id`|ジョブID（常駐バッチの設定でも使用したID）|
|`resident-batch.spring-batch-job-name`|Spring Batchで定義する`Job`の名前|

常駐バッチの起動コマンドの例を示す（ここではジョブIDとSpring Batchで定義する`Job`の名前をどちらも仮に`BX0000000`としている）。

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--resident-batch.enabled=true --resident-batch.job-id=BX0000000 --resident-batch.spring-batch-job-name=BX0000000"
```

常駐バッチを停止する場合は`ctrl + c`でJavaプログラムを止めるか、または常駐バッチの状態管理テーブルのレコードを更新すればよい。
