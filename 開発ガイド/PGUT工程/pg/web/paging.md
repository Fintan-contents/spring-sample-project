# ページング

- [画面からページ番号を送信し、Formで受け取る](#画面からページ番号を送信しformで受け取る)
- [ページが示す範囲のデータを取得する](#ページが示す範囲のデータを取得する)
- [検索結果を画面テンプレートへ渡す](#検索結果を画面テンプレートへ渡す)
- [ページングリンクを表示する](#ページングリンクを表示する)

## 画面からページ番号を送信し、Formで受け取る

検索時にページ番号を送信するには画面に`hidden`でページ番号を埋め込み、`Form`で受け取る。

```html
<!--/* (1) */-->
<input type="hidden" name="pageNumber" value="0"/>
```

```java
public class ProjectSearchForm implements Serializable {

    private Integer pageNumber; // (2)

    // アクセサを含むその他のコードは省略
}
```

- 実装のポイント
    - (1) `hidden`でページ番号を埋め込む。ページ番号は`0`オリジンなため1ページ目が`0`、2ページ目が`1`といった具合に`n`ページに対応するページ番号は`n - 1`になることに注意
    - (2) `Form`でページ番号を受け取るフィールドを用意する。データ型は`Integer`とし、名前は`pageNumber`とすること

## ページが示す範囲のデータを取得する

`Form`で受け取ったページ番号をSpringが提供する`Pageable`に変換し、それを検索条件を表す`Model`にセットして`offset`と`limit`を指定する検索SQLを発行することでページが示す範囲のデータを取得する。

```java
public class ProjectSearchCriteria {

    private Pageable pageable; // (1)

    // アクセサを含むその他のコードは省略
```

```java
    public ProjectSearchCriteria toProjectSearchCriteria(int size) { // (2)
        ProjectSearchCriteria criteria = new ProjectSearchCriteria();
        BeanUtils.copyProperties(this, criteria);

        Pageable pageable = PageRequest.of(pageNumber != null ? pageNumber : 0, size); // (3)
        criteria.setPageable(pageable); (4)

        return criteria;
    }
```

```xml
    <select id="selectProjectByCriteria"
            resultType="com.example.web.project.model.search.ProjectAndOrganization">
        select
            -- 項目は省略
        from
            project p
        -- joinとwhereは省略
        order by
            p.project_id
        limit #{pageable.pageSize} -- (5)
        offset #{pageable.offset}  -- (5)
    </select>
```

- 実装のポイント
    - (1) `Mapper`のメソッドへ渡す検索条件の`Model`に`Pageable`のフィールドを定義する
    - (2) `Form`に検索条件の`Model`へ変換するメソッドを定義する。その際、引数で1ページあたりのサイズを受け取る
    - (3) ページ番号と1ページあたりのサイズを使用して`Pageable`を構築する
    - (4) `Pageable`を検索条件の`Model`へセットする
    - (5) 検索SQLで`offset`と`limit`を指定する

## 検索結果を画面テンプレートへ渡す

ページが示す範囲のデータと全ページのデータ合計件数、`Pageable`を元にして`Page`を構築し、それを画面テンプレートへ渡す。

```java
    @Transactional(readOnly = true)
    public Page<ProjectDto> selectProjectes(ProjectSearchCriteria criteria) { // (1)

        long total = mapper.countProjectByCriteria(criteria); // (2)

        List<ProjectDto> content = mapper.selectProjectByCriteria(criteria) // (3)
                .stream()                                                   // (3)
                .map(ProjectDto::fromProject)                               // (3)
                .collect(Collectors.toList());                              // (3)

        Pageable pageable = criteria.getPageable(); // (4)

        return new PageImpl<>(content, pageable, total); // (5)
    }
```

```java
    @RequestMapping("search")
    @OnRejectError(path = "project/search/index")
    public String search(@Validated ProjectSearchForm form,
            BindingResult bindingResult, Model model) {

        ProjectSearchCriteria criteria = form.toProjectSearchCriteria(properties.getRecordsPerPage());
        Page<ProjectDto> page = service.selectProjectes(criteria);
        model.addAttribute("page", page); // (6)

        return "project/search/index";
    }
```

- 実装のポイント
    - (1) `Service`に検索を行うメソッドを実装する
    - (2) 全ページのデータ合計件数を取得する
    - (3) ページが示す範囲のデータを取得する。ここでは`Model`そのままではなく`Dto`に変換している
    - (4) 検索条件の`Model`から`Pageable`を取り出す
    - (5) `Page`を構築する。`Page`を実装したクラスとして`PageImpl`を使用する
    - (6) 画面テンプレートに`Page`を渡す

## ページングリンクを表示する

ページングリンクは基盤部品を利用して表示する。

```html
<!--/* (1) */-->
<div th:replace="common/paging :: navigation('/project/search/search', ${page})"></div>
```

- 実装のポイント
    - (1) `th:replace`属性を使用してページングリンクの基盤部品を読み込む。引数に検索処理のURLと検索結果（`page`）を渡す
