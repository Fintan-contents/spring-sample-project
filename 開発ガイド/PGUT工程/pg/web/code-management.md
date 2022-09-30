# コード管理

- [概要](#概要)
- [コードIDとコード値に対応するコード名称を取得する](#コードidとコード値に対応するコード名称を取得する)
- [コードIDに紐付くコード値のリストを取得する](#コードidに紐付くコード値のリストを取得する)
- [コードのドロップダウンリストを作る](#コードのドロップダウンリストを作る)

## 概要

コード管理は[Nablarchのコード管理機能](https://nablarch.github.io/docs/5u21/doc/application_framework/application_framework/libraries/code.html)を利用する。

`CodeManager`を`@Autowired`でインジェクションして使用する。

```java
    @Autowired
    private CodeManager codeManager;
```

どのようなメソッドが用意されているかは[CodeManagerのJavadoc](https://nablarch.github.io/docs/5u21/publishedApi/nablarch-all/publishedApiDoc/architect/nablarch/common/code/CodeManager.html)を参照。

ここでは代表的な使い方を紹介する。

## コードIDとコード値に対応するコード名称を取得する

`CodeManager`の`getName`を使う。

```java
    // プロジェクト種別：コードID = C0300001、コード値 = 01 の例
    String name = codeManager.getName("C0300001", "01");
```

`getName`の代わりに`getShortName`を使用するとコードの略称が取得できる。

```java
    // プロジェクト種別：コードID = C0300001、コード値 = 01 の例
    String name = codeManager.getShortName("C0300001", "01");
```

また、オプション名称を設定している場合、`getOptionalName`を使用してオプション名称が取得できる。

```java
    // プロジェクト種別：コードID = C0300001、コード値 = 01、オプション名称のカラム名 = KANA_NAME の例
    String name = codeManager.getOptionalName("C0300001", "01", "KANA_NAME");
```

## コードIDに紐付くコード値のリストを取得する

`CodeManager`の`getValues`メソッドを使う。

```java
    // プロジェクト種別：コードID = C0300001 の例
    List<String> genders = codeManager.getValues("C0300001");
```

パターンを定義している場合、パターンを指定してコード値のリストを取得できる。

```java
    // プロジェクト種別：コードID = C0300001、パターン = 1 の例
    List<String> pattern1Genders = codeManager.getValues("C0300001", "1");
```

## コードのドロップダウンリストを作る

基盤部品の`com.example.web.common.helper.CodeViewHelper`を使用してドロップダウンリストを作る。

```html
<select th:field="*{projectType}">
  <option value=""></option>
  <!--/* (1) (2) (3) */-->
  <option th:each="codeValue : ${@codeViewHelper.getValues('C0300001')}"
          th:value="${codeValue}"
          th:text="${@codeViewHelper.getName('C0300001', codeValue)}"></option>
</select>
```

- 実装のポイント
    - (1) `CodeViewHelper`の`getValues`を使用してコードIDに紐付くコード値のリストを取得し、`th:each`で1件ずつループする
    - (2) `th:value`には取得したコード値を出力する
    - (3) `th:text`には`CodeViewHelper`の`getName`を使用してコードIDとコード値に対応するコード名称を取得して表示する
