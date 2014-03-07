CREATE TABLE `strategies`.`tactics` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `target_quisition` INT NULL,
  `target_date` DATE NULL,
  `owner` INT NULL,
  `opportunity_id` INT NULL,
  `created_by` INT NULL,
  `individual_met` INT NULL,
  `propose` VARCHAR(455) NULL,
  `actual_outcome` VARCHAR(455) NULL,
  `resulting_action` VARCHAR(455) NULL,
  `create_time` DATETIME NULL,
  `modifier_date` DATETIME NULL,
  `modified_by` int NULL,
  PRIMARY KEY (`id`));
  ALTER TABLE `strategies`.`tactics` 
ADD COLUMN `name` VARCHAR(45) NULL AFTER `modified_by`;

ALTER TABLE `strategies`.`opportunitycontactteam` 
ADD COLUMN `decision_role` INT NULL AFTER `isowner`,
ADD COLUMN `buying_style` INT NULL AFTER `decision_role`,
ADD COLUMN `status` INT NULL AFTER `buying_style`,
ADD COLUMN `influence_to` INT NULL AFTER `status`;



CREATE TABLE `strategies`.`userrole` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NULL,
  `role_id` INT NULL,
  `opportunity_id` INT NULL,
  PRIMARY KEY (`id`));
  
  ALTER TABLE `strategies`.`opportunityuserteam` 
ADD COLUMN `team_role` INT NULL AFTER `modified_date`;
