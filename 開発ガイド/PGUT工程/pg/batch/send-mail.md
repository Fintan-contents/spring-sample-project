# メール送信

- [メール送信要求を登録する](#メール送信要求を登録する)

## メール送信要求を登録する

メール送信要求の登録はNablarchの提供するライブラリを使用して行う。
詳しくは[7.7.3.2. メール送信要求を登録する](https://nablarch.github.io/docs/5u21/doc/application_framework/application_framework/libraries/mail.html#mail-request)を参照すること。

Nablarchでは`MailUtil.getMailRequester()`を使用して`MailRequester`を取得するが、本アプリケーションでは`@Autowired`を使用して`MailRequester`をインジェクションする。

```java
@Autowired
private MailRequester requester;
```

`MailRequester`をインジェクションして良いステレオタイプは次の通り。

- `ItemProcessor`

その後はNablarchと同様にメール送信要求を構築し、`MailRequester`の`requestToSend`に渡すことで登録ができる。

```java
// メール送信要求を作成する。
TemplateMailContext mailRequest = new TemplateMailContext();
mailRequest.setFrom("from@tis.co.jp");
mailRequest.addTo("to@tis.co.jp");
mailRequest.addCc("cc@tis.co.jp");
mailRequest.addBcc("bcc@tis.co.jp");
mailRequest.setSubject("件名");
mailRequest.setTemplateId("テンプレートID");
mailRequest.setLang("ja");

// テンプレートのプレースホルダに対する値を設定する。
mailRequest.setVariable("name", "名前");
mailRequest.setVariable("address", "住所");
mailRequest.setVariable("tel", "電話番号");
// 以下のように値にnullを設定した場合、空文字列で置き換えが行われる。
mailRequest.setVariable("option", null);

// 添付ファイルを設定する。
AttachedFile attachedFile = new AttachedFile("text/plain", new File("path/to/file"));
mailRequest.addAttachedFile(attachedFile);

// メール送信要求を登録する。
String mailRequestId = requester.requestToSend(mailRequest);
```

登録されたメール送信要求は、別プロセスとして実行されている常駐バッチにより検出され、実際のメール送信処理が行われる。
