# Taskletの実装方法

`Tasklet`実装時のポイントを以下に示す。

```java
@Component // (1)
@StepScope // (1)
public class SampleTasklet implements Tasklet { // (2), (3)
    @Autowired // (4)
    private SampleMapper sampleMapper;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        // 業務処理の実装は省略 (5)
        return RepeatStatus.FINISHED; // (6)
    }
}
```

- 実装のポイント
    - (1) `Tasklet`を実装したクラスは、`@Component`と`@StepScope`を設定する
    - (2) クラス名は、論理的な名前またはバッチ処理IDを元に命名する（バッチ処理IDを元にする場合は`BA1010101Tasklet`など）
    - (3) `Tasklet`インタフェースを実装する
    - (4) 依存するBeanはフィールドで定義し、`@Autowired`でフィールドインジェクションして取得する
    - (5) `execute()`メソッドの中で【030_アプリ設計/010_システム機能設計/システム機能設計書】の「処理詳細」に書かれた業務処理を実装する
    - (6) `execute()`メソッドは`RepeatStatus.FINISHED`を`return`するように実装する
