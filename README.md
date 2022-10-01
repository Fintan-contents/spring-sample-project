# サンプルプロジェクト

## 概要

[「Nablarchシステム開発ガイド」から提供されているサンプルプロジェクト](https://github.com/Fintan-contents/nablarch-system-development-guide/tree/master/%E3%82%B5%E3%83%B3%E3%83%97%E3%83%AB%E3%83%97%E3%83%AD%E3%82%B8%E3%82%A7%E3%82%AF%E3%83%88)のSpring Framework版です。

システムのプロファイルや機能要件などはNablarch版を踏襲しているため、Nablarch版の説明を参照してください。

## 構成

このリポジトリには以下の3つのディレクトリが置かれています。

- [設計書](./設計書)
    - サンプルプロジェクトの設計書です。

      [Nablarch版の設計書](https://github.com/Fintan-contents/nablarch-system-development-guide/tree/master/%E3%82%B5%E3%83%B3%E3%83%97%E3%83%AB%E3%83%97%E3%83%AD%E3%82%B8%E3%82%A7%E3%82%AF%E3%83%88/%E8%A8%AD%E8%A8%88%E6%9B%B8)をベースにしてSpring Frameworkを使用するための修正を行なっています(Spring Securityが使用するメッセージの追加、Spring Sessionが使用するテーブルの追加、など)。

      `030_アプリ設計`に格納されている各種設計書について、実際のプロジェクトでは[Nablarch開発標準で提供されている設計書フォーマット](https://github.com/nablarch-development-standards/nablarch-development-standards/tree/master/030_%E8%A8%AD%E8%A8%88%E3%83%89%E3%82%AD%E3%83%A5%E3%83%A1%E3%83%B3%E3%83%88/010_%E3%83%95%E3%82%A9%E3%83%BC%E3%83%9E%E3%83%83%E3%83%88)を使用して設計書を作成してください。

      上記以外は本リポジトリに格納されたファイルをご活用ください。

- [sourcecode](./sourcecode)
    - サンプルプロジェクトのソースコードです。

      [Nablarch版のソースコード](https://github.com/Fintan-contents/nablarch-system-development-guide/tree/master/%E3%82%B5%E3%83%B3%E3%83%97%E3%83%AB%E3%83%97%E3%83%AD%E3%82%B8%E3%82%A7%E3%82%AF%E3%83%88/%E3%82%BD%E3%83%BC%E3%82%B9%E3%82%B3%E3%83%BC%E3%83%89)は「プロジェクト管理システム」と「顧客管理システム」が完全に分かれていますが、Spring Framework版ではまとめて配置しています。

      実際のプロジェクトでは実装時の参考にして頂いたり、アプリケーションのベースを作成する際に流用してお使いください。

- [開発ガイド](./開発ガイド)
    - サンプルプロジェクトの開発ガイドです。
      Nablarchでは詳しい実装方法を知りたければ解説書を参照しますが、Spring Framework版ではこの開発ガイドに実装方法を詳しく解説しています。
      [Thymeleafコーディング規約](./開発ガイド/PGUT工程/style-guide/thymeleaf-coding-rule.md)や、MyBatisの使用を前提とした[SQLコーディング規約](./開発ガイド/PGUT工程/style-guide/SQLコーディング規約.docx)なども含まれます。

      実際のプロジェクトではお使いのGitホスティングサービス・ソフトウェアに取り込み、適宜修正して使用してください。

## ライセンス

ドキュメントは[Fintan コンテンツ 使用許諾条項](https://fintan.jp/page/295/#Fintan%E3%82%B3%E3%83%B3%E3%83%86%E3%83%B3%E3%83%84%E4%BD%BF%E7%94%A8%E8%A8%B1%E8%AB%BE%E6%9D%A1%E9%A0%85)の元に提供されており、ソースコードは[Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0)の元に提供されています。

