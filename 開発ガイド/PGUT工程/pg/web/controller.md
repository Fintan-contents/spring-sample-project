# Controllerの作成

HTTPリクエストを受け取り遷移先画面テンプレートのパスを返却する`Controller`の実装方法について解説する。

- [Controllerの作成単位](#controllerの作成単位)
- [Controllerの実装方法](#controllerの実装方法)
  - [クラスの作成](#クラスの作成)
  - [HTTPリクエストをハンドリングするメソッドの作成](#httpリクエストをハンドリングするメソッドの作成)
    - [URLマッピング](#urlマッピング)
    - [戻り値](#戻り値)
- [Controllerのメソッド命名ルール](#controllerのメソッド命名ルール)

## Controllerの作成単位

`Controller`は【030_アプリ設計/010_システム機能設計/システム機能一覧】の取引単位に作成する。

## Controllerの実装方法

### クラスの作成

`Controller`のクラスには`@Controller`と`@RequestMapping`アノテーションを付与する。

```java
@Controller
@RequestMapping("project/detail")
public class ProjectDetailController {
```

`Controller`のクラス名は論理的な名前または取引IDをもとに命名する（取引IDを使う場合は `WA10101Controller` など）。

`@RequestMapping`には【030_アプリ設計/010_システム機能設計/リクエスト一覧】のURLのうち、取引内で共通となるURLを設定する。URLの残りの部分とHTTPメソッドについては後述する方法で`Controller`のメソッドにマッピングする。

### HTTPリクエストをハンドリングするメソッドの作成

#### URLマッピング

【030_アプリ設計/010_システム機能設計/リクエスト一覧】のURLごとにHTTPリクエストをハンドリングするメソッドを作成する。

```java
@Controller
@RequestMapping("project/search")
public class ProjectSearchController {
    @GetMapping("search") // (1) (2)
    public String search(@Validated ProjectSearchForm form, // (3)
            BindingResult bindingResult, Model model) {
```

- (1)アノテーションによってエンドポイント（HTTPメソッド、URL）と`Controller`のメソッドをマッピングする
    - 利用できるアノテーションは以下

    | HTTPメソッド | 対応するアノテーション |
    |--------------|------------------------|
    | GET          | `@GetMapping`          |
    | POST         | `@PostMapping`         |

- (2)アノテーションに【030_アプリ設計/010_システム機能設計/リクエスト一覧】のURLのうち、`@RequestMapping`に設定したURLからの相対パスを指定する
- (3)リクエストパラメータを受け取る場合は`Form`をメソッド引数に指定する

#### 戻り値

戻り値は`String`型とし、遷移先画面テンプレートのパスを返却する。
遷移先画面テンプレートは`src/main/resources/templates/[機能名]/[取引名]/[メソッド名].html`が基本となる。
メソッドの戻り値には`src/main/resources/templates`からの相対パスから拡張子を除いた`[機能名]/[取引名]/[メソッド名]`を指定する。

```shell
src
 └ main
   └ resources
      └ templates
         └ project
            └ search
               └ index.html
```

上記のようなディレクトリ構成で、index.htmlに遷移する場合の`Controller`の実装は以下の通り。

```java
@Controller
@RequestMapping("project/search")
@SessionAttributes(types = ProjectSearchForm.class)
public class ProjectSearchController {
    @GetMapping
    public String index(ProjectSearchForm form) {
        // 処理は省略

        return "project/search/index";
    }
```

## Controllerのメソッド命名ルール

|画面パターン|操作|命名ルール|
|---|---|---|
|検索一覧|初期表示|`index`|
|検索一覧|検索、ページング|`search`|
|照会|初期表示|`index`|
|登録/更新/削除|初期表示（入力画面）|`index`|
|登録/更新/削除|確認画面への遷移|`confirm`|
|登録/更新/削除|確認画面から入力画面へ戻る|`back`|
|登録/更新/削除|登録/更新/削除|`execute`|
|登録/更新/削除|完了|`complete`|
|（共通）|戻る|`back`、または`backTo<戻り先の画面名>`|

※画面パターンが「（共通）」となっているものは、いずれの画面パターンでも使えることを表している
