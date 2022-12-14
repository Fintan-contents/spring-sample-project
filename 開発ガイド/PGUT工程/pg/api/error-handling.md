# エラーハンドリング

- [エラーハンドリング方針](#エラーハンドリング方針)

## エラーハンドリング方針

【方式設計書】に記載の通り、`RestController`ではエラーの分類に関わらず個々の機能ではエラーのハンドリングを直接行わず、上位の共通的な仕組みで制御する方針とする。

そのため、個々の機能の実装担当者はエラーハンドリングについて意識する必要はない。
ただし、アプリケーションからスローする例外は【方式設計書】に則ること。
具体的には、`Service`でビジネスルールのチェックを行いエラーを検知した場合は`BusinessException`を、楽観排他でエラーを検知した場合は`OptimisticLockException`をスローする。なお入力値精査のエラーについてはSpringが例外をスローするため実装担当者は意識する必要はない。
ビジネスルールのチェックの実装方法については[`Request`で完結しない精査](./validation.md#Requestで完結しない精査)を参照。
楽観排他の実装方法については[排他制御](./exclusion-control.md)を参照。
