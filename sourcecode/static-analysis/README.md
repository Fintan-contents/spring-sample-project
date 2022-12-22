# 静的チェックツール


[Checkstyle](http://checkstyle.sourceforge.net/)、[SpotBugs](http://spotbugs.readthedocs.io/ja/latest/index.html)
を導入しています。

設定方法や運用方法（チェックの除外方法など）は以下のガイドを参照してください。

- [Checkstyleガイド](https://github.com/Fintan-contents/coding-standards/tree/3.2/java/staticanalysis/checkstyle)
- [SpotBugsガイド](https://github.com/Fintan-contents/coding-standards/tree/3.2/java/staticanalysis/spotbugs)

## 実行方法

チェックを行いたいモジュールに移動し、以下のコマンドを実行します。
Checkstyle、Spotbugsによるチェックが実行されます。

```bash
./mvnw verify
```

