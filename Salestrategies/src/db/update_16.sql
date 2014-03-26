ALTER TABLE `strategies`.`competitor` 
ADD COLUMN `isMycompany` INT(11) NULL AFTER `owner`;

CREATE TABLE `strategies`.`isMycompany_pl` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `val` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

INSERT INTO `strategies`.`isMycompany_pl` (`id`, `val`) VALUES ('1', '是');
INSERT INTO `strategies`.`isMycompany_pl` (`id`, `val`) VALUES ('2', '否');
