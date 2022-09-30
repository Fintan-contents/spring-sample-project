# 排他制御

- [楽観排他制御](#楽観排他制御)
    - [更新対象となるデータのバージョン番号を画面にセットする](#更新対象となるデータのバージョン番号を画面にセットする)
    - [画面から送信されたバージョン番号をFormで受け取る](#画面から送信されたバージョン番号をformで受け取る)
    - [バージョン番号を使用した更新処理](#バージョン番号を使用した更新処理)
    - [楽観排他制御エラーのハンドリング](#楽観排他制御エラーのハンドリング)

## 楽観排他制御

Web画面ではテーブルに持つバージョン番号を使用した楽観排他制御を行う。

### 更新対象となるデータのバージョン番号を画面にセットする

まず更新対象のテーブルからデータを取得する際、必要なカラムと共にバージョン番号も取得する。
データ取得のタイミングは、更新処理を行う画面遷移の初期表示時(変更データ入力画面等)が該当する。

```xml
    <select id="selectProjectByPrimaryKey" 
            resultType="com.example.common.generated.model.Project">
    select
        project_id,
        project_name,
        -- 他のカラムは省略
        version_no -- (1)
    from
        project
    where
        project_id = #{projectId}
    </select>
```

- 実装のポイント
    - (1) バージョン番号を取得する

取得したバージョン番号は画面テンプレートへ渡し、フォームの中にセットする。

```html
<input type="hidden" th:field="*{versionNo}" /><!--/* (1) */-->
```

- 実装のポイント
    - (1) `type=hidden`の`input`要素を使用してバージョン番号をセットする

### 画面から送信されたバージョン番号をFormで受け取る

更新ボタンを押すなどをして画面から送信されたバージョン番号は`Form`で受け取る。
バージョン番号の送信と`Form`での受け取りは更新確認画面から完了画面へ遷移する際に行う。

```java
public class ProjectUpdateForm {

    @Required
    private Integer projectId;

    @Required
    @Domain("projectName")
    private String projectName;

    @Required
    private Long versionNo; // (1)

    // 他のフィールドや、アクセサメソッドは省略
```

- 実装のポイント
    - (1)バージョン番号を受け取るためのフィールドを定義する

### バージョン番号を使用した更新処理

`Form`で受け取ったバージョン番号は`Form`から`Dto`、`Dto`から`Model`へと受け渡す。

更新のSQLでは更新条件にバージョン番号を含める。

```xml
    <update id="updateProjectByPrimaryKey">
        update
            project
        set
            project_name = #{projectName},
            -- 他のカラムは省略
            version_no = #{versionNo} + 1 -- (1)
        where
            project_id = #{projectId}
        and version_no = #{versionNo} -- (2)
    </update>
```

- 実装のポイント
    - (1) バージョン番号の値は`+1`したもので更新する
    - (2) 更新条件にバージョン番号を含める

更新結果が`0`件だった場合、`OptimisticLockException`をスローする。

```java
@Service
@Transactional
public class ProjectUpdateService {

    public void updateProject(ProjectDto projectDto) {
        Project project = projectDto.toProject();

        int updateCount = mapper.updateProjectByPrimaryKey(project);
        if (updateCount == 0) {                  // (1)
            throw new OptimisticLockException(); // (1)
        }
    }

    // その他のコードは省略
```

- 実装のポイント
    - (1) 更新結果が`0`件だった場合、`OptimisticLockException`をスローする

### 楽観排他制御エラーのハンドリング

楽観排他制御エラーは`@OnRejectError`でハンドリングを行う。

`@OnRejectError`については[エラーハンドリング](./error-handling.md)を参照すること。
