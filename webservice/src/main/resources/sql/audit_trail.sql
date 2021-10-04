CREATE TABLE IF NOT EXISTS ph_audit_trail_action(
created_date TIMESTAMP NOT NULL DEFAULT now(),
"action" VARCHAR(30),
PRIMARY KEY ("action")
);

CREATE TABLE IF NOT EXISTS ph_audit_trail(
"id" SERIAL,
"table_name" VARCHAR(30) NOT NULL,
user_id INT NOT NULL,
created_date TIMESTAMP NOT NULL DEFAULT now(),
"action" VARCHAR(30) NOT NULL,
entity_id VARCHAR(200) NULL,
PRIMARY KEY ("id"),
FOREIGN KEY ("action") REFERENCES ph_audit_trail_action ("action") ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS ph_audit_trail_value(
audit_trail_id INT,
"column_name" VARCHAR(30),
old_value TEXT,
new_value TEXT,
PRIMARY KEY(audit_trail_id,"column_name"),
FOREIGN KEY (audit_trail_id) REFERENCES ph_audit_trail ("id") ON DELETE CASCADE ON UPDATE CASCADE
);

SELECT audit.*,action."action",value.* FROM ph_audit_trail AS audit
INNER JOIN ph_audit_trail_action AS action ON audit.action_id = action."id"
INNER JOIN ph_audit_trail_value AS value ON audit."id" = value.audit_trail_id
WHERE audit."id" = 5