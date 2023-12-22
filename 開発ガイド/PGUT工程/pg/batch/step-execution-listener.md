# バッチ処理の前後で処理を行う

Chunkで処理する対象とは別のテーブルを後処理で更新したい場合など、バッチ処理本体とは別に前後で何かしらの処理を行いたい場合の実装方法について説明する。

- [StepExecutionListenerの実装方法](#stepexecutionlistenerの実装方法)
- [StepExecutionListenerの設定方法](#stepexecutionlistenerの設定方法)

## StepExecutionListenerの実装方法

バッチ処理の前後処理は、`StepExecutionListener`を用いることで実現する。
ここでは、`StepExecutionListener`の実装方法を説明する。

```java
package com.example.batch.project.listener; // (1)

// import文は省略

@Component // (2)
@StepScope // (2)
public class CreateUserProjectsStepExecutionListener implements StepExecutionListener { // (3), (4)
    
    @Autowired // (5)
    private CreateUsersProjectsMapper createUsersProjectsMapper;

    @Override
    public void beforeStep(StepExecution stepExecution) { // (6)
        // 実装は省略
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) { // (6)
        // 実装は省略
        return null; // (7)
    }
}
```

- 実装のポイント
    - (1) 作成した`StepExecutionListener`は、<ドメイン>.<機能>.`listener`パッケージの下に配置する
    - (2) 作成した`StepExecutionListener`には、`@Component`と`@StepScope`アノテーションを設定する
    - (3) Bean名が他のバッチ処理と重複しないようにするため、クラス名は論理的な名前かバッチ処理IDを元に命名する。  
      このとき、接尾辞には`StepExecutionListener`をつけること（バッチ処理IDを用いる場合は`BA1010101StepExecutionListener`など）
    - (4) `StepExecutionListener`を実装して作成する
    - (5) 依存するBeanのインスタンスはフィールドに`@Autowired`をつけて、フィールドインジェクションで取得する
    - (6) 前処理を実装する場合は`beforeStep`メソッドを、後処理を実装する場合は`afterStep`メソッドをオーバーライドして実装する
    - (7) `afterStep`メソッドを実装した場合、戻り値は`null`を返すように実装する

## StepExecutionListenerの設定方法

作成した`StepExecutionListener`は、`Config`で`Step`に設定することで有効になる。
ここでは、`Config`で`Step`に`StepExecutionListener`を設定する方法を説明する。

```java
public class CreateUsersProjectsConfig extends BatchBaseConfig {
    
    @Autowired
    private CreateUserProjectsStepExecutionListener createUserProjectsStepExecutionListener; // (1)

    // 他の実装は省略

    @Bean
    public Step createUsersProjectsStep() {
        int chunkSize = createUsersProjectsProperties().getChunkSize();

        return new StepBuilder("BA1060301", jobRepository)
                // 途中の実装は省略
                .listener(createUserProjectsStepExecutionListener) // (2)
                .build();
    }
}
```

- 実装のポイント
    - (1) 作成した`StepExecutionListener`を、`Config`のフィールドで宣言して`@Autowired`をつけてフィールドインジェクションする
    - (2) `Step`を定義しているメソッドで、(1)でフィールドインジェクションした`StepExecutionListener`のインスタンスを`listener`メソッドに渡す
