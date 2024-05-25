# 字典重构
ALTER TABLE `money_pos`.`sys_dict` 
CHANGE COLUMN `name` `dict_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典名称' AFTER `id`,
CHANGE COLUMN `description` `dict_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '字典描述' AFTER `dict_name`,
ADD UNIQUE INDEX `uk_dict_name`(`dict_name`) COMMENT '字典名称唯一';

ALTER TABLE `money_pos`.`sys_dict_detail` 
CHANGE COLUMN `label` `cn_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '中文描述' AFTER `value`,
MODIFY COLUMN `dict` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '所属字典' AFTER `id`,
ADD COLUMN `en_desc` varchar(255) NOT NULL DEFAULT '' COMMENT '英文描述' AFTER `cn_desc`;