CREATE TABLE `strategies`.`swot` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `strengths` VARCHAR(255) NULL,
  `weaknesses` VARCHAR(255) NULL,
  `opportunities` VARCHAR(255) NULL,
  `threats` VARCHAR(255) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

ALTER TABLE `strategies`.`swot` 
ADD COLUMN `opportunity_id` INT(11) NULL AFTER `threats`;

CREATE TABLE `strategies`.`summary` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `vertial_market` VARCHAR(255) NULL,
  `compelling_mechanism` VARCHAR(255) NULL,
  `our_solution` VARCHAR(255) NULL,
  `our_quantified` VARCHAR(255) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

ALTER TABLE `strategies`.`summary` 
ADD COLUMN `opportunity_id` INT(11) NULL AFTER `our_quantified`;

ALTER TABLE `strategies`.`summary` 
ADD COLUMN `strategy` INT(11) NULL AFTER `opportunity_id`;