// 共通JavaScritptファイル
// jQueryを用いた共通関数を定義する


// 顧客検索イベント
// 顧客選択画面の検索ボタン押下時に起動する
$(function () {
  $("#selectClient").click(function () {
    const clientName = $("#selectClientName").val();
    const industryCode = $("#industryCode").val();
    let data = {};
    if (clientName) {
      data.clientName = clientName;
    }
    if (industryCode) {
      data.industryCode = industryCode;
    }

    // メタ属性からAPI URLを取得
    const clientApiUrl = $("meta[name='_client_api_url']").attr("content");

    $.ajax({
      type: "GET",
      url: clientApiUrl + "/clients",
      dataType: "json",
      data: data,
      success: function (data) {
        $("#messageArea").empty();

        // プルダウンの初期化
        $("#clientList").children().remove();
        $("#clientList").append($("<option>").text("検索結果から選択してください。").val(""));

        // プルダウンの初期化に合わせて選択ボタンを無効化
        $("#apply").prop("disabled", true);

        // 検索結果をプルダウンリストに反映する
        $.each(data.clients, function (idx, val) {
          $("#clientList").append($("<option>").text(val.clientName).val(val.clientId));
        });

        // 検索結果の件数を反映
        $("#numberOfClients").text(data.clients.length);

        // 検索結果の領域を有効化
        $("#clientResultArea").css("display", "block");
      },
      error: function (error) {
        //バリデーションエラーの場合
        if (error.status == 400) {
          $("#messageArea").empty();
          $.each(error.responseJSON.messages, function (idx, message) {
            $("#messageArea").append("<span class='text-danger'>" + message + "</span><br>");
          });
        } else {
          alert("エラーが発生しました。処理を再度実行してください。");
        }
      },
    });
  });
});

// 顧客選択イベント
$(function () {
  // 未選択状態時には選択ボタンを無効化
  $("#clientList").change(function () {
    const value = $("#clientList").val();
    $("#apply").prop("disabled", value === "");
  });

  // 顧客選択画面の選択ボタン押下時に起動
  $("#apply").click(function () {
    // プルダウンリストの値を取得する
    const clientId = $("#clientList").val();
    // clientIdのID属性を持つ項目に値をセットする
    $("#clientId").val(clientId);
    // プルダウンリストの選択中のテキストを取得する
    const clientName = $("#clientList option:selected").text();
    // clientNameのID属性を持つ項目に値をセットする
    $("#clientName").val(clientName);
  });
});
