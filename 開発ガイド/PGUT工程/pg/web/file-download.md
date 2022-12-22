# ファイルダウンロード

- [CSVファイルをダウンロードする](#csvファイルをダウンロードする)

## CSVファイルをダウンロードする

CSVファイルのダウンロードは`Controller`上でアプリ基盤部品の`CsvFileDownloadView`と`FileDownloadAttributes`を使用して行う。

```java
@GetMapping("file")
public String file(Model model) { // (1)
    String targetFilePath = "file:/workspace/files/target.csv";
    String downloadFileName = "download.csv";

    // (2)
    FileDownloadAttributes fileDownloadAttributes =
        new FileDownloadAttributes(targetFilePath, downloadFileName);

    // (3)
    model.addAttribute(
        CsvFileDownloadView.DOWNLOAD_FILE_INFO_KEY, fileDownloadAttributes);

    return CsvFileDownloadView.VIEW_NAME; // (4)
}
```

- 実装のポイント
    - (1) `Controller`のメソッド引数で`Model`を受け取る
        - ※この`Model`はステレオタイプの`Model`ではなく、Springが提供している`org.springframework.ui.Model`であることに注意
    - (2) `FileDownloadAttributes`のインスタンスを作成する
        - 第1引数はアプリケーションが動作しているサーバ上にあるファイルのパスを渡す（`file:`に絶対パスを繋げたものとすること）
        - 第2引数はブラウザでファイルを保存するときに表示するファイル名を渡す
    - (3) `addAttribute`で`fileDownloadAttributes`を設定する
        - 名前は`CsvFileDownloadView.DOWNLOAD_FILE_INFO_KEY`を設定すること
    - (4) `CsvFileDownloadView.VIEW_NAME`を返す
