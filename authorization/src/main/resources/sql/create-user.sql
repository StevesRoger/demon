INSERT INTO "user_account" (
"usr_acc_id",
"is_activate",
"usr_acc_password",
"usr_acc_username",
"usr_id",
"status",
"date_of_birth",
"email",
"first_name",
"last_name",
"primary_phone",
"secondary_phone",
)
VALUES
(
nextval('user_account_id_seq'),
't',
'$2a$10$/bm31NHSpcYZ4sTAI1D4A.X6uE4IyLRrFSnQcuW0es.MGX5qHYTxO',
'admin',
'1',
'active',
'1994-08-07 08:06:58',
'test@gmail.com',
'test',
'test',
'077553335',
'081553335'
);

INSERT INTO "user_account" (
"usr_acc_id",
"is_activate",
"usr_acc_password",
"usr_acc_username",
"usr_id",
"status",
"date_of_birth",
"email",
"first_name",
"last_name",
"primary_phone",
"secondary_phone"
)
VALUES
(
nextval('user_account_id_seq'),
't',
'$2a$10$/bm31NHSpcYZ4sTAI1D4A.X6uE4IyLRrFSnQcuW0es.MGX5qHYTxO',
'userAccount',
'1',
'active',
'1994-08-07 08:06:58',
'test@gmail.com',
'test',
'test',
'077553335',
'081553335'
);

INSERT INTO "user_role" (
"id",
"role",
"description",
"created_by",
"created_date"
)
VALUES
(nextval('user_role_id_seq'),'ADMIN', 'administrator','system',now());

INSERT INTO "user_role" (
"id",
"role",
"description",
"created_by",
"created_date"
)
VALUES
(nextval('user_role_id_seq'),'USER', 'normal user','system',now());

INSERT INTO "public"."status" (
"sta_id",
"sta_description",
"sta_value"
)
VALUES
(nextval('status_sta_id_seq'), 'active', 'active');