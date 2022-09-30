# プロパティ管理

基本的に、バッチでのプロパティ管理は[Webのプロパティ管理](../web/property-management.md)に準じる。

ただしバッチでは、環境によって読み込むプロパティファイルを追加できるように`Config`を実装する。
プロパティファイルの追加は、プロファイルの仕組みを利用して以下のように実装する。

```java
@Configuration
@PropertySource(value = {
        "classpath:/properties/project/ExportProjectsInPeriod.properties",
        "classpath:/properties/project/ExportProjectsInPeriod-${spring.profiles.active}.properties", // (1)
}, encoding = "UTF-8", ignoreResourceNotFound = true) // (2)
public class ExportProjectsInPeriodConfig extends BatchBaseConfig {
    // 省略
```

- 実装のポイント
  - (1) `Config`に付けた`@PropertySource`の`value`要素に、追加で読み込むプロパティファイルのパスを加える
    - 追加で読み込むプロパティファイルのパスは、ファイル名のベース部分の末尾に`-${spring.profiles.active}`を追加したものにする
  - (2) `@PropertySource`の`ignoreResourceNotFound`要素に`true`を設定する

これにより、プロファイルを指定して起動したときだけプロパティファイルを追加で読み込めるようになる。
