DROP DATABASE  IF EXISTS bucao_database;
create database IF NOT EXISTS bucao_database;

use bucao_database;

-- bucao_info:每件布草信息表
CREATE TABLE bucao_info(
RFID VARCHAR(4) NOT NULL PRIMARY KEY,
state VARCHAR(20) NOT NULL,
wash_times int NOT NULL
);
-- rfid_kinds:RFID标签分类表
CREATE TABLE rfid_kinds(
RFNO VARCHAR(4) NOT NULL PRIMARY KEY,
kind VARCHAR(20) NOT NULL,
stock int NOT NULL
);

-- usr_info:病人信息表
CREATE TABLE User_info(
id VARCHAR(4) NOT NULL PRIMARY KEY,
tetephon VARCHAR(20) unique NOT NULL,
addr varchar(30) not NULL,
days int , -- 住院天数
expenses decimal(2)    -- 应缴费用
);

