
CREATE TABLE `strategies`.`opportunity_probability_pl` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `val` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));
INSERT INTO `strategies`.`opportunity_probability_pl` (`val`) VALUES ('10%');
INSERT INTO `strategies`.`opportunity_probability_pl` (`val`) VALUES ('20%');
INSERT INTO `strategies`.`opportunity_probability_pl` (`val`) VALUES ('30%');
INSERT INTO `strategies`.`opportunity_probability_pl` (`val`) VALUES ('50%');
INSERT INTO `strategies`.`opportunity_probability_pl` (`val`) VALUES ('70%');
INSERT INTO `strategies`.`opportunity_probability_pl` (`val`) VALUES ('90%');
INSERT INTO `strategies`.`opportunity_probability_pl` (`val`) VALUES ('100%');
INSERT INTO `strategies`.`opportunity_probability_pl` (`val`) VALUES ('0%');

ALTER TABLE `strategies`.`contact` 
ADD COLUMN `hometown` INT NULL AFTER `modifier_date`,
ADD COLUMN `nation` INT NULL AFTER `hometown`,
ADD COLUMN `education` INT NULL AFTER `nation`,
ADD COLUMN `language` INT NULL AFTER `education`,
ADD COLUMN `age` INT NULL AFTER `language`,
ADD COLUMN `school` VARCHAR(45) NULL AFTER `age`;

CREATE TABLE `strategies`.`hometown_pl` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `val` VARCHAR(45) NULL,
  `externalid` INT NULL,
  PRIMARY KEY (`id`));
  INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('1', '北京', '110000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('2', '天津', '120000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('3', '河北', '130000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('4', '山西', '140000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('5', '内蒙古', '150000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('6', '辽宁', '210000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('7', '吉林', '220000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('8', '黑龙江', '230000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('9', '上海', '310000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('10', '江苏', '320000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('11', '浙江', '330000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('12', '安徽', '340000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('13', '福建', '350000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('14', '江西', '360000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('15', '山东', '370000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('16', '河南', '410000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('17', '湖北', '420000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('18', '湖南', '430000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('19', '广东', '440000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('20', '广西', '450000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('21', '海南', '460000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('22', '重庆', '500000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('23', '四川', '510000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('24', '贵州', '520000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('25', '云南', '530000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('26', '西藏', '540000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('27', '陕西', '610000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('28', '甘肃', '620000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('29', '青海', '630000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('30', '宁夏', '640000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('31', '新疆', '650000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('32', '台湾', '710000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('33', '香港', '810000');
INSERT INTO `strategies`.`hometown_pl` (`id`, `val`, `externalid`) VALUES ('34', '澳门', '820000');

CREATE TABLE `strategies`.`nation` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `val` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));
  
  INSERT INTO `strategies`.`nation` (`val`) VALUES ('汉族 ');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('蒙古族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('回族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('藏族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('维吾尔族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('苗族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('彝族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('壮族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('布依族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('朝鲜族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('满族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('侗族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('瑶族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('白族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('土家族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('哈尼族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('哈萨克族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('傣族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('黎族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('僳僳族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('佤族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('畲族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('高山族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('拉祜族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('水族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('东乡族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('纳西族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('景颇族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('柯尔克孜族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('土族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('达斡尔族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('仫佬族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('羌族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('布朗族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('撒拉族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('毛南族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('仡佬族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('锡伯族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('阿昌族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('普米族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('塔吉克族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('怒族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('乌孜别克族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('俄罗斯族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('鄂温克族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('德昂族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('保安族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('裕固族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('京族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('塔塔尔族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('独龙族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('鄂伦春族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('赫哲族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('门巴族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('珞巴族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('基诺族');
INSERT INTO `strategies`.`nation` (`val`) VALUES ('其他');


CREATE TABLE `strategies`.`education_pl` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `val` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));

INSERT INTO `strategies`.`education_pl` (`val`) VALUES ('小学');
INSERT INTO `strategies`.`education_pl` (`val`) VALUES ('初中');
INSERT INTO `strategies`.`education_pl` (`val`) VALUES ('中专/高中');
INSERT INTO `strategies`.`education_pl` (`val`) VALUES ('大专');
INSERT INTO `strategies`.`education_pl` (`val`) VALUES ('本科');
INSERT INTO `strategies`.`education_pl` (`val`) VALUES ('硕士研究生');
INSERT INTO `strategies`.`education_pl` (`val`) VALUES ('博士研究生');
INSERT INTO `strategies`.`education_pl` (`val`) VALUES ('其他');

CREATE TABLE `strategies`.`language_pl` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `val` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));
  
   INSERT INTO `strategies`.`language_pl` (`val`) VALUES ('普通话');
INSERT INTO `strategies`.`language_pl` (`val`) VALUES ('广东话');
INSERT INTO `strategies`.`language_pl` (`val`) VALUES ('江浙话');
INSERT INTO `strategies`.`language_pl` (`val`) VALUES ('闽南话');
INSERT INTO `strategies`.`language_pl` (`val`) VALUES ('湖南话');
INSERT INTO `strategies`.`language_pl` (`val`) VALUES ('江西话');
INSERT INTO `strategies`.`language_pl` (`val`) VALUES ('客家话');
INSERT INTO `strategies`.`language_pl` (`val`) VALUES ('英语');
INSERT INTO `strategies`.`language_pl` (`val`) VALUES ('法语');
INSERT INTO `strategies`.`language_pl` (`val`) VALUES ('俄语');
INSERT INTO `strategies`.`language_pl` (`val`) VALUES ('葡萄牙语');
INSERT INTO `strategies`.`language_pl` (`val`) VALUES ('西班牙语');
INSERT INTO `strategies`.`language_pl` (`val`) VALUES ('德语');
INSERT INTO `strategies`.`language_pl` (`val`) VALUES ('阿拉伯语');

ALTER TABLE `strategies`.`opportunityuserteam` 
CHANGE COLUMN `created_date` `create_date` DATETIME NULL DEFAULT NULL ;
