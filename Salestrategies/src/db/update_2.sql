CREATE TABLE `strategies`.`opportunityuserteam` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `opportunity_id` INT NULL,
  `user_id` INT NULL,
  `created_by` INT NULL,
  `created_date` DATETIME NULL,
  `modified_by` INT NULL,
  `modified_date` DATETIME NULL,
  PRIMARY KEY (`id`));

  ALTER TABLE `strategies`.`account` 
ADD COLUMN `industry` INT NULL AFTER `modifier_date`;

ALTER TABLE `strategies`.`activity` 
ADD COLUMN `modified_by` INT NULL AFTER `description`,
ADD COLUMN `modifier_date` DATETIME NULL AFTER `modified_by`;
ALTER TABLE `strategies`.`activity` 
ADD COLUMN `status` INT NULL AFTER `modifier_date`;
ALTER TABLE `strategies`.`opportunity` 
CHANGE COLUMN `created_date` `create_date` DATETIME NULL DEFAULT NULL ;
ALTER TABLE `strategies`.`opportunity` 
CHANGE COLUMN `modified_date` `modifier_date` DATETIME NULL DEFAULT NULL ;

ALTER TABLE `strategies`.`competitor` 
ADD COLUMN `owner` INT NULL AFTER `statue`;
ALTER TABLE `strategies`.`competitor` 
CHANGE COLUMN `crete_date` `create_date` DATETIME NULL DEFAULT NULL ;


