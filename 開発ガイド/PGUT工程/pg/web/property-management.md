# プロパティ管理

- [プロパティファイルの置き場所](#プロパティファイルの置き場所)
- [プロパティの定義](#プロパティの定義)
- [PropertiesとConfigの実装方法](#propertiesとconfigの実装方法)
- [Propertiesの使用方法](#propertiesの使用方法)

## プロパティファイルの置き場所

アプリケーションのプロパティは取引ごとにプロパティファイルを作成して管理する。

プロパティファイルの置き場所は次の通り。

- `src/main/resources/properties/[機能名]/[取引名].properties`

例えば機能名が`Client`、取引名が`ClientSearch`だとするとプロパティファイルは次の場所に置かれる。

- `src/main/resources/properties/Client/ClientSearch.properties`

## プロパティの定義

プロパティは`key=value`形式でプロパティファイルへ記述する。

`key`のネーミングルールは次のように機能名、取引名、項目名をドット（`.`）で繋げたもので構成される。

- `[機能名].[取引名].[項目名]`

ドット（`.`）で繋げられた各項目はlower-kebab-caseで記述する。

例えば機能名が`client`、取引名が`client-search`、項目名が`search-upper-limit`の場合、`key`は次のようになる。

- `client.client-search.search-upper-limit`

## PropertiesとConfigの実装方法

プロパティファイルからロードされたプロパティをアプリケーションで使用するため`Properties`を作成する。

```java
public class ClientSearchProperties { // (1)

    private int searchUpperLimit; // (2)

    // アクセサは省略
}
```

- 実装のポイント
    - (1) 何もアノテーションを付けない
    - (2) フィールド名はプロパティの`key`から`[機能名].[取引名].`を取り除き、`[項目名]`だけとする。また、`key`はlower-kebab-caseだが、フィールド名はlowerCamelCaseにすること

`Properties`へプロパティを設定するため`Config`を作成する。

```java
@Configuration // (1)
@PropertySource(value = "classpath:/properties/Client/ClientSearch.properties", encoding = "UTF-8") // (2)
public class ClientSearchConfig {

    @Bean // (3)
    @ConfigurationProperties(prefix = "client.client-search") // (4)
    public ClientSearchProperties clientSearchProperties() { // (5)
        return new ClientSearchProperties(); // (6)
    }
}
```

- 実装のポイント
    - (1) クラスに`@Configuration`を付ける
    - (2) クラスに`@PropertySource`を付けてプロパティファイルのパスとエンコーディングを記述する。プロパティファイルのパスは`classpath:`から始めて`src/main/resources`からの相対パスを記述する
    - (3) メソッドに`@Bean`を付ける
    - (4) メソッドに`@ConfigurationProperties`を付けて`prefix`要素に`key`の`[機能名].[取引名]`の部分を記述する
    - (5) メソッドの戻り値は`Properties`とし、メソッド名は`Properties`のクラス名を元に付ける
    - (6) メソッドの内容は`Properties`をインスタンス化して返すのみ

## Propertiesの使用方法

`Properties`はフィールドに`@Autowired`を付けてインジェクションして使用する。

```java
    @Autowired
    private ClientSearchProperties properties; // フィールド名はpropertiesを推奨
```

`Properties`をインジェクションして良いステレオタイプは次の通り。

- `Controller`
- `RestController`
- `Service`
- `ViewHelper`
