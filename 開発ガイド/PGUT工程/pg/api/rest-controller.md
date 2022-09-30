# RestControllerの作成

HTTPリクエストを受け取りレスポンスを返却する`RestController`の実装方法について解説する。

- [RestControllerの作成単位](#restcontrollerの作成単位)
- [RestControllerの実装方法](#restcontrollerの実装方法)
  - [クラスとメソッドの作成](#クラスとメソッドの作成)
  - [URLマッピング](#urlマッピング)
  - [HTTPステータスコード](#httpステータスコード)
- [RestControllerのメソッド命名ルール](#restcontrollerのメソッド命名ルール)

## RestControllerの作成単位

`RestController`は「WebサービスAPI一覧」のAPI単位に作成する。  
そうすることで実装担当者間での競合を最小に抑え開発効率の向上が期待できる。

## RestControllerの実装方法

### クラスとメソッドの作成

`RestController`のクラスには`@RestController`アノテーションを付与する。

```java
@RestController
public class ClientDetailController {
```

`RestController`のクラス名は論理的な名前または取引IDをもとに命名する（取引IDを使う場合は `B10101Controller` など）。

`RestController`にHTTPリクエストをハンドリングするメソッドを作成する。  
メソッドの引数には`Request`、戻り値には`Response`のステレオタイプを使用する。  
（`Request`、`Response`の作成については[リクエスト・レスポンス](./request-and-response.md)を参照。）  
HTTPリクエストをハンドリングするメソッドには、後述する方法でURLマッピングと返却するHTTPステータスコードを設定する。

[RestControllerの作成単位](#restcontrollerの作成単位)の方針に則り、`RestController`とHTTPリクエストをハンドリングするメソッドは1対1の関係になる。

### URLマッピング

【030_アプリ設計/010_システム機能設計/システム機能設計書】の「Webサービス取引概要」の「リクエストURL」および「HTTPメソッド」に記載されたリクエストURL・HTTPメソッドと`RestController`とのマッピングは、Springのアノテーションを使って実装する。

以下に例を示す。

```java
@RestController
public class ClientDetailController {
    @GetMapping("clients/{clientId}") // (1) (2)
    public ClientDetailResponse getClient(@PathVariable Integer clientId) {
```

- (1)アノテーションによってエンドポイント（HTTPメソッド、URL）と`RestController`のメソッドをマッピングする
    - 利用できるアノテーションは以下

    | HTTPメソッド | 対応するアノテーション |
    |--------------|------------------------|
    | GET          | `@GetMapping`          |
    | PUT          | `@PutMapping`          |
    | POST         | `@PostMapping`         |
    | DELETE       | `@DeleteMapping`       |

- (2)アノテーションにリクエストURLを指定する
    - リクエストURLにパスパラメータを含む場合は、`{`と`}`で囲まれた名前と`@PathVariable`を付けた引数名を一致させることでパスパラメータを表現することができる。詳しくは[パスパラメータの受け取り方](./request-and-response.md#パスパラメータの受け取り方)を参照。

### HTTPステータスコード

【030_アプリ設計/010_システム機能設計/システム機能設計書】の「処理結果一覧」に記載されたHTTPステータスコードが、`200 (OK)`以外の場合は、`@ResponseStatus`アノテーションを使って返却するHTTPステータスコードを設定する。
メソッドが正常終了した場合はデフォルトで`200 (OK)`が返却されるため、`200 (OK)`を明示的に設定する必要はない。

以下にPOSTメソッドによってリソースを作成し、HTTPステータスコードとして`201 (Created)`の成功レスポンスを返却する例を示す。

```java
@PostMapping("clients")
@ResponseStatus(HttpStatus.CREATED)
public ClientCreationResponse createClient(@RequestBody @Validated ClientCreationRequest request) {
```

`@ResponseStatus`アノテーションのvalue属性には、`org.springframework.http.HttpStatus`列挙型を指定する。


## RestControllerのメソッド命名ルール

|操作|命名ルール|例|
|---|---|---|
|複数件のリソースを検索する|`search<リソース名>`|`searchClient`|
|1件のリソースを取得する|`get<リソース名>`|`getClient`|
|リソースを作成する|`create<リソース名>`|`createClient`|
|リソースを更新する|`update<リソース名>`|`updateClient`|
|リソースを削除する|`delete<リソース名>`|`deleteClient`|
|※上記以外の操作|動詞から始まる名前|`login`、`execute`|
