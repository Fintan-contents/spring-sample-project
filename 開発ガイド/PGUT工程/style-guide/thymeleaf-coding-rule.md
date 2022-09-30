# Thymeleafコーディング規約

- [1. はじめに](#1-はじめに)
  - [1.1. 指標](#11-指標)
  - [1.2. Thymeleaf/HTMLバージョン](#12-thymeleafhtmlバージョン)
  - [1.3. 表記ルール](#13-表記ルール)
- [2. 必須事項](#2-必須事項)
  - [2.1. 文書型宣言を書いてください](#21-文書型宣言を書いてください)
  - [2.2. XML名前空間を設定してください](#22-xml名前空間を設定してください)
  - [2.3. head要素、meta要素、title要素を書いてください](#23-head要素meta要素title要素を書いてください)
  - [2.4. body要素を書いてください](#24-body要素を書いてください)
  - [2.5. フォームやリンクなどにはルートパスからの相対パスを記述してください](#25-フォームやリンクなどにはルートパスからの相対パスを記述してください)
- [3. 禁止事項](#3-禁止事項)
  - [3.1. th:utextは使用しないでください](#31-thutextは使用しないでください)
  - [3.2. インライン記法は使わないでください](#32-インライン記法は使わないでください)
  - [3.3. 通常のHTMLで使われるコメントの形式は使用しないでください](#33-通常のhtmlで使われるコメントの形式は使用しないでください)
  - [3.4. style要素、style属性によるCSS適用は行わないでください](#34-style要素style属性によるcss適用は行わないでください)
  - [3.5. script要素内にJavaScriptコードを書かないでください](#35-script要素内にjavascriptコードを書かないでください)
  - [3.6. アプリケーションプログラマーの独断でHTMLテンプレートを分割しないでください](#36-アプリケーションプログラマーの独断でhtmlテンプレートを分割しないでください)

## 1. はじめに

本規約はThymeleafを使用してWeb画面を開発するプロジェクトにおいて、アプリケーションプログラマーが守るべきルールやより良いコードを書くための指針を解説しています。

### 1.1. 指標

本規約を書くにあたって、次の3つを指標としました。

- 一般的なプラクティスを尊重する
- エンバグの危険を減らす
- 脆弱性を作り込む危険を減らす

### 1.2. Thymeleaf/HTMLバージョン

本規約が対象とするThymeleaf、HTMLのバージョンは次の通りです。

- Thymeleaf 3.x
- HTML5

### 1.3. 表記ルール

規約の解説中にコード例を掲載することがあります。

やってはいけない「禁止コード」の例は先頭に`<!--/* NG */-->`とThymeleafパーサーレベルコメントの形式で記載しています。 

禁止コードとの対比として挙げている、やってもよい・ぜひやるべき「推奨コード」の例は先頭に`<!--/* OK */-->`と記載しています。

`<!--/* NG */-->`と`<!--/* OK */-->`のどちらも記載されていないコード例は、「推奨コード」か、もしくは単なる解説の補助として掲載しています。

コード例の中で特に見せたい箇所以外は省略する場合があります。
その際は`...`と書いて省略を表しています。

## 2. 必須事項

プロジェクト内で統一的なコーディングを行うため、必須となることを定めています。

### 2.1. 文書型宣言を書いてください

HTMLテンプレートの先頭にHTML5の文書型宣言を書いてください。

```html
<!DOCTYPE html>
```

### 2.2. XML名前空間を設定してください

HTMLテンプレートはXHTMLとして処理されます。
`th`名前空間の属性(`th:*`)を使用するためにXML名前空間の設定を行ってください。

具体的には`html`要素に`xmlns:th`属性を書いてください。
属性値は`http://www.thymeleaf.org`です。

```html
<html xmlns:th="http://www.thymeleaf.org">
```

HTML5に対応した記法として`data-th-*`がありますが、Thymeleaf公式リファレンスで使用されているのが`th:*`なため、この選択としています。

### 2.3. head要素、meta要素、title要素を書いてください

`html`要素の直下に`head`要素を置いてください。
`head`要素の中には次の要素を置いてください。

- 文字集合を示す`meta`要素。`charset`属性を用いて`utf-8`を指定
- ビューポートを設定する`meta`要素。`content`属性を用いて`width=device-width, initial-scale=1`
- `title`要素

```html
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>...</title>
</head>
```

なお、プロジェクトによっては画面共通のレイアウトを適用するため、共通レイアウトのHTMLテンプレートが用意されていることがままあります。

その場合、共通レイアウトのHTMLテンプレートにマージされるのは`body`要素の内容のみで、`head`要素、`meta`要素、`title`要素は無視される構成になっていることもあります。

しかし、ThymeleafのHTMLテンプレートはブラウザで直接開くことで画面モックとして使えます。
そのため、共通レイアウトのHTMLテンプレートでは無視される構成であっても`head`要素、`meta`要素、`title`要素は書いておいてください。

### 2.4. body要素を書いてください

`html`要素の直下に`body`要素を置いてください。

画面の内容は`body`要素の中に記述していきます。

```html
<body>
</body>
```

### 2.5. フォームやリンクなどにはルートパスからの相対パスを記述してください

フォームやリンクなど画面のパスを設定する場合、Thymeleafが処理する属性(`th:*`)には現在の画面からの相対パスではなくルートパスからの相対パスを記述してください。

ただし、HTMLテンプレートをブラウザで直接開いて画面モックとして使用する場合を想定して、Thymeleafが処理しない属性は現在の画面からの相対パスを記述しても構いません。

```html
<!--/* NG */-->
<a th:href="@{../other/}" href="../other/index.html">...</a>
```

```html
<!--/* OK */-->
<!--/* th:href属性はThymeleafが処理するためルートパスからの相対パスを記述する */-->
<!--/* href属性は画面モックのときに使用するため現在の画面からの相対パスを記述してよい */-->
<a th:href="@{/root/other/}" href="../other/index.html">...</a>
```

## 3. 禁止事項

バグを発生させそうな危ういコードを減らすため、禁止事項を定めています。

### 3.1. th:utextは使用しないでください

Thymeleafには要素の中にテキストを出力するための属性として`th:text`と`th:utext`が用意されています。
これらのうち`th:utext`はHTML特殊文字のエスケープを行わないためXSSのリスクがあります。

そのため`th:utext`は使用しないでください。

```html
<!--/* NG */-->
<p th:utext="${content}"></p>
```

```html
<!--/* OK */-->
<p th:text="${content}"></p>
```

- 参考：[Unescaped Text](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#unescaped-text)

### 3.2. インライン記法は使わないでください

Thymeleafは要素の内容を書く際、`[[...]]`または`[(...)]`といった記法で式を使用できます。

これが便利な場面はあり得ますが、`[(...)]`が`th:utext`に対応しておりHTML特殊文字がエスケープされずXSSのリスクがあります。

また、HTMLテンプレートを画面モックとして使用する場合、インライン記法を使用していると画面モック内に`[[...]]`が現れることとなり不自然です。

以上のことからインライン記法は使用しないでください。

- 参考：[12.1 Expression inlining](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#inlining)

### 3.3. 通常のHTMLで使われるコメントの形式は使用しないでください

通常のHTMLでは`<!--`と`-->`で囲った箇所がコメントとして扱われますが、この形式のコメントはHTMLテンプレートの処理後も残ってしまいます。
つまりブラウザでWeb画面を表示した際、「ページのソースを表示」といったブラウザのメニューを選ぶとコメントも見えてしまいます。

```html
<!-- NG -->
<!-- この形式は使用禁止 -->
```

HTMLテンプレート内にコメントを記載したい場合は`<!--/*`と`*/-->`で囲むThymeleafパーサーレベルコメントを使用してください。
ThymeleafパーサーレベルコメントはThymeleafがHTMLテンプレートを処理する際にコメントを取り除いてくれる記法です。

```html
<!--/* OK */-->
<!--/* ここに書いたコメントはHTMLテンプレートの処理後は残らない */-->
```

- 参考：[11.2. Thymeleaf parser-level comment blocks](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#thymeleaf-parser-level-comment-blocks)

### 3.4. style要素、style属性によるCSS適用は行わないでください

HTMLは文書構造を表し、CSSは文書の見た目を整えます。
一般的に構造と見た目を分離するのは良いプラクティスです。

そのため`style`要素や`style`属性は使用しないでください。

```html
<!--/* NG */-->
<!--/* style要素によるCSSの記述は禁止 */-->
<style>
  .info {
    color: blue;
  }
</style>
```

```html
<!--/* NG */-->
<!--/* style属性によるCSSの記述は禁止 */-->
<p style="color: blue;">...</p>
```

CSSは別途静的なファイル(拡張子は`.css`)として作成し、それを`link`要素で読み込むことで適用してください。

```html
<!--/* OK */-->
<link rel="stylesheet" href="../../static/xxx/yyy.css" th:href="@{/xxx/yyy.css}"/>
```

### 3.5. script要素内にJavaScriptコードを書かないでください

HTMLは文書構造を表し、JavaScriptは文書に振る舞いを与えます。
一般的に構造と振る舞いを分離するのは良いプラクティスです。

そのため`script`要素の中にJavaScriptコードは書かないでください。

```html
<!--/* NG */-->
<!--/* script要素内にJavaScriptコードを記載することは禁止 */-->
<script>
    const calendarInputs = document.querySelectorAll('input[type=calendar]');
    calendarInputs.forEach(calendarInput => {
        calendarInput.addEventListener('click', openDatePicker, false);
    });
</script>
```

JavaScriptは別途静的なファイル(拡張子は`.js`)として作成し、それを`script`要素で読み込むことで適用してください。

```html
<!--/* OK */-->
<script src="../../static/xxx/yyy.js" th:src="@{/xxx/yyy.js}"></script>
```

### 3.6. アプリケーションプログラマーの独断でHTMLテンプレートを分割しないでください

Thymeleafには`th:fragment`、`th:insert`、`th:replace`を使って別のHTMLテンプレートの一部をマージできる機能がありますが、これらをHTMLテンプレートの分割目的で使用しないでください。
HTMLテンプレートがみだりに分割されると、HTMLテンプレート間の関係を読み取ることが困難になるためです。

これらの機能は共通レイアウトや共通部品のHTMLテンプレートをマージするときのみ使用してください。
