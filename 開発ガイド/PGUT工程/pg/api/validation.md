# 入力値精査

- [単項目精査](#単項目精査)
- [項目間精査](#項目間精査)
- [ネストした項目の精査](#ネストした項目の精査)
- [Requestで完結しない精査](#requestで完結しない精査)

## 単項目精査

【030_アプリ設計/010_システム機能設計/システム機能設計書】の「入力データ定義」をもとにして単項目精査のアノテーションを付ける。「入力データ定義」から【030_アプリ設計/030_インタフェース設計/外部インタフェース設計書】を参照している場合は【030_アプリ設計/030_インタフェース設計/外部インタフェース設計書】をもとにして単項目精査のアノテーションを付ける。

```java
    @Required                   // (1)
    @Domain("projectName")      // (2)
    private String projectName;
```

- 実装のポイント
    - (1) 「入力データ定義」または【030_アプリ設計/030_インタフェース設計/外部インタフェース設計書】の「必須」に「○」が付いている場合は`@Required`を付ける
    - (2) 「入力データ定義」または【030_アプリ設計/030_インタフェース設計/外部インタフェース設計書】の「ドメイン名」が書かれている場合は`@Domain`を付ける
        - `@Domain`に設定する値は【030_アプリ設計/070_データモデル設計/ドメイン定義書】の印刷範囲外にある「ドメイン名（物理）」である。「入力データ定義」または【030_アプリ設計/030_インタフェース設計/外部インタフェース設計書】の「ドメイン名」と、【030_アプリ設計/070_データモデル設計/ドメイン定義書】の「ドメイン名（論理）」を突き合わせてドメインを特定すること

## 項目間精査

項目間精査は`Request`に`@AssertTrue`を付けたメソッドを作成することで行う。

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

ネストした`Request`を精査する場合、ネストした`Request`を持つフィールドに`@Valid`を付ける。
ネストした`Request`には精査のアノテーションを付ける。

```java
public class ProjectBulkUpdateRequest {

    @Valid // (2)
    private List<ProjectBulkUpdateSubRequest> projects;

    // その他のフィールド、アクセサは省略

    public static class ProjectBulkUpdateSubRequest {

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
    - (1) ネストした`Request`のフィールドには精査のアノテーションを付ける
    - (2) ネストした`Request`を持つフィールドには`@Valid`を付ける

## Requestで完結しない精査

DBを用いた精査や、ログインユーザの情報を用いた精査など、Requestで完結しない精査を行いたい場合は`Service`に精査処理を実装する。

```java
@Service
@Transactional
public class ClientSearchService {
    @Transactional(readOnly = true)
    public List<Client> searchClient(ClientSearchCriteria criteria) {
        int upperLimit = properties.getSearchUpperLimit();
        if (mapper.countClientByCriteria(criteria) > upperLimit) { // (1)
            throw new BusinessException("FB1999902", HttpStatus.BAD_REQUEST, "errors.upper.limit", upperLimit); // (2)
        }
        return mapper.selectClientByCriteria(criteria);
    }
    // その他のコードは省略
}
```

- 実装のポイント
    - (1) `Service`のメソッド内で精査を行う
    - (2) エラーの場合、`BusinessException`をスローする
        - `BusinessException`には、障害コード、返却するHTTPステータス、エラーメッセージのメッセージID、エラーメッセージに埋め込む追加情報を設定する
        - ※エラーメッセージの定義方法は、[メッセージ管理](./message-management.md)を参照すること
