# HTTPクライアント

バッチでHTTPクライアントを使用するケースとしては、大容量のファイルのダウンロードとアップロードを想定している。
したがって、ここではこれらの実装方法について説明する。

HTTPクライアントを用いた基本的なリクエストの送信方法については、[WebのHTTPクライアントのガイド](../web/http-client.md)を参照。

- [連携先からファイルをダウンロードする](#連携先からファイルをダウンロードする)
  - [リクエストボディを送信する](#リクエストボディを送信する)
- [連携先へファイルをアップロードする](#連携先へファイルをアップロードする)
- [エラーハンドリング](#エラーハンドリング)

## 連携先からファイルをダウンロードする

連携先からファイルをダウンロードする処理は、`Tasklet`で以下のように実装する。

```java
@Component
@StepScope
public class FileDownloadSampleTasklet implements Tasklet {
    @Autowired // (1)
    private RestTemplate restTemplate; // (1)
    @Autowired
    private FileDownloadSampleProperties fileDownloadSampleProperties;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        ResponseExtractor<ResponseEntity<Void>> responseExtractor = response -> { // (2)
            FileCopyUtils.copy( // (3)
                response.getBody(), // (3)
                Files.newOutputStream(fileDownloadSampleProperties.getOutputFilePath()) // (3)
            );

            return ResponseEntity.status(response.getStatusCode()) // (4)
                    .headers(response.getHeaders()) // (4)
                    .build(); // (4)
        };

        ResponseEntity<Void> response = restTemplate.execute( // (5)
            "/path/for/request", // (5)
            HttpMethod.GET, // (5)
            null, // (5)
            responseExtractor // (5)
        );

        HttpStatusCode statusCode = response.getStatusCode(); // (6)
        HttpHeaders headers = response.getHeaders(); // (6)
        // 省略
        return RepeatStatus.FINISHED;
    }
}
```

- 実装のポイント
  - (1) `RestTemplate`を`Tasklet`のフィールドに定義し、`@Autowired`を付けてフィールドインジェクションで取得する
  - (2) `ResponseExtractor<ResponseEntity<Void>>`を実装して、レスポンスのボディをファイルに出力する処理を作成する
  - (3) `FileCopyUtils`の`copy`メソッドでレスポンスのボディをファイルに出力する
    - 第一引数には、`ClientHttpResponse`の`getBody`メソッドで取得できる`InputStream`を設定する
    - 第二引数には、出力先ファイルの`OutputStream`を設定する
      - 出力先ファイルのパスはプロパティで管理し、`Properties`から受け取るようにする
      - `Properties`については[プロパティ管理](property-management.md)を参照
  - (4) `ResponseEntity<Void>`を返すように実装する
    - `ResponseEntity`の`status`メソッドでステータスコードを設定する
      - ステータスコードは、`ClientHttpResponse`の`getStatusCode`メソッドで取得できる
    - `headers`メソッドでレスポンスヘッダーを設定する
      - レスポンスヘッダーは、`ClientHttpResponse`の`getHeaders`メソッドで取得できる
    - `build`メソッドで`ResponseEntity`のインスタンスを取得する
  - (5) `RestTemplate`の`execute`メソッドでリクエストを送信する
    - 第一引数には、連携先のパスを`String`か`URI`で設定する
      - `URI`のスキームからポート番号まで（`https://xxxx:8080`）は共通的な仕組みで設定しているので、アプリケーションではパスだけを設定する
      - パスは`/`始まりで記述すること
        - ○：`/path/for/request`
        - ×：`path/for/request`
      - クエリパラメータを設定する方法についてはWebのHTTPクライアントの[クエリパラメータの設定](../web/http-client.md#クエリパラメータの設定)を参照
    - 第二引数には、HTTPメソッドを設定する
    - 第三引数には、`null`を設定する
    - 第四引数には、上で作成した`ResponseExtractor`を設定する
  - (6) `execute`メソッドが返す`ResponseEntity`から、レスポンスの情報を取得できる
    - `getStatusCode`メソッドでステータスコードを取得できる
    - `getHeaders`メソッドでレスポンスヘッダーを取得できる

### リクエストボディを送信する

POSTなどでリクエストボディを送る必要がある場合は、以下のように実装する。
なお、送信データのフォーマットはJSONを前提とする。

```java
@Component
@StepScope
public class FileDownloadSampleTasklet implements Tasklet {
    // 省略

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        FileDownloadSampleRequestDto requestDto = new FileDownloadSampleRequestDto(); // (1)
        
        // 省略

        RequestCallback requestCallback = restTemplate.httpEntityCallback(requestDto); // (2)

        ResponseEntity<Void> response = restTemplate.execute(
            "/path/for/request",
            HttpMethod.POST,
            requestCallback, // (3)
            responseExtractor
        );

        // 省略

        return RepeatStatus.FINISHED;
    }
}
```

- 実装のポイント
  - (1) リクエストボディで送信するデータを保持する`Dto`を作成する
    - `Dto`の作成方法については、WebのHTTPクライアントの[リクエストをマッピングする`Dto`の作成](../web/http-client.md#リクエストをマッピングするdtoの作成)を参照
  - (2) `RestTemplate`の`httpEntityCallback`メソッドに上で作成した`Dto`を渡し、`RequestCallback`を取得する
  - (3) `execute`メソッドの第三引数に、上で取得した`RequestCallback`を設定する

## 連携先へファイルをアップロードする

連携先へファイルをアップロードする処理は、`Tasklet`で以下のように実装する。

```java
@Component
@StepScope
public class FileUploadSampleTasklet implements Tasklet {
    @Autowired // (1)
    private RestTemplate restTemplate; // (1)
    @Autowired
    private FileUploadSampleProperties properties;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        RequestCallback requestCallback = request -> { // (2)
            FileCopyUtils.copy( // (3)
                Files.newInputStream(properties.getUploadFilePath()), // (3)
                request.getBody() // (3)
            );

            request.getHeaders().setContentType(MediaType.TEXT_PLAIN); // (4)
        };

        ResponseExtractor<ResponseEntity<FileUploadSampleResponseDto>> responseExtractor =
                restTemplate.responseEntityExtractor(FileUploadSampleResponseDto.class); // (5)

        ResponseEntity<FileUploadSampleResponseDto> response = restTemplate.execute( // (6)
            "/path/for/upload", // (6)
            HttpMethod.POST, // (6)
            requestCallback, // (6)
            responseExtractor // (6)
        );

        HttpStatusCode statusCode = response.getStatusCode(); // (7)
        HttpHeaders headers = response.getHeaders(); // (7)
        FileUploadSampleResponseDto body = response.getBody(); // (7)
        // 省略
        return RepeatStatus.FINISHED;
    }
}
```

- 実装のポイント
  - (1) `RestTemplate`を`Tasklet`のフィールドに定義し、`@Autowired`を付けてフィールドインジェクションで取得する
  - (2) `RequestCallback`を実装して、アップロードファイルをリクエストボディに書き込む処理を作成する
  - (3) `FileCopyUtils`の`copy`メソッドでファイルをリクエストボディに書き出す
    - 第一引数には、アップロードするファイルの`InputStream`を設定する
      - アップロードするファイルのパスはプロパティで管理し、`Properties`から受け取るようにする
      - `Properties`については[プロパティ管理](property-management.md)を参照
    - 第二引数には、`ClientHttpRequest`の`getBody`メソッドで取得できる`OutputStream`を設定する
  - (4) リクエストヘッダーを設定する場合は、`ClientHttpRequest`の`getHeaders`メソッドで`HttpHeaders`を取得して設定する
  - (5) `RestTemplate`の`responseEntityExtractor`メソッドを使って`ResponseExtractor`を生成する
    - 引数には、レスポンスボディをマッピングする`Dto`の`Class`オブジェクトを設定する
    - `Dto`の作成方法についてはWebのHTTPクライアントの[レスポンスをマッピングする`Dto`の作成](../web/http-client.md#レスポンスをマッピングするdtoの作成)を参照
    - レスポンスボディを受け取らない場合は`Void.class`を設定する
  - (6) `RestTemplate`の`execute`メソッドでリクエストを送信する
    - 第一引数には、連携先のパスを`String`か`URI`で設定する
      - `URI`のスキームからポート番号まで（`https://xxxx:8080`）は共通的な仕組みで設定しているので、アプリケーションではパスだけを設定する
      - パスは`/`始まりで記述すること
        - ○：`/path/for/request`
        - ×：`path/for/request`
      - クエリパラメータを設定する方法についてはWebのHTTPクライアントの[クエリパラメータの設定](../web/http-client.md#クエリパラメータの設定)を参照
    - 第二引数には、HTTPメソッドを設定する
    - 第三引数には、上で作成した`RequestCallback`を設定する
    - 第四引数には、上で作成した`ResponseExtractor`を設定する
  - (7) `execute`メソッドが返す`ResponseEntity`から、レスポンスの情報を取得できる
    - `getStatusCode`メソッドでステータスコードを取得できる
    - `getHeaders`メソッドでレスポンスヘッダーを取得できる
    - `getBody`メソッドでレスポンスボディをマッピングした`Dto`を取得できる

## エラーハンドリング

`execute`メソッドも、`getForEntity`メソッドなどと同じようにエラーごとに異なる例外をスローする。
したがって、スローされる例外の型ごとに処理を切り替えることでエラーをハンドリングできる。

どういうエラーのときにどういう例外がスローされるかについては、WebのHTTPクライアントの[エラーハンドリング](../web/http-client.md#エラーハンドリング)を参照。
