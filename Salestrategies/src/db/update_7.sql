ALTER TABLE `strategies`.`activity` 
ADD COLUMN `activity_end_time` DATETIME NULL AFTER `status`;

CREATE TABLE `strategies`.`activity_type` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `val` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));
  INSERT INTO `strategies`.`activity_type` (`val`) VALUES ('专业化拜访');
INSERT INTO `strategies`.`activity_type` (`val`) VALUES ('事物性拜访');
