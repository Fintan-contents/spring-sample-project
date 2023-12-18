# メッセージ管理

- [メッセージの定義方法](#メッセージの定義方法)
- [メッセージの種類](#メッセージの種類)
  - [業務エラーメッセージ](#業務エラーメッセージ)
  - [精査エラーメッセージ](#精査エラーメッセージ)
  - [Itemのフィールド名](#itemのフィールド名)
  - [エラー以外の情報メッセージ](#エラー以外の情報メッセージ)

## メッセージの定義方法

`src/main/resources/messages.properties`へメッセージID=値の形式でメッセージを定義する。

```properties
ERR001=業務エラーが発生しました
MSG001=登録しました
```

## メッセージの種類

`messages.properties`に定義するメッセージは以下の4つに分類される。

- 業務エラーメッセージ
- 精査エラーメッセージ
- `Item`のフィールド名
- エラー以外の情報メッセージ

### 業務エラーメッセージ

【030_アプリ設計/010_システム機能設計/システム機能設計書】の「処理詳細」に記載されたビジネスルールのチェックでエラーとなった場合に使用するメッセージ。

業務エラー発生時は、基盤部品の`BatchSkipItemException`にメッセージIDを設定する。

```java
@Component
@StepScope
public class ImportProjectsItemProcessor implements ItemProcessor<ProjectWork, Project> {

    @Override
    public Project process(ProjectWork work) {
        if (importProjectsMapper.selectOrganizationById(work.getOrganizationId()) == null) {
            throw new BatchSkipItemException("errors.organization-not-found" // (1)
                , work.getProjectWorkId(), work.getOrganizationId()); // (1)
        }

        // その他のコードは省略
    }
}
```

- 実装のポイント
    - (1) エラーの場合、`BatchSkipItemException`をスローする
        - `BatchSkipItemException`のコンストラクタの第一引数にエラーメッセージのメッセージIDを、第二引数以降は必要に応じてエラーメッセージに埋め込む追加情報を指定する

### 精査エラーメッセージ

精査エラー時のメッセージは以下のルールに則ってメッセージIDが決定される。

- 型変換エラー  

    入力データを`Item`のフィールドに定義された型に変換できなかった場合は、`typeMismatch.`から始まるメッセージIDが採用される。
    例えば以下のようなフィールドを持つ`Item`に対して`clientId`に数値に変換できない文字列が入力された場合型変換エラーとなる。

    ```java
    public class ImportProjectsToWorkCsvItem implements LineNumberItem {
        // 精査のアノテーションやアクセサは省略
        private Integer clientId;
    }
    ```

    この場合以下のようなメッセージIDが使用可能となる。

    ```properties
    # (1)
    typeMismatch.clientId=顧客IDは数値を入力してください。
    # (2)
    typeMismatch.java.lang.Integer=数値を入力してください。
    ```

    - (1)特定フィールド（この例では`clientId`）に固有のメッセージを割り当てたい場合は、`typeMismatch.[フィールド名]`をメッセージIDとする
    - (2)特定の型への型変換エラー（この例では`java.lang.Integer`への型変換エラー）に対する汎用的なメッセージIDは、`typeMismatch.[対象となる型の完全修飾名]`とする
    - ※何らかの理由で上記以外のメッセージIDとしたい場合は[DefaultMessageCodesResolverのJavaDoc](https://docs.spring.io/spring-framework/docs/6.1.x/javadoc-api/org/springframework/validation/DefaultMessageCodesResolver.html)を参照。

- アノテーションを使用した単項目精査、項目間精査  

    アノテーションにデフォルトで設定されたメッセージIDまたは、アノテーションの`message`要素に指定されたメッセージIDが採用される。

- `Item`で完結しない精査

    [業務エラーメッセージ](#業務エラーメッセージ)同様、`BatchSkipItemException`に設定されたメッセージIDが採用される。

### Itemのフィールド名

単項目精査でエラーとなった場合の精査エラーメッセージに`Item`のフィールド名を含めるには、メッセージに`{0}`を指定する。
`Item`のフィールド名はデフォルトではJava上での`Item`のフィールド名が使用される。（以下の例では`projectName`）`messages.properties`に`Java上のフィールド名=表示したいフィールド名`を定義することで、適切な名称をメッセージに含めることができる。

以下のようなメッセージが定義された`messages.properties`と`jakarta.validation.constraints.NotNull`を付与したフィールドを持つ`Item`がある場合、`projectName`の入力がなかった場合は「PJ名を入力してください。」というメッセージが生成される。

```properties
jakarta.validation.constraints.NotNull.message={0}を入力してください。
projectName=PJ名
```

```java
@NotNull
private String projectName;
```

### エラー以外の情報メッセージ

`messages.properties`にはエラーに限らず任意のメッセージを定義することができる。
