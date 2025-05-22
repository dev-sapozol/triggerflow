CREATE TABLE user (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  father_name VARCHAR(255) NOT NULL,
  mother_name VARCHAR(255) NULL,
  country VARCHAR(255) NOT NULL,
  gender ENUM('MALE', 'FEMALE', 'OTHER') NOT NULL, # 0 - male, 1 - female, 2 - other
  birthday DATE NOT NULL,
  cellphone VARCHAR(255) NOT NULL,
  age INT(11) NOT NULL,
  timezone VARCHAR(255) NOT NULL,

  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL);