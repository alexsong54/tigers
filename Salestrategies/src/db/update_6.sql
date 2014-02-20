ALTER TABLE `strategies`.`countactuserteam` 
RENAME TO  `strategies`.`contactuserteam` ;
ALTER TABLE `strategies`.`activity` 
CHANGE COLUMN `countact_id` `contact_id` INT(11) NULL DEFAULT NULL ;
