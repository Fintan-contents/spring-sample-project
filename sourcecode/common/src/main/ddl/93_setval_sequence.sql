-- テストデータで投入するprojectのIDと重複しないように、シーケンスの開始位置を変更する
alter sequence public.project_project_id_seq restart with 201;
alter sequence public.projects_by_user_request_id_seq restart with 7;

