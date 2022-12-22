# Form

- [Formの作成](#formの作成)

## Formの作成

【030_アプリ設計/010_システム機能設計/システム機能設計書】の「画面項目定義」をもとにして`Form`を作成する。

```java
public class ProjectCreateForm { // (1)

    private String projectName;         // (2)

    private LocalDate projectStartDate; // (2)

    private Integer divisionId;         // (2)

    public String getProjectName() { // (3)
        return projectName;
    }

    public void setProjectName(String projectName) { // (3)
        this.projectName = projectName;
    }

    public LocalDate getProjectStartDate() { // (3)
        return projectStartDate;
    }

    public void setProjectStartDate(LocalDate projectStartDate) { // (3)
        this.projectStartDate = projectStartDate;
    }

    public Integer getDivisionId() { // (3)
        return divisionId;
    }

    public void setDivisionId(Integer divisionId) { // (3)
        this.divisionId = divisionId;
    }

    // その他のコードは省略
}
```

- 実装のポイント
    - (1) `Form`のクラス名は論理的な名前または取引IDをもとに命名する（取引IDを使う場合は `B10101Form` など）
        - 検索画面の`Form`は`Serializable`を実装する必要がある。詳しくは[スコープ管理の検索フォームをセッションスコープで管理する](./scope-management.md#検索フォームをセッションスコープで管理する)を参照すること
    - (2) 画面項目定義をもとにフィールドを作成する
        - フィールド名は印刷範囲外に書かれた「【内部設計情報】画面項目名（物理）」をもとにする
    - (3) アクセサを作成する

フィールドの型は次の表にあるうちのいずれか、あるいは後述するネストした`Form`にする。

|フィールドの型|説明|主な用途|
|---|---|---|
|`String`|文字列|名前、コード値、文字列で表現される項目|
|`Integer`|整数|サロゲートキー、`Integer`の範囲で表現可能な整数項目|
|`Long`|整数|バージョン番号、`Long`の範囲で表現可能な整数項目|
|`BigDecimal`|数値|金額|
|`LocalDate`|日付|日付|
|`LocalTime`|時刻|時刻|
|`LocalDateTime`|日時|日時|
|`boolean`|真偽値|チェックボックスの状態を受け取る（チェックされていれば`true`となる）|
|`String[]`|文字列の配列|グループ化されたチェックボックスで複数の値を受け取る|
|`MultipartFile`|マルチパートファイル|アップロードされたファイルを受け取る|

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

また、一括登録・一括更新・一括削除をするためネストした`Form`の`List`を持ちたい場合がある。
そういった場合、`Form`の中に`Form`を作成して、その`Form`の`List`を持つフィールドを作成する。

```java
public class ProjectBulkUpdateForm {

    private List<ProjectBulkUpdateSubForm> projects; // (2)

    public List<ProjectBulkUpdateSubForm> getProjects() { // (3)
        return projects;
    }

    public void setProjects(List<ProjectBulkUpdateSubForm> projects) { // (3)
        this.projects = projects;
    }

    // その他のフィールド、アクセサは省略

    public static class ProjectBulkUpdateSubForm { // (1)

        private Integer projectId;

        private String projectName;

        public Integer getProjectId() {
            return projectId;
        }

        public void setProjectId(Integer projectId) {
            this.projectId = projectId;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        // その他のフィールド、アクセサは省略
    }
}
```

- 実装のポイント
    - (1) ネストした`Form`を作成する。アクセス修飾子は`public static`とすること
    - (2) ネストした`Form`の`List`を持つフィールドを作成する
    - (3) アクセサを作成する

