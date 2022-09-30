# 認証・認可

- [ログインユーザの情報を取得する](#ログインユーザの情報を取得する)
- [画面テンプレートで認証・認可に関する記述を行う](#画面テンプレートで認証認可に関する記述を行う)

## ログインユーザの情報を取得する

`SecurityContextHolder`から`SecurityContext`を取得し、さらにそこから`Authentication`を取得する。
`Authentication`からはログイン認証の際に使用したユーザ名と、保有している権限が取得できる。

```java
SecurityContext securityContext = SecurityContextHolder.getContext();
Authentication authentication = securityContext.getAuthentication();
// (1)
if (authentication != null && authentication.isAuthenticated()) {
    // (2)
    String name = authentication.getName();
    // (3)
    Set<String> authorities = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
}
```

- 実装のポイント
    - (1) `null`チェックと`isAuthenticated`メソッドを使用してログイン認証済みであることをチェックしている
    - (2) `getName`メソッドでユーザ名を取得している
        - ここで取得できるユーザ名はログイン画面で入力した値である
    - (3) `getAuthorities`メソッドで保有権限を取得し、`AuthorityUtils.authorityListToSet`を使用して扱いやすくしている
        - 権限によって処理を分岐させたい場合などは、この方法で取得した保有権限の`Set`を利用すること

## 画面テンプレートで認証・認可に関する記述を行う

[画面テンプレートの認証・認可に関する記述](./view-template.md#認証認可に関する記述)を参照。
