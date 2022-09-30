# 二重サブミット防止

- [実装方法](#実装方法)
- [トークンの生成](#トークンの生成)
- [トークンのチェック](#トークンのチェック)
- [トークンチェックエラーのハンドリング](#トークンチェックエラーのハンドリング)

## 実装方法

二重サブミット防止は`@TransactionTokenCheck`を使用して実装する。
`@TransactionTokenCheck`ではワンタイムトークンを利用することで二重サブミットを防止する。
※以降ではワンタイムトークンのことを単にトークンと表現する

【UI標準(画面)】における以下の標準パターンでは`入力画面 → 確認画面 → 完了画面`という流れで画面遷移する。

- 「新規登録」型
- 「検索(検索一覧) → 単一変更・削除」型

このとき、`入力画面 → 確認画面`の画面遷移を行う際にトークンを生成し、`確認画面 → 完了画面`の画面遷移を行う際にトークンのチェックを行う。

## トークンの生成

```java
@Controller
@RequestMapping("project/create")
@TransactionTokenCheck("project/create") // (1)
public class ProjectCreateController {
    // 他のメソッドは省略
    @PostMapping("confirm")
    @TransactionTokenCheck(type = TransactionTokenType.BEGIN) // (2)
    @OnRejectError(path = "project/create/index")
    public String confirm(@Validated ProjectCreateForm form, BindingResult bindingResult) {
        return "project/create/confirm";
    }
}
```

- 実装のポイント
    - (1)クラスに`@TransactionTokenCheck`を付け`@RequestMapping`に設定したURLと同じ値を設定する
    - (2)`入力 → 確認`の遷移を行うメソッドに`@TransactionTokenCheck(type = TransactionTokenType.BEGIN)`を付けることで、以下の処理が実行される
        - トークンの生成とサーバ側でのトークンの保持
        - 生成したトークンを画面にhidden項目として自動設定

## トークンのチェック

```java
@Controller
@RequestMapping("project/create")
@TransactionTokenCheck("project/create")
public class ProjectCreateController {
    // 他のメソッドは省略
    @PostMapping(path = "execute", params = "execute")
    @TransactionTokenCheck // (1)
    @OnRejectError(path = "project/create/index")
    public String execute(@Validated ProjectCreateForm form, BindingResult bindingResult) {
        ProjectDto projectDto = form.toProjectDto();
        service.insertProject(projectDto);
        return "redirect:/project/create/complete"; // (2)
    }
    @GetMapping("complete") // (2)
    public String complete() {
        return "project/create/complete";
    }
}
```

- 実装のポイント
    - (1)登録/更新/削除処理を行うメソッドに@TransactionTokenCheckを付ける。こうすることで二重サブミットされたかどうかのチェックが行われるようになる
    - (2)完了画面へ遷移する際はリダイレクトを行うこと。これはPRGパターンと呼ばれる手法である

## トークンチェックエラーのハンドリング

基盤部品が二重サブミットを検知した場合、`InvalidTransactionTokenException`がスローされる。
`InvalidTransactionTokenException`は共通的な仕組みでハンドリングされエラー画面に遷移する。
