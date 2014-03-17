ALTER TABLE `strategies`.`opportunity` 
ADD COLUMN `success_probability` INT NULL AFTER `modifier_date`;

CREATE TABLE `strategies`.`opportunity_probability_pl` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `val` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));
