DROP DATABASE  IF EXISTS bucao_database;
create database IF NOT EXISTS bucao_database;

use bucao_database;


-- rfid_kinds:RFID标签分类表
CREATE TABLE rfid_kinds(
RFNO VARCHAR(4) NOT NULL PRIMARY KEY,
kind VARCHAR(20) NOT NULL,
stock int NOT NULL check(stock>=0),
section varchar(15) not null,  -- 所属部门
note varchar(20) not null
);
insert into rfid_kinds values
('Aclothes','病号服',4334,"住院部","衣服"),
('Atrousers','病号服',4334,"住院部","裤子"),
('Bsheet','床上用品',4334,"住院部","床单"),
('Bcover','床上用品',4334,"住院部","被罩"),
('Bpillow','床上用品',4334,"住院部","枕头");

-- bucao_info:每件布草信息表
CREATE TABLE bucao_info(
-- RFNO VARCHAR(4) NOT NULL references reid_kinds(RFNO),
RFID VARCHAR(10) NOT NULL primary KEY,
state VARCHAR(20) NOT NULL,
wash_times int NOT NULL check(wash_time>=0),
-- constraint bucao_rf primary key(RFNO,RFID)
);
insert into bucao_info values
('Aclothes000001','闲置',12),
('Aclothes000002','闲置',14),
('Aclothes000003','使用',8),
('Aclothes000004','回收',21);

-- user_info:病人信息表
CREATE TABLE User_info(
id VARCHAR(20) NOT NULL PRIMARY KEY,    -- 实体完整性约束
psd varchar(15) NOT NULL,
telephone CHAR(11),
address varchar(30) ,
sex char(2) Not null check (sex in('男','女')),
days int check(days>=0), -- 住院天数
expenses numeric(3,0) check(expenses>=0) -- 应缴费用
);
insert into user_info values
('0311','0311','17826103075','贵州毕节','女',0,0.00);

-- Manager_info:管理员信息表
CREATE TABLE Manager_info(
id VARCHAR(20) NOT NULL PRIMARY KEY,
psd varchar(15) NOT NULL,
telephone VARCHAR(20),
address varchar(30) ,
sex char(2) Not null check (sex in('男','女'))  -- 用户自定义完整性约束
);
insert into manager_info values
('0311','0311','17826103075','贵州毕节','女');

-- room_info: 病房信息表
create table room_info(
room_id varchar(15) not null  primary key,
section varchar(15) not null
);
insert into room_info values
('14-0318','住院部');

-- bucao_user:病人与布草绑定
CREATE TABLE bucao_user(
rfid VARCHAR(20) NOT NULL references bucao_info(rfid),  -- 参照完整性约束
user_id varchar(20) NOT NULL references user_info(id),
constraint rfid_userid primary key(rfid,userid)
);
insert into BUCAO_USER values
('Aclothes000001','0311');

-- bucao_room:布草与房间绑定
create table bucao_room(
rfid VARCHAR(20) NOT NULL references bucao_info(rfid),
room_id varchar(20) NOT NULL references room_info(id),
num int check(num>=0),                                    -- 数量
constraint rfid_roomid primary key(rfid,room_id)          -- 实体完整性约束
);
insert into bucao_room values
('Aclothes000001','14-0318',2);


