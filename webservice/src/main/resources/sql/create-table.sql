CREATE TABLE
IF NOT EXISTS ph_address (
	"id" SERIAL,
	street_number VARCHAR (50),
	home_number VARCHAR (50),
	village VARCHAR (100),
	district VARCHAR (100),
	commune VARCHAR (100),
	province VARCHAR (100),
	PRIMARY KEY ("id")
);

CREATE TABLE
IF NOT EXISTS ph_e_unit_type (
	"name" VARCHAR (50),
	"desc" VARCHAR (100),
	PRIMARY KEY ("name")
);

CREATE TABLE
IF NOT EXISTS ph_e_brand (
	"name" VARCHAR (50),
	"desc" VARCHAR (100),
	PRIMARY KEY ("name")
);

CREATE TABLE
IF NOT EXISTS ph_e_country (
	"name" VARCHAR (50),
	"desc" VARCHAR (100),
	PRIMARY KEY ("name")
);

CREATE TABLE
IF NOT EXISTS ph_e_customer_type (
	"name" VARCHAR (50),
	"desc" VARCHAR (100),
	PRIMARY KEY ("name")
);

CREATE TABLE
IF NOT EXISTS ph_e_media_type (
	"name" VARCHAR (50),
	"desc" VARCHAR (100),
	PRIMARY KEY ("name")
);

CREATE TABLE
IF NOT EXISTS ph_e_currency (
	"name" VARCHAR (50),
	"desc" VARCHAR (100),
	PRIMARY KEY ("name")
);

CREATE TABLE
IF NOT EXISTS ph_e_scope (
	"name" VARCHAR (50),
	"desc" VARCHAR (100),
	PRIMARY KEY ("name")
);

CREATE TABLE
IF NOT EXISTS ph_e_status (
	"name" VARCHAR (50),
	"desc" VARCHAR (100),
	PRIMARY KEY ("name")
);

CREATE TABLE
IF NOT EXISTS ph_e_multiply_type(
	"name" VARCHAR (50),
	"desc" VARCHAR (100),
	PRIMARY KEY ("name")
);

CREATE TABLE
IF NOT EXISTS ph_product (
	"id" SERIAL,
	brand VARCHAR (50),
	unit_price REAL DEFAULT 0.0,
	label VARCHAR (100),
	country VARCHAR (50),
	unit_type VARCHAR (50),
	composition TEXT,
	"name" VARCHAR (100),
	name_kh VARCHAR (100),
	status VARCHAR (15),
	"desc" VARCHAR,
	created_date TIMESTAMP DEFAULT now(),
	modified_date TIMESTAMP,
	created_by VARCHAR (50),
	modified_by VARCHAR (50),
	deleted_date TIMESTAMP,
	PRIMARY KEY ("id"),
	FOREIGN KEY (country) REFERENCES ph_e_country ("name") ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (brand) REFERENCES ph_e_brand ("name") ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (unit_type) REFERENCES ph_e_unit_type ("name") ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (status) REFERENCES ph_e_status ("name") ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE
IF NOT EXISTS ph_product_barcode (
	product_id INT,
	barcode TEXT,
	created_date TIMESTAMP DEFAULT now(),
	created_by VARCHAR (50),
	PRIMARY KEY (product_id, barcode),
	FOREIGN KEY (product_id) REFERENCES ph_product ("id") ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE
IF NOT EXISTS ph_image (
	"id" SERIAL,
	mine_type VARCHAR (100),
	"name" VARCHAR (100),
	"path" TEXT,
	"bytes" bytea,
	url TEXT,
	created_date TIMESTAMP DEFAULT now(),
	created_by VARCHAR (50),
	PRIMARY KEY ("id")
);

CREATE TABLE
IF NOT EXISTS ph_stock (
	"id" SERIAL,
	post_quantity INT DEFAULT 0,
	pre_quantity INT DEFAULT 0,
	remaining_alert INT DEFAULT 0,
	created_date TIMESTAMP DEFAULT now(),
	modified_date TIMESTAMP,
	created_by VARCHAR (50),
	modified_by VARCHAR (50),
	product_id INT NOT NULL,
	PRIMARY KEY ("id"),
	FOREIGN KEY (product_id) REFERENCES ph_product ("id") ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE
IF NOT EXISTS ph_supplier (
	"id" SERIAL,
	tel1 VARCHAR (20),
	tel2 VARCHAR (20),
	email VARCHAR (50),
	created_date TIMESTAMP DEFAULT now(),
	modified_date TIMESTAMP,
	created_by VARCHAR (50),
	modified_by VARCHAR (50),
	PRIMARY KEY ("id")
);

CREATE TABLE
IF NOT EXISTS ph_supplying (
	"id" SERIAL,
	quantity INT DEFAULT 0,
	unit_price REAL DEFAULT 0.0,
	expiry_date TIMESTAMP,
	publish_date TIMESTAMP,
	created_date TIMESTAMP DEFAULT now(),
	created_by VARCHAR (50),
	product_id INT NOT NULL,
	supplier_id INT NOT NULL,
	unit_type VARCHAR (50),
	user_id INT NOT NULL,
	PRIMARY KEY ("id"),
	FOREIGN KEY (product_id) REFERENCES ph_product ("id") ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (supplier_id) REFERENCES ph_supplier ("id") ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (unit_type) REFERENCES ph_e_unit_type ("name") ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE
IF NOT EXISTS ph_customer (
	"id" SERIAL,
	"name" VARCHAR (20),
	tel1 VARCHAR (20),
	tel2 VARCHAR (20),
	customer_type VARCHAR (50),
	PRIMARY KEY ("id"),
	FOREIGN KEY (customer_type) REFERENCES ph_e_customer_type ("name") ON DELETE NO ACTION ON UPDATE CASCADE
);

CREATE TABLE
IF NOT EXISTS ph_order (
	"id" SERIAL,
	user_id INT NOT NULL,
	status VARCHAR (15),
	remark VARCHAR (100),
	refer_number VARCHAR (50),
	total_amount REAL DEFAULT 0.0,
	receive_amount REAL DEFAULT 0.0,
	remaining_amount REAL DEFAULT 0.0,
	receive_currency VARCHAR (50),
	change REAL DEFAULT 0.0,
	modified_date TIMESTAMP,
	created_date TIMESTAMP DEFAULT now(),
	created_by VARCHAR (50),
	modified_by VARCHAR (50),
	customer_id INT NOT NULL,
	PRIMARY KEY ("id"),
	FOREIGN KEY (customer_id) REFERENCES ph_customer ("id") ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (receive_currency) REFERENCES ph_e_currency ("name") ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (status) REFERENCES ph_e_status ("name") ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE
IF NOT EXISTS ph_product_order (
	product_id INT,
	order_id INT,
	quantity INT DEFAULT 0,
	unit_price REAL DEFAULT 0.0,
	vat REAL DEFAULT 0.0,
	discount REAL DEFAULT 0.0,
	multiply_type VARCHAR(15),
	PRIMARY KEY (product_id, order_id),
	FOREIGN KEY (product_id) REFERENCES ph_product ("id") ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (order_id) REFERENCES ph_order ("id") ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (multiply_type) REFERENCES ph_e_multiply_type ("name") ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE
IF NOT EXISTS ph_user_product (
	product_id INT NOT NULL,
	user_id INT NOT NULL,
	FOREIGN KEY (product_id) REFERENCES ph_product ("id") ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE
IF NOT EXISTS ph_user_product_shared (
	"scope" VARCHAR (100),
	product_id INT NOT NULL,
	owner_user_id INT NOT NULL,
	guest_user_id INT NOT NULL,
	PRIMARY KEY (product_id,owner_user_id,guest_user_id),
	FOREIGN KEY (product_id) REFERENCES ph_product ("id") ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY (scope) REFERENCES ph_e_scope ("name") ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE
IF NOT EXISTS ph_user_media (
	media_id INT,
	user_id INT,
	PRIMARY KEY(media_id, user_id),
	FOREIGN KEY (media_id) REFERENCES ph_media ("id") ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE
IF NOT EXISTS ph_sys_config (
	"id" SERIAL,
	sys_key VARCHAR (100) NOT NULL,
	sys_value VARCHAR (100) NOT NULL,
	sys_category VARCHAR (100) NOT NULL,
	sys_desc VARCHAR (200),
	created_date TIMESTAMP DEFAULT now(),
	PRIMARY KEY ("id")
);

CREATE TABLE
IF NOT EXISTS ph_media (
	"id" SERIAL,
	"link" TEXT UNIQUE,
	status VARCHAR (20),
	created_date TIMESTAMP DEFAULT now(),
	publish_date TIMESTAMP DEFAULT now(),
	modified_date TIMESTAMP,
	created_by VARCHAR (50),
	modified_by VARCHAR (50),
	publisher VARCHAR (50),
	"type" VARCHAR (20),
	image_id INT,
	width BIGINT,
	height BIGINT,
	background_color VARCHAR (10),
	title VARCHAR (250) UNIQUE,
	PRIMARY KEY ("id"),
	FOREIGN KEY ("type") REFERENCES ph_e_media_type ("name") ON DELETE NO ACTION ON UPDATE CASCADE,
	FOREIGN KEY (status) REFERENCES ph_e_status ("name") ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE
IF NOT EXISTS ph_alert_log (
	"id" SERIAL,
	created_date TIMESTAMP DEFAULT now(),
	created_by VARCHAR (50),
	content TEXT,
	response TEXT,
	"type" VARCHAR (20),
	status VARCHAR (20),
	PRIMARY KEY ("id")
);

CREATE TABLE
IF NOT EXISTS ph_inbox (
	"id" SERIAL,
	user_id INT NOT NULL,
	created_date TIMESTAMP DEFAULT now(),
	created_by VARCHAR (50),
	title VARCHAR (50),
	message TEXT,
	seen boolean DEFAULT false,
	"type" VARCHAR (20),
	PRIMARY KEY ("id")
);