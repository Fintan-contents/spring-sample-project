# データアクセス

- [MapperとMapper XMLを作成する](#mapperとmapper-xmlを作成する)
  - [Mapper XMLの配置場所と命名ルール](#mapper-xmlの配置場所と命名ルール)
- [自動生成されたModelを使用する](#自動生成されたmodelを使用する)
- [自動生成されたModelの継承](#自動生成されたmodelの継承)
- [自動生成されたModelを継承せずにModelを作成する](#自動生成されたmodelを継承せずにmodelを作成する)
- [データの検索](#データの検索)
  - [大量データを扱う場合](#大量データを扱う場合)
- [データの登録](#データの登録)
- [データの更新](#データの更新)
  - [特定の項目のみの更新](#特定の項目のみの更新)
- [データの削除](#データの削除)
- [Mapperのメソッド命名ルール](#mapperのメソッド命名ルール)

## MapperとMapper XMLを作成する

データアクセスを行うには`Mapper`とMapper XMLが必要となる。

```java
@Mapper // (1)
public interface ProjectUpdateMapper { // (2)(3)
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd"><!-- (4) -->
<mapper namespace="com.example.web.project.mapper.ProjectUpdateMapper"><!-- (5) -->

</mapper>
```

- 実装のポイント
    - (1) `@Mapper`を付ける
    - (2) `interface`として定義する
    - (3) 論理的な名前または取引IDをもとに命名する（取引IDを使う場合は `B10101Mapper` など）
    - (4) この例に記載した通りに文書型宣言を記述する
    - (5) `mapper`要素の`namespace`属性に`Mapper`の完全修飾名を記述する

### Mapper XMLの配置場所と命名ルール

Mapper XMLの配置場所と名前は対になる`Mapper`によって決まる。

`src/main/resources`以下に対になる`Mapper`のパッケージと同様のディレクトリを作成し、その中に`<対になるMapperの単純名>.xml`という名前でファイルを作成する。

例えば`com.example.web.project.mapper.ProjectCreateMapper`という`Mapper`と対になるMapper XMLは`src/main/resources/com/example/web/project/mapper/ProjectCreateMapper.xml`となる。

## 自動生成されたModelを使用する

データベースを読み取ってテーブルを表す`Model`を自動生成してある。

1テーブルに対する単純な検索や、1件の登録・更新には自動生成された`Model`を使用すること。

## 自動生成されたModelの継承

自動生成された`Model`に少しのフィールド・アクセサメソッドを足した`Model`が欲しい場合、自動生成された`Model`を継承して`Model`を作成してよい。

```java
public class ProjectAndOrganization extends Project { // (1)

    private String organizationName; // (2)

    public String getOrganizationName() { // (2)
        return organizationName;
    }

    public void setOrganizationName(String organizationName) { // (2)
        this.organizationName = organizationName;
    }

    @Override
    public boolean equals(Object o) { // (3)
        // 内容は省略
    }

    @Override
    public int hashCode() { // (3)
        // 内容は省略
    }
}
```

- 実装のポイント
    - (1) 自動生成された`Model`を継承する
    - (2) 必要なフィールド・アクセサメソッドを定義する
    - (3) `equals`と`hashCode`を定義する。手作業で実装すると冗長かつミスが起きやすいため、IDEのコード生成機能を使用すること

## 自動生成されたModelを継承せずにModelを作成する

次のような場合、自動生成された`Model`を継承せずに`Model`を作成して良い。

- テーブルの結合結果で、自動生成された`Model`の拡張では用途に合わない
- 検索条件のみで使用する`Model`

```java
public class ClientSearchCriteria { // (1)

    private String clientName; // (2)

    private String industryCode; // (2)

    public String getClientName() { // (2)
        return clientName;
    }

    public void setClientName(String clientName) { // (2)
        this.clientName = clientName;
    }

    public String getIndustryCode() { // (2)
        return industryCode;
    }

    public void setIndustryCode(String industryCode) { // (2)
        this.industryCode = industryCode;
    }

    // (3)
}
```

- 実装のポイント
    - (1) なにも継承しない
    - (2) 必要なフィールド・アクセサメソッドを定義する
    - (3) `equals`と`hashCode`を定義しなくて良い

検索条件のみで使用する`Model`は名前の接尾辞を`Criteria`とすること。

## データの検索

`Mapper`にメソッドを定義して、それに対応する`select`要素をMapper XMLに記述する。
1件の検索、複数件の検索、件数の取得について説明する。

```java
@Mapper
public interface ClientSearchMapper {

    // 1件の検索
    Client selectClientByPrimaryKey(Integer clientId); // (1)

    // 複数件の検索
    List<Client> selectClientByCriteria(ClientSearchCriteria criteria); // (2)

    // 件数の取得
    long countClientByCriteria(ClientSearchCriteria criteria); // (3)
}
```

- 実装のポイント
    - (1) 1件のデータを検索する場合、戻り値は`Model`になる。このメソッドは主キーで検索をするので引数は主キーのみとしている
    - (2) 複数件のデータを検索する場合、戻り値は`List`になる。このメソッドはいくつかの検索条件を設定するため引数は`Model`としている
    - (3) 件数を取得する場合、戻り値は`long`になる。このメソッドは`selectClientByCriteria`と同じ検索条件を設定するため引数も同じものを定義している

```xml
    <!-- (1)(2)(3) -->
    <select id="selectClientByPrimaryKey"
            resultType="com.example.common.generated.model.Client">
        select
            client_id,
            client_name,
            industry_code,
            version_no
        from
            client
        where
            client_id = #{clientId} <!-- (6) -->
    </select>

    <!-- (4) -->
    <select id="selectClientByCriteria"
            resultType="com.example.common.generated.model.Client">
        select
            client_id,
            client_name,
            industry_code,
            version_no
        from
            client
        <!-- (5) -->
        <include refid="Where_Clause_For_List"/>
        order by
            client_name
        limit #{pageable.pageSize}
        offset #{pageable.offset}
    </select>

    <!-- (5) -->
    <select id="countClientByCriteria"
            resultType="_long">
        select
            count(*)
        from
            client
        <!-- (8) -->
        <include refid="Where_Clause_For_List"/>
    </select>

    <!-- (8) -->
    <sql id="Where_Clause_For_List">
        <!-- (9) -->
        <where>
            <!-- (10) -->
            <if test="clientName != null and clientName != ''">
                <!-- (7) -->
                <!-- (11) -->
                and client_name like #{clientName} || '%'
            </if>
            <if test="industryCode != null">
                and industry_code = #{industryCode}
            </if>
        </where>
    </sql>
```

- 実装のポイント
    - (1) `select`要素を使用する
    - (2) `id`属性には`Mapper`のメソッド名を記述する
    - (3) `resultType`属性には`Mapper`のメソッドの戻り値の型を完全修飾名で記述する
    - (4) 複数件の検索の場合、戻り値の型は`List`だが、`resultType`には`List`ではなく要素の型を記述する
    - (5) 件数を取得する場合、`resultType`には`_long`と記述する。※先頭にアンダースコアが付いていることに注意すること
    - (6) `#{引数名}`という記法で引数をバインドする
    - (7) 引数が`Model`の場合、`#{Modelのフィールド名}`になる
    - (8) 複数のSQL間で同じ検索条件を使用したい場合、`sql`要素と`include`要素を使用してSQLの一部を共有できる。`include`要素の`refid`で指定された`id`を持つ`sql`要素の内容が`include`要素が書かれた場所に展開される
    - (9) 動的に検索条件を組み立てたい場合は`where`要素を使用する
    - (10) `where`要素の中で`if`要素を使用して動的に検索条件を組み立てられる
    - (11) `where`要素の中に書く検索条件は必ず`and`を付けること
        - ※`where`要素の先頭が`and`で始まっていた場合は、MyBatisが削除する

### 大量データを扱う場合

検索処理で大量のデータを取得する場合、一度にすべての検索結果を読み込むとアプリケーションのメモリを大きく消費し、最悪の場合`OutOfMemoryError`が発生してしまう。

これを避けるため、ファイルダウンロード等で行う検索処理で大量のデータを取得する場合は`ResultHandler`を使用すること。
`ResultHandler`は少しずつ検索結果を読み取って処理するためメモリ消費を小さく抑えられる。

```java
@Mapper
public interface ClientSearchMapper {

    void selectClientByCriteria(ClientSearchCriteria criteria, // (2)(3)
        ResultHandler<Client> resultHandler); // (1)
}
```

- 実装のポイント
    - (1) 引数に`ResultHandler`を定義する。`ResultHandler`には`<Model>`を付けること。この`Model`が実際に`ResultHandler`で処理する型となる
    - (2) 戻り値は`void`とすること
    - (3) 検索条件の引数を定義する

Mapper XMLは`ResultHandler`を使用しない検索のときと特に変わらない。

```xml
    <!-- (1)(2)(3) -->
    <select id="selectClientByCriteria"
            resultType="com.example.common.generated.model.Client">
        select
            client_id,
            client_name,
            industry_code,
            version_no
        from
            client
        <where>
            <if test="clientName != null and clientName != ''">
                and client_name like #{clientName} || '%'
            </if>
            <if test="industryCode != null">
                and industry_code = #{industryCode}
            </if>
        </where>
        order by
            client_name
    </select>
```

- 実装のポイント
    - (1) `select`要素を使用する
    - (2) `id`属性には`Mapper`のメソッド名を記述する
    - (3) `resultType`属性には`ResultHandler`が処理する型を完全修飾名で記述する

定義した`Mapper`のメソッドを使用するときはラムダ式で`ResultHandler`を渡す。

```java
clientSearchMapper.selectClientByCriteria(criteria, client -> { // (1)
    // 処理は省略
});
```

- 実装のポイント
    - (1) ラムダ式で`ResultHandler`を渡す。引数は`Model`で、検索結果が1件ずつ渡される

## データの登録

`Mapper`にメソッドを定義して、それに対応する`insert`要素をMapper XMLに記述する。

```java
@Mapper
public interface ClientCreateMapper {

    int insertClient(Client client); // (1)(2)
```

- 実装のポイント
    - (1) 引数に登録対象となるテーブルの`Model`を定義する。基本的には自動生成された`Model`を使用すること
    - (2) 戻り値は`int`とする。登録件数が返される

```xml
    <!-- (1)(2) -->
    <insert id="insertClient">
        insert into
            client
            (
            client_id,
            client_name,
            industry_code,
            version_no
            )
        values
            (
            -- (3)
            #{clientId},
            #{clientName},
            #{industryCode},
            #{versionNo}
            )
    </insert>
```

- 実装のポイント
    - (1) `insert`要素を使用する
    - (2) `id`属性には`Mapper`のメソッド名を記述する
    - (3) 引数となる`Model`のプロパティを`#{プロパティ名}`という記法でバインドする

主キーが自動生成される場合、データの登録後に`Model`へ主キーを設定できる。

```xml
    <!-- (1)(2) -->
    <insert id="insertClient"
        keyProperty="clientId"
        useGeneratedKeys="true">
        insert into
            client
            (
            -- (3)
            client_name,
            industry_code,
            version_no
            )
        values
            (
            -- (3)
            #{clientName},
            #{industryCode},
            #{versionNo}
            )
    </insert>
```

- 実装のポイント
    - (1) `keyProperty`属性に主キーに対応する`Model`のプロパティ名を設定する
    - (2) `useGeneratedKeys`属性に`true`を設定する
    - (3) SQLには主キーを記述しない

## データの更新

`Mapper`にメソッドを定義して、それに対応する`update`要素をMapper XMLに記述する。

```java
@Mapper
public interface ClientUpdateMapper {

    int updateClientByPrimaryKey(Client client); // (1)(2)
```

- 実装のポイント
    - (1) 引数に更新対象となるテーブルの`Model`を定義する。基本的には自動生成された`Model`を使用すること
    - (2) 戻り値は`int`とする。更新件数が返される

```xml
    <!-- (1)(2) -->
    <update id="updateClientByPrimaryKey">
        update
            client
        set
            -- (3)
            client_name = #{clientName},
            industry_code = #{industryCode},
            version_no = #{versionNo} + 1
        where
            -- (3)
            client_id = #{clientId}
            and version_no = #{versionNo}
    </update>
```

- 実装のポイント
    - (1) `update`要素を使用する
    - (2) `id`属性には`Mapper`のメソッド名を記述する
    - (3) 引数となる`Model`のプロパティを`#{プロパティ名}`という記法でバインドする

データの更新では必要に応じて楽観排他制御を行う必要がある。
楽観排他制御のやり方については[排他制御](./exclusion-control.md)を参照すること。

### 特定の項目のみの更新

`Mapper`にメソッドを定義して、それに対応する`update`要素をMapper XMLに記述する。

```java
@Mapper
public interface SecurityLoginMapper {

    // (2)
    int updateSystemAccountSetLoginInfoByLoginId(
        // (1)
        LocalDateTime lastLoginDateTime, String loginId);

```

- 実装のポイント
    - (1) 引数に更新対象となる項目や更新条件となる項目を定義する。この例のように項目ごとに定義してもよいし、`Model`にまとめてもよい
    - (2) 戻り値は`int`とする。更新件数が返される

```xml
    <!-- (1)(2) -->
    <update id="updateSystemAccountSetLoginInfoByLoginId">
        update
            system_account
        set
            failed_count = 0,
            -- (3)
            last_login_date_time = #{lastLoginDateTime}
        where
            -- (3)
            login_id = #{loginId}
    </update>
```

- 実装のポイント
    - (1) `update`要素を使用する
    - (2) `id`属性には`Mapper`のメソッド名を記述する
    - (3) 引数を`#{引数名}`という記法でバインドする

## データの削除

`Mapper`にメソッドを定義して、それに対応する`delete`要素をMapper XMLに記述する。

```java
@Mapper
public interface ClientDeleteMapper {

    int deleteClientByPrimaryKey(Integer clientId); // (1)(2)
```

- 実装のポイント
    - (1) 引数に削除条件にバインドする値を定義する。このメソッドは主キーを条件に削除を行う
    - (2) 戻り値は`int`とする。削除件数が返される

```xml
    <delete id="deleteClientByPrimaryKey">
        delete from
            client
        where
            client_id = #{clientId}
    </delete>
```

- 実装のポイント
    - (1) `delete`要素を使用する
    - (2) `id`属性には`Mapper`のメソッド名を記述する
    - (3) 引数に取った削除条件を`#{引数名}`という記法でバインドする

## Mapperのメソッド命名ルール

|操作|命名ルール|例|
|---|---|---|
|検索|`select<検索対象のテーブル名>By<条件>`|`selectProjectByPrimaryKey`、`selectClientByCriteria`|
|件数の取得|`count<検索対象のテーブル名>By<条件>`|`countProjectByProjectType`、`countClientByCriteria`|
|登録|`insert<登録対象のテーブル名>`|`insertProject`|
|更新|`update<更新対象のテーブル名>By<条件>`|`updateProjectByPrimaryKey`|
|特定の項目のみの更新|`update<更新対象のテーブル名><項目への操作内容>By<条件>`|`updateSystemAccountSetLoginInfoByLoginId`|
|削除|`delete<削除対象のテーブル名>By<条件>`|`deleteProjectByPrimaryKey`|

`<条件>`が主キーの場合は`PrimaryKey`、複合的な条件の場合は`Criteria`とする。
