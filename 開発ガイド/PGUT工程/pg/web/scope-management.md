# スコープ管理

- [画面テンプレートへ値を渡す](#画面テンプレートへ値を渡す)
- [検索フォームをセッションスコープで管理する](#検索フォームをセッションスコープで管理する)
- [リダイレクト後の処理へ値を渡す](#リダイレクト後の処理へ値を渡す)

## 画面テンプレートへ値を渡す

`Controller`から画面テンプレートへ値を渡すにはリクエストスコープを使用する。

```java
    @GetMapping
    public String index(@RequestParam Integer projectId, Model model) { // (1)
        ProjectDto projectDto = service.findProjectById(projectId);

        model.addAttribute("projectDto", projectDto); // (2)

        return "project/detail/index";
    }
```

- 実装のポイント
    - (1) `Controller`のメソッド引数で`Model`を受け取る
        - ※この`Model`はステレオタイプの`Model`ではなく、Springが提供している`org.springframework.ui.Model`であることに注意
    - (2) `addAttribute`で画面テンプレートに渡す名前と値を設定する

画面テンプレートへ渡された値はThymeleafの機能を使って参照できる。

```html
<p th:text="${projectDto.projectName}"></p>
```

## 検索フォームをセッションスコープで管理する

検索一覧画面の検索フォームを格納する`Form`は、他のページから検索一覧画面に戻る遷移を踏まえてセッションスコープで管理する。

```java
public class ProjectSearchForm implements Serializable { // (1)
```

```java
@Controller
@RequestMapping("project/search")
@SessionAttributes(types = ProjectSearchForm.class) // (2)
public class ProjectSearchController {
```

- 実装のポイント
    - (1) `Form`は`Serializable`を実装する
    - (2) `Controller`に`@SessionAttributes`を付けて`types`で`Form`を設定する

`@SessionAttributes`に指定した`Form`は、`Controller`のメソッドで処理内で最初に使用された時点から、そのインスタンスがセッションスコープで管理されるようになる。

また、検索画面の初期表示時は過去の検索条件をクリアする必要がある。

```java
public class ProjectSearchForm implements Serializable {

    public void clear() { // (1)
        ProjectSearchForm newForm = new ProjectSearchForm(); // (2)
        BeanUtils.copyProperties(newForm, this);             // (2)
    }

    // 他のコードは省略
```

```java
    @GetMapping
    public String index(ProjectSearchForm form) {
        form.clear(); // (3)
        return "project/search/index";
    }
```

- 実装のポイント
    - (1) `Form`にクリアするメソッドを定義する
    - (2) 初期状態の`Form`を生成して、自分自身へ値をコピーすることでクリアする
    - (3) `Controller`の初期表示を行うメソッド内で`Form`をクリアするメソッドを呼び出す

## リダイレクト後の処理へ値を渡す

リダイレクト後の処理へ値を渡したい場合、Springが提供している`RedirectAttributes`を使用する。

```java
    @PostMapping(path = "execute", params = "execute")
    @TransactionTokenCheck
    @OnRejectError(path = "project/create/index")
    public String execute(@Validated ProjectCreateForm projectCreateForm, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) { // (1)

        ProjectDto projectDto = projectCreateForm.toProjectDto();
        service.insertProject(projectDto);

        redirectAttributes.addFlashAttribute("projectId", projectDto.getProjectId()); // (2)

        return "redirect:/project/create/complete";
    }
```

- 実装のポイント
    - (1) `Controller`のメソッド引数で`RedirectAttributes`を受け取る
    - (2) `addFlashAttribute`でリダイレクト後の処理へ渡す名前と値を設定する

リダイレクト後の`Controller`の処理で渡された値を使用する場合、`@ModelAttribute`を使用する。

```java
    @GetMapping("complete")
    public String complete(@ModelAttribute("projectId") String projectId) {
        // メソッドの内容は省略
    }
```

あるいは画面テンプレートで参照することも可能。

```html
<input type="hidden" th:value="${projectId}"/>
```
