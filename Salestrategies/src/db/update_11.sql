ALTER TABLE `strategies`.`opportunitycontactteam` 
ADD COLUMN `relat_status` INT(11) NULL AFTER `influence_to`,
ADD COLUMN `time_spent` INT(11) NULL AFTER `relat_status`;

CREATE TABLE `strategies`.`time_spent_pl` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `val` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

ALTER TABLE `strategies`.`buying_style` 
ADD COLUMN `name` VARCHAR(45) NULL AFTER `val`;

ALTER TABLE `strategies`.`descision` 
ADD COLUMN `name` VARCHAR(45) NULL AFTER `val`;

ALTER TABLE `strategies`.`relation_status` 
ADD COLUMN `name` VARCHAR(45) NULL AFTER `val`;

ALTER TABLE `strategies`.`time_spent_pl` 
ADD COLUMN `name` VARCHAR(45) NULL AFTER `val`;

UPDATE `strategies`.`descision` SET `name`='A' WHERE `id`='1';
UPDATE `strategies`.`descision` SET `name`='C' WHERE `id`='2';
UPDATE `strategies`.`descision` SET `name`='D' WHERE `id`='3';
UPDATE `strategies`.`descision` SET `name`='E' WHERE `id`='4';
UPDATE `strategies`.`descision` SET `name`='U' WHERE `id`='5';

UPDATE `strategies`.`buying_style` SET `name`='V' WHERE `id`='1';
UPDATE `strategies`.`buying_style` SET `name`='R' WHERE `id`='2';
UPDATE `strategies`.`buying_style` SET `name`='F' WHERE `id`='3';
UPDATE `strategies`.`buying_style` SET `name`='P' WHERE `id`='4';
UPDATE `strategies`.`buying_style` SET `name`='T' WHERE `id`='5';

INSERT INTO `strategies`.`time_spent_pl` (`id`, `val`, `name`) VALUES ('1', 'Have Not Met', '0');
INSERT INTO `strategies`.`time_spent_pl` (`id`, `val`, `name`) VALUES ('2', 'Introduction', '1');
INSERT INTO `strategies`.`time_spent_pl` (`id`, `val`, `name`) VALUES ('3', 'One-on-One', '2');
INSERT INTO `strategies`.`time_spent_pl` (`id`, `val`, `name`) VALUES ('4', 'Quality Time', '3');

INSERT INTO `strategies`.`relation_status` (`id`, `val`, `name`) VALUES ('1', 'Mentor', '★');
INSERT INTO `strategies`.`relation_status` (`id`, `val`, `name`) VALUES ('2', 'Supporter', '＋');
INSERT INTO `strategies`.`relation_status` (`id`, `val`, `name`) VALUES ('3', 'Neutral', '＝');
INSERT INTO `strategies`.`relation_status` (`id`, `val`, `name`) VALUES ('4', 'Non-supporter', '－');
INSERT INTO `strategies`.`relation_status` (`id`, `val`, `name`) VALUES ('5', 'Enemy', 'X');

ALTER TABLE `strategies`.`opportunitycontactteam` 
ADD COLUMN `report_to` INT(11) NULL AFTER `time_spent`;


