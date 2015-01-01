CREATE TABLE `pledge_applicant` (
  `charId` int(10) NOT NULL,
  `clanId` int(10) NOT NULL,
  `karma` tinyint(1) NOT NULL,
  `message` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`charId`,`clanId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;