# 排他制御

- [楽観排他制御](#楽観排他制御)
    - [更新対象となるデータのバージョン番号を取得する](#更新対象となるデータのバージョン番号を取得する)
    - [送信されたバージョン番号をRequestで受け取る](#送信されたバージョン番号をrequestで受け取る)
    - [バージョン番号を使用した更新処理](#バージョン番号を使用した更新処理)
    - [楽観排他制御エラーのハンドリング](#楽観排他制御エラーのハンドリング)

## 楽観排他制御

APIではテーブルに持つバージョン番号を使用した楽観排他制御を行う。

### 更新対象となるデータのバージョン番号を取得する

まず更新対象のテーブルからデータを取得するAPIにて必要なカラムと共にバージョン番号も取得する。

```xml
    <select id="selectClientByPrimaryKey" resultType="Client">
        select
            client_id,
            client_name,
            industry_code,
            version_no -- (1)
        from
            client
        where
            client_id = #{clientId}
    </select>
```

- 実装のポイント
    - (1) バージョン番号を取得する

取得したバージョン番号は`Response`にセットする。

```java
public class ClientDetailResponse {
    private Long versionNo; // (1)
    // 他のフィールドや、アクセサメソッドは省略
}
```

- 実装のポイント
    - (1) バージョン番号を`Response`のフィールドとして返却する

### 送信されたバージョン番号をRequestで受け取る

API利用者が上記の`Response`から取得したバージョン番号を更新のリクエストに含めて送信する。送信されたバージョン番号は`Request`で受け取る。

```java
public class ClientUpdateRequest {
    @Required
    @Domain("versionNo")
    private Long versionNo; // (1)

    // 他のフィールドや、アクセサメソッドは省略
}
```

- 実装のポイント
    - (1)バージョン番号を受け取るためのフィールドを定義する

### バージョン番号を使用した更新処理

`Request`で受け取ったバージョン番号は`Request`から`Dto`、`Dto`から`Model`へと受け渡す。

更新のSQLでは更新条件にバージョン番号を含める。

```xml
    <update id="updateClientByPrimaryKey">
        update
            client
        set
            client_name = #{clientName},
            industry_code = #{industryCode},
            version_no = #{versionNo} + 1 -- (1)
        where
            client_id = #{clientId}
            and version_no = #{versionNo} -- (2)
    </update>
```

- 実装のポイント
    - (1) バージョン番号の値は`+1`したもので更新する
    - (2) 更新条件にバージョン番号を含める

更新結果が`0`件だった場合、`OptimisticLockException`をスローする。

```java
    public void updateClient(Client model) {
        // その他のコードは省略

        int count = mapper.updateClientByPrimaryKey(model);
        if (count == 0) {                        // (1)
            throw new OptimisticLockException(); // (1)
        }
    }
```

- 実装のポイント
    - (1) 更新結果が`0`件だった場合、`OptimisticLockException`をスローする

### 楽観排他制御エラーのハンドリング

楽観排他制御エラーは、共通的な仕組みで制御するため個々の機能の実装担当者はエラーハンドリングについて意識する必要はない。

楽観排他制御エラー発生時に`OptimisticLockException`をスローすることでエラーハンドリングが行われる。

エラーハンドリング方針については[エラーハンドリング](./error-handling.md)を参照。
