CREATE TABLE `strategies`.`dna_implement` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `opportunity_id` INT(11) NULL,
  `temporary_technichal_advantage` MEDIUMTEXT NULL,
  `projects_or_problem` MEDIUMTEXT NULL,
  `quantifiable_business_outcome` MEDIUMTEXT NULL,
  `customer_coals_and_objectives` MEDIUMTEXT NULL,
  `customer_political_agenda` MEDIUMTEXT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

ALTER TABLE `strategies`.`dna_implement` 
ADD COLUMN `created_by` INT(11) NULL AFTER `customer_political_agenda`,
ADD COLUMN `created_date` DATETIME NULL AFTER `created_by`,
ADD COLUMN `modified_by` INT(11) NULL AFTER `created_date`,
ADD COLUMN `modified_date` DATETIME NULL AFTER `modified_by`;

ALTER TABLE `strategies`.`dna` 
CHANGE COLUMN `temporary_technichal_advantage` `temporary_technichal_advantage` MEDIUMTEXT CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL ,
CHANGE COLUMN `projects_or_problem` `projects_or_problem` MEDIUMTEXT CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL ,
CHANGE COLUMN `quantifiable_business_outcome` `quantifiable_business_outcome` MEDIUMTEXT CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL ,
CHANGE COLUMN `customer_coals_and_objectives` `customer_coals_and_objectives` MEDIUMTEXT CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL ,
CHANGE COLUMN `customer_political_agenda` `customer_political_agenda` MEDIUMTEXT CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL ;
