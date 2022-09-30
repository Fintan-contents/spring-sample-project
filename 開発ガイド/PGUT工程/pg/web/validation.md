# 入力値精査

- [単項目精査](#単項目精査)
  - [String配列の単項目精査](#string配列の単項目精査)
- [項目間精査](#項目間精査)
- [ネストした項目の精査](#ネストした項目の精査)
- [Formで完結しない精査](#formで完結しない精査)

## 単項目精査

【030_アプリ設計/010_システム機能設計/システム機能設計書】の「画面項目定義」をもとにして単項目精査のアノテーションを付ける。

```java
    @Required                   // (1)
    @Domain("projectName")      // (2)
    private String projectName;
```

- 実装のポイント
    - (1) 「画面項目定義」の「必須」に「○」が付いている場合は`@Required`を付ける
    - (2) 「画面項目定義」の「ドメイン名」が書かれている場合は`@Domain`を付ける
        - `@Domain`に設定する値は【030_アプリ設計/070_データモデル設計/ドメイン定義書】の印刷範囲外にある「ドメイン名（物理）」である。「画面項目定義」の「ドメイン名」と、【030_アプリ設計/070_データモデル設計/ドメイン定義書】の「ドメイン名（論理）」を突き合わせてドメインを特定すること

### String配列の単項目精査

`String`配列のフィールドに対しては`@Domain`ではなく`@ArrayDomain`を付ける。

```java
    @ArrayDomain("projectType") // (1)
    private String[] projectTypes = {};
```

- 実装のポイント
    - (1) `@ArrayDomain`を付けることで`String`配列の各要素に対して精査が行われる
        - `@ArrayDomain`に設定する値は【030_アプリ設計/070_データモデル設計/ドメイン定義書】の印刷範囲外にある「ドメイン名（物理）」である。「画面項目定義」の「ドメイン名」と、【030_アプリ設計/070_データモデル設計/ドメイン定義書】の「ドメイン名（論理）」を突き合わせてドメインを特定すること

## 項目間精査

項目間精査は`Form`に`@AssertTrue`を付けたメソッドを作成することで行う。

```java
    @AssertTrue(message = "{E0001}") // (1)
    public boolean isValidProjectPeriod() {                                      // (2)
        if (projectStartDate == null || projectEndDate == null) {                 // (3)
            return true;                                                          // (3)
        }
        return !projectStartDate.isAfter(projectEndDate);                         // (4)
    }
```

- 実装のポイント
    - (1) `@AssertTrue`を付けて`message`でエラーメッセージのメッセージIDを設定する。エラーメッセージの定義方法は、[メッセージ管理](./message-management.md)参照
    - (2) 戻り値が`boolean`、引数なしのメソッドを作成する。名前は`is`から始める
    - (3) 項目間精査に使用する項目の`null`チェックを行う。どちらか片方でも`null`の場合、項目間精査ができないため`true`を返す
    - (4) 項目間の値をチェックする。この例では2つの日付の前後関係をチェックしている

## ネストした項目の精査

ネストした`Form`を精査する場合、ネストした`Form`を持つフィールドに`@Valid`を付ける。
ネストした`Form`には精査のアノテーションを付ける。

```java
public class ProjectBulkUpdateForm {

    @Valid // (2)
    private List<ProjectBulkUpdateSubForm> projects;

    // その他のフィールド、アクセサは省略

    public static class ProjectBulkUpdateSubForm {

        @Required //(1)
        private Integer projectId;

        @Required //(1)
        @Domain("projectName") //(1)
        private String projectName;

        // その他のフィールド、アクセサは省略
    }
}
```

- 実装のポイント
    - (1) ネストした`Form`のフィールドには精査のアノテーションを付ける
    - (2) ネストした`Form`を持つフィールドには`@Valid`を付ける

## Formで完結しない精査

DBを用いた精査や、ログインユーザの情報を用いた精査など、`Form`で完結しない精査を行いたい場合は`Service`に精査処理を実装する。

```java
@Service
@Transactional
public class ProjectUpdateService {

    @Transactional(readOnly = true)
    public ProjectDto findProjectById(Integer projectId) {
        ProjectAndOrganization projectAndOrganization = mapper.selectProjectByPrimaryKey(projectId);
        if (projectAndOrganization == null) {                // (1)
            throw ApplicationException.globalError("E0002"); // (2)
        }

    // その他のコードは省略
```

- 実装のポイント
    - (1) `Service`のメソッド内で精査を行う
    - (2) エラーの場合、`ApplicationException`をスローする
      - エラーの原因が特定のフィールドである場合は、フィールド名とメッセージIDを指定して`ApplicationException`の`fieldError`メソッドを使用する
      - エラーの原因がそれ以外の場合は、メッセージIDを指定して`ApplicationException`の`globalError`メソッドを使用する
      - ※エラーメッセージの定義方法は、[メッセージ管理](./message-management.md)を参照すること
