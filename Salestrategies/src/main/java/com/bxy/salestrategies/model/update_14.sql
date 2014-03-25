ALTER TABLE `strategies`.`opportunitycontactteam` 
ADD COLUMN `ranklevel` INT(11) NULL AFTER `report_to`;

CREATE TABLE `strategies`.`ranklevel_pl` (
  `id` INT(11) NOT NULL,
  `val` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));
  
INSERT INTO `strategies`.`ranklevel_pl` (`id`, `val`) VALUES ('0', '一级');
INSERT INTO `strategies`.`ranklevel_pl` (`id`, `val`) VALUES ('1', '二级');
INSERT INTO `strategies`.`ranklevel_pl` (`id`, `val`) VALUES ('2', '三级');
INSERT INTO `strategies`.`ranklevel_pl` (`id`, `val`) VALUES ('3', '四级');
INSERT INTO `strategies`.`ranklevel_pl` (`id`, `val`) VALUES ('4', '五级');
INSERT INTO `strategies`.`ranklevel_pl` (`id`, `val`) VALUES ('5', '六级');
INSERT INTO `strategies`.`ranklevel_pl` (`id`, `val`) VALUES ('6', '七级');
INSERT INTO `strategies`.`ranklevel_pl` (`id`, `val`) VALUES ('7', '八级');
INSERT INTO `strategies`.`ranklevel_pl` (`id`, `val`) VALUES ('8', '九级');
INSERT INTO `strategies`.`ranklevel_pl` (`id`, `val`) VALUES ('9', '十级');