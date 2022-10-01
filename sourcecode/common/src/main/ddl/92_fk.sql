-- gspプラグインでedmからDDLを生成した場合、削除のCASCADEがDDLに反映されないので、追加のSQLで別途作り直している
alter table public.spring_session_attributes drop constraint spring_session_attributes_session_primary_id_fkey;
alter table public.spring_session_attributes
add
foreign key (
  session_primary_id
) references public.spring_session (
  primary_id
) on delete cascade;
