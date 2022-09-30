# エラーハンドリング

- [基本的なエラーハンドリング](#基本的なエラーハンドリング)
- [単項目精査、項目間精査でエラーが発生した際に処理を行う](#単項目精査項目間精査でエラーが発生した際に処理を行う)
- [ハンドリング対象の例外を指定する](#ハンドリング対象の例外を指定する)

## 基本的なエラーハンドリング

エラーハンドリングはアプリ基盤が用意している`@OnRejectError`を使って行う。

対象となる`Controller`のメソッドに`@OnRejectError`を付けて`path`にエラー発生時の遷移先となる画面テンプレートのパスを設定する。

```java
    @PostMapping
    @OnRejectError(path = "xxx/example/index") // (1)
    public String post(@Validated ExampleForm form, // (2)
            BindingResult bindingResult) { // (3)
        ExampleDto = form.toExampleDto();
        service.createExample(ExampleDto);
        return "xxx/example/next";
    }
```

- 実装のポイント
    - (1) `Controller`のメソッドに`@OnRejectError`を付ける
    - (2) メソッドの戻り値は`String`にする
    - (3) 引数に`BindingResult`を取る
    - ※(2)と(3)は`@OnRejectError`を使用する上での制約である

上記の例では入力値の精査エラーが発生したり業務ロジック実行時に業務エラーが発生した際、エラーがハンドリングされて`xxx/example/index`へ遷移する。

その際、画面テンプレート（`xxx/example/index`）へはエラーメッセージが渡される。
渡されたエラーメッセージの表示方法は[画面テンプレート](./view-template.md)を参照すること。

## 単項目精査、項目間精査でエラーが発生した際に処理を行う

`@OnRejectError`を使ったエラーハンドリングでは単項目精査、項目間精査でエラーが発生した際、`Controller`のメソッドは実行されない。

実装する業務機能によっては単項目精査、項目間精査でエラーが発生した際、画面へ遷移する前に何かしらの処理を行いたい場合があり得る。

そういった場合は`@OnRejectError`の`handlingValidationError`を`false`に設定する。
そうすることで単項目精査、項目間精査でエラーが発生した際にも`Controller`のメソッドが実行されるため、任意の処理を行える。

```java
    @PostMapping
    @OnRejectError(path = "xxx/example/index", handlingValidationError = false) // (1)
    public String post(@Validated ExampleForm form,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) { // (2)
            service.createErrorLog(); // (3)
            return "xxx/example/index"; // (4)
        }
        ExampleDto = form.toExampleDto();
        service.createExample(ExampleDto);
        return "xxx/example/next";
    }
```

- 実装のポイント
    - (1) `handlingValidationError`に`false`を設定する
    - (2) 精査エラーが発生したらこの`if`ブロックに入る
    - (3) 任意の処理を行える
    - (4) メソッドの戻り値で遷移先の画面を指定する

## ハンドリング対象の例外を指定する

`@OnRejectError`はデフォルトでは以下の例外をハンドリング対象とする。

- `ApplicationException`（業務例外）
- `OptimisticLockException`（楽観排他ロック例外）

他の例外をハンドリングしたい場合、`@OnRejectError`の`types`でハンドリング対象となる例外を指定する。

```java
    @OnRejectError(path = "xxx/example/index", types = { // (1)
			ExampleException.class, // (1)
			ApplicationException.class, // (2)
			OptimisticLockException.class, // (2)
    })
    public String post(@Validated ExampleForm form,
```

- 実装のポイント
    - (1) `types`でハンドリング対象の例外を指定する。このとき指定された例外のサブクラスもハンドリング対象になる
    - (2) `types`は「追加」ではなく「上書き」なため、デフォルトでハンドリングされる例外も設定しておく

なお、`Controller`から以下の条件下で例外がスローされた場合は、共通処理によってエラーハンドリングが行われる。

- `@OnRejectError`でハンドリング対象として設定されていない例外がスローされる
- `@OnRejectError`が設定されていないメソッドの呼び出しで、例外がスローされる

業務機能がスローする例外は、`@OnRejectError`でハンドリングを行うことを原則とする。共通処理でハンドリングされるエラーは、データベース接続エラー等のシステムエラーを対象としたものである。
