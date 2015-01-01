CREATE TABLE `pledge_recruit` (
  `clan_id` int(10) NOT NULL,
  `karma` tinyint(1) NOT NULL,
  `information` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `detailed_information` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`clan_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;