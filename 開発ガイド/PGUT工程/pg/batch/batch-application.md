# バッチアプリケーションの実装方法

- [Configを作成する](#configを作成する)
- [Stepを定義する](#stepを定義する)
  - [Chunk型の場合の定義方法](#chunk型の場合の定義方法)
    - [入力と出力の型の設定方法](#入力と出力の型の設定方法)
    - [Chunkのサイズをプロパティファイルに定義する](#chunkのサイズをプロパティファイルに定義する)
  - [Tasklet型の場合の定義方法](#tasklet型の場合の定義方法)
- [Jobを定義する](#jobを定義する)

## Configを作成する

`Config`は、【030_アプリ設計/010_システム機能設計/システム機能設計書】の「バッチ取引概要」シートの「バッチ処理一覧」にあるバッチ処理の単位で作成する。
以下で、`Config`を実装するときのポイントを説明する。

```java
@Configuration // (1)
public class SampleConfig extends BatchBaseConfig { // (2), (3)
    @Autowired // (4)
    private SampleItemProcessor sampleItemProcessor;
    @Autowired // (4)
    private SampleItemWriter sampleItemWriter;

    // 省略

    @Bean
    @StepScope
    public MyBatisCursorItemReader<ProjectWork> sampleItemReader() { // (5)
        // 省略
    }

    @Bean
    @StepScope
    public TruncateTableListener sampleTruncateTableListener() { // (5)
        // 省略
    }

    @Bean
    public Step sampleStep() { // (6)
        // 省略
    }

    @Bean
    public Job sampleJob() { // (7)
        // 省略
    }
}
```

- 実装のポイント
    - (1) `Config`には`@Configuration`を設定すること
    - (2) クラス名は論理的な名前かバッチ処理IDを元に命名すること（バッチ処理IDを用いる場合は`BA1010102Config`など）
    - (3) 基盤部品の`BatchBaseConfig`を継承すること
    - (4) 依存するBeanはフィールドに`@Autowired`をつけて、フィールドインジェクションで取得すること
    - (5) `Step`を構成する要素（`ItemReader`、`ItemProcessor`、`ItemWriter`、`Tasklet`、`StepListener`）のうち、Spring BatchやMyBatisなどライブラリが提供するクラスをBeanとして定義する場合は、`Config`でBeanを定義するメソッドを作って定義する
    - (6) `Step`をBeanとして定義するメソッドを作成する。詳細は[`Step`を定義する](#stepを定義する)を参照
    - (7) `Job`をBeanとして定義するメソッドを作成する。詳細は[`Job`を定義する](#jobを定義する)を参照

## Stepを定義する

`Step`の定義は、バッチアプリケーションが`Chunk`型か`Tasklet`型かによって定義方法が異なる。
まず、いずれの場合でも共通となる実装のポイントを説明し、そのあとでそれぞれの場合でのポイントを説明する。

```java
@Configuration
public class SampleConfig extends BatchBaseConfig {

    // 省略

    @Bean // (1)
    public Step sampleStep() { // (1), (2)
        return new StepBuilder("BA1010101", jobRepository) // (3)

                // Chunk型かTasklet型かによって異なるため、この部分の詳細は後述する

                .build();
    }
}
```

- 実装のポイント
    - (1) `Step`を返すメソッドを作成し`@Bean`を設定する
    - (2) Bean名が他のバッチ処理と重複しないようにするため、メソッド名は論理的な名前かバッチ処理IDを用いて命名すること。  
      このとき、接尾辞として`Step`を設定すること （バッチ処理IDを用いる場合は、`ba1010101Step`など）
    - (3) `Step`のインスタンスは、`StepBuilder`を使用して作成する。  
      引数にはバッチ処理IDと`JobRepository`のインスタンスを渡す。  
      `JobRepository`のインスタンスは`BatchBaseConfig`に用意されているものを使用する

### Chunk型の場合の定義方法

【030_アプリ設計/010_システム機能設計/システム機能設計書】に「入力データ定義」と「出力データ定義」の両方が存在する場合、そのバッチアプリケーションは`Chunk`型となる。
ここでは、`Chunk`型での`Step`の定義方法について説明する。

```java
@Configuration
public class SampleConfig extends BatchBaseConfig {
    @Autowired // (3)
    private SampleItemProcessor sampleItemProcessor;
    @Autowired // (3)
    private SampleItemWriter sampleItemWriter;

    @Bean
    @StepScope
    public MyBatisCursorItemReader<ProjectWork> sampleItemReader() { // (3)
        // 省略
    }

    @Bean
    @StepScope
    public TruncateTableListener sampleTruncateTableListener() { // (5)
        // 省略
    }

    // 省略

    @Bean
    public Step sampleStep() {
        int chunkSize = sampleProperties().getChunkSize(); // (2)

        return new StepBuilder("BA1010101", jobRepository)

                .<ProjectWork, Project> chunk(chunkSize, platformTransactionManager) // (1), (2)

                .reader(sampleItemReader()) // (3)
                .processor(sampleItemProcessor) // (3)
                .writer(sampleItemWriter) // (3)

                .faultTolerant() // (4)
                .skip(FlatFileParseException.class) // (4)
                .skip(ValidationException.class) // (4)
                .skip(BatchSkipItemException.class) // (4)
                .skipLimit(Integer.MAX_VALUE) // (4)

                .listener(loggingSkipItemListener) // (4)
                .listener(loggingCountChunkListener) // (5)
                .listener(sampleTruncateTableListener()) // (6)
                .build();
    }

    // 省略
}
```

- 実装のポイント
    - (1) まず`chunk`メソッドを呼び出す。
      入力と出力の型の設定方法については[入力と出力の型の設定方法](#入力と出力の型の設定方法)を参照
    - (2) `chunk`メソッドの引数には`Chunk`のサイズと`PlatformTransactionManager`のインスタンスを渡す。  
      `Chunk`のサイズはプロパティファイルで管理する。したがって、`Properties`経由で値を取得する。  
      `Properties`経由でプロパティファイルの値を取得する方法については[プロパティ管理](property-management.md)を参照。  
      また、プロパティファイルの書き方については[`Chunk`のサイズをプロパティファイルに定義する](#chunkのサイズをプロパティファイルに定義する)を参照。  
      `PlatformTransactionManager`のインスタンスは`BatchBaseConfig`に用意されているものを使用する
    - (3) `Step`を構成する`ItemReader`、`ItemProcessor`、`ItemWriter`を設定する
        - Beanをメソッドで定義している場合は、そのメソッドを呼び出して設定する
        - `@Component`でBean定義している場合は、`@Autowired`でフィールドにインジェクションして設定する
    - (4) `Item`をスキップしたときのハンドリングに関する設定を行う。  
      詳細は[エラーハンドリング](error-handling.md)を参照
    - (5) バッチ処理の進捗をログ出力する設定を行う。`LoggingCountChunkListener`は`Chunk`を処理したタイミングでその時の入力件数・スキップ件数・コミット回数をログ出力する。
    - (6) ワークテーブルにデータを登録するバッチ処理では、`TruncateTableListener`を使ってバッチ処理開始時にテーブルのTRUNCATEを行うように実装する。  
      `TruncateTableListener`の使い方については[データアクセス](data-access.md)を参照

#### 入力と出力の型の設定方法

`<入力, 出力>chunk()`の入力と出力に指定する型は、【030_アプリ設計/010_システム機能設計/システム機能設計書】の「入力データ定義」および「出力データ定義」を元に決定する。

データベーステーブルが対象の場合は、そのテーブルに対応する`Model`を指定する。
一方、ファイルが対象の場合は、そのファイルの情報をマッピングする`Item`を指定する（`Item`については[ファイル操作](file-operation.md)を参照）。


#### Chunkのサイズをプロパティファイルに定義する

`Chunk`のサイズは`100`を基本とし、バッチ処理でチューニングが必要な場合は性能計測を行いチューニングを実施する。
このため、プロパティファイルでは以下のように`Chunk`サイズに`100`を設定すること。

```properties
project.sample.chunk-size=100
```

### Tasklet型の場合の定義方法

【030_アプリ設計/010_システム機能設計/システム機能設計書】に「入力データ定義」と「出力データ定義」の両方が存在しない場合、そのバッチアプリケーションは`Tasklet`型となる。
ここでは、`Tasklet`型での`Step`の定義方法について説明する。

```java
@Configuration
public class SampleConfig extends BatchBaseConfig {
    @Autowired // (1)
    private SampleTasklet sampleTasklet;

    @Bean
    public Step sampleStep() {
        return new StepBuilder("BA1010101", jobRepository)
                .tasklet(sampleTasklet, platformTransactionManager) // (2)
                .build();
    }

    // 省略
}
```

- 実装のポイント
    - (1) `Tasklet`のインスタンスはフィールドに`@Autowired`を設定してフィールドインジェクションで取得する
    - (2) `tasklet`メソッドで、`Tasklet`と`PlatformTransactionManager`のインスタンスを設定する。  
      `PlatformTransactionManager`のインスタンスは`BatchBaseConfig`に用意されているものを使用する

## Jobを定義する

以下で、`Job`を定義するときのポイントを説明する。

```java
@Configuration
public class SampleConfig extends BatchBaseConfig {
    // 省略

    @Bean
    public Step sampleStep() { // (5)
        // 省略
    }

    @Bean // (1)
    public Job sampleJob() { // (1), (2)
        return new JobBuilder("BA1010101", jobRepository) // (3)
                .start(sampleStep()) // (4)
                .listener(loggingCountJobListener) // (5)
                .build();
    }
}
```

- 実装のポイント
    - (1) `Job`を返すメソッドを作成し`@Bean`を設定する
    - (2) Bean名が他のバッチ処理と重複しないようにするため、メソッド名は論理的な名前かバッチ処理IDを用いて命名すること。  
      このとき、接尾辞として`Job`を設定すること（バッチ処理IDを用いる場合は、`ba1010101Job`など）
    - (3) `Job`のインスタンスは、`JobBuilder`を使用して作成する。  
      引数にはバッチ処理IDと`JobRepository`のインスタンスを渡す。  
      `JobRepository`のインスタンスは`BatchBaseConfig`に用意されているものを使用する
    - (4) `Step`を定義したメソッドを使い、`start`メソッドで`Step`を設定する
    - (5) `Step`が`Chunk`型の場合は入力件数とスキップした件数の結果をログに出力するため、`LoggingCountJobListener`を設定する
        - `LoggingCountJobListener`のインスタンスは、`BatchBaseConfig`に用意されているものを使用する
        - `Step`が`Tasklet`型の場合は`LoggingCountJobListener`を設定しない
