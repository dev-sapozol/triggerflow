ALTER TABLE `user`
  MODIFY `cellphone` VARCHAR(20)  NULL,
  MODIFY `role`      VARCHAR(20)  NOT NULL DEFAULT 'USER',
  MODIFY `language`  VARCHAR(2)   NOT NULL DEFAULT 'en';

UPDATE `user` SET `language` = 'en';
