# トランザクション管理

- [アノテーションによるトランザクション制御](#アノテーションによるトランザクション制御)
  - [ロールバックの対象となる例外](#ロールバックの対象となる例外)

## アノテーションによるトランザクション制御

`Service`または`ViewHelper`へ`@Transactional`を付けることでトランザクション制御を行う。
クラスまたはメソッドに`@Transactional`を付けると、メソッド開始時に自動的にトランザクションが開始される。
メソッドから規定の例外がスローされるとトランザクションはロールバックされ、それ以外の方法でメソッドが終了すると（多くの場合はメソッドが正常終了すると）トランザクションはコミットされる。

`Service`の例を次に示す。

```java
@Service
@Transactional // (1)
public class ProjectUpdateService {

    public void updateProject(ProjectDto projectDto) { // (2)
        // メソッドの内容は省略
    }

    @Transactional(readOnly = true) // (3)
    public ProjectDto findProjectById(Integer projectId) {
        // メソッドの内容は省略
    }

    // その他のコードは省略
}
```

- 実装のポイント
    - (1) クラスに`@Transactional`を付ける
    - (2) メソッドはトランザクション内で実行される
    - (3) DBに対して検索しか行わないメソッドには`@Transactional(readOnly = true)`を付ける

`ViewHelper`はDBに対して検索しか許可しないため、クラスに`@Transactional(readOnly = true)`を付ける。

```java
@Component
@Transactional(readOnly = true) // (1)
public class ProjectUpdateViewHelper {

    public String getOrganizationName(Integer organizationId) { // (2)
        // メソッドの内容は省略
    }

    // その他のコードは省略
}
```

- 実装のポイント
    - (1) クラスに`@Transactional(readOnly = true)`を付ける
    - (2) メソッドは読取専用のトランザクション内で実行される。`ViewHelper`のメソッドには`@Transactional(readOnly = true)`を付けなくてもよい

### ロールバックの対象となる例外

ロールバックの対象となる例外は`RuntimeException`とそのサブクラスである。
`Exception`や、`RuntimeException`を除く`Exception`のサブクラスはロールバックの対象とならない。
