# ファイル操作

バッチでファイルの入力や出力を行う方法について解説する。

- [ファイル入力](#ファイル入力)
  - [マッピング先のItemを作成する](#マッピング先のitemを作成する)
    - [Itemの作成](#itemの作成)
    - [LineNumberItemを実装する](#linenumberitemを実装する)
    - [フィールドとアクセサの作成](#フィールドとアクセサの作成)
    - [Modelへの変換メソッドを作成する](#modelへの変換メソッドを作成する)
    - [Itemの型の情報をFlatFileItemReaderに設定する](#itemの型の情報をflatfileitemreaderに設定する)
    - [プロパティ名を設定する](#プロパティ名を設定する)
- [ファイル出力](#ファイル出力)
  - [Itemを作成する](#itemを作成する)
    - [フィールドとアクセサメソッドを作成する](#フィールドとアクセサメソッドを作成する)
    - [Modelからの変換メソッドを作成する](#modelからの変換メソッドを作成する)
  - [出力項目のフォーマットを実装する](#出力項目のフォーマットを実装する)
    - [FieldExtractorの作成](#fieldextractorの作成)
    - [Configの設定](#configの設定)

## ファイル入力

ここでは、CSVファイルを例にしてファイル入力を実装する方法について解説する。

CSVファイルの入力には、Spring Batchが提供する`FlatFileItemReader`を使用する。
`Config`における`FlatFileItemReader`の基本的な実装内容を以下に示す。

```java
@Bean
@StepScope // (1)
public FlatFileItemReader<ImportProjectsToWorkItem> importProjectsToWorkReader() { // (2), (3)
    BeanWrapperFieldSetMapper<ImportProjectsToWorkItem> fieldSetMapper = new BeanWrapperFieldSetMapper<>(); // (2)
    fieldSetMapper.setTargetType(ImportProjectsToWorkItem.class); // (2)

    fieldSetMapper.setConversionService(new DefaultFormattingConversionService()); // (4)

    DefaultLineMapper<ImportProjectsToWorkItem> delegate = new DefaultLineMapper<>(); // (2)
    delegate.setFieldSetMapper(fieldSetMapper);

    DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
    tokenizer.setDelimiter(","); // (5)
    tokenizer.setNames( // (2)
        "projectId",
        "projectName",
        "projectType",
        "projectClass",
        "projectStartDate",
        "projectEndDate",
        "organizationId",
        "clientId",
        "projectManager",
        "projectLeader",
        "note",
        "sales");

    delegate.setLineTokenizer(tokenizer);

    LineMapper<ImportProjectsToWorkItem> lineMapper = new LineNumberMapper<>(delegate); // (2), (7)

    FlatFileItemReader<ImportProjectsToWorkItem> reader = new FlatFileItemReader<>(); // (2)
    reader.setEncoding("UTF-8"); // (5)
    reader.setSaveState(false); // (6)
    reader.setLineMapper(lineMapper); // (7)
    Path inputFilePath = importProjectsToWorkProperties().getInputFilePath(); // (8)
    reader.setResource(new PathResource(inputFilePath)); // (8)
    reader.setStrict(false); // (9)

    return reader;
}
```

- `FlatFileItemReader`作成時のポイント
    - (1) `ItemReader`は`@StepScope`で定義する
    - (2) 読み込むCSVファイルごとに、型引数や`setTargetType(Class)`に渡す`Item`の型、`setNames(String...)`に指定するプロパティ名を設定する。  
      詳細は[マッピング先のItemを作成する](#マッピング先のitemを作成する)を参照
    - (3) Beanの名前は他のバッチ処理と重複しないようにするため、論理的な名前またはバッチ処理IDを元に命名する（バッチ処理IDを使う場合は`ba1060201ItemReader`など）
    - (4) `BeanWrapperFieldSetMapper`に`DefaultFormattingConversionService`を設定する
    - (5) 【030_アプリ設計/030_インタフェース設計/外部インタフェース設計書】に記載された定義に合わせて、デリミタや文字コードを設定する
    - (6) `saveState`に`false`を設定する
    - (7) `FlatFileItemReader`に`LineNumberMapper`を設定する
    - (8) 入力するCSVファイルのパスはプロパティファイルで管理する。  
      したがって、`Properties`を作成し、そこからCSVファイルのパスを取得し`FlatFileItemReader`に設定する。  
      `Properties`の作成方法については[プロパティ管理](property-management.md)を参照
    - (9) 【030_アプリ設計/030_インタフェース設計/外部インタフェース設計書】に従い、入力ファイルが無いときに異常終了としない場合は`setStrict(false)`を設定する

### マッピング先のItemを作成する

CSVファイルの1レコードのデータをマッピングする`Item`を作成する方法と、`FlatFileItemReader`に設定する方法について説明する。

#### Itemの作成

まず、【030_アプリ設計/030_インタフェース設計/外部インタフェース設計書】のレコードの定義をもとに`Item`を作成する。

```java
public class ImportProjectsToWorkItem {
}
```

`Item`のクラス名は、論理的な名前またはバッチ処理IDを元に命名する。このとき、接尾辞として`Item`を設定する。

#### LineNumberItemを実装する

CSVファイルの入力をマッピングする`Item`は、`LineNumberItem`を実装する。

このインタフェースを実装することで、CSVファイルから読み込んだときに`Item`の元となった行番号が設定される。
この行番号の情報は、入力値精査でエラーとなったときに原因となった行を特定できるようにするためエラーメッセージに出力する形で利用される。

```java
public class ImportProjectsToWorkItem implements LineNumberItem {
    private Integer lineNumber;

    @Override
    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
}
```

`LineNumberItem`には行番号（`lineNumber`）のアクセサが定義されているので、`Item`に行番号のフィールドとアクセサを定義する。

#### フィールドとアクセサの作成

```java
public class ImportProjectsToWorkItem implements LineNumberItem {
    private Integer projectId; // (1)
    private String projectName; // (1)

    public Integer getProjectId() { // (2)
        return projectId;
    }

    public void setProjectId(Integer projectId) { // (2)
        this.projectId = projectId;
    }

    public String getProjectName() { // (2)
        return projectName;
    }

    public void setProjectName(String projectName) { // (2)
        this.projectName = projectName;
    }

    // その他のフィールド、アクセサは省略
}
```

- 実装のポイント
    - (1) 【030_アプリ設計/030_インタフェース設計/外部インタフェース設計書】のレコードの定義をもとにフィールドを作成する
        - フィールド名は、「項目ID」を使用する。ただし、「項目ID」はスネークケースとなっているのでキャメルケースに置き換えること（例：`PROJECT_ID`→`projectId`）
    - (2) アクセサを作成する

フィールドの型は次の表にあるうちのいずれかにする。

|フィールドの型|説明|主な用途|
|---|---|---|
|`String`|文字列|名前、コード値、文字列で表現される項目|
|`Integer`|整数|サロゲートキー、`Integer`の範囲で表現可能な整数項目|
|`Long`|整数|バージョン番号、`Long`の範囲で表現可能な整数項目|
|`BigDecimal`|数値|金額|
|`LocalDate`|日付|日付|
|`LocalTime`|時刻|時刻|
|`LocalDateTime`|日時|日時|
|`boolean`|真偽値|フラグなどの真偽値を受け取る|

これらのうち`LocalDate`、`LocalTime`、`LocalDateTime`は`@DateTimeFormat`で値のフォーマットを指定すること。

```java
    @DateTimeFormat(pattern = "uuuu-MM-dd") // (1)
    private LocalDate date;

    @DateTimeFormat(pattern = "HH:mm") // (1)
    private LocalTime time;

    @DateTimeFormat(pattern = "uuuu-MM-dd HH:mm") // (1)
    private LocalDateTime dateTime;
```

- 実装のポイント
    - (1) フォーマットは`DateTimeFormatter`を使用して行われる。そのため年を表すパターンは`u`となる


#### Modelへの変換メソッドを作成する

取り込み先のデータベーステーブルに対応する`Model`に変換するためのメソッドを`Item`に作成する。

```java
public class ImportProjectsToWorkItem implements LineNumberItem {
    // 他の実装は省略

    public ProjectWork toProjectWork() {
        ProjectWork projectWork = new ProjectWork();
        BeanUtils.copyProperties(this, projectWork);
        return projectWork;
    }
}
```

変換メソッドの名前は`to<Modelクラスの単純名>`とする。

単純なプロパティの移送で済む場合は`org.springframework.beans.BeanUtils`の`copyProperties()`を使用する。
`copyProperties()`で移送できない場合は、アクセサメソッドを使って値を移送する。


#### Itemの型の情報をFlatFileItemReaderに設定する

```java
public FlatFileItemReader<ImportProjectsToWorkItem> sampleReader() { // (1)
    BeanWrapperFieldSetMapper<ImportProjectsToWorkItem> fieldSetMapper = new BeanWrapperFieldSetMapper<>(); // (2)
    fieldSetMapper.setTargetType(ImportProjectsToWorkItem.class); // (3)

    // 省略

    DefaultLineMapper<ImportProjectsToWorkItem> delegate = new DefaultLineMapper<>(); // (4)
    
    // 省略

    LineMapper<ImportProjectsToWorkItem> lineMapper = new LineNumberMapper<>(delegate); // (5)
    
    FlatFileItemReader<ImportProjectsToWorkItem> reader = new FlatFileItemReader<>(); // (1)
    
    // 省略

    return reader;
}
```

[ファイル入力](#ファイル入力)で記載した`FlatFileItemReader`の実装例のうち`Item`の型の情報を渡している部分を、作成した`Item`の型に全て置き換える。
該当する置き換え箇所は、以下になる。

- (1) `FlatFileItemReader`の型引数
- (2) `BeanWrapperFieldSetMapper`の型引数
- (3) `BeanWrapperFieldSetMapper`の`setTargetType(Class)`の引数に渡す`Class`オブジェクト
- (4) `DefaultLineMapper`の型引数
- (5) `LineMapper`の型引数

#### プロパティ名を設定する

```java
public FlatFileItemReader<ImportProjectsToWorkItem> sampleReader() {
    // 省略
    DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
    // 省略
    tokenizer.setNames(
        "projectId",
        "projectName",
        "projectType",
        "projectClass",
        "projectStartDate",
        "projectEndDate",
        "organizationId",
        "clientId",
        "projectManager",
        "projectLeader",
        "note",
        "sales");
    // 省略
}
```

CSVファイルの各項目を、`Item`のどのプロパティにマッピングするかを、`DelimitedLineTokenizer`の`setNames(String...)`で設定する。
`setNames(String...)`に渡したプロパティ名は、CSVファイルの左の項目から順番にマッピングされていく。
順序は、【030_アプリ設計/030_インタフェース設計/外部インタフェース設計書】のレコードの定義の順序をもとに設定すること。

## ファイル出力

ここでは、CSVファイルを例にしてファイル出力を実装する方法について解説する。

CSVファイルの出力には、Spring Batchが提供する`FlatFileItemWriter`を使用する。
`Config`における`FlatFileItemWriter`の基本的な実装内容を以下に示す。

```java
@Bean
@StepScope // (1)
public FlatFileItemWriter<ExportProjectsInPeriodItem> exportProjectsInPeriodItemWriter() { // (2), (3)
    Path outputFilePath = exportProjectsInPeriodProperties().getOutputFilePath(); // (4)

    return new FlatFileItemWriterBuilder<ExportProjectsInPeriodItem>() // (2), (5)
            .saveState(false) // (6)
            .name("BA1060101") // (7)
            .encoding("UTF-8") // (8)
            .lineSeparator("\r\n") // (8)
            .delimited()
            .delimiter(",") // (8)
            .names(
                "projectId",
                "projectName",
                "projectType",
                "projectClass",
                "organizationId",
                "clientId",
                "projectManager",
                "projectLeader",
                "note",
                "sales",
                "versionNo") // (9)
            .shouldDeleteIfEmpty(false) // (10)
            .resource(new PathResource(outputFilePath)) // (4)
            .build();
}
```

- `FlatFileItemWriter`作成時のポイント
    - (1) `ItemWriter`は`@StepScope`で定義する
    - (2) `FlatFileItemWriter`と`FlatFileItemWriterBuilder`の型引数には出力で使用する`Item`の型を指定する。  
      `Item`の実装方法については[Itemを作成する](#itemを作成する)を参照
    - (3) Beanの名前は他のバッチ処理と重複しないようにするため、論理的な名前またはバッチ処理IDを元に命名する（バッチ処理IDを使う場合は`ba1060201ItemWriter`など）
    - (4) 出力するCSVファイルのパスはプロパティファイルで管理する。  
      したがって、`Properties`を作成し、そこからCSVファイルのパスを取得し、`FlatFileItemWriterBuilder`に設定する。  
      `Properties`の作成方法については[プロパティ管理](property-management.md)を参照
    - (5) `FlatFileItemWriter`のインスタンスは`FlatFileItemWriterBuilder`を使って作成する
    - (6) `saveState(false)`を設定する
    - (7) `name(String)`でバッチ処理IDを設定する
    - (8) 【030_アプリ設計/030_インタフェース設計/外部インタフェース設計書】に従い、文字コードや改行コード、デリミタを設定する
    - (9) CSVファイルに出力する`Item`のプロパティ名を指定する。  
      `names(String...)`の引数で指定した順番で、CSVファイルの左の項目から順番に出力される。  
      フォーマットの詳細については[出力項目のフォーマットを実装する](#出力項目のフォーマットを実装する)を参照
    - (10) 【030_アプリ設計/030_インタフェース設計/外部インタフェース設計書】に従い、出力データが空のときに空ファイルを作成する場合は`shouldDeleteIfEmpty(false)`を設定する

### Itemを作成する

【030_アプリ設計/030_インタフェース設計/外部インタフェース設計書】のレコードの定義に従い、出力用のデータを格納する`Item`を作成する。

```java
public class ExportProjectsInPeriodItem {
}
```

クラス名は他のバッチ処理と重複しないようにするため、論理的な名前またはバッチ処理IDを元に命名する（バッチ処理IDを使う場合は`BA1060201Item`など）。  
このとき、接尾辞は`Item`とする

#### フィールドとアクセサメソッドを作成する

```java
public class ExportProjectsInPeriodItem {
    private Integer projectId; // (1)
    private String projectName; // (1)

    public Integer getProjectId() { // (2)
        return projectId;
    }

    public void setProjectId(Integer projectId) { // (2)
        this.projectId = projectId;
    }

    public String getProjectName() { // (2)
        return projectName;
    }

    public void setProjectName(String projectName) { // (2)
        this.projectName = projectName;
    }

    // その他フィールドやアクセサは省略
}
```

- 実装のポイント
    - (1) 【030_アプリ設計/030_インタフェース設計/外部インタフェース設計書】のレコードの定義をもとにフィールドを作成する
        - フィールド名は、「項目ID」を使用する。ただし、「項目ID」はスネークケースとなっているのでキャメルケースに置き換えること（例：`PROJECT_ID`→`projectId`）
    - (2) アクセサを作成する

フィールドの型は次の表にあるうちのいずれかにする。

|フィールドの型|説明|主な用途|
|---|---|---|
|`String`|文字列|名前、コード値、文字列で表現される項目|
|`Integer`|整数|サロゲートキー、`Integer`の範囲で表現可能な整数項目|
|`Long`|整数|バージョン番号、`Long`の範囲で表現可能な整数項目|
|`BigDecimal`|数値|金額|
|`LocalDate`|日付|日付|
|`LocalTime`|時刻|時刻|
|`LocalDateTime`|日時|日時|
|`boolean`|真偽値|フラグなどの真偽値を出力する|

#### Modelからの変換メソッドを作成する

```java
public class ExportProjectsInPeriodItem {

    public static ExportProjectsInPeriodItem from(Project project) {
        ExportProjectsInPeriodItem item = new ExportProjectsInPeriodItem();
        BeanUtils.copyProperties(project, item);
        return item;
    }

    // その他の実装は省略
}
```

データベースから検索した結果を保持する`Model`から`Item`に変換するためのファクトリメソッドを作成する。
このとき、ファクトリメソッドの名前は`from`とする

単純なプロパティの移送で済む場合は`org.springframework.beans.BeanUtils`の`copyProperties()`を使用する。
`copyProperties()`で移送できない場合は、アクセサメソッドを使って値を移送する。


### 出力項目のフォーマットを実装する

`Item`のプロパティの値がCSVファイルに出力されるとき、デフォルトでは以下のようにフォーマットされる。

- `null`の場合
    - 空文字
- `null`でない場合
    - `toString()`した値

【030_アプリ設計/030_インタフェース設計/外部インタフェース設計書】の「フォーマット仕様」で指定されているフォーマットが、このデフォルトの仕様で満たせない場合は、`FieldExtractor`を作ってフォーマット処理を実装することで対応する。

以下で、`FieldExtractor`の実装方法を説明する。

#### FieldExtractorの作成

```java
package com.exmple.batch.project.writer;

@Component
@StepScope
public class ExportProjectsInPeriodFieldExtractor implements FieldExtractor<ExportProjectsInPeriodItem> {

    @Override
    public Object[] extract(ExportProjectsInPeriodItem item) {
        return null;
    }
}
```

`FieldExctractor`の実装クラスは、`ItemWriter`と同じパッケージに作成する。
`ItemWriter`を配置するパッケージについては[ステレオタイプ](stereotype-and-package.md)の説明を参照。

クラス名は、論理的な名前またはバッチ処理IDを元に命名し、接尾辞として`FieldExtractor`をつける。
また、クラスには`@Component`と`@StepScope`を設定する。

`FieldExctractor`は`FieldExtractor<出力型>`を実装して作成する。
このとき、出力型には出力で使用する`Item`の型を指定する。

出力項目のフォーマットは、`extract(T)`メソッドで実装する。
このメソッドの戻り値である`Object[]`は、出力するCSVファイルの1レコード分の情報を表しており、配列に入っている順番でCSVファイルの左の項目から順に出力されるようになっている。

以下に、実装例を示す。

```java
@Component
@StepScope
public class ExportProjectsInPeriodFieldExtractor implements FieldExtractor<ExportProjectsInPeriodItem> {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("uuuu/MM/dd");

    @Override
    public Object[] extract(ExportProjectsInPeriodItem item) {
        return new Object[] {
                item.getProjectId(),
                item.getProjectName(),
                item.getProjectType(),
                item.getProjectClass(),
                item.getProjectStartDate().format(DATE_FORMATTER),
                item.getProjectEndDate().format(DATE_FORMATTER),
                item.getOrganizationId(),
                item.getClientId(),
                item.getProjectManager(),
                item.getProjectLeader(),
                item.getNote(),
                item.getSales(),
                item.getVersionNo()
        };
    }
}
```

この例では、日付項目に対してフォーマットを行った値を配列の要素として設定している。

#### Configの設定

作成した`FieldExctractor`を`Config`に設定する。

```java
@Configuration
public class ExportProjectsInPeriodConfig extends BatchBaseConfig {
    @Autowired
    private ExportProjectsInPeriodFieldExtractor exportProjectsInPeriodFieldExtractor;

    @Bean
    @StepScope
    public FlatFileItemWriter<ExportProjectsInPeriodItem> exportProjectsInPeriodItemWriter() {
        return new FlatFileItemWriterBuilder<ExportProjectsInPeriodItem>()
                // 省略
                .fieldExtractor(exportProjectsInPeriodFieldExtractor)
                .build();
    }

    // 他の実装は省略
}
```

作成した`ExportProjectsInPeriodFieldExtractor`のインスタンスは、`@Autowired`で`Config`にインジェクションする。
そして、`FlatFileItemWriterBuilder`の`fieldExtractor(FieldExtractor)`で設定する。

