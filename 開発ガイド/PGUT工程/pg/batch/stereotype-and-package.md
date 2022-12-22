# ステレオタイプとパッケージ構成

- [ステレオタイプ](#ステレオタイプ)
- [パッケージ構成](#パッケージ構成)
- [命名規約](#命名規約)
- [異なるステレオタイプ間のオブジェクト変換方法](#異なるステレオタイプ間のオブジェクト変換方法)
- [定数の定義ルール](#定数の定義ルール)

## ステレオタイプ

ステレオタイプの一覧を以下に示す。

| ステレオタイプ        | Spring管理[^1] | ロジック     | 状態 | トランザクション管理 | 命名規約（接尾辞）    | パッケージ                            |
|-----------------------|----------------|--------------|------|----------------------|-----------------------|---------------------------------------|
| Config                | 対象           | 無           | 無   | 無                   | Config                | <ドメイン>.<機能>.configuration       |
| Properties            | 対象           | 無           | 有   | 無                   | Properties            | <ドメイン>.<機能>.configuration       |
| ItemReader            | 対象           | 有           | 有   | 有                   | ItemReader            | <ドメイン>.<機能>.reader              |
| ItemProcessor         | 対象           | 有           | 有   | 有                   | ItemProcessor         | <ドメイン>.<機能>.processor           |
| ItemWriter            | 対象           | 有           | 有   | 有                   | ItemWriter            | <ドメイン>.<機能>.writer              |
| Tasklet               | 対象           | 有           | 無   | 有                   | Tasklet               | <ドメイン>.<機能>.tasklet             |
| StepExecutionListener | 対象           | 有           | 無   | 有                   | StepExecutionListener | <ドメイン>.<機能>.listener            |
| Item                  | 対象外         | 無           | 有   | 無                   | Item                  | <ドメイン>.<機能>.item                |
| Mapper                | 対象           | 有           | 無   | 無                   | Mapper                | <ドメイン>.<機能>.mapper              |
| Model                 | 対象外         | 無           | 有   | 無                   | -                     | <ドメイン>.<機能>.model.<バッチ処理>  |

[^1]: この表での「Spring管理」とは、Springにコンポーネントとして登録する、またはSpringの処理内でインスタンス生成されるものを指している。

ステレオタイプとは、ある責務を持ったクラスの種類のことである。
各ステレオタイプが負った責務の説明は【方式設計書】を参照。


上記表の項目説明は後続の章を参照。


## パッケージ構成

パッケージ構成は、各業務ごとに独立して開発が進められるよう、<ドメイン>.<機能>.<ステレオタイプ>を基本形とする。

| 要素           | 例                                            |
|----------------|-----------------------------------------------|
| ドメイン       | **com.example.batch**                         |
| 機能           | com.example.batch.**project**                 |
| ステレオタイプ | com.example.batch.project.**reader**          |
| バッチ処理     | com.example.batch.project.model.**ba1060201** |

バッチ処理単位のパッケージ階層は、`Model`のステレオタイプのみで使用する。
パッケージ名にはバッチ処理IDで命名する。

`Model`は実装するバッチ処理毎に異なる目的、用途で利用されるため、バッチ処理間で類似のものがあっても使い回せないことが多い。
`Model`に関してはバッチ処理単位のパッケージ階層を用意し、バッチ処理毎に独立して作成する方針とする。
バッチ処理を跨いだ`Model`の共有は行わない。


## 命名規約

[Web画面の命名規約](../web/stereotype-and-package.md#命名規約)に準ずる。


## 異なるステレオタイプ間のオブジェクト変換方法

ファイルを入出力するときのデータは`Item`を使用し、データベースを入出力するときのデータは`Model`を使用する。
したがって、File to DBやDB to Fileのバッチを作成する際は、`Item`と`Model`を相互に変換しなければならない。

`Model`から`Item`に変換する場合は、以下のように`Item`に`from`という名前のメソッドを作成する。

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

また、`Item`から`Model`に変換する場合は、以下のように`Item`に`to<Modelの名前>`という名前のメソッドを作成する。

```java
public class ImportProjectsToWorkItem implements LineNumberItem {
    // その他の実装は省略

    public ProjectWork toProjectWork() {
        ProjectWork projectWork = new ProjectWork();
        BeanUtils.copyProperties(this, projectWork);
        return projectWork;
    }
}
```

単純なプロパティの移送の場合は、`org.springframework.beans.BeanUtils#copyProperties`を使用する。
`BeanUtils`で移送できない場合はアクセサーメソッドを使用して値の移送を行う。

これらのメソッドを実際に使用して変換を行う処理は、`ItemProcessor`で実装する。
以下は、`Item`から`Model`に変換する処理を`ItemProcessor`で実装した場合の例となる。

```java
@Component
@StepScope
public class ImportProjectsToWorkItemProcessor implements ItemProcessor<ImportProjectsToWorkItem, ProjectWork> {

    @Override
    public ProjectWork process(ImportProjectsToWorkItem item) throws Exception {
        return item.toProjectWork();
    }
}
```

## 定数の定義ルール

クラス内で使用する定数は可視性`private`とし、クラス外から参照されないようにする。
クラスを跨いで使用する場合は、`Properties`に定数を定義する。

定数を命名する際は、値そのものではなく、その値が表す業務的な意味を反映させる。
例えば、以下の例は値そのものを定数名にしており、文法上は定数であるが意味的にはマジックナンバーと変わりがない。

```java
/** 家族会員数の上限（3人まで）*/
private static final int THREE = 3;
```

業務的な意味を反映させると以下のような命名となる。

```java
/** 家族会員の上限（3人まで）*/
private static final int MAX_FAMILY_MEMBER = 3;
```

