ALTER TABLE `strategies`.`activity` 
ADD COLUMN `tactics_id` INT NULL AFTER `activity_end_time`;

UPDATE `strategies`.`account_industry_pl` SET `val`='农、林、牧、渔业' WHERE `id`='1';
UPDATE `strategies`.`account_industry_pl` SET `val`='采矿业' WHERE `id`='2';
UPDATE `strategies`.`account_industry_pl` SET `val`='制造业' WHERE `id`='3';
UPDATE `strategies`.`account_industry_pl` SET `val`='电力、燃气及水的生产和供应' WHERE `id`='4';
UPDATE `strategies`.`account_industry_pl` SET `val`='建筑业' WHERE `id`='5';
UPDATE `strategies`.`account_industry_pl` SET `val`='交通运输、仓储和邮政' WHERE `id`='6';
UPDATE `strategies`.`account_industry_pl` SET `val`='信息传输、计算机服务和软件业' WHERE `id`='7';
UPDATE `strategies`.`account_industry_pl` SET `val`='批发和零售' WHERE `id`='8';
UPDATE `strategies`.`account_industry_pl` SET `val`='住宿和餐饮业' WHERE `id`='9';
UPDATE `strategies`.`account_industry_pl` SET `val`='金融业' WHERE `id`='10';
UPDATE `strategies`.`account_industry_pl` SET `val`='房地产业' WHERE `id`='11';
UPDATE `strategies`.`account_industry_pl` SET `val`='租赁和商务服务' WHERE `id`='12';
UPDATE `strategies`.`account_industry_pl` SET `val`='科学研究、技术服务和地质勘查' WHERE `id`='13';
UPDATE `strategies`.`account_industry_pl` SET `val`='水利、环境和公共设施管理' WHERE `id`='14';
UPDATE `strategies`.`account_industry_pl` SET `val`='居民服务和其他服务' WHERE `id`='15';
UPDATE `strategies`.`account_industry_pl` SET `val`='教育' WHERE `id`='16';
UPDATE `strategies`.`account_industry_pl` SET `val`='卫生、社会保障和社会福利' WHERE `id`='17';
UPDATE `strategies`.`account_industry_pl` SET `val`='文化、体育和娱乐业' WHERE `id`='18';
UPDATE `strategies`.`account_industry_pl` SET `val`='公共管理和社会组织' WHERE `id`='19';
UPDATE `strategies`.`account_industry_pl` SET `val`='国际组织' WHERE `id`='20';

ALTER TABLE `strategies`.`tactics` 
DROP COLUMN `individual_met`;

ALTER TABLE `strategies`.`contact` 
CHANGE COLUMN `telephone` `address` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL ;
