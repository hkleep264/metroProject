CREATE DATABASE `lhkdb` IF NOT EXISTS lhkdb;

USE lhkdb;

CREATE TABLE `member` (
  `no` int(11) NOT NULL AUTO_INCREMENT COMMENT 'no',
  `name` varchar(20) NOT NULL COMMENT '이름',
  `id` varchar(80) NOT NULL COMMENT 'ID',
  `pw` varchar(256) NOT NULL COMMENT '비밀번호',
  `reg_date` datetime DEFAULT current_timestamp() COMMENT '생성일',
  PRIMARY KEY (`no`),
  UNIQUE KEY `user_id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `metro` (
  `line_no` varchar(20) NOT NULL COMMENT '호선',
  `station_cd` varchar(20) NOT NULL COMMENT '전철역 코드',
  `station_nm_eng` varchar(160) NOT NULL COMMENT '지하철 역이름(영어)',
  `station_nm_ko` varchar(160) NOT NULL COMMENT '지하철 역이름(한글)',
  `fr_code` varchar(10) NOT NULL COMMENT '외부코드',
  `x` varchar(256) DEFAULT NULL COMMENT 'x',
  `y` varchar(256) DEFAULT NULL COMMENT 'x'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


