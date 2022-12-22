# ファイルアップロード

- [ファイルをアップロードする](#ファイルをアップロードする)
- [マルチパートファイルを扱う](#マルチパートファイルを扱う)
  - [ファイルのチェックを行う](#ファイルのチェックを行う)
  - [ファイルを保存する](#ファイルを保存する)


## ファイルをアップロードする

ファイルのアップロードを行うには、まず画面テンプレートでファイル添付のフォームコントロールを使用する。
詳細は[画面テンプレートのファイル添付](./view-template.md#ファイル添付)を参照。

次にアップロードされたファイルを受け取るフィールドを`Form`に定義する。

```java
public class ExampleForm {

    /**
     * 添付ファイル
     */
    private MultipartFile targetFile; // (1)

    // その他のフィールド、アクセサは省略
}
```

- 実装のポイント
    - (1) `MultipartFile`型のフィールドを定義する
        - 補足：`MultipartFile`はSpringが提供しているインターフェースで、完全修飾名は`org.springframework.web.multipart.MultipartFile`

## マルチパートファイルを扱う

### ファイルのチェックを行う

`MultipartFile`が持つメソッドを用いてファイルのチェックが行える。

```java
    MultipartFile targetFile = form.getTargetFile();

    // (1)
    if (StringUtils.isBlank(targetFile.getOriginalFilename())) {
        throw ApplicationException.fieldError("targetFile", "errors.example.noFile");
    }

    // (2)
    if (targetFile.isEmpty()) {
        throw ApplicationException.fieldError("targetFile", "errors.example.emptyFile");
    }

    // (3)
    if (targetFile.getSize() > 512_000) {
        throw ApplicationException.fieldError("targetFile", "errors.example.fileExceededMaxSize");
    }
```

- 実装のポイント
    - (1) `getOriginalFilename`メソッドでファイルがアップロードされたかどうかをチェックできる
        - `getOriginalFilename`メソッドの戻り値が`null`の場合、ファイルはアップロードされていない（つまり、ファイル添付されていない）
    - (2) `isEmpty`メソッドでアップロードされたファイルの内容が空かどうかをチェックできる
    - (3) `getSize`メソッドでアップロードされたファイルのサイズをチェックできる
        - この例では512KBを超えているかどうかをチェックしている

### ファイルを保存する

`MultipartFile`を保存する場合、基盤部品の`UploadSupport`を使用する。
本システムでは登録・更新系の機能は入力画面、確認画面、完了画面の3画面構成となっている。
そのため、アップロードされたファイルを保存するには、入力画面から確認画面への遷移時にアップロードされたファイルを一時領域で保持しておき、確認画面から完了画面への遷移時に所定の場所へ保存する必要がある。
`UploadSupport`は、この処理フローをサポートする基盤部品である。

`UploadSupport`を使用するには`@Autowired`を使い、フィールドインジェクションする。

```java
    @Autowired
    private UploadSupport uploadSupport;
```

`UploadSupport`を使用して行える操作は次の通り。

```java
    MultipartFile targetFile = form.getTargetFile();

    // (1)
    String tempFileId = uploadSupport.saveTemporary(targetFile);

    // (2)
    Path dest = Path.of("/path/to/permanent.txt");
    uploadSupport.moveFromTemporaryFile(tempFileId, dest);

    // (3)
    uploadSupport.removeTemporaryFile(tempFileId);

    // (4)
    if (!uploadSupport.existsTemporaryFile(tempFileId)) {
        throw ApplicationException.globalError("errors.example.tempFileNotFound");
    }
```

- 実装のポイント
    - (1) `saveTemporary`メソッドへ`MultipartFile`を渡すことで一時ファイルとして保存できる
        - 戻り値は一時ファイルIDが返される
        - 一時ファイルに対する操作は、この一時ファイルIDを用いて行う
        - 主に入力画面から確認画面へ遷移する際に使用することを想定している
    - (2) `moveFromTemporaryFile`メソッドで一時ファイルを任意のパスへ移動できる
        - 主に確認画面から完了画面へ遷移する際に使用することを想定している
    - (3) `removeTemporaryFile`メソッドで一時ファイルを削除できる
        - 主に確認画面から入力画面へ戻る際に使用することを想定している
    - (4) `existsTemporaryFile`メソッドで一時ファイルの存在チェックを行える
        - 一時ファイルを操作する際、必要に応じて使用することを想定している
