# 排他制御

- [MapperとMapper.xmlの作成](#mapperとmapperxmlの作成)
- [ItemProcessorで悲観ロックを取得する](#itemprocessorで悲観ロックを取得する)
- [バージョン番号の更新](#バージョン番号の更新)

バッチ処理でデータベーステーブルを更新する際は、悲観ロックで排他制御を行う。
ここでは、悲観ロックの実装方法について説明する。

## MapperとMapper.xmlの作成

まず、`Mapper`に悲観ロックを取得するためのメソッドを作成する。

```java
@Mapper
public interface SampleMapper {
    Project selectProjectByIdForUpdate(int projectId); // (1), (2), (3)
    // 他の実装は省略
}
```

- 実装のポイント
    - (1) 戻り値の型は、ロック対象となるテーブルに対応する`Model`にする。  
      ロック対象となるテーブルは、【030_アプリ設計/010_システム機能設計/システム機能設計書】の「入出力一覧」で「ロック対象」が○になっているものになる
    - (2) メソッド名は`ForUpdate`で終わるように命名する
    - (3) 【030_アプリ設計/010_システム機能設計/システム機能設計書】の「処理詳細」に記載されている検索条件を引数で受け取れるように定義する

次に、`Mapper.xml`に悲観ロックを取得するSQLの定義を追加する。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.batch.project.mapper.SampleMapper">
    <select id="selectProjectByIdForUpdate" resultType="com.example.common.generated.model.Project"><![CDATA[
        select
            project_id,
            project_name,
            project_type,
            project_class,
            project_start_date,
            project_end_date,
            organization_id,
            client_id,
            project_manager,
            project_leader,
            note,
            sales,
            version_no
        from
            project
        where
            project_id = #{projectid} -- (1)
        for update -- (2)
    ]]></select>
    <!-- 他の実装は省略 -->
</mapper>
```

- 実装のポイント
    - (1) 【030_アプリ設計/010_システム機能設計/システム機能設計書】の「処理詳細」に記載されている検索条件で、悲観ロックを取得する対象を絞り込む
    - (2) `for update`をSQLの末尾につける

## ItemProcessorで悲観ロックを取得する

悲観ロックは、`ItemProcessor`の先頭で取得するように実装する。
以下に、実装例を示す。

```java
@Component
@StepScope
public class SampleItemProcessor implements ItemProcessor<ProjectWork, Project> {
    @Autowired
    private SampleMapper sampleMapper;

    @Override
    public Project process(ProjectWork work) {
        Project project = sampleMapper.selectProjectByIdForUpdate(work.getProjectId()); // (1)

        // 省略
    }
}
```

- 実装のポイント
    - (1) `ItemProcessor`の先頭で悲観ロックを取得する

## バージョン番号の更新

オンライン処理と競合することを考慮し、バージョン番号(`version_no`)のカラムを持つテーブルを更新する場合はバージョン番号の更新を行うこと。

新規登録の場合は、`ItemProcessor`の中でバージョン番号の初期値を設定するように実装する。
以下に、実装例を示す。

```java
@Component
@StepScope
public class SampleItemProcessor implements ItemProcessor<ProjectWork, Project> {

    @Override
    public Project process(ProjectWork work) {
        // 省略

        project.setVersionNo(1L); // (1)

        return project;
    }
}
```

- 実装のポイント
    - (1) 新規登録の場合は`1`を設定する

また、更新の場合はSQLでバージョン番号を更新するように実装する。
以下に、実装例を示す。

```xml
<update id="updateProject"><![CDATA[
    update
        project
    set
        project_name = #{projectname},
        project_type = #{projecttype},
        project_class = #{projectclass},
        project_start_date = #{projectstartdate},
        project_end_date = #{projectenddate},
        organization_id = #{organizationid},
        client_id = #{clientid},
        project_manager = #{projectmanager},
        project_leader = #{projectleader},
        note = #{note},
        sales = #{sales},
        version_no = version_no + 1 -- (1)
    where
        project_id = #{projectid}
]]></update>
```

- 実装のポイント
    - (1) `set`句でバージョン番号を`1`加算する
