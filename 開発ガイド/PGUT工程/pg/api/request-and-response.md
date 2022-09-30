# リクエスト・レスポンス

クエリパラメータ・パスパラメータ・リクエストボディをアプリケーションで受け取り、レスポンスを返す際の実装方法を解説する。

- [リクエストを受け取る](#リクエストを受け取る)
  - [パスパラメータの受け取り方](#パスパラメータの受け取り方)
  - [クエリパラメータの受け取り方](#クエリパラメータの受け取り方)
  - [リクエストボディの受け取り方](#リクエストボディの受け取り方)
- [レスポンスを返す](#レスポンスを返す)
  - [単一データを返すResponseの実装](#単一データを返すresponseの実装)
  - [複数件のデータを返すResponseの実装](#複数件のデータを返すresponseの実装)

## リクエストを受け取る

リクエストの受け取り方は以下の3つに分かれる。

- パスパラメータ
- クエリパラメータ
- リクエストボディ

これらのうちクエリパラメータとリクエストボディの受け取り方は非常に似ているが、異なる点があるため注意すること。

### パスパラメータの受け取り方

URLマッピングでパラメータ名を定義し、`RestController`のメソッド引数に`@PathVariable`アノテーションを付与することでパスパラメータを受け取ることができる。

```java
@GetMapping("clients/{clientId}") // (1)
public ClientShowResponse show(@PathVariable Integer clientId) { // (2)
```

上記は(1)URLマッピングに`{clientId}`というパスパラメータ名を定義し、(2)`@PathVariable`で修飾したメソッド引数`clientId`にパスパラメータをバインドする例。

パスパラメータで受け取る値は多くの場合、リソースを特定するためのIDである。
IDは整数値や、英数字と一部の記号からなる文字列(UUIDなど)であることが多いため、パスパラメータを受け取る型は以下で事足りる。

- `Integer`
- `Long`
- `String`

もし、他の型として扱いたい場合は必要に応じて業務処理内で変換すること。

### クエリパラメータの受け取り方

【030_アプリ設計/010_システム機能設計/システム機能設計書】の「入力データ定義」にあわせて`Request`を作成する。
クエリパラメータのパラメータ名と`Request`のプロパティ名は一致させる必要がある。
以下の`Request`は`?clientName=XXXX&industryCode=XXXX`のようなクエリパラメータを受け取ることができる。

```java
public class ClientSearchRequest {
    @Domain("clientName")
    private String clientName;
    @Domain("industryCode")
    private String industryCode;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getIndustryCode() {
        return industryCode;
    }

    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }
}
```

フィールドの型は次の表にあるうちのいずれかにする。

|フィールドの型|説明|主な用途|
|---|---|---|
|`String`|文字列|名前、コード値、文字列で表現される項目|
|`Integer`|整数|サロゲートキー、`Integer`の範囲で表現可能な整数項目|
|`Long`|整数|バージョン番号、`Long`の範囲で表現可能な整数項目|
|`BigDecimal`|数値|金額|
|`LocalDate`|日付|日付|
|`LocalTime`|時刻|時刻|
|`LocalDateTime`|日時|日時|
|`boolean`|真偽値|フラグ|

これらのうち`LocalDate`、`LocalTime`、`LocalDateTime`は`@DateTimeFormat`で値のフォーマットを指定すること。

```java
    @DateTimeFormat(pattern = "uuuu-MM-dd") // (1)
    private LocalDate date;

    @DateTimeFormat(pattern = "HH:mm") // (1)
    private LocalTime time;

    @DateTimeFormat(pattern = "uuuu-MM-dd HH:mm") // (1)
    private LocalDateTime dateTime;
```

- 実装のポイント
    - (1) フォーマットは`DateTimeFormatter`を使用して行われる。そのため年を表すパターンは`u`となる

作成した`Request`を`RestController`のメソッド引数に指定することで、クエリパラメータが`Request`にバインドされる。
`@Validated`アノテーションをつけると、`Request`のプロパティに付与したアノテーション（`@Domain`など）のバリデーションが実行される。

```java
@GetMapping("clients")
public ClientSearchResponse search(@Validated ClientSearchRequest request) {
```

`@RequestParam`でクエリパラメータを個別に取得することはせず、`Request`としてまとめて受け取ること。

### リクエストボディの受け取り方

主にJSONを受け取る際、この方法を使用する。
【030_アプリ設計/010_システム機能設計/システム機能設計書】の「入力データ定義」から参照されるIF定義にあわせて`Request`を作成する。

```java
public class ClientCreationRequest {
    @Required
    @Domain("clientName")
    private String clientName;

    @Required
    @Domain("industryCode")
    private String industryCode;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getIndustryCode() {
        return industryCode;
    }

    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }
}
```

フィールドの型は次の表にあるうちのいずれか、あるいは後述するネストした`Request`にする。

|フィールドの型|説明|主な用途|
|---|---|---|
|`String`|文字列|名前、コード値、文字列で表現される項目|
|`Integer`|整数|サロゲートキー、`Integer`の範囲で表現可能な整数項目|
|`Long`|整数|バージョン番号、`Long`の範囲で表現可能な整数項目|
|`BigDecimal`|数値|金額|
|`LocalDate`|日付|日付|
|`LocalTime`|時刻|時刻|
|`LocalDateTime`|日時|日時|
|`boolean`|真偽値|フラグ|

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

また、JSONは構造化されたデータ形式なため、`Request`をネストする場合がある。

例えば次のようなJSONがあったとする。

```json
{
    "foo": "hello",
    "bar": {
        "baz": "world"
    }
}
```

このJSONを受け取る`Request`は次のようにネストされたものになる。

```java
public class ExampleRequest {
    private String foo;
    private ExampleNestedRequest bar;

    // getter、setterは省略

    public static class ExampleNestedRequest {
        private String baz;

        // getter、setterは省略
    }
}
```

作成した`Request`を`RestController`のメソッド引数に指定し、`@RequestBody`アノテーションを付与することでリクエストボディが`Request`にバインドされる。
`@Validated`アノテーションをつけると、`Request`のプロパティに付与したアノテーション（`@Domain`など）のバリデーションが実行される。

```java
@PostMapping("clients")
@ResponseStatus(HttpStatus.CREATED)
public ClientCreationResponse create(@RequestBody @Validated ClientCreationRequest request) {
```

## レスポンスを返す

`RestController`は任意の`Response`をJSONオブジェクトとして返却することができる。

`RestController`の戻り値には必ず`Response`を使用すること。`Response`には、項目の取得元に関わらず【030_アプリ設計/010_システム機能設計/システム機能設計書】の「データ出力定義」に従った項目を明示的に定義する。

特にレスポンスの内容が`Model`の情報を元に構成される場合、`Model`をそのままレスポンスとすると実装が容易だと感じられるかもしれない。
しかし、`Model`には外部に公開されるべきではない情報を含んでいるケースもあり、また初期の定義では「データ出力定義」と一致していても、その後の項目追加などで意図せず情報を外部公開してしまうことも考えられる。

このようなリスクを回避するため、`RestController`のレスポンスは`Model`の定義に依存せず、「データ出力定義」に沿って`Response`を明示的に定義する方針とする。

また、`Response`と`Model`のプロパティが一致する場合、上記の方針に従うと`Response`から`Model`への単純な値のコピー処理を実装する必要がある。
単純なコピー処理を`RestController`のロジックの途中に書いてしまうと可読性が低下するため、[単一データを返すResponseの実装](#単一データを返すresponseの実装)の例にあるように`Model`から`Response`に変換するメソッドを`Response`に定義することを推奨する。

### 単一データを返すResponseの実装

【030_アプリ設計/010_システム機能設計/システム機能設計書】の「データ出力定義」にあわせて`Response`を作成する。

```java
public class ClientShowResponse {
    private Integer clientId;
    private String clientName;
    private String industryCode;
    private Long versionNo;

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getIndustryCode() {
        return industryCode;
    }

    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }

    public Long getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Long versionNo) {
        this.versionNo = versionNo;
    }

    public static ClientShowResponse fromClient(Client client) {
        ClientShowResponse response = new ClientShowResponse();
        BeanUtils.copyProperties(client, response);
        return response;
    }
}
```

フィールドの型は次の表にあるうちのいずれか、あるいは後述するネストした`Response`にする。

|フィールドの型|説明|主な用途|
|---|---|---|
|`String`|文字列|名前、コード値、文字列で表現される項目|
|`Integer`|整数|サロゲートキー、`Integer`の範囲で表現可能な整数項目|
|`Long`|整数|バージョン番号、`Long`の範囲で表現可能な整数項目|
|`BigDecimal`|数値|金額|
|`LocalDate`|日付|日付|
|`LocalTime`|時刻|時刻|
|`LocalDateTime`|日時|日時|
|`boolean`|真偽値|フラグ|

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

また、JSONは構造化されたデータ形式なため、`Response`をネストする場合がある。

例えば次のようなJSONがあったとする。

```json
{
    "foo": "hello",
    "bar": {
        "baz": "world"
    }
}
```

このJSONを返す`Response`は次のようにネストされたものになる。

```java
public class ExampleResponse {
    private String foo;
    private ExampleNestedResponse bar;

    // getter、setterは省略

    public static class ExampleNestedResponse {
        private String baz;

        // getter、setterは省略
    }
}
```

### 複数件のデータを返すResponseの実装

一覧検索のように複数件のデータを返却する場合は、`Response`のプロパティとしてコレクションを持つよう実装する。
コレクションの要素についても`Model`は使わず、専用のオブジェクトを定義し必要以上に情報が公開されないように制御する。

```java
public class ClientSearchResponse {
    private List<ClientResponse> clients;

    public List<ClientResponse> getClients() {
        return clients;
    }

    public void setClients(List<ClientResponse> clients) {
        this.clients = clients;
    }

    public static ClientSearchResponse fromClientList(List<Client> clientList) {
        ClientSearchResponse response = new ClientSearchResponse();
        response.setClients(
                clientList.stream().map(client -> {
                    ClientResponse clientResponse = new ClientResponse();
                    BeanUtils.copyProperties(client, clientResponse);
                    return clientResponse;
                }).collect(Collectors.toList())
        );
        return response;
    }

    public static class ClientResponse {
        private Integer clientId;
        private String clientName;
        private String industryCode;
        private Long versionNo;

        public Integer getClientId() {
            return clientId;
        }

        public void setClientId(Integer clientId) {
            this.clientId = clientId;
        }

        public String getClientName() {
            return clientName;
        }

        public void setClientName(String clientName) {
            this.clientName = clientName;
        }

        public String getIndustryCode() {
            return industryCode;
        }

        public void setIndustryCode(String industryCode) {
            this.industryCode = industryCode;
        }

        public Long getVersionNo() {
            return versionNo;
        }

        public void setVersionNo(Long versionNo) {
            this.versionNo = versionNo;
        }
    }
}
```
