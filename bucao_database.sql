DROP DATABASE  IF EXISTS bucao_database;
create database IF NOT EXISTS bucao_database;

use bucao_database;

-- 0.section:医院部门表
CREATE TABLE section(
id VARCHAR(20) NOT NULL primary KEY COMMENT'部门id',
na VARCHAR(20) NOT NULL COMMENT'部门名称',
CONSTRAINT S_NA UNIQUE(NA)
);
INSERT INTO SECTION VALUES
('section1','住院部'),
('section2','急诊部'),
('section3','发热门诊'),
('section4','手术部'),
('section5','普通门诊');

-- 1.rfid_kinds:RFID标签分类表
CREATE TABLE rfid_kinds(
RFNO VARCHAR(20) NOT NULL PRIMARY KEY COMMENT'布草类型编号',
kind VARCHAR(20)  NOT NULL  COMMENT'布草类型',
stock int NOT NULL check(stock>=0) COMMENT'库存',
section varchar(20) not null   COMMENT'所属部门',  -- 所属部门
note varchar(20)   COMMENT'描述'
);
insert into rfid_kinds values
('Aclothes','病号服',4334,"住院部","衣服"),
('Atrousers','病号服',4334,"住院部","裤子"),
('Bsheet','床上用品',4334,"住院部","床单"),
('Bcover','床上用品',4334,"住院部","被罩"),
('Bpillow','床上用品',4334,"住院部","枕头"),
('BPillow-towel','床上用品',4334,"住院部","枕巾"),
('Bmattress','床上用品',4334,"住院部","被褥"),
('CMedical','医疗服',4334,"急诊","枕头"),
('Gauze','纱布',4334,"住院部",null),
('Isolation-linen','隔离类',4334,"发热门诊",null),
('Surgical-suit','手术服',4334,"手术部",null),
('Nurse-Clothes','护士服',4334,"普通门诊","白外套"),
('instrument-shield','医疗仪器护罩',4334,"发热门诊",null);


-- 2.bucao_info:每件布草信息表
CREATE TABLE bucao_info(
RFNO VARCHAR(20) NOT NULL COMMENT'布草类型',
RFID VARCHAR(20) NOT NULL COMMENT'布草ID',
state VARCHAR(20) NOT NULL COMMENT'布草状态',
washtimes int NOT NULL check(washtimes>=0) COMMENT'洗涤次数',
indate date not null COMMENT'入库时间',    -- 入库时间
outdate date COMMENT'出库时间',            -- 报废时间
primary key(RFNO,RFID),
CONSTRAINT B_RFID FOREIGN KEY(rfno) REFERENCES rfid_kinds(RFNO)
);
insert into bucao_info values
('Aclothes','000001','闲置中',12, '2022-01-12',null),
('Aclothes','000002','闲置中',14,'2022-01-12',null),
('Aclothes','000003','使用中',8,'2022-01-12',null),
('Bsheet','000003','未知',8,'2022-01-12',null),
('Aclothes','000004','已回收',21,'2022-12-12','2022-04-03');

-- 3.user_info:病人信息表
CREATE TABLE User_info(
id VARCHAR(20) NOT NULL PRIMARY KEY COMMENT'病人的id',    -- 实体完整性约束
uname varcharacter(20) not null COMMENT'病人姓名',
portrait varcharacter(2048) COMMENT'病人头像',            -- 头像
psd varchar(20) NOT NULL COMMENT'密码',
telephone CHAR(20) COMMENT'电话',
address varchar(30) COMMENT'地址',
sex char(2)  check (sex in('男','女')) COMMENT'性别',
roles varcharacter(15) not null check (roles in('user','manager')) COMMENT'角色'
);
insert into user_info values
('0311','何元梅','https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg','0311','17826103075','贵州毕节','女','user'),
('6666','张怀旭','https://cube.elemecdn.com/6/94/4d3ea53c084bad6931a56d5158a48jpeg.jpeg','6666','17826103075','江苏无锡','男','user');

 -- 4.Manager_info:管理员信息表
 CREATE TABLE Manager_info(
 id VARCHAR(20) NOT NULL PRIMARY KEY COMMENT'管理员id',
 mname varcharacter(20) not null COMMENT'管理员姓名',
 psd varchar(20) COMMENT'管理员密码',
 PRI varcharacter(20) not null COMMENT'权限',                              -- 管理员优先级
 portrait varcharacter(2048) COMMENT'头像',
 telephone VARCHAR(20) COMMENT'电话',
 address varchar(30) COMMENT'地址',
 email  varcharacter(40) COMMENT'邮箱',
 sex char(2) Not null check (sex in('男','女')) COMMENT'性别' -- 部门自定义完整性约束
 );
 insert into manager_info values
 ('0311','何元梅','0311','超级管理员','https://fuss10.elemecdn.com/3/28/bbf893f792f03a54408b3b7a7ebf0jpeg.jpeg','17826103075','贵州毕节','3304534355@qq.com','女'),
 ('0333','吴宇韬','0333','管理员','https://cube.elemecdn.com/6/94/4d3ea53c084bad6931a56d5158a48jpeg.jpeg','17826103075','云南','1033180333@stu.jiangnan.edu.cn','男');

-- 5.room_info: 病房信息表
create table Room_info(
id varchar(20) not null  primary key COMMENT'病房ID',
section varchar(20) not null COMMENT'所属部门',
CONSTRAINT R_SECTION FOREIGN KEY(SECTION) REFERENCES SECTION(NA)
);
insert into room_info values
('14-0318','住院部');

-- 6.bucao_user:病人与布草绑定
CREATE TABLE bucao_user(
RFNO varcharacter(20) NOT NULL  COMMENT'布草类型',
rfid VARCHAR(20) NOT NULL COMMENT'布草RFID',  -- 参照完整性约束
user_id varchar(20) NOT NULL COMMENT'用户ID',
user_name varchar(20) NOT NULL COMMENT'用户姓名',
room_id varcharacter(20) not null COMMENT'所在病房号',
constraint rfid_userid primary key(RFNO,rfid,user_id),
CONSTRAINT B_RFNO FOREIGN KEY(RFNO,RFID) references BUCAO_INFO(RFNO,RFID),
CONSTRAINT B_userID FOREIGN KEY(USER_ID) references USER_INFO(ID),
CONSTRAINT B_ROOMID FOREIGN KEY(ROOM_ID) references ROOM_INFO(ID)
);
insert into BUCAO_USER values
('Aclothes','000003','0311','何元梅','14-0318');

-- 7.bucao_room:布草与房间绑定
create table bucao_room(
rfno varchar(20) not null COMMENT'布草类型',
rfid VARCHAR(20) NOT NULL COMMENT'布草RFID',
room_id varchar(20) NOT NULL COMMENT'病房编号',
room_section varchar(20) not null COMMENT'病房所属部门',
bucao_section varchar(20) not null COMMENT'布草所属部门',

constraint br_RFNO foreign key(rfno,RFID) references bucao_info(rfno,RFID),
constraint BR_ID foreign key(room_id) references room_info(id),
constraint rfid_roomid primary key(rfno,rfid,room_id)          -- 实体完整性约束
);

insert into bucao_room values
('Aclothes','000001','14-0318','住院部','住院部'),
('Aclothes','000002','14-0318','住院部','住院部');
-- user_room表
CREATE TABLE USER_ROOM(
USERID VARCHARACTER(20) NOT NULL COMMENT '用户id',
ROOMID VARCHARACTER(20) NOT NULL COMMENT'病房号',
come_time date not null  COMMENT'入院时间',
out_time date  COMMENT'出院时间',
expenses numeric(12,2) check(expenses>=0) COMMENT'应缴费用',
CONSTRAINT PRIMARY KEY(USERID,ROOMID,come_time),
CONSTRAINT U_ID FOREIGN KEY(USERID) REFERENCES USER_INFO(ID),
CONSTRAINT R_ID FOREIGN KEY(ROOMID) REFERENCES ROOM_INFO(ID)
);
INSERT INTO USER_ROOM VALUES
('0311','14-0318','2022-01-12',null,0.00);

-- order表
CREATE TABLE Order_info(
orderNo varcharacter(30) not null comment '订单编号',
USER_ID VARCHARACTER(20) NOT NULL COMMENT '用户id',
ROOM_ID VARCHARACTER(20) NOT NULL COMMENT'病房号',
createtime datetime not null COMMENT'创建时间',
`subject` varcharacter(20)  COMMENT'订单名称',
expenses numeric(12,2) check(expenses>=0) COMMENT'应缴费用',
payTime datetime COMMENT '缴费时间',
state varcharacter(5) not null,
CONSTRAINT U_R FOREIGN KEY(USER_ID,ROOM_ID) REFERENCES USER_ROOM(USERID,ROOMID),
CONSTRAINT PRIMARY KEY(orderNo)
);
INSERT INTO Order_info VALUES('202204152148591560810311','0311','14-0318','2022-04-12','医疗费',213.00,'2022-04-12','已支付');



