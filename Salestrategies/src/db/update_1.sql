CREATE TABLE `strategies`.`opportunity_stage_pl` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `val` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

INSERT INTO `strategies`.`opportunity_stage_pl` (`id`, `val`) VALUES ('1', 'Needs Analysis');
INSERT INTO `strategies`.`opportunity_stage_pl` (`id`, `val`) VALUES ('2', 'Value Proposition');
INSERT INTO `strategies`.`opportunity_stage_pl` (`id`, `val`) VALUES ('3', 'Id.Decision Markers');
INSERT INTO `strategies`.`opportunity_stage_pl` (`id`, `val`) VALUES ('4', 'Perception analysis');
INSERT INTO `strategies`.`opportunity_stage_pl` (`id`, `val`) VALUES ('5', 'Proposal/price Quote');
INSERT INTO `strategies`.`opportunity_stage_pl` (`id`, `val`) VALUES ('6', 'Negotiation/Review');
INSERT INTO `strategies`.`opportunity_stage_pl` (`id`, `val`) VALUES ('7', 'Closed Won');
INSERT INTO `strategies`.`opportunity_stage_pl` (`id`, `val`) VALUES ('8', 'Closed Lost');

CREATE TABLE `strategies`.`account_type_pl` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `val` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

INSERT INTO `strategies`.`account_type_pl` (`id`, `val`) VALUES ('1', 'A');
INSERT INTO `strategies`.`account_type_pl` (`id`, `val`) VALUES ('2', 'B');
INSERT INTO `strategies`.`account_type_pl` (`id`, `val`) VALUES ('3', 'C');
INSERT INTO `strategies`.`account_type_pl` (`id`, `val`) VALUES ('4', 'VIP');
INSERT INTO `strategies`.`account_type_pl` (`id`, `val`) VALUES ('5', '非重点客户');

CREATE TABLE `strategies`.`account_industry_pl` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `val` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

INSERT INTO `strategies`.`account_industry_pl` (`id`, `val`) VALUES ('1', 'A农、林、牧、渔业');
INSERT INTO `strategies`.`account_industry_pl` (`id`, `val`) VALUES ('2', 'B采矿业');
INSERT INTO `strategies`.`account_industry_pl` (`id`, `val`) VALUES ('3', 'C制造业');
INSERT INTO `strategies`.`account_industry_pl` (`id`, `val`) VALUES ('4', 'D电力、燃气及水的生产和供应');
INSERT INTO `strategies`.`account_industry_pl` (`id`, `val`) VALUES ('5', 'E建筑业');
INSERT INTO `strategies`.`account_industry_pl` (`id`, `val`) VALUES ('6', 'F交通运输、仓储和邮政');
INSERT INTO `strategies`.`account_industry_pl` (`id`, `val`) VALUES ('7', 'G信息传输、计算机服务和软件业');
INSERT INTO `strategies`.`account_industry_pl` (`id`, `val`) VALUES ('8', 'H批发和零售');
INSERT INTO `strategies`.`account_industry_pl` (`id`, `val`) VALUES ('9', 'I住宿和餐饮业');
INSERT INTO `strategies`.`account_industry_pl` (`id`, `val`) VALUES ('10', 'J金融业');
INSERT INTO `strategies`.`account_industry_pl` (`id`, `val`) VALUES ('11', 'K房地产业');
INSERT INTO `strategies`.`account_industry_pl` (`id`, `val`) VALUES ('12', 'L租赁和商务服务');
INSERT INTO `strategies`.`account_industry_pl` (`id`, `val`) VALUES ('13', 'M科学研究、技术服务和地质勘查');
INSERT INTO `strategies`.`account_industry_pl` (`id`, `val`) VALUES ('14', 'N水利、环境和公共设施管理');
INSERT INTO `strategies`.`account_industry_pl` (`id`, `val`) VALUES ('15', 'O居民服务和其他服务');
INSERT INTO `strategies`.`account_industry_pl` (`id`, `val`) VALUES ('16', 'P教育');
INSERT INTO `strategies`.`account_industry_pl` (`id`, `val`) VALUES ('17', 'Q卫生、社会保障和社会福利');
INSERT INTO `strategies`.`account_industry_pl` (`id`, `val`) VALUES ('18', 'R文化、体育和娱乐业');
INSERT INTO `strategies`.`account_industry_pl` (`id`, `val`) VALUES ('19', 'S公共管理和社会组织');
INSERT INTO `strategies`.`account_industry_pl` (`id`, `val`) VALUES ('20', 'T国际组织');

CREATE TABLE `strategies`.`status_pl` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `val` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

INSERT INTO `strategies`.`status_pl` (`id`, `val`) VALUES ('1', '有效');
INSERT INTO `strategies`.`status_pl` (`id`, `val`) VALUES ('2', '无效');

CREATE TABLE `strategies`.`team_role_pl` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `val` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

INSERT INTO `strategies`.`team_role_pl` (`id`, `val`) VALUES ('1', '项目经理');
INSERT INTO `strategies`.`team_role_pl` (`id`, `val`) VALUES ('2', '售前顾问');
INSERT INTO `strategies`.`team_role_pl` (`id`, `val`) VALUES ('3', '销售代表');
INSERT INTO `strategies`.`team_role_pl` (`id`, `val`) VALUES ('4', '销售经理');
INSERT INTO `strategies`.`team_role_pl` (`id`, `val`) VALUES ('5', '实施');
INSERT INTO `strategies`.`team_role_pl` (`id`, `val`) VALUES ('6', '顾问');

CREATE TABLE `strategies`.`team_permission_pl` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `val` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

INSERT INTO `strategies`.`team_permission_pl` (`id`, `val`) VALUES ('1', '只读');
INSERT INTO `strategies`.`team_permission_pl` (`id`, `val`) VALUES ('2', '可编辑');
INSERT INTO `strategies`.`team_permission_pl` (`id`, `val`) VALUES ('3', '删除');
INSERT INTO `strategies`.`team_permission_pl` (`id`, `val`) VALUES ('4', '负责人');


CREATE TABLE `strategies`.`contact_department_pl` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `val` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

INSERT INTO `strategies`.`contact_department_pl` (`id`, `val`) VALUES ('1', '行政部');
INSERT INTO `strategies`.`contact_department_pl` (`id`, `val`) VALUES ('2', '财务部');
INSERT INTO `strategies`.`contact_department_pl` (`id`, `val`) VALUES ('3', '质量管理部');
INSERT INTO `strategies`.`contact_department_pl` (`id`, `val`) VALUES ('4', '营销部');
INSERT INTO `strategies`.`contact_department_pl` (`id`, `val`) VALUES ('5', '运营部');
INSERT INTO `strategies`.`contact_department_pl` (`id`, `val`) VALUES ('6', '技术部');
INSERT INTO `strategies`.`contact_department_pl` (`id`, `val`) VALUES ('7', '维修部门');
INSERT INTO `strategies`.`contact_department_pl` (`id`, `val`) VALUES ('8', '人力资源部');
INSERT INTO `strategies`.`contact_department_pl` (`id`, `val`) VALUES ('9', '客户服务部');
INSERT INTO `strategies`.`contact_department_pl` (`id`, `val`) VALUES ('10', '销售部');
INSERT INTO `strategies`.`contact_department_pl` (`id`, `val`) VALUES ('11', '董事会');

CREATE TABLE `strategies`.`job_title_pl` (
  `id` INT(11) NOT NULL,
  `val` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

INSERT INTO `strategies`.`job_title_pl` (`id`, `val`) VALUES ('1', 'CEO');
INSERT INTO `strategies`.`job_title_pl` (`id`, `val`) VALUES ('2', 'CTO');
INSERT INTO `strategies`.`job_title_pl` (`id`, `val`) VALUES ('3', 'CIO');
INSERT INTO `strategies`.`job_title_pl` (`id`, `val`) VALUES ('4', 'CFO');
INSERT INTO `strategies`.`job_title_pl` (`id`, `val`) VALUES ('5', 'COO');
INSERT INTO `strategies`.`job_title_pl` (`id`, `val`) VALUES ('6', 'VP');
INSERT INTO `strategies`.`job_title_pl` (`id`, `val`) VALUES ('7', 'SVP');
INSERT INTO `strategies`.`job_title_pl` (`id`, `val`) VALUES ('8', 'SM');
INSERT INTO `strategies`.`job_title_pl` (`id`, `val`) VALUES ('9', 'MM');
INSERT INTO `strategies`.`job_title_pl` (`id`, `val`) VALUES ('10', 'QA');
INSERT INTO `strategies`.`job_title_pl` (`id`, `val`) VALUES ('11', 'QC');
INSERT INTO `strategies`.`job_title_pl` (`id`, `val`) VALUES ('12', 'SR');

CREATE TABLE `strategies`.`gender_pl` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `val` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

INSERT INTO `strategies`.`gender_pl` (`id`, `val`) VALUES ('1', '男');
INSERT INTO `strategies`.`gender_pl` (`id`, `val`) VALUES ('2', '女');



