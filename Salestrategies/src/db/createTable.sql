CREATE SCHEMA `strategie` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin ;
CREATE TABLE `strategie`.`account` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

ALTER TABLE `strategie`.`account` 
ADD COLUMN `country` INT NULL AFTER `name`,
ADD COLUMN `province` INT NULL AFTER `country`,
ADD COLUMN `city` INT NULL AFTER `province`,
ADD COLUMN `office_tel` VARCHAR(45) NULL AFTER `city`,
ADD COLUMN `address` VARCHAR(255) NULL AFTER `office_tel`,
ADD COLUMN `legal_representative` VARCHAR(45) NULL AFTER `address`,
ADD COLUMN `type` INT NULL AFTER `legal_representative`,
ADD COLUMN `status` INT NULL AFTER `type`,
ADD COLUMN `website` VARCHAR(255) NULL AFTER `status`,
ADD COLUMN `fax` VARCHAR(45) NULL AFTER `website`,
ADD COLUMN `employee_number` INT NULL AFTER `fax`,
ADD COLUMN `revenue` VARCHAR(45) NULL AFTER `employee_number`,
ADD COLUMN `date_of_establish` DATE NULL AFTER `revenue`,
ADD COLUMN `owner` VARCHAR(45) NULL AFTER `date_of_establish`,
ADD COLUMN `created_by` VARCHAR(45) NULL AFTER `owner`,
ADD COLUMN `create_date` DATE NULL AFTER `created_by`,
ADD COLUMN `modified_by` VARCHAR(45) NULL AFTER `create_date`,
ADD COLUMN `modifier_date` DATE NULL AFTER `modified_by`;
ALTER TABLE `strategie`.`account` 
CHANGE COLUMN `create_date` `create_date` DATETIME NULL DEFAULT NULL ,
CHANGE COLUMN `modifier_date` `modifier_date` DATETIME NULL DEFAULT NULL ,
CHANGE COLUMN `owner` `owner` INT NULL DEFAULT NULL ,
CHANGE COLUMN `created_by` `created_by` INT NULL DEFAULT NULL ,
CHANGE COLUMN `modified_by` `modified_by` INT NULL DEFAULT NULL ;


CREATE TABLE `strategie`.`contact` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));
  
  ALTER TABLE `strategie`.`contact` 
ADD COLUMN `account_id` INT NOT NULL AFTER `name`,
ADD COLUMN `department` VARCHAR(45) NULL AFTER `account_id`,
ADD COLUMN `status` INT NULL AFTER `department`,
ADD COLUMN `office_tel` VARCHAR(45) NULL AFTER `status`,
ADD COLUMN `job_title` VARCHAR(45) NULL AFTER `office_tel`,
ADD COLUMN `telephone` VARCHAR(45) NULL AFTER `job_title`,
ADD COLUMN `fax` VARCHAR(45) NULL AFTER `telephone`,
ADD COLUMN `email` VARCHAR(45) NULL AFTER `fax`,
ADD COLUMN `gender` INT NULL AFTER `email`,
ADD COLUMN `report_to` INT NULL AFTER `gender`,
ADD COLUMN `created_by` VARCHAR(45) NULL AFTER `report_to`,
ADD COLUMN `owner` VARCHAR(45) NULL AFTER `created_by`,
ADD COLUMN `create_date` DATETIME NULL AFTER `owner`,
ADD COLUMN `modified_by` VARCHAR(45) NULL AFTER `create_date`,
ADD COLUMN `modifier_date` DATETIME NULL AFTER `modified_by`;
ALTER TABLE `strategie`.`contact` 
CHANGE COLUMN `created_by` `created_by` INT NULL DEFAULT NULL ,
CHANGE COLUMN `owner` `owner` INT NULL DEFAULT NULL ,
CHANGE COLUMN `modified_by` `modified_by` INT NULL DEFAULT NULL ;

  CREATE TABLE `strategie`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));
  ALTER TABLE `strategie`.`user` 
ADD COLUMN `employee_number` VARCHAR(45) NULL AFTER `name`,
ADD COLUMN `gender` INT NULL AFTER `employee_number`,
ADD COLUMN `job_title` INT NULL AFTER `gender`,
ADD COLUMN `report_to` INT NULL AFTER `job_title`,
ADD COLUMN `login_name` VARCHAR(45) NULL AFTER `report_to`,
ADD COLUMN `password` VARCHAR(45) NULL AFTER `login_name`,
ADD COLUMN `telephone` VARCHAR(45) NULL AFTER `password`,
ADD COLUMN `office_tel` VARCHAR(45) NULL AFTER `telephone`,
ADD COLUMN `fax` VARCHAR(45) NULL AFTER `office_tel`,
ADD COLUMN `email` VARCHAR(45) NULL AFTER `fax`,
ADD COLUMN `created_by` INT NULL AFTER `email`,
ADD COLUMN `create_date` DATETIME NULL AFTER `created_by`,
ADD COLUMN `modified_by` INT NULL AFTER `create_date`,
ADD COLUMN `modifier_date` DATETIME NULL AFTER `modified_by`;

  CREATE TABLE `strategie`.`activity` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));
  ALTER TABLE `strategie`.`activity` 
ADD COLUMN `opportunity_id` INT NULL AFTER `name`,
ADD COLUMN `account_id` INT NULL AFTER `opportunity_id`,
ADD COLUMN `countact_id` INT NULL AFTER `account_id`,
ADD COLUMN `user_id` INT NULL AFTER `countact_id`,
ADD COLUMN `owner` INT NULL AFTER `user_id`,
ADD COLUMN `created_by` INT NULL AFTER `owner`,
ADD COLUMN `create_date` DATETIME NULL AFTER `created_by`,
ADD COLUMN `start_time` DATETIME NULL AFTER `create_date`,
ADD COLUMN `end_time` DATETIME NULL AFTER `start_time`,
ADD COLUMN `type` INT NULL AFTER `end_time`,
ADD COLUMN `duration_mininute` INT NULL AFTER `type`,
ADD COLUMN `call_property` INT NULL AFTER `duration_mininute`,
ADD COLUMN `description` VARCHAR(455) NULL AFTER `call_property`;

  
  CREATE TABLE `strategie`.`opportunity` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));
  ALTER TABLE `strategie`.`opportunity` 
ADD COLUMN `owner` INT NULL AFTER `name`,
ADD COLUMN `account_id` INT NULL AFTER `owner`,
ADD COLUMN `stage_name` INT NULL AFTER `account_id`,
ADD COLUMN `amount` VARCHAR(45) NULL AFTER `stage_name`,
ADD COLUMN `start_date` DATE NULL AFTER `amount`,
ADD COLUMN `end_date` DATE NULL AFTER `start_date`,
ADD COLUMN `type` INT NULL AFTER `end_date`,
ADD COLUMN `quote` VARCHAR(455) NULL AFTER `type`,
ADD COLUMN `expected_revenue` VARCHAR(455) NULL AFTER `quote`,
ADD COLUMN `probability` INT NULL AFTER `expected_revenue`,
ADD COLUMN `created_by` INT NULL AFTER `probability`,
ADD COLUMN `created_date` DATETIME NULL AFTER `created_by`,
ADD COLUMN `modified_by` INT NULL AFTER `created_date`,
ADD COLUMN `modified_date` DATETIME NULL AFTER `modified_by`;

  CREATE TABLE `strategie`.`competitor` (
  `idcompetitor` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`idcompetitor`));
  
ALTER TABLE `strategie`.`competitor` 
ADD COLUMN `responsible_person` INT NULL AFTER `name`,
ADD COLUMN `advantage` VARCHAR(2555) NULL AFTER `responsible_person`,
ADD COLUMN `disadvantage` VARCHAR(2555) NULL AFTER `advantage`,
ADD COLUMN `created_by` INT NULL AFTER `disadvantage`,
ADD COLUMN `crete_date` DATETIME NULL AFTER `created_by`,
ADD COLUMN `modified_by` INT NULL AFTER `crete_date`,
ADD COLUMN `modifier_date` DATETIME NULL AFTER `modified_by`,
ADD COLUMN `statue` INT NULL AFTER `modifier_date`;

CREATE TABLE `strategie`.`accountuserteam` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `account_id` INT NULL,
  `user_id` INT NULL,
  `owner` INT NULL,
  `status` INT NULL,
  `add_time` DATETIME NULL,
  `team_role` INT NULL,
  `permission` INT NULL,
  `modified_by` INT NULL,
  `modified_date` DATETIME NULL,
  `created_by` INT NULL,
  PRIMARY KEY (`id`));
 
  CREATE TABLE `strategie`.`countactuserteam` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `contact_id` INT NULL,
  `user_id` INT NULL,
  `owner` INT NULL,
  `created_date` DATETIME NULL,
  `created_by` INT NULL,
  `modified_date` DATETIME NULL,
  PRIMARY KEY (`id`));
  CREATE TABLE `strategie`.`dna` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `owner` INT NULL,
  `telephone` VARCHAR(45) NULL,
  `email` VARCHAR(45) NULL,
  `created_date` DATETIME NULL,
  `account_id` INT NULL,
  `opportunity_id` INT NULL,
  `created_by` INT NULL,
  `modified_by` INT NULL,
  `modified_date` DATETIME NULL,
  `property` INT NULL,
  `temporary_technichal_advantage` VARCHAR(455) NULL,
  `projects_or_problem` VARCHAR(455) NULL,
  `quantifiable_business_outcome` VARCHAR(455) NULL,
  `customer_coals_and_objectives` VARCHAR(455) NULL,
  `customer_political_agenda` VARCHAR(455) NULL,
  PRIMARY KEY (`id`));
CREATE TABLE `strategie`.`politicalmap` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `contact_id` INT NULL,
  `opportunity_id` INT NULL,
  `modified_by` INT NULL,
  `modified_date` DATETIME NULL,
  `decision_id` INT NULL,
  `buyint_style_id` INT NULL,
  `relation_status_id` INT NULL,
  `time_spent_id` INT NULL,
  `power_line_id` INT NULL,
  `created_by` INT NULL,
  `created_date` DATETIME NULL,
  PRIMARY KEY (`id`));
CREATE TABLE `strategie`.`opportunitycontactteam` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `opportunity_id` INT NULL,
  `contact_id` INT NULL,
  `isowner` INT NULL,
  PRIMARY KEY (`id`));
  
  CREATE TABLE `strategie`.`target_acquisition` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `opportunity_id` INT NULL,
  `user_id` INT NULL,
  `competitor_id` INT NULL,
  `created_date` DATETIME NULL,
  `created_by` INT NULL,
  `need_value` INT NULL,
  `time_value` INT NULL,
  `money_value` INT NULL,
  `value_value` INT NULL,
  `criteria_value` INT NULL,
  `resources_value` INT NULL,
  `process_value` INT NULL,
  `execution_value` INT NULL,
  `evaluation_value` INT NULL,
  `support_value` INT NULL,
  `executives_value` INT NULL,
  `political_value` INT NULL,
  `modified_by` INT NULL,
  `modified_date` DATETIME NULL,
  PRIMARY KEY (`id`));
