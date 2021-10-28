INSERT INTO "oauth_client_details"(
"client_id",
"resource_ids",
"client_secret",
"scope",
"authorized_grant_types",
"web_server_redirect_uri",
"authorities",
"access_token_validity",
"refresh_token_validity",
"additional_information",
"autoapprove"
)
VALUES (
'read-client',
'resource-server-rest-api',
'$2a$10$lO6BMcS1U6meax/oBPUgiui/zDrrbKr04G2uasgXMgcmm526rPShG',
'read',
'password,authorization_code,refresh_token,implicit',
NULL,
'USER',
'3600',
'3800',
NULL,
NULL
);

INSERT INTO "oauth_client_details" (
"client_id",
"resource_ids",
"client_secret",
"scope",
"authorized_grant_types",
"web_server_redirect_uri",
"authorities",
"access_token_validity",
"refresh_token_validity",
"additional_information",
"autoapprove"
)
VALUES
(
'read-write-client',
'resource-server-rest-api',
'$2a$10$lO6BMcS1U6meax/oBPUgiui/zDrrbKr04G2uasgXMgcmm526rPShG',
'read,write',
'password,authorization_code,refresh_token,implicit',
NULL,
'USER',
'3600',
'3800',
NULL,
NULL
);


