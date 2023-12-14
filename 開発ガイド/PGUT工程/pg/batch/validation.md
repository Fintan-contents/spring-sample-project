# 入力値精査

- [Itemの精査](#itemの精査)
- [項目間精査](#項目間精査)
- [Itemの精査を有効にする](#itemの精査を有効にする)
- [Itemで完結しない精査](#itemで完結しない精査)
- [起動パラメータの精査](#起動パラメータの精査)
  - [必須精査だけで済む場合](#必須精査だけで済む場合)
  - [複雑な精査が必要な場合](#複雑な精査が必要な場合)
  - [DefaultJobParametersValidatorとアプリ基盤が提供するJobParametersValidatorを組み合わせる](#defaultjobparametersvalidatorとアプリ基盤が提供するjobparametersvalidatorを組み合わせる)

## Itemの精査

`Item`の作成方法については[ファイル操作](./file-operation.md)を参照。

`Item`の精査を行うには、【030_アプリ設計/030_インタフェース設計/外部インタフェース設計書】のレコードごとの項目を定義したシートの情報をもとにして、単項目精査のアノテーションを付ける。

```java
    @Required // (1)
    @Domain("projectName") // (2)
    private String projectName;
```

- 実装のポイント
    - (1) 項目の定義で「必須」に「○」が付いている場合は`@Required`を付ける
    - (2) 項目の定義で「ドメイン名」が書かれている場合は`@Domain`を付ける
        - `@Domain`に設定する値は【030_アプリ設計/070_データモデル設計/ドメイン定義書】の印刷範囲外にある「ドメイン名（物理）」である。項目の「ドメイン名」と、【030_アプリ設計/070_データモデル設計/ドメイン定義書】の「ドメイン名（論理）」を突き合わせてドメインを特定すること

## 項目間精査

項目間精査は`Item`に`@AssertTrue`を付けたメソッドを作成することで行う。

```java
    @AssertTrue(message = "{E0001}") // (1)
    public boolean isValidProjectPeriod() { // (2)
        if (projectStartDate == null || projectEndDate == null) { // (3)
            return true; // (3)
        }
        return !projectStartDate.isAfter(projectEndDate); // (4)
    }
```

- 実装のポイント
    - (1) `@AssertTrue`を付けて`message`でエラーメッセージのメッセージIDを設定する。エラーメッセージの定義方法は、[メッセージ管理](./message-management.md)参照
    - (2) 戻り値が`boolean`、引数なしのメソッドを作成する。名前は`is`から始める
    - (3) 項目間精査に使用する項目の`null`チェックを行う。どちらか片方でも`null`の場合、項目間精査ができないため`true`を返す
    - (4) 項目間の値をチェックする。この例では2つの日付の前後関係をチェックしている

## Itemの精査を有効にする

`Item`に設定したアノテーションによる精査を有効にするためには、Spring Batchが提供する`BeanValidatingItemProcessor`を`Step`に設定する必要がある。
またこのとき、`Item`を登録先のテーブルに対応する`Model`に変換するための`ItemProcessor`も同時に設定する必要がある。

ここでは、`BeanValidatingItemProcessor`を複数の`ItemProcessor`と組み合わせて`Step`に設定する方法について説明する。

```java
public class ImportProjectsToWorkConfig extends BatchBaseConfig {
    @Autowired // (1)
    private BeanValidatingItemProcessor<ImportProjectsToWorkItem> beanValidatingItemProcessor; // (2)
    @Autowired
    private ImportProjectsToWorkItemProcessor importProjectsToWorkItemProcessor;

    // 省略

    @Bean // (3)
    @StepScope // (3)
    public CompositeItemProcessor<ImportProjectsToWorkItem, ProjectWork> // (3), (4)
        importProjectsToWorkCompositeItemProcessor() { // (5)
        return new CompositeItemProcessorBuilder<ImportProjectsToWorkItem, ProjectWork>() // (6)
                .delegates(beanValidatingItemProcessor, importProjectsToWorkItemProcessor) // (7)
                .build();
    }
    
    @Bean
    public Step importProjectsToWorkStep() {
        // 省略

        return new StepBuilder("BA1060201", jobRepository)
                // 省略
                .processor(importProjectsToWorkCompositeItemProcessor()) // (8)
                // 省略
    }

    // 省略
}
```

- 実装のポイント
    - (1) `BeanValidatingItemProcessor`のフィールドを定義し、`@Autowired`をつける
    - (2) `BeanValidatingItemProcessor`の型引数には、入力する`Item`を指定する（上記の例だと`ImportProjectsToWorkItem`）
    - (3) `CompositeItemProcessor`を返すメソッドを定義し、`@Bean`と`@StepScope`を設定する
    - (4) `CompositeItemProcessor<入力, 出力>`の入出力の型は、`入力`には`Item`を設定し、`出力`には出力先テーブルに対応する`Model`を設定する
    - (5) Bean名が他のバッチ処理と重複しないようにするため、メソッド名は論理的な名前かバッチ処理IDをもとに命名する。  
      このとき、接尾辞は`CompositeItemProcessor`とすること（バッチ処理IDを用いた場合は`ba1060201CompositeItemProcessor`など）
    - (6) `CompositeItemProcessor`のインスタンスは、`CompositeItemProcessorBuilder`を使って生成する
    - (7) `delegates`メソッドに、組み合わせる`ItemProcessor`を実行順に設定する。  
      このとき、入力値精査が先に実行されるようにするため、必ず`BeanValidatingItemProcessor`を先頭に設定すること
    - (8) `CompositeItemProcessor`を定義したメソッドを使用し、`processor`メソッドで`Step`に設定する


## Itemで完結しない精査

DBを用いた精査など、`Item`で完結しない精査を行いたい場合は`ItemProcessor`に精査処理を実装する。

```java
@Component
@StepScope
public class ImportProjectsItemProcessor implements ItemProcessor<ProjectWork, Project> {

    @Override
    public Project process(ProjectWork work) {
        if (work.getProjectId() != null
            && importProjectsMapper.selectProjectByIdForUpdate(work.getProjectId()) == null) { // (1)
            throw new BatchSkipItemException(
                "ERR001", work.getProjectWorkId(), work.getProjectId()); // (2)
        }

        // 省略
    }
}
```

- 実装のポイント
    - (1) `ItemProcessor`のメソッド内で精査を行う
    - (2) エラーの場合、`BatchSkipItemException`をスローする
      - コンストラクタの第一引数には、エラーメッセージのメッセージIDを設定する
      - コンストラクタの第二引数以降には、エラーメッセージに埋め込む値を設定する


## 起動パラメータの精査

### 必須精査だけで済む場合

起動パラメータの精査が必須の精査だけで済む場合は、Spring Batchが提供する`DefaultJobParametersValidator`を使用する。

```java
public class SampleConfig extends BatchBaseConfig {

    // 省略

    @Bean // (1)
    public DefaultJobParametersValidator sampleJobParametersValidator() { // (1), (2)
        DefaultJobParametersValidator validator = new DefaultJobParametersValidator();
        validator.setRequiredKeys(new String[] {"jsr_batch_run_id", "sampleRequiredParameter"}); // (3)
        validator.setOptionalKeys(new String[] {"sampleOptionalParameter"}); // (4)
        return validator;
    }

    @Bean
    public Job sampleJob() {
        return new JobBuilder("BA0000000", jobRepository)
                // 省略
                .validator(sampleJobParametersValidator()) // (5)
                .build();
    }
}
```

- 実装のポイント
    - (1) `Config`に`DefaultJobParametersValidator`を返すメソッドを作成し、`@Bean`を付ける
    - (2) Bean名が他のバッチ処理と重複しないようにするため、メソッド名は論理的な名前かバッチ処理IDを用いて命名する。  
      このとき、接尾辞は`JobParametersValidator`とすること（バッチ処理IDを用いる場合は`ba1010101JobParametersValidator`など）
    - (3) 【030_アプリ設計/010_システム機能設計/システム機能設計書】の「起動パラメータ」で、「必須」に「○」が付いているパラメータを`setRequiredKeys`で設定する。  
      設定する値には、印刷範囲外の「パラメータ名（物理）」を使用する。  
      ただし、`jsr_batch_run_id`は【030_アプリ設計/010_システム機能設計/システム機能設計書】の「起動パラメータ」には記載されていないが必ず設定すること
    - (4) 【030_アプリ設計/010_システム機能設計/システム機能設計書】の「起動パラメータ」で、「必須」に「○」が付いていないパラメータを`setOptionalKeys`で設定する。  
      設定する値には、印刷範囲外の「パラメータ名（物理）」を使用する
    - (5) `DefaultJobParametersValidator`を定義したメソッドを使い、`validator`メソッドで`Job`に設定する

### 複雑な精査が必要な場合

項目間精査やデータベースを用いた精査が必要な場合は、アプリ基盤が提供する`JobParametersValidator`の実装クラスを使用する。

```java
public class SampleConfig extends BatchBaseConfig {
    // 省略

    @Bean // (1)
    public SampleJobParametersValidator sampleJobParametersValidator() { // (1), (2)
        return new SampleJobParametersValidator(); // (1)
    }

    @Bean
    public Job sampleJob() {
        return new JobBuilder("BA0000000", jobRepository)
                // 省略
                .validator(sampleJobParametersValidator()) // (3)
                .build();
    }
}
```

- 実装のポイント
    - (1) `Config`にアプリ基盤が提供する`JobParametersValidator`を返すメソッドを作成し、`@Bean`を付ける。  
      上記例では`SampleJobParametersValidator`が、アプリ基盤が提供する`JobParametersValidator`になる
    - (2) Bean名が他のバッチ処理と重複しないようにするため、メソッド名は論理的な名前かバッチ処理IDを用いて命名する。  
      このとき、接尾辞は`JobParametersValidator`とすること（バッチ処理IDを用いる場合は`ba1010101JobParametersValidator`など）
    - (3) `SampleJobParametersValidator`を定義したメソッドを使い、`validator`メソッドで`Job`に設定する


### DefaultJobParametersValidatorとアプリ基盤が提供するJobParametersValidatorを組み合わせる

単純な必須精査と複雑な精査の両方を行う場合は、2つの`JobParametersValidator`を組み合わせて使用する。

```java
public class SampleConfig extends BatchBaseConfig {

    @Bean // (1)
    public CompositeJobParametersValidator sampleCompositeJobParametersValidator() { // (1), (2)
        DefaultJobParametersValidator defaultValidator = new DefaultJobParametersValidator();
        defaultValidator.setRequiredKeys(new String[] {"jsr_batch_run_id", "requriedParam"});
        defaultValidator.setOptionalKeys(new String[] {"sampleParam1", "sampleParam2"});

        CompositeJobParametersValidator compositeValidator = new CompositeJobParametersValidator();
        compositeValidator.setValidators(List.of(defaultValidator, new SampleJobParametersValidator())); // (3)

        return compositeValidator;
    }

    // 省略

    @Bean
    public Job sampleJob() {
        return new JobBuilder("BA0000000", jobRepository)
                // 省略
                .validator(sampleCompositeJobParametersValidator()) // (4)
                .build();
    }
}
```

- 実装のポイント
    - (1) `Config`に`CompositeJobParametersValidator`を返すメソッドを定義し、`@Bean`を設定する
    - (2) Bean名が他のバッチ処理と重複しないようにするため、メソッド名は論理的な名前かバッチ処理IDを元に命名する。  
      このとき、接尾辞は`CompositeJobParametersValidator`とすること（バッチ処理IDを用いる場合は`ba1010101CompositeJobParametersValidator`など）
    - (3) `CompositeJobParametersValidator`の`setValidators`メソッドで、組み合わせる`JobParametersValidator`のリストを設定する。  
      このとき、必須精査を先に実行するため`DefaultJobParametersValidator`を先頭にすること
    - (4) `CompositeJobParametersValidator`を定義したメソッドを使い、`validator`メソッドで`Job`に設定する
