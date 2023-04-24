--DROP TABLE IF EXISTS customer_master;

CREATE TABLE IF NOT EXISTS order_master (
  id INT AUTO_INCREMENT PRIMARY KEY,
  order_id VARCHAR(250) NOT NULL,
  user_id VARCHAR(250) NOT NULL,
  customer_name VARCHAR(250) NOT NULL,
  product_id VARCHAR(250) NOT NULL,
  product_type VARCHAR(250) NOT NULL,
  quantity VARCHAR(250) DEFAULT NULL,
  order_status VARCHAR(250) DEFAULT NULL,
  order_desc VARCHAR(250) DEFAULT NULL,
  correlation_id VARCHAR(250) DEFAULT NULL,
  transaction_id VARCHAR(250) DEFAULT NULL,
  order_datetime VARCHAR(250) DEFAULT NULL,
  created_datetime VARCHAR(250) DEFAULT NULL,
  modified_datetime VARCHAR(250) DEFAULT NULL,
  created_by VARCHAR(250) DEFAULT NULL,
  modified_by VARCHAR(250) DEFAULT NULL
);