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
