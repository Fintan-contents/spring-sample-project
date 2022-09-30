# Chunkの実装方法

- [Chunk型のバッチアプリケーションの構成](#chunk型のバッチアプリケーションの構成)
- [ライブラリが提供するクラス](#ライブラリが提供するクラス)
- [ライブラリが提供するクラスを使用する場合のポイント](#ライブラリが提供するクラスを使用する場合のポイント)
- [各ステレオタイプの実装を自作する場合のポイント](#各ステレオタイプの実装を自作する場合のポイント)
- [入力と出力の型の設定方法](#入力と出力の型の設定方法)

## Chunk型のバッチアプリケーションの構成

`Chunk`型のバッチアプリケーションでは、処理を`ItemReader`、`ItemProcessor`、`ItemWriter`の3つに分けて実装する。
それぞれで実行する処理は、以下のように【030_アプリ設計/010_システム機能設計/システム機能設計書】と対応する。

|ステレオタイプ |【030_アプリ設計/010_システム機能設計/システム機能設計書】との対応関係                    |
|---------------|-----------------------------------------------------|
|`ItemReader`   |「入力データ定義」に書かれた仕様でデータを入力する    |
|`ItemProcessor`|「処理詳細」に書かれた処理を実行する※                |
|`ItemWriter`   |「出力データ定義」に書かれた仕様でデータを出力する    |

※「入力データ定義」および「出力データ定義」を入出力する処理は除く

## ライブラリが提供するクラス

ファイルやデータベーステーブルの入出力など、一般的によく利用される機能についてはSpring BatchやMyBatisがクラスを提供している。
以下に、本システムで使用するクラスと、使い方を解説しているガイドのリンクを記載する。

|用途              |クラス                                             |ガイド                            |
|------------------|---------------------------------------------------|----------------------------------|
|CSVファイル入出力 |`FlatFileItemReader`、`FlatFileItemWriter`         |[ファイル操作](./file-operation.md)|
|`Item`の精査      |`BeanValidatingItemProcessor`                      |[入力値精査](validation.md)        |
|データベース入出力|`MyBatisCursorItemReader`、`MyBatisBatchItemWriter`|[データアクセス](data-access.md)   |

これらのクラスの機能で足りる場合は、これらのクラスを使用すること。
ライブラリが提供するクラスの機能では【030_アプリ設計/010_システム機能設計/システム機能設計書】に記載された仕様を実現できない場合は、それぞれのステレオタイプの実装を自作して対応する。


## ライブラリが提供するクラスを使用する場合のポイント

ライブラリが提供する`ItemReader`などの実装を利用する場合のポイントを以下で説明する。

```java
@Configuration
public class ImportProjectsConfig extends BatchBaseConfig {
    // 省略

    @Bean // (1)
    @StepScope // (1)
    public MyBatisCursorItemReader<ProjectWork> importProjectsItemReader() { // (1), (2), (3)
        return new MyBatisCursorItemReaderBuilder<ProjectWork>() // (4)
                .sqlSessionFactory(sqlSessionFactory)
                .parameterValues(Map.of("businessDate", businessDateSupplier.getDate()))
                .queryId(ImportProjectsMapper.class.getName() + ".selectProjectWorksInPeriod")
                .build();
    }

    // 省略
}
```

- 実装のポイント
    - (1) `Config`で、ライブラリが提供するクラスのインスタンスをSpringのBeanとして定義するメソッドを作成する。  
      ライブラリが提供するクラスのインスタンスを返すメソッドを作成し、`@Bean`と`@StepScope`を設定する
    - (2) 戻り値の型は、ライブラリが提供するクラスにすること。  
      [入力と出力の型の設定方法](#入力と出力の型の設定方法)も参照すること
    - (3) Bean名が他のバッチ処理と重複しないようにするため、メソッド名は論理的な名前かバッチ処理IDを元に命名する。  
      このとき、接尾辞にはステレオタイプの名前をつけること（バッチ処理IDを用いる場合は`ba1010101ItemReader`など）
    - (4) インスタンスの生成方法はクラスごとに異なる。  
      具体的な生成方法は、[ライブラリが提供するクラス](#ライブラリが提供するクラス)に記載したそれぞれのガイドを参照

## 各ステレオタイプの実装を自作する場合のポイント

各ステレオタイプの実装を自作する場合のポイントを以下で説明する。

```java
@Component // (1)
@StepScope // (1)
public class ImportProjectsItemProcessor implements ItemProcessor<ProjectWork, Project> { // (2), (3)
    @Autowired // (4)
    private ImportProjectsMapper importProjectsMapper;

    @Override
    public Project process(ProjectWork work) {
        // 省略 (5)
    }
}
```

- 実装のポイント
    - (1) 自作したクラスは`@Component`と`@StepScope`を設定してSpringのBeanとして定義する
    - (2) Bean名が他のバッチ処理と重複しないようにするため、クラス名は論理的な名前かバッチ処理IDを元に命名する（バッチ処理IDを用いた場合は`BA1010101ItemProcessor`など）
    - (3) 作成するステレオタイプに合わせて`ItemReader<入力>`、`ItemProcessor<入力, 出力>`、`ItemWriter<出力>`のいずれかのインタフェースを実装する。  
      入出力に指定する型については[入力と出力の型の設定方法](#入力と出力の型の設定方法)を参照
    - (4) 依存するBeanのインスタンスはフィールドに`@Autowired`をつけて、フィールドインジェクションで取得する
    - (5) 【030_アプリ設計/010_システム機能設計/システム機能設計書】に従い処理を実装する。  
      ステレオタイプごとに【030_アプリ設計/010_システム機能設計/システム機能設計書】のどの部分を参照するかは[`Chunk`型のバッチアプリケーションの構成](#chunk型のバッチアプリケーションの構成)を参照

## 入力と出力の型の設定方法

`ItemReader<入力>`、`ItemProcessor<入力, 出力>`、`ItemWriter<出力>`は、それぞれ入出力する型を設定する必要がある。
これらの入力と出力に指定する型は、`Step`を定義するときに使用する`chunk`メソッドの入出力に指定する型と同じになる。
`chunk`メソッドで入力と出力を決める方法については、[バッチアプリケーションの実装方法](batch-application.md#入力と出力の型の設定方法)を参照。
