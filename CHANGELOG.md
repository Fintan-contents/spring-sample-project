# CHANGELOG

リリース日と主な変更点は次の通りです。

## [2023-12-25](https://github.com/Fintan-contents/spring-sample-project/milestone/1?closed=1)

- 変更
    - Spring Boot を 3.2 へバージョンアップ([#1](https://github.com/Fintan-contents/spring-sample-project/pull/1),
      [#2](https://github.com/Fintan-contents/spring-sample-project/pull/2),
      [#3](https://github.com/Fintan-contents/spring-sample-project/pull/3))
- バグフィックス
    - 開発ガイドでリンク先の誤りを修正([#3](https://github.com/Fintan-contents/spring-sample-project/pull/3))

## 2023-01-27

- 改善
    - テストデータと期待値をExcel 97-2003ブック(`*.xls`)からExcelブック(`*.xlsx`)へ変更
    - Database Riderの使用モジュールを`rider-spring`から`rider-junit5`へ変更
    - Database RiderではDB接続をキャッシュしないよう変更(Spring側でコネクションプールを使用しているため)

## 2022-12-23

- サンプル追加
    - Web: ユーザ別従事プロジェクト(ファイルダウンロード)
    - Web: プロジェクトアップロード(ファイルアップロード)
    - バッチ: ユーザ別従事プロジェクト抽出(常駐バッチ)
    - バッチ: 業務日付更新(Tasklet)
    - API: 顧客更新(PUTメソッド)
    - API: 顧客削除(DELETEメソッド)

- バグフィックス
    - `BusinessDateSupplier`から不要な`@Transactional`を削除
    - `@PropertySource`を使用している箇所で`encoding = "UTF-8"`と指定するよう修正

- 改善
    - ドメイン定義を見直し

## 2022-10-01

- バグフィックス
    - `compose.yml`が日本語を含むディレクトリに配置されていたためDocker Composeが起動できない問題を修正

- 改善
    - ソースコードのセットアップを行う際、躓きそうなポイントについて説明を補強

## 2022-09-30

- 公開

