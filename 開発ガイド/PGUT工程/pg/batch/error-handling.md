# エラーハンドリング

- [Chunk型のバッチ処理でのエラーハンドリング](#chunk型のバッチ処理でのエラーハンドリング)
- [バッチ処理を明示的に異常終了させる](#バッチ処理を明示的に異常終了させる)
- [終了コードの制御](#終了コードの制御)

## Chunk型のバッチ処理でのエラーハンドリング

`Item`の処理中に例外がスローされたとき、バッチ処理を異常終了させるのではなく、その`Item`はスキップしてバッチ処理を継続したい場合がある。

そういった場合は`Step`を定義する際、スキップ対象となる例外を設定する。

```java
@Configuration
public class ImportProjectsToWorkConfig extends BatchBaseConfig {
    // 省略

    @Bean
    public Step importProjectsToWorkStep() {
        return new StepBuilder("BA1060201", jobRepository)
                // 省略

                .faultTolerant() // (1)
                .skip(ValidationException.class) // (2)
                .skip(FlatFileParseException.class) // (2)
                .skip(BatchSkipItemException.class) // (2)
                .skipLimit(Integer.MAX_VALUE) // (3)

                .listener(loggingSkipItemListener) // (4)
                .build();
    }
}
```

- 実装のポイント
    - (1) `faultTolerant`メソッドを呼び出す
    - (2) `skip`メソッドでスキップ対象となる例外を設定する。スキップ対象となる例外は複数、設定できる。なお、次の例外は必ずスキップ対象とすること
        - `BatchSkipItemException`
        - `FlatFileParseException`
        - `ValidationException` [^1]
    - (3) `skipLimit`メソッドでスキップする回数の上限を設定する。【030_アプリ設計/010_システム機能設計/システム機能設計書】で指定が無い限り`Integer.MAX_VALUE`を設定すること
    - (4) スキップした際にログを出力するため、`LoggingSkipItemListener`を必ず設定すること

[^1]: `org.springframework.batch.item.validator.ValidationException`

## バッチ処理を明示的に異常終了させる

【030_アプリ設計/010_システム機能設計/システム機能設計書】にしたがってバッチ処理を明示的に異常終了させたい場合、基盤部品の`BatchSystemException`をスローする。

```java
@Component
@StepScope
public class ImportProjectsItemProcessor implements ItemProcessor<ProjectWork, Project> {
    @Autowired
    private ImportProjectsMapper importProjectsMapper;
    @Autowired // (1)
    private BatchSystemExceptionCreator batchSystemExceptionCreator; // (1)

    @Override
    public Project process(ProjectWork work) {
        if (check(work)) {
            throw batchSystemExceptionCreator.create( // (2)
                "errors.system-error", work.getProjectWorkId(), work.getProjectId()); // (3)
        }

        // 省略
    }
}
```

- 実装のポイント
    - (1) `BatchSystemExceptionCreator`のフィールドを定義し、`@Autowired`を設定する
    - (2) `BatchSystemExceptionCreator`の`create`メソッドで`BatchSystemException`を生成し`throw`する
    - (3) `create`メソッドの第一引数にはエラーメッセージのメッセージIDを設定し、第二引数以降にはメッセージに埋め込む値を設定する

`BatchSystemException`のハンドリングはSpring Batchが行う。 したがって、実装者が個別に対応する必要はない。

## 終了コードの制御

スキップが発生した場合の警告終了や異常終了のときの終了コードの制御については、共通的な仕組みで処理される。
したがって、実装者が個別に対応する必要はない。
