DROP TABLE IF EXISTS `world_statistic_result_monthly`;

CREATE TABLE `world_statistic_result_monthly` (
`categoryId` int(10) unsigned NOT NULL DEFAULT '0',
`subCategoryId` int(10) unsigned NOT NULL DEFAULT '0',
`place` int(10) unsigned NOT NULL DEFAULT '0',
`charId` int(10) unsigned NOT NULL DEFAULT '0',
`char_name` varchar(35) NOT NULL,
`statValue` bigint(20) unsigned NOT NULL DEFAULT '0',
PRIMARY KEY (`categoryId`,`subCategoryId`,`charId`),
KEY `Categories` (`categoryId`,`subCategoryId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;