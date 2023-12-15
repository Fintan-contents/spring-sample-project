# HTTPクライアント

- [基本的なリクエストの送信方法](#基本的なリクエストの送信方法)
  - [リクエストをマッピングするDtoの作成](#リクエストをマッピングするdtoの作成)
  - [レスポンスをマッピングするDtoの作成](#レスポンスをマッピングするdtoの作成)
  - [リクエストの送信](#リクエストの送信)
- [パスパラメータの設定](#パスパラメータの設定)
- [クエリパラメータの設定](#クエリパラメータの設定)
- [任意のヘッダーを設定する](#任意のヘッダーを設定する)
- [エラーハンドリング](#エラーハンドリング)

ここでは、サーバーから他システムのWeb APIに連携する方法について説明する。
なお、送受信するボディのデータはJSON形式であることを前提としている。

## 基本的なリクエストの送信方法

ここでは、POSTメソッドでリクエストを送信する場合を例にして、基本的なリクエストの送信方法について説明する。

### リクエストをマッピングするDtoの作成

【030_アプリ設計/030_インタフェース設計/外部インタフェース設計書】の定義にしたがって`Dto`を作成する。

```java
public class PostRequestSampleRequestDto { // (1)
    private Integer clientId; // (2)

    public Integer getClientId() { // (3)
        return clientId;
    }

    public void setClientId(Integer clientId) { // (3)
        this.clientId = clientId;
    }
}
```

- 実装のポイント
  - (1) クラス名は論理的な名前か取引IDを元に命名する。  
    このとき、接尾子は`RequestDto`とする（取引IDを用いる場合は`BA10101RequestDto`など）
  - (2) 【030_アプリ設計/030_インタフェース設計/外部インタフェース設計書】のレコードの定義にしたがってフィールドを定義する
    - フィールド名には「項目ID」を使用する
  - (3) アクセサを作成する

フィールドの型は次の表にあるうちのいずれか、あるいは後述するネストした`Dto`にする。

|フィールドの型|説明|主な用途|
|---|---|---|
|`String`|文字列|名前、コード値、文字列で表現される項目|
|`Integer`|整数|サロゲートキー、`Integer`の範囲で表現可能な整数項目|
|`Long`|整数|バージョン番号、`Long`の範囲で表現可能な整数項目|
|`BigDecimal`|数値|金額|
|`LocalDate`|日付|日付|
|`LocalTime`|時刻|時刻|
|`LocalDateTime`|日時|日時|
|`boolean`|真偽値|フラグなどの真偽値|
|`List`|リスト|複数の値をリストで受け取る（`String`のリストであれば`List<String>`など）|


これらのうち`LocalDate`、`LocalTime`、`LocalDateTime`は`@JsonFormat`で値のフォーマットを指定すること。

```java
    @JsonFormat(pattern = "uuuu-MM-dd") // (1)
    private LocalDate date;

    @JsonFormat(pattern = "HH:mm") // (1)
    private LocalTime time;

    @JsonFormat(pattern = "uuuu-MM-dd HH:mm") // (1)
    private LocalDateTime dateTime;
```

- 実装のポイント
  - (1) フォーマットは`DateTimeFormatter`を使用して行われる。そのため年を表すパターンは`u`となる

また、ネストした`Dto`や、ネストした`Dto`の`List`を送信したい場合がある。
そういった場合、`Dto`の中に`Dto`を作成して、その`Dto`や、`Dto`の`List`を持つフィールドを作成する。

```java
public class PostRequestSampleRequestDto {
    private PostRequestSampleSubRequestDto subRequestDto; // (2)

    private List<PostRequestSampleSubRequestDto> subRequestDtoList; // (2)

    public PostRequestSampleSubRequestDto getSubRequestDto() { // (3)
        return subRequestDto;
    }

    public void setSubRequestDto(PostRequestSampleSubRequestDto subRequestDto) { // (3)
        this.subRequestDto = subRequestDto;
    }

    public List<PostRequestSampleSubRequestDto> getSubRequestDtoList() { // (3)
        return subRequestDtoList;
    }

    public void setSubRequestDtoList(List<PostRequestSampleSubRequestDto> subRequestDtoList) { // (3)
        this.subRequestDtoList = subRequestDtoList;
    }

    public static class PostRequestSampleSubRequestDto { // (1)
        private Integer projectId;
        private String projectName;

        // アクセサは省略
    }
}
```

- 実装のポイント
  - (1) ネストした`Dto`を作成する。アクセス修飾子は`public static`とすること
  - (2) ネストした`Dto`や、`Dto`の`List`を持つフィールドを作成する
  - (3) アクセサを作成する

### レスポンスをマッピングするDtoの作成

【030_アプリ設計/030_インタフェース設計/外部インタフェース設計書】の定義にしたがって`Dto`を作成する。

```java
public class PostRequestSampleResponseDto { // (1)
    private Integer clientId;

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }
}
```

- 実装のポイント
  - (1) クラス名は論理的な名前か取引IDを元に命名する。  
    このとき、接尾子は`ResponseDto`とする（取引IDを用いる場合は`BA10101ResponseDto`など）

その他のポイントはリクエストをマッピングする`Dto`と同じであるため、[リクエストをマッピングする`Dto`の作成](#リクエストをマッピングするdtoの作成)を参照。


### リクエストの送信

```java
public class PostRequestSamapleService {
    @Autowired // (1)
    private RestTemplate restTemplate; // (1)

    // 省略
    
    public Integer execute() {
        PostRequestSampleRequestDto requestDto = new PostRequestSampleRequestDto();
        // 省略
        ResponseEntity<PostRequestSampleResponseDto> response = restTemplate.postForEntity( // (2), (6)
            "/path/for/request", // (3)
            requestDto, // (4)
            PostRequestSampleResponseDto.class // (5)
        );

        HttpStatusCode statusCode = response.getStatusCode(); // (7)
        HttpHeaders headers = response.getHeaders(); // (8)
        PostRequestSampleResponseDto responseDto = response.getBody(); // (9)
        // 省略
    }
}
```

- 実装のポイント
  - (1) HTTPリクエストの送信には、Springが提供する`RestTemplate`を使用する。
    - `RestTemplate`のインスタンスは、Web APIの連携を行うクラスに`RestTemplate`のフィールドを定義し`@Autowired`を付けることでフィールドインジェクションで取得する
  - (2) `postForEntity`メソッドで、POSTリクエストを送信できる
    - この他にも、`RestTemplate`にはHTTPメソッドごとにリクエスト送信用のメソッドが用意されている。これらは、対応するHTTPメソッドの名前で始まるようになっており、例えばGETメソッドには`getForEntity`というメソッドが用意されている。  
      個々のメソッドの詳しい使い方は、[`RestTemplate`のJavadoc](https://docs.spring.io/spring-framework/docs/6.1.x/javadoc-api/org/springframework/web/client/RestTemplate.html)を参照
  - (3) `postForEntity`メソッドの第一引数には、送信先のパスを設定する
    - URLのスキームからポート番号までのベース部分（`https://xxxx:8080`）は共通的な仕組みで設定済みなので、各リクエストではパスを設定するだけとなる
    - パスの先頭は`/`から始めること
      - ○：`/path/for/request`
      - ×：`path/for/request`
  - (4) `postForEntity`メソッドの第二引数には、リクエストボディで送信する`Dto`を設定する
    - もし、リクエストボディで送信するデータが無い場合は`null`を設定する
  - (5) `postForEntity`メソッドの第三引数には、レスポンスボディをマッピングする`Dto`の`Class`オブジェクトを設定する
    - もし、レスポンスボディを受け取る必要が無い場合は`Void.class`を設定する
  - (6) レスポンスは`ResponseEntity<レスポンスのDto>`で受け取る
  - (7) `ResponseEntity`の`getStatusCode`メソッドで、HTTPステータスコードを取得できる
  - (8) `ResponseEntity`の`getHeaders`メソッドで、レスポンスヘッダーを取得できる
  - (9) `ResponseEntity`の`getBody`メソッドで、レスポンスボディをマッピングした`Dto`を取得できる

## パスパラメータの設定

パスの一部にパラメータを埋め込む方法を説明する。

```java
ResponseEntity<PostRequestSampleResponseDto> response = restTemplate.postForEntity(
    "/path/for/request/{param1}/{param2}", // (1)
    requestDto,
    PostRequestSampleResponseDto.class,
    "foo", "bar" // (2)
);
```

- 実装のポイント
  - (1) パラメータを埋め込む部分を`{パラメータ名}`のように記述する
  - (2) `postForEntity`や`getForEntity`などのリクエスト送信メソッドの最後の引数は、パスパラメータに埋め込む値を設定できるようになっている
    - 可変長引数になっており、複数指定した場合は指定した順序でパラメータが埋め込まれる
    - 上記実装の場合、パスは`/path/for/request/foo/bar`と組み立てられる

また、埋め込むパラメータを`Map`で指定することもできる。

```java
ResponseEntity<PostRequestSampleResponseDto> response = restTemplate.postForEntity(
    "/path/for/request/{param1}/{param2}",
    requestDto,
    PostRequestSampleResponseDto.class,
    Map.of("param1", "foo", "param2", "bar") // (1)
);
```

- 実装のポイント
  - (1) 可変長引数の代わりに`Map`を渡す
    - `Map`のキーに、`{パラメータ名}`のパラメータ名を設定する
    - `Map`の値に、埋め込む値を設定する

## クエリパラメータの設定

リクエストURIにクエリパラメータを設定する方法を説明する。

```java
URI uri = UriComponentsBuilder.fromPath("/path/to/request") // (1)
        .queryParam("clientId", 12) // (2)
        .build() // (3)
        .toUri(); // (3)

restTemplate.postForEntity(uri, requestDto, PostRequestSampleResponseDto.class); // (4)
```

- 実装のポイント
  - (1) `UriComponentsBuilder`を使って`URI`を生成する
    - `fromPath`メソッドでパスを設定する
  - (2) `queryParam`メソッドでクエリパラメータを設定する
    - 第一引数にクエリパラメータのキーを設定する
    - 第二引数にクエリパラメータの値を設定する
  - (3) `build()`で`UriComponents`が取得でき、`toUri`メソッドで`URI`を取得できる
  - (4) 取得した`URI`オブジェクトを`RestTemplate`のリクエスト送信用のメソッドの引数に渡す
    - 上記例では、`postForEntity`の第一引数で渡している

なお、同時にパスパラメータも設定する必要がある場合は、以下のように実装する。

```java
URI uri = UriComponentsBuilder.fromPath("/path/to/request/{pathParam}") // (1)
        .queryParam("clientId", 12)
        .build("test"); // (2)
```

- 実装のポイント
  - (1) パス文字列の中でパラメータを埋め込みたい部分を`{パラメータ名}`と記述する
  - (2) `build`メソッドの引数で、パラメータ部分に埋め込む値を設定する
    - このとき、戻り値の型は直接`URI`になる
    - `build`の引数は可変長引数になっており、複数のパラメータを受け取ることができる。その場合は指定した順番でパラメータ部分に値が埋め込まれる
    - `build`は`Map`を受け取ることもできる。その場合の挙動は`postForEntity`などと同じなので[パスパラメータの設定](#パスパラメータの設定)を参照


## 任意のヘッダーを設定する

任意のリクエストヘッダーを設定してリクエストを送信する場合の方法について説明する。

```java
RequestEntity<PostRequestSampleRequestDto> requestEntity =
    RequestEntity.post("/path/to/request") // (1)
        .contentType(MediaType.TEXT_PLAIN) // (2)
        .contentLength(1200) // (2)
        .accept(MediaType.APPLICATION_XML) // (2)
        .acceptCharset(StandardCharsets.UTF_8) // (2)
        .header("Foo-Header", "header value") // (3)
        .body(requestDto); // (4)

ResponseEntity<PostRequestSampleResponseDto> exchange = restTemplate
    .exchange(requestEntity, PostRequestSampleResponseDto.class); // (1), (5)
```

- 実装のポイント
  - (1) 任意のリクエストヘッダーを設定してリクエストを送信する場合は、`RequestEntity`を作成して`exchange`メソッドでリクエストを送信する
    - `RequestEntity`のインスタンスは、`RequestEntity`に用意された`static`メソッドで生成する
    - HTTPメソッドごとにメソッドが用意されているので、それを利用する。ここでは、`post`メソッドを使ってPOSTメソッド用のインスタンスを生成している。その他のメソッドについては[RequestEntityのJavadoc](https://docs.spring.io/spring-framework/docs/6.1.x/javadoc-api/org/springframework/http/RequestEntity.html)を参照
    - `post`メソッドの引数には、パスを設定する
  - (2) 一部のよく設定されるヘッダーは、専用の設定用のメソッドが用意されている
  - (3) それ以外の任意のヘッダーを設定したい場合は、`header`メソッドを使用する
    - 第一引数に、ヘッダーの名前を設定する
    - 第二引数に、ヘッダーの値を設定する
  - (4) リクエストボディを持つことができるHTTPメソッドの場合は、`body`メソッドを呼ぶことで`RequestEntity`が生成される
    - `body`メソッドの引数には、リクエストボディで送信する`Dto`を設定する
    - ボディを送信する必要が無い場合や、GETメソッドなどボディを送信できないHTTPメソッドの場合は、`build`メソッドを呼ぶことでボディを設定せずに`RequestEntity`を取得できる
  - (5) `RestTemplate`の`exchange`メソッドでリクエストを送信する
    - 第一引数に、作成した`RequestEntity`を設定する
    - 第二引数には、レスポンスボディをマッピングする`Dto`の`Class`オブジェクトを設定する
      - レスポンスボディを受け取る必要が無い場合は`Void.class`を設定する


## エラーハンドリング

HTTPリクエストの結果エラー系のステータスコード（4xx, 5xx）が返された場合や通信障害が発生した場合に、エラーをどのようにすればハンドリングできるかについて説明する。

いずれのエラーも`RestTemplate`が例外をスローするため、例外を`catch`することでハンドリングができる。

```java
try {
    ResponseEntity<PostRequestSampleResponseDto> response =
        restTemplate.getForEntity(uri, PostRequestSampleResponseDto.class);
    // 省略
} catch (HttpClientErrorException e) { // (1)
    // 4xx系のレスポンスが返された場合
    HttpStatusCode statusCode = e.getStatusCode(); // (5)
    HttpHeaders responseHeaders = e.getResponseHeaders(); // (5)
    String responseBodyAsString = e.getResponseBodyAsString(); // (5)
} catch (HttpServerErrorException e) { // (2)
    // 5xx系のレスポンスが返された場合
} catch (ResourceAccessException e) { // (3)
    // 通信障害が発生した場合
} catch (RestClientException e) { // (4)
    // その他の例外が発生した場合
}
```

- 実装のポイント
  - (1) 4xx系のステータスコードが返された場合は、`HttpClientErrorException`がスローされる
  - (2) 5xx系のステータスコードが返された場合は、`HttpServerErrorException`がスローされる
  - (3) 連携先のサーバーに接続できないなどの通信障害が発生した場合は、`ResourceAccessException`がスローされる
  - (4) その他の例外は、`RestClientException`を`catch`することでハンドリングできる
  - (5) `HttpClientErrorException`または`HttpServerErrorException`からは、レスポンスの情報を取得できる
    - `getStatusCode`メソッドで、具体的なステータスコードを取得できる
    - `getResponseHeaders`メソッドで、レスポンスヘッダーを取得できる
    - `getResponseBodyAsString`メソッドで、レスポンスボディを`String`で取得できる
      - `getResponseBodyAsByteArray`メソッドを使うと、レスポンスボディを`byte[]`で取得することもできる

