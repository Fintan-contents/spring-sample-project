# 日付・時刻

- [日付・時刻の型](#日付時刻の型)
- [システム日時の取得方法](#システム日時の取得方法)
- [業務日付の取得方法](#業務日付の取得方法)

## 日付・時刻の型

システム内で扱う日付や時刻の型は、使用したい精度に応じて以下のいずれかを使用する。

- `java.time.LocalDateTime`
- `java.time.LocalDate`
- `java.time.LocalTime`

`java.util.Date`や`java.util.Calendar`等は原則として使用しない。

## システム日時の取得方法

システム日時はJava標準APIを使用して取得する。

```java
// システム日時
LocalDateTime dateTime = LocalDateTime.now();

// システム日付
LocalDate date = LocalDate.now();

// システム時刻
LocalTime time = LocalTime.now();
```

## 業務日付の取得方法

業務日付はアプリ基盤が提供する`BusinessDateSupplier`を使用して取得する。
`BusinessDateSupplier`を`@Autowired`でインジェクションして、`getDate`を呼び出す。

```java
@Autowired
private BusinessDateSupplier businessDateSupplier;
```

```java
LocalDate businessDate = businessDateSupplier.getDate();
```
