--medicine type
INSERT INTO ph_medicine_type("id", "name", "desc") VALUES( nextval('ph_medicine_type_id_seq'), 'Box', 'Box');
INSERT INTO ph_medicine_type("id", "name", "desc") VALUES( nextval('ph_medicine_type_id_seq'), 'Bottle', 'Bottle');
INSERT INTO ph_medicine_type("id", "name", "desc") VALUES( nextval('ph_medicine_type_id_seq'), 'Package', 'Package');
--company
INSERT INTO ph_company("id", "name") VALUES( nextval('ph_company_id_seq'), 'MS');
INSERT INTO ph_company("id", "name") VALUES( nextval('ph_company_id_seq'), 'Uniliver');
INSERT INTO ph_company("id", "name") VALUES( nextval('ph_company_id_seq'), 'One Piece');
--audit trail action
INSERT INTO ph_audit_trail_action("action") VALUES('insert');
INSERT INTO ph_audit_trail_action("action") VALUES('update');
INSERT INTO ph_audit_trail_action("action") VALUES('delete');
--supplier
INSERT INTO ph_supplier ("id","tel1", "email", "created_date",  "created_by" ) VALUES (nextval('ph_supplier_id_seq'),'077553335', 'test@gmail.com', '2020-10-30 21:48:31.053427',  'system');
