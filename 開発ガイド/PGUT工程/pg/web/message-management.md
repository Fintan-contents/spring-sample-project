# メッセージ管理

- [メッセージの定義方法](#メッセージの定義方法)
- [メッセージの種類](#メッセージの種類)
  - [業務エラーメッセージ](#業務エラーメッセージ)
  - [精査エラーメッセージ](#精査エラーメッセージ)
  - [フォームコントロールの名前](#フォームコントロールの名前)
  - [エラー以外の情報メッセージ](#エラー以外の情報メッセージ)
- [画面へエラーメッセージを表示する方法](#画面へエラーメッセージを表示する方法)
- [画面へエラー以外のメッセージを表示する方法](#画面へエラー以外のメッセージを表示する方法)

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
- フォームコントロールの名前
- エラー以外の情報メッセージ

### 業務エラーメッセージ

【030_アプリ設計/010_システム機能設計/システム機能設計書】の「画面イベント詳細」に記載されたビジネスルールのチェックでエラーとなった場合に使用するメッセージ。

業務エラー発生時は、基盤部品の`ApplicationException`にメッセージIDを設定する。

```java
@Service
@Transactional
public class ProjectUpdateService {

    @Transactional(readOnly = true)
    public ProjectDto findProjectById(Integer projectId) {
        ProjectAndOrganization projectAndOrganization = mapper.selectProjectByPrimaryKey(projectId);
        if (projectAndOrganization == null) {                
            throw ApplicationException.globalError("E0002"); // (1)
        }

    // その他のコードは省略
```

- 実装のポイント
    - (1) エラーの場合、`ApplicationException`をスローする
        - エラーの原因が特定のフィールドである場合は、フィールド名とメッセージIDを指定して`ApplicationException`の`fieldError`メソッドを使用する
        - エラーの原因がそれ以外の場合は、メッセージIDを指定して`ApplicationException`の`globalError`メソッドを使用する

### 精査エラーメッセージ

精査エラー時のメッセージは以下のルールに則ってメッセージIDが決定される。

- 型変換エラー  

    `Form`のフィールドに定義された型にリクエストパラメータが変換できなかった場合は、`typeMismatch.`から始まるメッセージIDが採用される。
    例えば以下のようなフィールドを持つ`Form`に対して`clientId`に数値に変換できない文字列が入力された場合型変換エラーとなる。

    ```java
    public class ProjectCreateForm {
        // 精査のアノテーションやアクセサは省略
        private Integer clientId;
    }
    ```

    この場合以下のようなメッセージIDが使用可能となる。

    ```properties
    # (1)
    typeMismatch.projectCreateForm.clientId=プロジェクト情報の顧客IDは数値を入力してください。
    # (2)
    typeMismatch.clientId=顧客IDは数値を入力してください。
    # (3)
    typeMismatch.java.lang.Integer=数値を入力してください。
    ```

    - (1) 特定`Form`の特定フィールド（この例では`ProjectCreateForm`の`clientId`）に固有のメッセージを割り当てたい場合は、`typeMismatch.[Form名].[フィールド名]`をメッセージIDとする。`Form`名はクラスの単純名をlowerCamelCaseにしたものとなる
    - (2)`Form`は限定しないが特定フィールド（この例では`clientId`）に固有のメッセージを割り当てたい場合は、`typeMismatch.[フィールド名]`をメッセージIDとする
    - (3)特定の型への型変換エラー（この例では`java.lang.Integer`への型変換エラー）に対する汎用的なメッセージIDは、`typeMismatch.[対象となる型の完全修飾名]`とする
    - ※何らかの理由で上記以外のメッセージIDとしたい場合は[DefaultMessageCodesResolverのJavaDoc](https://docs.spring.io/spring-framework/docs/6.1.x/javadoc-api/org/springframework/validation/DefaultMessageCodesResolver.html)を参照。

- アノテーションを使用した単項目精査、項目間精査  

    アノテーションにデフォルトで設定されたメッセージIDまたは、アノテーションの`message`要素に指定されたメッセージIDが採用される。

- フォームで完結しない精査

    [業務エラーメッセージ](#業務エラーメッセージ)同様、`ApplicationException`に設定されたメッセージIDが採用される。

### フォームコントロールの名前

単項目精査でエラーとなった場合の精査エラーメッセージにフォームコントロールの名前を含めるには、メッセージに`{0}`を指定する。
フォームコントロールの名前はデフォルトでは`Form`のフィールド名が使用される。（以下の例では`projectName`）`messages.properties`に`フィールド名=表示したいフォームコントロールの名前`を定義することで、適切な名称をメッセージに含めることができる。

以下のようなメッセージが定義された`messages.properties`と`jakarta.validation.constraints.NotNull`を付与したフィールドを持つ`Form`がある場合、`projectName`の入力がなかった場合は「PJ名を入力してください。」というメッセージが生成される。

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

## 画面へエラーメッセージを表示する方法

[画面テンプレートのガイド](./view-template.md#画面へエラーメッセージを表示する方法)を参照。

## 画面へエラー以外のメッセージを表示する方法

[画面テンプレートのガイド](./view-template.md#画面へエラー以外のメッセージを表示する方法)を参照。
