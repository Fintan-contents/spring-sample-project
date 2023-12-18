# 起動パラメータの利用方法

起動パラメータの受け取り方は、起動パラメータを利用するクラスのBeanをどのように定義するかによって変わる。
そして、Beanの定義方法は、以下のようにして決まる。

- ステレオタイプ(`ItemReader`、`ItemProcessor`、`ItemWriter`、`Tasklet`)を自作する場合は`@Component`を使用する
- Spring BatchやMyBatisが提供する`ItemReader`、`ItemProcessor`、`ItemWriter`を使う場合はBeanを定義するメソッドを使用する

それぞれのBean定義の方法における起動パラメータの受け取り方を、以下で解説する。


- [@ComponentでBean定義したクラスで受け取る](#componentでbean定義したクラスで受け取る)
- [Beanを定義するメソッドで受け取る](#beanを定義するメソッドで受け取る)
- [デフォルト値を設定する](#デフォルト値を設定する)
- [起動パラメータの型](#起動パラメータの型)

## @ComponentでBean定義したクラスで受け取る

`@Component`でBean定義したクラスで起動パラメータを受け取る方法について、実装例を以下に示す。

```java
@Component
@StepScope
public class SampleItemProcessor implements ItemProcessor<ProjectWork, Project> {
    @Value("#{jobParameters['key']}") // (1)
    private String key; // (2), (3)

    // 省略
}
```

- 実装のポイント
    - (1) 起動パラメータを受け取るフィールドを定義し、`@Value("#{jobParameters['<パラメータ名>']}")`を付ける。  
      `<パラメータ名>`には受け取りたい起動パラメータのパラメータ名を設定する。  
      パラメータ名は、【030_アプリ設計/010_システム機能設計/システム機能設計書】の「起動パラメータ」の印刷範囲外に記載されている「パラメータ名（物理）」を使用する
    - (2) フィールドの型については[起動パラメータの型](#起動パラメータの型)を参照
    - (3) フィールド名は、【030_アプリ設計/010_システム機能設計/システム機能設計書】の「起動パラメータ」の印刷範囲外に記載されている「パラメータ名（物理）」を使用する

## Beanを定義するメソッドで受け取る

`Config`のメソッドでBeanを定義している場合に起動パラメータを受け取る方法について、実装例を以下に示す。

```java
@Configuration
public class SampleConfig extends BatchBaseConfig {

    @Bean
    @StepScope
    public MyBatisBatchItemWriter sampleItemWriter(@Value("#{jobParameters['key']}") String key) { // (1), (2), (3)
        // 省略
    }

    @Bean
    public Step sampleStep() {
        return new StepBuilder("BA1010101", jobRepository)
                // 省略
                .processor(sampleItemWriter(null)) // (4)
                // 省略
                .build()
    }
}
```

- 実装のポイント
    - (1) 起動パラメータを受け取る引数を定義し、`@Value("#{jobParameters['<パラメータ名>']}")`を付ける。  
      `<パラメータ名>`には受け取りたい起動パラメータのパラメータ名を設定する。  
      パラメータ名は、【030_アプリ設計/010_システム機能設計/システム機能設計書】の「起動パラメータ」の印刷範囲外に記載されている「パラメータ名（物理）」を使用する
    - (2) 引数の型については[起動パラメータの型](#起動パラメータの型)を参照
    - (3) 引数の名前は、【030_アプリ設計/010_システム機能設計/システム機能設計書】の「起動パラメータ」の印刷範囲外に記載されている「パラメータ名（物理）」を使用する
    - (4) 起動パラメータを受け取るメソッドを他のメソッドから使用する場合は、引数に`null`を指定する。  
      この値は、実行時にSpringによって対応する起動パラメータの値に置き換えられる

## デフォルト値を設定する

```java
@Value("#{jobParameters['key'] ?: 'default value'}")
```

`@Value("#{jobParameters[<パラメータ名>] ?: <デフォルト値>}")`とすることで、起動パラメータが指定されなかった場合のデフォルト値を設定できる。

## 起動パラメータの型

起動パラメータを受け取るフィールドや引数の型は、次の表にあるうちのいずれかにする。

|型|説明|主な用途|
|---|---|---|
|`String`|文字列|名前、コード値、文字列で表現される項目|
|`Integer`|整数|サロゲートキー、`Integer`の範囲で表現可能な整数項目|
|`Long`|整数|バージョン番号、`Long`の範囲で表現可能な整数項目|
|`BigDecimal`|数値|金額|
|`LocalDate`|日付|日付|
|`LocalTime`|時刻|時刻|
|`LocalDateTime`|日時|日時|
|`boolean`|真偽値|フラグ|
|`List<String>`|文字列のリスト|複数の値を受け取る場合|

これらのうち`LocalDate`、`LocalTime`、`LocalDateTime`は`@DateTimeFormat`で値のフォーマットを指定すること。

```java
    @Value("#{jobParameters['date']}")
    @DateTimeFormat(pattern = "uuuu-MM-dd") // (1)
    private LocalDate date;

    @Value("#{jobParameters['time']}")
    @DateTimeFormat(pattern = "HH:mm") // (1)
    private LocalTime time;

    @Value("#{jobParameters['dateTime']}")
    @DateTimeFormat(pattern = "uuuu-MM-dd HH:mm") // (1)
    private LocalDateTime dateTime;
```

- 実装のポイント
    - (1) フォーマットは`DateTimeFormatter`を使用して行われる。そのため年を表すパターンは`u`となる

なお、引数で受け取る場合の例は以下のようになる。

```java
@Bean
@StepScope
public MyBatisBatchItemWriter sampleItemWriter(
        @Value("#{jobParameters['date']}") @DateTimeFormat(pattern = "uuuu-MM-dd") LocalDate date,
        @Value("#{jobParameters['time']}") @DateTimeFormat(pattern = "HH:mm") LocalTime time,
        @Value("#{jobParameters['dateTime']}") @DateTimeFormat(pattern = "uuuu-MM-dd HH:mm") LocalDateTime dateTime
    ) {
    // 省略
}
```
