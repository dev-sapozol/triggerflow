CREATE TABLE email_inbox (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL, 
  original_message_id INT NULL,
  email VARCHAR(255) NOT NULL,
  `from` VARCHAR(255) NOT NULL,
  `to` TEXT NOT NULL,
  cc TEXT NULL,
  subject TEXT NOT NULL,
  preview TEXT NOT NULL,
  inbox_type INT NOT NULL,
  is_read BOOLEAN NOT NULL,
  has_attachment BOOLEAN NOT NULL,
  importance INT NOT NULL,
  in_reply_to LONGTEXT NULL,
  `references` LONGTEXT NULL,
  s3_url TEXT NULL,
  thread_id VARCHAR(255) NULL,
  text_body LONGTEXT NOT NULL,
  html_body LONGTEXT NULL,
  folder INT NULL,
  deleted_at DATETIME NULL,

  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL,
  
  FOREIGN KEY (user_id) REFERENCES user(id)
  );