-- edmではシーケンスオブジェクトが定義できないので、追加のSQLで生成している
drop sequence if exists mail_request_id;
create sequence mail_request_id;

drop sequence if exists batch_step_execution_seq;
drop sequence if exists batch_job_execution_seq;
drop sequence if exists batch_job_seq;
create sequence batch_step_execution_seq maxvalue 9223372036854775807 no cycle;
create sequence batch_job_execution_seq maxvalue 9223372036854775807 no cycle;
create sequence batch_job_seq maxvalue 9223372036854775807 no cycle;
