# データアクセス

- [MapperとMapper XMLを作成する](#mapperとmapper-xmlを作成する)
- [MyBatisが提供するItemReader、ItemWriter](#mybatisが提供するitemreaderitemwriter)
  - [MyBatisCursorItemReaderの使い方](#mybatiscursoritemreaderの使い方)
  - [MyBatisBatchItemWriterの使い方](#mybatisbatchitemwriterの使い方)
- [条件によってinsertやupdateなどを切り替える](#条件によってinsertやupdateなどを切り替える)
- [複数テーブルに書き出す](#複数テーブルに書き出す)
- [テーブルのTRUNCATE](#テーブルのtruncate)

## MapperとMapper XMLを作成する

データアクセスを行うにはMapperとMapper XMLが必要となる。

```java
@Mapper
public interface ImportProjectsToWorkMapper { // (1)
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.batch.project.mapper.ImportProjectsToWorkMapper">
</mapper>
```

- 実装のポイント
    - (1) `Mapper`はバッチ処理単位に作成する。  
      このとき、Bean名が他のバッチ処理と重複しないようにするため、クラス名は論理的な名前かバッチ処理IDをもとに命名する（バッチ処理IDを用いる場合は`BA1060201Mapper`など）

その他の`Mapper`やMapper XMLの実装方法についてはWebと同様であるため、[Webのガイド](../web/data-access.md)を参照。


## MyBatisが提供するItemReader、ItemWriter

MyBatisは、以下の`ItemReader`と`ItemWriter`の実装を提供している。

- `MyBatisCursorItemReader`
- `MyBatisBatchItemWriter`

`Chunk`型のバッチ処理で入出力先がデータベースとなる場合は、基本的にこれらのクラスを使用すること。
これらのクラスがうまく適用できない場合は`ItemReader`、`ItemWriter`の実装クラスを作成し、`Mapper`を用いて個別にデータアクセスの処理を実装する。

### MyBatisCursorItemReaderの使い方

```java
public class ImportProjectsConfig extends BatchBaseConfig {
    // 省略

    @Bean // (1)
    @StepScope // (1)
    public MyBatisCursorItemReader<ProjectWork> importProjectsItemReader() { // (1), (2)
        return new MyBatisCursorItemReaderBuilder<ProjectWork>() // (3)
                .sqlSessionFactory(sqlSessionFactory) // (4)
                .queryId(ImportProjectsMapper.class.getName() + ".selectProjectWorksInPeriod") // (5)
                .build();
    }

    // 省略
}
```

- 実装のポイント
  - (1) `Config`に`MyBatisCursorItemReader<入力するModelの型>`を返すメソッドを定義し、`@Bean`と`@StepScope`を設定する
  - (2) Bean名が他のバッチ処理と重複しないようにするため、メソッド名は論理的な名前かバッチ処理IDを元に命名する。  
    このとき、接尾辞は`ItemReader`とする（バッチ処理IDを用いる場合は`ba1060202ItemReader`など）
  - (3) `MyBatisCursorItemReader`のインスタンスは、`MyBatisCursorItemReaderBuilder`を使って生成する
  - (4) `sqlSessionFactory`メソッドを使い、`SqlSessionFactory`を設定する。  
    `SqlSessionFactory`のインスタンスは、`BatchBaseConfig`に用意されているものを使用する
  - (5) `queryId`メソッドを使い、検索で使用するSQLを指定する。  
    SQLは、`<Mapperの型>.class.getName() + ".<Mapperのメソッド名>"`と指定する

### MyBatisBatchItemWriterの使い方

```java
public class ImportProjectsToWorkConfig extends BatchBaseConfig {
    // 省略

    @Bean // (1)
    @StepScope // (1)
    public MyBatisBatchItemWriter<ProjectWork> importProjectsToWorkItemWriter() { // (1), (2)
        return new MyBatisBatchItemWriterBuilder<ProjectWork>() // (3)
                .sqlSessionFactory(sqlSessionFactory) // (4)
                .statementId(ImportProjectsToWorkMapper.class.getName() + ".insertProjectWork") // (5)
                .build();
    }

    // 省略
}
```

- 実装のポイント
  - (1) `Config`に`MyBatisBatchItemWriter<出力するModelの型>`を返すメソッドを定義し、`@Bean`と`@StepScope`を設定する
  - (2) Bean名が他のバッチ処理と重複しないようにするため、メソッド名は論理的な名前かバッチ処理IDを元に命名する。  
    このとき、接尾辞は`ItemWriter`とする（バッチ処理IDを用いる場合は`ba1060202ItemWriter`など）
  - (3) `MyBatisBatchItemWriter`のインスタンスは、`MyBatisBatchItemWriterBuilder`を使って生成する
  - (4) `sqlSessionFactory`メソッドを使い、`SqlSessionFactory`を設定する。  
    `SqlSessionFactory`のインスタンスは、`BatchBaseConfig`に用意されているものを使用する
  - (5) `statementId`メソッドを使い、更新で使用するSQLを指定する。  
    SQLは、`<Mapperの型>.class.getName() + ".<Mapperのメソッド名>"`と指定する


## 条件によってinsertやupdateなどを切り替える

条件によって`insert`や`update`を切り替える場合は、`ItemWriter`を自作して対応する。
`ItemWriter`を自作するときの実装のポイントについては[各ステレオタイプの実装を自作する場合のポイント](chunk.md#各ステレオタイプの実装を自作する場合のポイント)を参照。

```java
@Component
@StepScope
public class ImportProjectsItemWriter implements ItemWriter<Project> {
    @Autowired
    private ImportProjectsMapper importProjectsMapper;

    @Override
    public void write(Chunk<? extends Project> items) {
        // (1)
        for (Project project : items) {
            // (2)
            if (project.getProjectId() != null) {
                importProjectsMapper.updateProject(project);
            }
        }
        // (1)
        for (Project project : items) {
            // (2)
            if (project.getProjectId() == null) {
                importProjectsMapper.insertProject(project);
            }
        }
    }
}
```

- 実装のポイント
  - (1) 本システムでは性能を出すためMyBatisの`BatchExecutor`を使用しているが、その場合は同一の`Mapper`メソッドを一度にまとめて呼び出す必要がある
    - もし、ひとつの`for`ループの中で異なる複数の`Mapper`メソッドが順に呼び出されるような実装だと`BatchExecutor`が有効に働かず、高い性能が出せない可能性があるため注意すること
  - (2) 条件によって使用する`Mapper`のメソッドを切り替えるように実装する
    - 上記実装例では、プロジェクトIDが`null`かどうかで`insert`か`update`のどちらを実行するかを切り替えている


## 複数テーブルに書き出す

複数のテーブルに書き出す場合の実装方法について説明する。

まず、それぞれの出力先テーブルに対応する`Model`をまとめたクラスを作成する。

```java
package com.example.batch.sample.model.ba1010101; // (1)

public class SampleModels { // (2)
    private Project project; // (3)
    private Organization organization; // (3)

    // アクセサ省略
}
```

- 実装のポイント
  - (1) `Model`をまとめたクラスを配置するパッケージは、`Model`と同じルールに従う
  - (2) クラス名は論理的な名前かバッチ処理IDを元に命名する。  
    このとき、接尾子は`Models`とする（バッチ処理IDを用いた場合は`BA1010101Models`など）
  - (3) 出力先テーブルに対応する`Model`をフィールドで定義する

次に、それぞれのテーブルへ出力するためのメソッドを`Mapper`に追加する。

```java
public interface SampleMapper {
    int insertProject(SampleModels sampleModels); // (1)
    int insertOrganization(SampleModels sampleModels); // (1)
}
```

- 実装のポイント
  - (1) メソッドの引数は、上で作成した`Model`をまとめたクラスを受け取るようにする

また、これらのメソッドに対応するSQLをMapper XMLに追加する。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.batch.project.mapper.SampleMapper">
    <insert id="insertProject">
        insert into
            project
            (
            project_name,
            project_type,
            project_class,
            -- 省略
            )
        values
            (
            #{project.projectName}, -- (1)
            #{project.projectType}, -- (1)
            #{project.projectClass}, -- (1)
            -- 省略
            )
    </insert>

    <insert id="insertOrganization">
        insert into
            organization
            (
            organization_name,
            upper_organization
            )
        values
            (
            #{organization.organizationName}, -- (1)
            #{organization.upperOrganization} -- (1)
            )
    </insert>
</mapper>
```

- 実装のポイント
  - (1) `Mapper`の引数には`Model`をまとめたクラスが渡されるので、`Model`の各プロパティは`<ModelをまとめたクラスでのModelのプロパティ名>.<各プロパティ名>`で参照できる

最後に、これらのSQLを使用する`MyBatisBatchItemWriter`を作成し、`CompositeItemWriter`を使ってまとめたものを`ItemWriter`として定義する。

```java
public class SampleConfig extends BatchBaseConfig {
    // 省略
    
    @Bean
    @StepScope
    public CompositeItemWriter<SampleModels> sampleItemWriter() {
        CompositeItemWriter<SampleModels> writer = new CompositeItemWriter<>(); // (1)

        List<ItemWriter<? super CompositeItemWriterModels>> delegates = List.of( // (2)
            new MyBatisBatchItemWriterBuilder<>() // (2)
                .sqlSessionFactory(sqlSessionFactory)
                .statementId(SampleMapper.class.getName() + ".insertProject")
                .build(),
            new MyBatisBatchItemWriterBuilder<>() // (2)
                .sqlSessionFactory(sqlSessionFactory)
                .statementId(SampleMapper.class.getName() + ".insertOrganization")
                .build()
        );

        writer.setDelegates(delegates); // (3)

        return writer;
    }

    // 省略
}
```

- 実装のポイント
  - (1) `CompositeItemWriter`のインスタンスを生成する
  - (2) 更新用のSQLごとに`MyBatisBatchItemWriter`を生成して`List`にする
  - (3) `setDelegates`メソッドで、`MyBatisBatchItemWriter`のリストを設定する

## テーブルのTRUNCATE

バッチ処理の開始前に更新対象のテーブルをTRUNCATEするような場合に、どのように実装するか説明する。

```java
public class ImportProjectsToWorkConfig extends BatchBaseConfig {
    // 省略

    @Bean // (1)
    @StepScope // (1)
    public TruncateTableListener importProjectsToWorkTruncateTableListener() { // (1), (2)
        return new TruncateTableListener(batchCommonMapper, "project_work"); // (3)
    }
    
    @Bean
    public Step importProjectsToWorkStep() {
        return new StepBuilder("BA1060201", jobRepository)
                // 省略
                .listener(importProjectsToWorkTruncateTableListener()) // (4)
                .build();
    }

    // 省略
}
```

- 実装のポイント
  - (1) `Config`に`TruncateTableListener`を返すメソッドを定義し、`@Bean`と`@StepScope`を設定する
  - (2) Bean名が他のバッチ処理と重複しないようにするため、メソッド名は論理的な名前かバッチ処理IDを元に命名する。  
    このとき、接尾辞は`TruncateTableListener`とする（バッチ処理IDを用いる場合は`ba1060202TruncateTableListener`など）
  - (3) `TruncateTableListener`のインスタンスを生成して返す
    - コンストラクタの第一引数には、`BatchBaseConfig`で用意されている`BatchCommonMapper`のインスタンスを設定する
    - コンストラクタの第二引数には、TRUNCATE対象のテーブル名を設定する（可変長引数になっており複数設定可）
  - (4) Bean定義したメソッドを使って`TruncateTableListener`を取得し、`listener`メソッドで`Step`に設定する
