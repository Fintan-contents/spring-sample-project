# 非同期通信（Ajax）

- [サーバーの実装](#サーバーの実装)
- [クライアントの実装](#クライアントの実装)

## サーバーの実装

非同期通信のサーバー側の実装には`RestController`を用いる。
`RestController`の作成方法はAPIのガイドを参照すること。

- [`RestController`の作成](../api/rest-controller.md)

## クライアントの実装

非同期通信のクライアント側の実装ではJavaScriptを用いてHTTPリクエストを送信する。
本プロジェクトではjQueryを利用するため、`ajax`メソッドを使用すること。

- 参考：[jQuery.ajax() | jQuery API Documentation](https://api.jquery.com/jQuery.ajax/)
