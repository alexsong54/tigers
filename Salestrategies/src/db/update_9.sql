CREATE TABLE `strategies`.`target_acquisition_value_pl` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `val` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));
INSERT INTO `strategies`.`target_acquisition_value_pl` (`id`, `val`) VALUES ('1', '＋');
INSERT INTO `strategies`.`target_acquisition_value_pl` (`id`, `val`) VALUES ('2', '－');
INSERT INTO `strategies`.`target_acquisition_value_pl` (`id`, `val`) VALUES ('3', '？');

ALTER TABLE `strategies`.`target_acquisition` 
CHANGE COLUMN `created_date` `created_date` DATE NULL DEFAULT NULL ;

ALTER TABLE `strategies`.`opportunityuserteam` 
CHANGE COLUMN `modified_date` `modifier_date` DATETIME NULL DEFAULT NULL ;
