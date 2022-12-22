# 画面テンプレート

- [画面テンプレートの概要](#画面テンプレートの概要)
- [画面テンプレートの配置場所](#画面テンプレートの配置場所)
- [必要な記述の追加](#必要な記述の追加)
- [テキストの書き出し](#テキストの書き出し)
- [条件分岐](#条件分岐)
- [繰り返し](#繰り返し)
- [フォームコントロール](#フォームコントロール)
  - [form要素へFormを設定する](#form要素へformを設定する)
  - [テキストボックス](#テキストボックス)
  - [単一のチェックボックス](#単一のチェックボックス)
  - [グループ化されたチェックボックス](#グループ化されたチェックボックス)
  - [ラジオボタン](#ラジオボタン)
  - [セレクトボックス（ドロップダウンリスト）](#セレクトボックスドロップダウンリスト)
  - [テキストエリア](#テキストエリア)
  - [hidden](#hidden)
  - [ファイル添付](#ファイル添付)
- [リンク](#リンク)
- [日付・金額のフォーマット](#日付金額のフォーマット)
  - [日付のフォーマット](#日付のフォーマット)
  - [金額のフォーマット](#金額のフォーマット)
- [ViewHelperの使用方法](#viewhelperの使用方法)
- [画面へエラーメッセージを表示する方法](#画面へエラーメッセージを表示する方法)
- [画面へエラー以外のメッセージを表示する方法](#画面へエラー以外のメッセージを表示する方法)
- [ネストしたForm](#ネストしたform)
- [認証・認可に関する記述](#認証認可に関する記述)
  - [ログイン済みの場合のみ要素を表示する](#ログイン済みの場合のみ要素を表示する)
  - [特定の権限を持つ場合のみ要素を表示する](#特定の権限を持つ場合のみ要素を表示する)
  - [特定のURLへアクセスする権限を持つ場合のみ要素を表示する](#特定のurlへアクセスする権限を持つ場合のみ要素を表示する)

## 画面テンプレートの概要

本プロジェクトで使用するThymeleafはHTMLファイルを画面テンプレートとして利用できる。
そのため設計工程で作成された画面モックをコピーし、必要な記述を追加することで画面テンプレートを作成する。

## 画面テンプレートの配置場所

画面テンプレートは`src/main/resources/templates/<機能を表す名前>/<取引を表す名前>/<リクエストを表す名前>.html`に配置する。

`<リクエストを表す名前>`は基本的に画面パターンによって以下の名前を付けること。

|画面|リクエストを表す名前|
|---|---|
|一覧検索画面|`index`|
|詳細画面|`index`|
|登録・更新・削除の初期画面|`index`|
|登録・更新・削除の確認画面|`confirm`|
|登録・更新・削除の完了画面|`complete`|

例えばプロジェクト登録の確認画面だと次のパスに画面テンプレートを配置する。

- `src/main/resources/templates/project/create/confirm.html`

## 必要な記述の追加

画面モックをコピーして用意した画面テンプレートに対して、以下に挙げた記述を追加する。

- Thymeleafを使用することを表すため、`html`要素へ`xmlns:th`属性を追加する
- Spring Securityを使用することを表すため、`html`要素へ`xmlns:sec`属性を追加する
- 共通レイアウトを適用するため、`html`要素へ`th:replace`属性を追加する

これらを追加した`html`要素は次のようになる。

```html
<html xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/extras/spring-security"
      th:replace="~{common/layout :: layout(~{:: title}, ~{:: body/content()}, true)}">
```

## テキストの書き出し

要素の内容にテキストを書き出したい場合、`th:text`属性を使用する。

```html
<!--/* (1) */-->
<span th:text="${projectName}">...</span>
```

- 実装のポイント
    - (1) `th:text`属性には`${<名前>}`という形式で式を記述する。`<名前>`に指定する名前は次のいずれかとなる
        - `Controller`のメソッド内で`Model`インターフェースの`addAttribute`メソッドを使用して値をセットしたときの名前
        - `Controller`のメソッドに付けた`@ModelAttribute`の`value`属性値

## 条件分岐

条件に応じて画面要素の表示・非表示を分岐したい場合、`th:if`属性を使用する。

```html
<!--/* (1) */-->
<div th:if="${page != null && page.totalElements > 0}">
    <!--/* div要素の内容は省略 */-->
</div>
```

- 実装のポイント
    - (1) `th:if`属性には`boolean`を返す式を記述する。式の評価が`true`の場合、この`div`要素は表示される

## 繰り返し

リスト内の値を1つずつ繰り返し処理を行いたい場合、`th:each`属性を使用する。

```html
<tr th:each="project : ${page.content}"><!--/* (1) */-->
    <td th:text="${project.projectName}">プロジェクト名1</td><!--/* (2) */-->
    <!--/* その他の項目は省略 */-->
</tr>
```

- 実装のポイント
    - (1) `th:each`属性には`<リスト内の値を受け取る変数> : <リストを返す式>`という形式で記述する
        - 上記の例では`page`が持つ`content`がリストであり、`project`という変数で値を受け取っている
        - リストが持つ値の数だけ`tr`要素が書き出される
    - (2) 値を受け取った変数を用いて値の書き出しなどの処理が行える

## フォームコントロール

### form要素へFormを設定する

フォームコントロールに`Form`を対応づけるため、`form`要素に`th:object`属性を記述する。

```html
<!--/* (1)(2) */-->
<form method="POST" th:action="@{/project/create/confirm}" action="./confirm.html"
    th:object="${projectCreateForm}">
```

- 実装のポイント
    - (1) `th:action`属性に遷移先のURLを記述する
        - URLを記述する際は`@{<ルートパスからの相対パス>}`という形式を用いること
    - (2) `th:object`属性に`${<Formの名前>}`という形式で対応させる`Form`を記述する
        - `Form`の名前はクラス名の先頭1文字を小文字に変換したもの

ここからフォームコントロールの種類別に`Form`のフィールドを対応付ける方法を説明する。

### テキストボックス

テキストボックスへ`Form`のフィールドを対応させるには`input`要素へ`th:field`属性を記述する。

```html
<!--/* (2) */-->
<label for="projectName">PJ名</label>
<!--/* (1) */-->
<input type="text" th:field="*{projectName}"/>
```

- 実装のポイント
    - (1) `th:field`属性には`*{<対応付けるフィールド名>}`という形式で対応させるフィールドを記述する
        - これだけで`id`属性、`name`属性、`value`属性が書き出される
    - (2) `label`要素の`for`属性にはフィールド名を記述する

### 単一のチェックボックス

単一のチェックボックスへ`Form`のフィールドを対応させるには`input`要素へ`th:field`属性を記述する。

```html
<!--/* (1) */-->
<input type="checkbox" th:field="*{agreement}" />
<!--/* (2) */-->
<label th:for="${#ids.prev('agreement')}">同意する</label>
```
- 実装のポイント
    - (1) `th:field`属性には`*{<対応付けるフィールド名>}`という形式で対応させるフィールドを記述する
    - (2) `label`要素の`th:for`属性には`${#ids.prev('<フィールド名>')}`を記述する

なお、この例では対応するフィールドの型は`boolean`を想定している。

```java
public class AccountRegistrationForm {

    private boolean agreement;

    // その他のコードは省略
}
```

### グループ化されたチェックボックス

グループ化されたチェックボックスへ`Form`のフィールドを対応させるには`input`要素へ`th:field`属性を記述する。

```html
<div th:each="codeValue : ${@codeViewHelper.getValues('C0300001', 'pattern01')}">
    <!--/* (1)(2) */-->
    <input type="checkbox" th:field="*{projectTypes}" th:value="${codeValue}" />
    <!--/* (3) */-->
    <label th:for="${#ids.prev('projectTypes')}"
           th:text="${@codeViewHelper.getName('C0300001', codeValue)}">...</label>
</div>
```

- 実装のポイント
    - (1) `th:field`属性には`*{<対応付けるフィールド名>}`という形式で対応させるフィールドを記述する
    - (2) `th:value`属性を記述する
        - 対応付けたフィールドの値の中に`th:value`（または`value`属性）へ設定した値が含まれる場合、そのチェックボックスが選択された状態になる
    - (3) `label`要素の`th:for`属性には`${#ids.prev('<フィールド名>')}`を記述する

なお、この例では対応するフィールドの型は`String`配列を想定している。

```java
public class ProjectSearchForm implements Serializable {

    private String[] projectTypes;

    // その他のコードは省略
}
```

### ラジオボタン

ラジオボタンへ`Form`のフィールドを対応させるには`input`要素へ`th:field`属性を記述する。

```html
<div th:each="codeValue : ${@codeViewHelper.getValues('C0200001', 'pattern01')}">
    <!--/* (1)(2) */-->
    <input type="radio" th:field="*{projectClass}" th:value="${codeValue}" />
    <!--/* (3) */-->
    <label th:for="${#ids.prev('projectClass')}"
           th:text="${@codeViewHelper.getName('C0200001', codeValue)}">...</label>
</div>
```

- 実装のポイント
    - (1) `th:field`属性には`*{<対応付けるフィールド名>}`という形式で対応させるフィールドを記述する
    - (2) `th:value`属性を記述する
        - 対応付けたフィールドの値と`th:value`（または`value`属性）へ設定した値が同じ場合、そのラジオボタンが選択された状態になる
    - (3) `label`要素の`th:for`属性には`${#ids.prev('<フィールド名>')}`を記述する

### セレクトボックス（ドロップダウンリスト）

セレクトボックスへ`Form`のフィールドを対応させるには`select`要素へ`th:field`属性を記述する。

```html
<!--/* (2) */-->
<label for="organizationId">部門</label>
<!--/* (1) */-->
<select th:field="*{organizationId}">
    <option value=""></option>
    <option th:each="organization : ${organizations}"
            th:value="${organization.organizationId}"
            th:text="${organization.organizationName}">...</option>
</select>
```

- 実装のポイント
    - (1) `th:field`属性には`*{<対応付けるフィールド名>}`という形式で対応させるフィールドを記述する
        - 対応付けたフィールドの値と`option`要素の`th:value`属性（または`value`属性）へ設定した値が同じ場合、その`option`要素が選択された状態になる
    - (2) `label`要素の`for`属性にはフィールド名を記述する

### テキストエリア

テキストエリアへ`Form`のフィールドを対応させるには`textarea`要素へ`th:field`属性を記述する。

```html
<!--/* (2) */-->
<label for="note">備考</label>
<!--/* (1) */-->
<textarea th:field="*{note}">...</textarea>
```

- 実装のポイント
    - (1) `th:field`属性には`*{<対応付けるフィールド名>}`という形式で対応させるフィールドを記述する
    - (2) `label`要素の`for`属性にはフィールド名を記述する

### hidden

`type="hidden"`の`input`要素へ`Form`のフィールドを対応させるには`input`要素へ`th:field`属性を記述する。

```html
<!--/* (1) */-->
<input type="hidden" th:field="*{projectName}"/>
```

- 実装のポイント
    - (1) `th:field`属性には`*{<対応付けるフィールド名>}`という形式で対応させるフィールドを記述する

### ファイル添付

ファイル添付のコントロールへ`Form`のフィールドを対応させるには`input`要素へ`th:field`属性を記述する。

```html
<!--/* (2) */-->
<label for="targetFile">ファイル</label>
<!--/* (1) */-->
<input type="file" th:field="*{targetFile}"/>
```

- 実装のポイント
    - (1) `th:field`属性には`*{<対応付けるフィールド名>}`という形式で対応させるフィールドを記述する
    - (2) `label`要素の`for`属性にはフィールド名を記述する

## リンク

リンクでは`th:href`属性にURLを記述する。

```html
<!--/* (1) */-->
<a th:href="@{/project/create/index}" href="./create/index.html">...</a>
<!--/* (2) */-->
<a th:href="@{/project/detail/index(projectId=${projectId})}" href="./detail/index.html">...</a>
```

- 実装のポイント
    - (1) `th:href`属性に遷移先のURLを記述する
        - URLを記述する際は`@{<ルートパスからの相対パス>}`という形式を用いること
    - (2) クエリパラメータを設定する場合、パスの後ろに`(<パラメータ名>=<パラメータ値>)`という形式で記述すること
        - 上記の例だと`/project/detail/index?projectId=...`といったURLが出力される

## 日付・金額のフォーマット

### 日付のフォーマット

日付のフォーマットには`#temporals.format`を使用する。

```html
<!--/* (1) */-->
<span th:text="${#temporals.format(localDate, 'uuuu/MM/dd')}">...</span>
<!--/* (2) */-->
<span th:text="${#temporals.format(localDateTime, 'uuuu/MM/dd HH:mm')}">...</span>
<span th:text="${#temporals.format(localTime, 'HH:mm')}">...</span>
```

- 実装のポイント
    - (1) `#temporals.format`に`LocalDate`の値とフォーマットのパターンを渡す
        - フォーマットは`DateTimeFormatter`を使用して行われる。そのため年を表すパターンは`u`となる
    - (2) `LocalDateTime`や`LocalTime`の値も渡せる

### 金額のフォーマット

金額のフォーマットには`#numbers.formatInteger`を使用する。

```html
<!--/* (1) */-->
<span th:text="${#numbers.formatInteger(money, 0, 'COMMA')}"></span>
```

- 実装のポイント
    - (1) `#numbers.formatInteger`に`Integer`または`Long`、`BigDecimal`の値と`0`と`'COMMA'`を渡す
        - こうすることで数値を3桁カンマ区切りでフォーマットできる

## ViewHelperの使用方法

画面テンプレートから`ViewHelper`を使用するには`${@<ViewHelperの名前>.<呼び出したいメソッド>(<引数>)}`という形式で式を記述する。

```html
<!--/* (1) */-->
<span th:text="${@projectDetailViewHelper.getOrganizationName(organizationId)}">...</span>
```

- 実装のポイント
    - `@`の後に記述する`ViewHelper`の名前はクラス名の先頭1文字を小文字にしたもの

## 画面へエラーメッセージを表示する方法

画面にエラーメッセージを表示する場合は、`th:errors`属性を使用する。
フォームコントロールに紐づくエラーメッセージを表示する時は、`th:errors="*{フォームコントロール名}"`とする。フォームコントロール名を`"*{all}"`とした場合は、すべてのフォームコントロールに紐づくエラーメッセージが表示される。
特定のフォームコントロールに紐づかないグローバルなエラーメッセージは`th:errors="*{global}"`とすることで表示される。
詳細は[Thymeleafのチュートリアル](https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html#validation-and-error-messages)を参照。

## 画面へエラー以外のメッセージを表示する方法

画面テンプレートで`#{メッセージID}`という式を使うことで、`messages.properties`に定義したメッセージを使用することができる。
詳細は[Thymeleafのチュートリアル](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#messages)を参照。
※なお、参照先のチュートリアルではメッセージを表示するために`th:utext`属性を用いているが、本プロジェクトでは`th:utext`属性の使用は禁止している。
そのため、メッセージの表示でも`th:text`属性を使用すること。

## ネストしたForm

次のようにネストした`Form`を使いたい場合がある。

```java
public class ProjectBulkUpdateForm {

    @Valid
    private List<ProjectBulkUpdateSubForm> projects;

    // その他のフィールド、アクセサは省略

    public static class ProjectBulkUpdateSubForm {

        @Required
        private Integer projectId;

        @Required
        @Domain("projectName")
        private String projectName;

        // その他のフィールド、アクセサは省略
    }
}
```

こういったネストした`Form`のフィールドを画面テンプレートで扱う場合、`<フィールド名>[<インデックス>].<ネストしたFormのフィールド名>`といった形式で式を記述する。

```html
<form method="POST" th:action="@{/updateBulk}" th:object="${projectBulkUpdateForm}">
    <!--/* (1) */-->
    <div th:each="subForm, stat : *{projects}">
        <input type="hidden" th:field="*{projects[__${stat.index}__].projectId}" />

        <!--/* (3) */-->
        <label th:for="${'projects' + stat.index + '.projectName'}">PJ名</label>
        <!--/* (2) */-->
        <input type="text" th:field="*{projects[__${stat.index}__].projectName}" />
        <!--/* (4) */-->
        <span th:if="${#fields.hasErrors('projects[' + stat.index + '].projectName')}"
              th:errors="*{projects[__${stat.index}__].projectName}"></span>
```

- 実装のポイント
    - (1) ネストした`Form`を`th:each`で繰り返し処理する
        - インデックスを使用したいため、リスト内の値を受け取る変数に加えて状態を受け取る変数を記述する（この例では`stat`がそれに当たる）
    - (2) `th:field`属性には`*{<フィールド名>[__${stat.index}__]}.<ネストしたFormのフィールド名>`という形式で対応させるフィールドを記述する
        - `*{...}`形式の式の中で`${...}`を使いたい場合、その前後に`__`を記述する
    - (3) `label`要素の`th:for`属性には`${'<フィールド名>' + stat.index + '.<ネストしたFormのフィールド名>'}`という形式で記述する
    - (4) エラーメッセージの表示条件として`#fields.hasErrors`へ渡す値は`'<フィールド名>[' + stat.index + '].<ネストしたFormのフィールド名>'`という形式で記述する

## 認証・認可に関する記述

### ログイン済みの場合のみ要素を表示する

`sec:authorize`属性と`isAuthenticated`を使用する。

```html
<p sec:authorize="isAuthenticated()">
    ログイン認証済みの場合、この要素が表示される。
</p>
```

### 特定の権限を持つ場合のみ要素を表示する

`sec:authorize`属性と`hasAuthority`を使用する。

```html
<p sec:authorize="hasAuthority('AUTH1')">
    AUTH1という権限を持つ場合、この要素が表示される。
</p>
<p sec:authorize="hasAuthority('AUTH2')">
    AUTH2という権限を持つ場合、この要素が表示される。
</p>
<p sec:authorize="hasAnyAuthority('AUTH1', 'AUTH2')">
    AUTH1とAUTH2、どちらかの権限を持つ場合、この要素が表示される。
</p>
<p sec:authorize="hasAuthority('AUTH1') && hasAuthority('AUTH2')">
    AUTH1とAUTH2、両方の権限を持つ場合、この要素が表示される。
</p>
```

### 特定のURLへアクセスする権限を持つ場合のみ要素を表示する

`sec:authorize-url`属性を使用する。

```html
<p sec:authorize-url="/page1">
    /page1 へアクセスする権限を持つ場合、この要素が表示される。
</p>
```
