use RentDB;
go
drop table if exists admin_table;
go
drop table if exists photos;
go
drop table if exists complaints;
go
drop table if exists furniture_table;
go
drop table if exists booking_table;
go
drop table if exists Messages;
go
drop table if exists review_table;
go
drop table if exists cart_items;
go
drop table if exists cart_table;
go
drop table if exists ads_table;
go
drop table if exists adtype_table;
go
drop table if exists collect_table;
go
drop table if exists orders_table;
go
drop table if exists condition_table;
go
drop table if exists houseBookingTimeSlot_table;
go
drop table if exists image_table;
go
drop table if exists house_table;
go
drop table if exists user_table;
go
/*drop database if exists RentDB;
go
create database RentDB;
go
use RentDB;
go*/

-- Admins 資料表
CREATE TABLE admin_table (
    adminId INT PRIMARY KEY IDENTITY(1,1),
    adminName NVARCHAR(20) NOT NULL,
    adminEmail NVARCHAR(50) NOT NULL UNIQUE,
    adminPassword NVARCHAR(MAX) NOT NULL,
    adminPhone VARCHAR(20),
	role varchar(20)
);

ALTER TABLE admin_table ALTER COLUMN adminPassword NVARCHAR(MAX);

-- 插入三筆隨機管理者資料
INSERT INTO admin_table (adminName, adminEmail, adminPassword, adminPhone,role) 
VALUES 
('香蕉', 'banana@apple.com', '123', '0954716111', 'SAdmin'),
('張小明', 'xiaoming.zhang@example.com', 'password123', '09541579101','RAdmin-user'),
('李大華', 'dahua.li@example.com', 'securepass456', '0954714181','RAdmin-book'),
('王美麗', 'meili.wang@example.com', 'admin789', '0954796311','RAdmin-complaint');


--使用者資料表
create table user_table(
	user_id bigint primary key identity(1,1),
	name nvarchar(50) NOT NULL,
	email nvarchar(50) UNIQUE NOT NULL,
	password NVARCHAR(max) NOT NULL,
	phone varchar(20),
	picture varbinary(max),
	createtime datetime DEFAULT GETDATE()  ,
	gender tinyint, -- ( 0--男 , 1--女)
	coupon tinyint DEFAULT 3,
	status tinyint default 1 --modify origin not default
);
go


--data
DECLARE @counter INT = 1;
DECLARE @randValue INT = 1;
WHILE @counter <= 7046
BEGIN
	SET @randValue = CAST((RAND() * 100) AS INT)
    INSERT INTO user_table (name, email, password, phone, picture,createtime, gender , status)
    VALUES (
        CONCAT('User', @counter),
        CONCAT('user', @counter, '@example.com'),
        CONCAT('password12', RIGHT(CAST(@counter AS VARCHAR(3)), 3)),
        CONCAT('098765', RIGHT(CAST(@counter AS VARCHAR(3)), 5)),
        (SELECT * FROM OPENROWSET(BULK 'C:\a.jpg', SINGLE_BLOB) AS ImageData),
		DATEADD(DAY, ABS(CHECKSUM(NEWID())) % DATEDIFF(DAY, '2024-01-01', '2024-11-1')+1,'2024-01-01'),
        CASE WHEN @randValue % 2 = 0 THEN 0 ELSE 1 END,
		CASE WHEN @randValue % 3 = 0 THEN 0 WHEN @randValue % 3 =1 THEN 1 WHEN @randValue % 3 =2 THEN 2 END
    );
    SET @counter = @counter + 1
END;
go

-- 新增官方帳號
update user_table set name='abcd' , email = 'abcd@example.com' , password='123' where user_id = 50;
update user_table set name='abcd' , email = 'aabcd@example.com' , password='123' where user_id = 51;
update user_table set name='Official account' , email='oa@example.com' , password='123' where user_id=52;


-- Messages 資料表
CREATE TABLE Messages (
    id bigint PRIMARY KEY IDENTITY(1,1),  --modify origin int
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    message NVARCHAR(MAX),
    timestamp DATETIME DEFAULT GETDATE(),
	picture nvarchar(max),
    FOREIGN KEY (sender_id) REFERENCES user_table(user_id),
    FOREIGN KEY (receiver_id) REFERENCES user_table(user_id)
);
GO

--data --need modify
DECLARE @counter INT = 1;
DECLARE @randValue INT = 1;
WHILE @counter <= 50
BEGIN

    SET @randValue = CASE WHEN @counter = 50 THEN 1 ELSE CAST(((RAND() * 50) + 1) AS INT) END;
    IF @randValue = @counter
    BEGIN
        SET @randValue = CASE WHEN @counter = 50 THEN 1 ELSE @randValue + 1 END;
    END;

    INSERT INTO Messages (sender_id, receiver_id, message, timestamp)
    VALUES (
        @counter,
        @randValue,
        CONCAT('哈囉~這是來自使用者', @counter, '的訊息'),
        GETDATE()
    );

    SET @counter = @counter + 1;
END;
go


--官方訊息  --modify
DECLARE @counter INT = 1;
DECLARE @randValue INT = 1;
DECLARE @maxUserId INT = 1000;
DECLARE @minUserId INT = 1;
DECLARE @targetUserCount INT = 10; -- 51號要對話的目標人數

-- 選擇10個隨機使用者，不包含51號和52號
DECLARE @selectedUsers TABLE (userId INT);
WHILE (SELECT COUNT(*) FROM @selectedUsers) < @targetUserCount
BEGIN
    SET @randValue = ABS(CHECKSUM(NEWID())) % (@maxUserId - @minUserId) + @minUserId + 1; -- 避免選到51或52
    IF @randValue NOT IN (51, 52) AND NOT EXISTS (SELECT 1 FROM @selectedUsers WHERE userId = @randValue)
    BEGIN
        INSERT INTO @selectedUsers (userId) VALUES (@randValue);
    END
END;

-- 51號與選定的使用者進行對話
DECLARE @currentUserId INT = 51;
WHILE EXISTS (SELECT * FROM @selectedUsers)
BEGIN
    DECLARE @conversationCount INT = 10; -- 51號與每個人對話10次
    WHILE @conversationCount > 0
    BEGIN
        SET @randValue = ABS(CHECKSUM(NEWID())) % @targetUserCount + 1;
        SELECT TOP 1 @randValue = userId FROM @selectedUsers ORDER BY NEWID();

        INSERT INTO Messages (sender_id, receiver_id, message, timestamp)
        VALUES (
            @currentUserId,
            @randValue,
            '這是 51 發送給 ' + CAST(@randValue AS VARCHAR) + ' 的訊息',
            GETDATE()
        );

        SET @conversationCount = @conversationCount - 1;
    END;

    DELETE TOP (1) FROM @selectedUsers;
END;


--房源資料表
create table house_table(
	house_id bigint primary key identity(1,1),
	user_id bigint,
	title nvarchar(300), --房屋名稱
	price bigint,
	description nvarchar(600), --房屋簡介
	size int,
	address nvarchar(200), --地址
	lat Decimal(10,7) default null, --緯度
	lng Decimal(10,7) default null, --經度
	room smallint,
	bathroom smallint,
	livingroom smallint,
	kitchen smallint,
	floor smallint,
	atticAddition bit, --頂樓加蓋
	house_type nvarchar(20), --房屋類型
	status tinyint, -- 0--待審核 1--通過 2--禁止
	clickCount int, --點擊次數
	foreign key (user_id) references user_table(user_id),
);
go

--data
DECLARE @counter INT = 1;
DECLARE @randValue INT =1;
DECLARE @City NVARCHAR(100);
DECLARE @Township NVARCHAR(100);
DECLARE @Street NVARCHAR(100);
WHILE @counter <= 7046
BEGIN
	SET @randValue = CAST((RAND() * 100) AS INT)
	SET @City = '台南市'
	SET @Township=CASE WHEN @randValue %4 = 0 THEN '新市區' WHEN @randValue %4 = 1 THEN '善化區' WHEN @randValue%4=2 THEN '永康區' WHEN @randValue%4=3 THEN '永康區' END
    SET @Street=CONCAT(@counter, '街' , @counter , '號')
	INSERT INTO house_table (user_id,title,price,description,size,address,
	room,bathroom,livingroom,kitchen,floor,atticAddition,house_type,status,clickCount)
    VALUES (
        @counter,
        CONCAT('租屋網@', @counter),
		@randValue * 100,
		'歡迎租屋',
		@counter ,
		CONCAT(@City,@TownShip,@Street),
		CASE WHEN @randValue %4 = 0 THEN 1 WHEN @randValue %4 = 1 THEN 2 WHEN @randValue %4 =2 THEN 3 WHEN @randValue%4 =3 THEN 3 END,
		CASE WHEN @randValue %3 = 0 THEN 3 WHEN @randValue %3 = 1 THEN 2 WHEN @randValue %3 =2 THEN 1 END,
		CASE WHEN @randValue %2 = 0 THEN 1 WHEN @randValue %2 = 1 THEN 2 END,
		CASE WHEN @randValue %2 = 0 THEN 2 WHEN @randValue %2 = 1 THEN 1 END,
		CASE WHEN @randValue %4 = 0 THEN 1 WHEN @randValue %4 = 1 THEN 2 WHEN @randValue %4 =2 THEN 3 WHEN @randValue%4 =3 THEN 4 END,
		CASE WHEN @randValue %3 = 0 THEN 0 WHEN @randValue %3 = 1 THEN 0 WHEN @randValue %3 =2 THEN 1 END,
		CASE WHEN @randValue %4 = 0 THEN '透天' WHEN @randValue %4 = 1 THEN '獨立套房' WHEN @randValue %4 =2 THEN '分租套房' WHEN @randValue%4 =3 THEN '雅房' END,
		CASE WHEN @randValue %4 = 0 THEN 1 WHEN @randValue %4 = 1 THEN 1 WHEN @randValue %4 =2 THEN 0 WHEN @randValue%4 =3 THEN 2 END,
		@randValue
		);

    SET @counter = @counter + 1;
END;
go



--收藏資料表
create table collect_table(
	collect_id bigint primary key,
	user_id bigint,
	house_id bigint,
	collect_time datetime,
	foreign key (user_id) references user_table(user_id),
	foreign key (house_id) references house_table(house_id)
);
go

--data


--照片資料表
create table image_table(
	image_id bigint primary key identity(1,1),
	house_id bigint,
	user_id bigint,
	image_url varbinary(max),
	foreign key (user_id) references user_table(user_id),
	foreign key (house_id) references house_table(house_id)
);
go

--data
DECLARE @counter INT = 1;
WHILE @counter <= 50
BEGIN
    INSERT INTO image_table(house_id,user_id,image_url)
    VALUES (
        @counter,
        @counter,
        (SELECT * FROM OPENROWSET(BULK 'C:\a.jpg', SINGLE_BLOB) AS ImageData)
    );

    SET @counter = @counter + 1
END;
go

--預定資料表
create table booking_table(
	booking_id bigint primary key identity(1,1),
	house_id bigint,
	user_id bigint,
	create_date datetime2(0),
	booking_date date,
	booking_time time,
	status tinyint,
	foreign key (house_id) references house_table(house_id),
	foreign key (user_id) references user_table(user_id)
);
go

--data
INSERT INTO booking_table(house_id,user_id,create_date,booking_date,booking_time,status)
values(5,1,'2024-01-05','2024-01-04 10:00:00','10:00:00','4'),
(7,3,'2024-01-21','2024-01-20 08:00:00','10:00:00','4'),
(11,5,'2024-02-08','2024-02-06 13:00:00','09:00:00','4'),
(19,6,'2024-03-05','2024-03-04 14:00:00','09:00:00','4'),
(21,32,'2024-03-20','2024-03-19 08:00:00','14:00:00','4'),
(33,18,'2024-04-02','2024-04-01 10:00:00','14:00:00','4'),
(34,21,'2024-04-07','2024-04-06 09:00:00','14:30:00','4'),
(36,23,'2024-04-19','2024-04-18 10:30:00','15:00:00','4'),
(38,25,'2024-05-01','2024-05-01 10:00:00','15:00:00','4'),
(40,44,'2024-05-24','2024-05-23 14:00:00','15:00:00','4');
go

--房源預約時段
create table houseBookingTimeSlot_table (
       duration tinyint,
       from_date date,
       from_time time,
       to_date date,
       to_time time,
       week_day varchar(7),
       house_id bigint not null,
       primary key (house_id),
	   foreign key (house_id) references house_table(house_id)
   )

--data
DECLARE @counter INT = 1;
DECLARE @randValue INT =1;
DECLARE @bookdate datetime = '2024-01-01'
WHILE @counter <= 50
BEGIN
	SET @randValue = CAST((RAND() * 100) AS INT)
	SET @bookdate = DATEADD(DAY, ABS(CHECKSUM(NEWID())) % DATEDIFF(DAY, '2024-01-01', '2024-11-1')+1,'2024-01-01')
    INSERT INTO houseBookingTimeSlot_table(duration,from_date,from_time,to_date,to_time,week_day,house_id)
	VALUES(
		CASE WHEN @randValue %4 = 0 THEN 30 WHEN @randValue %4 = 1 THEN 30 WHEN @randValue %4 =2 THEN 30 WHEN @randValue%4 =3 THEN 60 END,
		@bookdate,
		'09:00:00',
		DATEADD(DAY,30,@bookdate),
		'21:00:00',
		'1111111',
		@counter
	)
    SET @counter = @counter + 1
END;
go

--廣告類型資料表
CREATE TABLE adtype_table(
	id int primary key identity(1,1),
	adname nvarchar(20),
	adprice int
)
go

--date
INSERT INTO adtype_table(adname,adprice)
VALUES('30天',300),('60天',550)
go



--廣告資料表
CREATE TABLE ads_table (
	ad_id BIGINT PRIMARY KEY IDENTITY(1,1),
	user_id BIGINT, --是否可以刪掉
    house_id BIGINT NOT NULL,
	adtype_id int, --廣告方案
    ad_price INT NOT NULL,  -- 根據刊登種類不同，價格不同
	is_paid BIT DEFAULT 0, --付款時更新
	order_id nvarchar(20) DEFAULT NULL, --該ad隸屬於哪一張訂單，未付款時為null
	paid_date DATETIME DEFAULT NULL,
	FOREIGN KEY (user_id) references user_table(user_id),
    FOREIGN KEY (house_id) references house_table(house_id),
	FOREIGN KEY (adtype_id) references adtype_table(id)
);
go


--訂單資料表
create table orders_table(
	user_id bigint not null,
	merchant_id nvarchar(10), 
	merchantTradNo nvarchar(20), --流水號
	merchantTradDate datetime, --交易日期
	totalAmount bigint, --總金額
	tradeDesc nvarchar(200), --交易描述
	itemName nvarchar(400), --商品名稱
	order_status tinyint, -- 0--逾期/取消/刪除狀態 1-- 已付款生效 
	returnUrl nvarchar(200),
	choosePayment nvarchar(20)　default 'Credit',　--交易方式
	checkMacValue nvarchar(100),
	foreign key (user_id) references user_table(user_id),
);
go

--data
DECLARE @counter INT = 1;
DECLARE @randValue INT =1;
DECLARE @paid BIT = 0;
DECLARE @randDate DATETIME='2023-01-01';
DECLARE @orderid INT = 0;
DECLARE @adType NVARCHAR(20) = '';
DECLARE @adPrice INT = 0;
WHILE @counter <= 50
BEGIN
	SET @randValue = CAST(((RAND() * 7046)+1) AS INT)
	SET @paid = CASE WHEN @randValue % 3 = 0 THEN 1 ELSE 0 END;
	SET @orderid = CASE WHEN @paid =1 THEN @orderid+1 ELSE @orderid END;
	SET @adPrice = CASE WHEN @randValue % 2 = 0 THEN 10000 ELSE 20000 END;
	SET @adType = CASE WHEN @randValue % 2 = 0 THEN 1 ELSE 2 END;
	SET @randDate=DATEADD(DAY, ABS(CHECKSUM(NEWID())) % DATEDIFF(DAY, '2023-01-01', '2024-09-30')+1,'2023-01-01');
    INSERT INTO ads_table(user_id,house_id,adtype_id,ad_price,is_paid,order_id,paid_date)
	VALUES(
		@randValue,
		@randValue,
		@adType,
		@adPrice,
		@paid ,
		CASE WHEN @paid = 1 THEN (SELECT CONCAT('ORD', CONVERT(NVARCHAR(20), @randDate, 112), '-', RIGHT('0000' + CAST(@counter AS NVARCHAR(4)), 4))) ELSE NULL END,
		CASE WHEN @paid = 1 THEN @randDate ELSE NULL END
	)

	IF @paid = 1 
	BEGIN
		INSERT INTO orders_table(user_id,merchant_id,merchantTradNo,merchantTradDate
		,totalAmount,tradeDesc,itemName,order_status,returnUrl,checkMacValue)
		VALUES(
			@counter,
			@counter,
			(SELECT CONCAT('ORD', CONVERT(NVARCHAR(20), @randDate, 112), '-', RIGHT('0000' + CAST(@counter AS NVARCHAR(4)), 4))),
			@randDate,
			@adPrice,
			CASE WHEN @counter <10 THEN CONCAT('描述00' , @counter) WHEN @counter>=10 THEN CONCAT('描述0' , @counter)  END,
			CASE WHEN @counter <10 THEN CONCAT('商品00' , @counter) WHEN @counter>=10 THEN CONCAT('商品0' , @counter)  END,
			CASE WHEN @randValue % 4 = 0 THEN 0 WHEN @randValue % 3 = 0 THEN 1 END,
			CONCAT('https://www.rental', @counter , '.com.tw'),
			(SELECT ABS(LOWER(CAST(CHECKSUM(NEWID()) AS varchar(50)))))
		)
	END

	SET @counter = @counter + 1
END;
go

CREATE TABLE cart_table (
    cart_id INT PRIMARY KEY IDENTITY(1,1), -- 購物車ID (主鍵，自動增長)
    user_id BIGINT UNIQUE, -- 用戶ID
    created_at DATETIME DEFAULT GETDATE(), -- 購物車創建時間
	foreign key (user_id) references user_table(user_id)
);
go

CREATE TABLE cart_items (
    ad_id BIGINT PRIMARY KEY, -- 廣告ID，作為主鍵，確保每則廣告只能加入購物車一次
    cart_id INT NOT NULL, -- 購物車ID，標示該廣告屬於哪個購物車
    adtype_id INT NOT NULL, -- 廣告方案ID
    ad_price INT NOT NULL, -- 廣告金額，記錄當前價格
    added_date DATETIME DEFAULT GETDATE(), -- 加入購物車的日期(ie.處理保存期限XX天)
    FOREIGN KEY (cart_id) REFERENCES cart_table(cart_id), -- 關聯購物車
);
go


--特色與限制資料表
create table condition_table(
	house_id bigint,
	pet bit, --寵物
	parkingSpace bit, --停車位
	elevator bit, --電梯
	balcony bit, --陽台
	shortTerm bit, --短期租屋
	cooking bit, --開伙
	waterDispenser bit, --飲水機
	fee bit , --管理費
	genderRestrictions tinyint, --性別限制 (0-無 , 1-限男 , 2-限女)
	foreign key (house_id) references house_table(house_id)
);
go


--data
DECLARE @counter INT = 1;
DECLARE @randValue INT =1;
WHILE @counter <= 50
BEGIN
	SET @randValue = CAST((RAND() * 100) AS INT)
    INSERT INTO condition_table(house_id,pet,parkingSpace,elevator,balcony,shortTerm,cooking,waterDispenser,fee,genderRestrictions)
	VALUES(
		@counter,
		CASE WHEN @randValue %4 = 0 THEN 0 WHEN @randValue %4 = 1 THEN 0 WHEN @randValue %4 =2 THEN 1 WHEN @randValue%4 =3 THEN 1 END,
		CASE WHEN @randValue %4 = 0 THEN 1 WHEN @randValue %4 = 1 THEN 0 WHEN @randValue %4 =2 THEN 0 WHEN @randValue%4 =3 THEN 1 END,
		CASE WHEN @randValue %4 = 0 THEN 1 WHEN @randValue %4 = 1 THEN 1 WHEN @randValue %4 =2 THEN 0 WHEN @randValue%4 =3 THEN 0 END,
		CASE WHEN @randValue %4 = 0 THEN 0 WHEN @randValue %4 = 1 THEN 0 WHEN @randValue %4 =2 THEN 0 WHEN @randValue%4 =3 THEN 1 END,
		CASE WHEN @randValue %4 = 0 THEN 1 WHEN @randValue %4 = 1 THEN 1 WHEN @randValue %4 =2 THEN 0 WHEN @randValue%4 =3 THEN 1 END,
		CASE WHEN @randValue %4 = 0 THEN 1 WHEN @randValue %4 = 1 THEN 1 WHEN @randValue %4 =2 THEN 0 WHEN @randValue%4 =3 THEN 0 END,
		CASE WHEN @randValue %4 = 0 THEN 0 WHEN @randValue %4 = 1 THEN 0 WHEN @randValue %4 =2 THEN 1 WHEN @randValue%4 =3 THEN 1 END,
		CASE WHEN @randValue %4 = 0 THEN 0 WHEN @randValue %4 = 1 THEN 0 WHEN @randValue %4 =2 THEN 1 WHEN @randValue%4 =3 THEN 0 END,
		CASE WHEN @randValue %3 = 0 THEN 0 WHEN @randValue %3 = 1 THEN 0 WHEN @randValue %3 =2 THEN 2 END
	)
    SET @counter = @counter + 1
END;
go


--家具
create table furniture_table(
	house_id bigint,
	washingMachine bit, --洗衣機
	airConditioner bit, --冷氣
	network bit, --網路
	bedstead bit, --床架
	mattress bit, --床墊
	refrigerator bit, --冰箱
	ewaterHeater bit, --電熱水器
	gwaterHeater bit, --瓦斯熱水器
	television bit, --電視
	channel4 bit , --第四台
	sofa bit , --沙發
	tables bit, --桌椅
	foreign key (house_id) references house_table(house_id)
);
go

--data
DECLARE @counter INT = 1;
DECLARE @randValue INT =1;
WHILE @counter <= 50
BEGIN
	SET @randValue = CAST((RAND() * 100) AS INT)
    INSERT INTO furniture_table(house_id,washingMachine,airConditioner,network,bedstead,mattress,
	refrigerator,ewaterHeater,gwaterHeater,television,channel4,sofa,tables)
	VALUES(
		@counter,
		CASE WHEN @randValue %4 = 0 THEN 1 WHEN @randValue %4 = 1 THEN 1 WHEN @randValue %4 =2 THEN 0 WHEN @randValue%4 =3 THEN 1 END,
		CASE WHEN @randValue %4 = 0 THEN 1 WHEN @randValue %4 = 1 THEN 0 WHEN @randValue %4 =2 THEN 0 WHEN @randValue%4 =3 THEN 1 END,
		CASE WHEN @randValue %4 = 0 THEN 1 WHEN @randValue %4 = 1 THEN 1 WHEN @randValue %4 =2 THEN 0 WHEN @randValue%4 =3 THEN 0 END,
		CASE WHEN @randValue %4 = 0 THEN 0 WHEN @randValue %4 = 1 THEN 0 WHEN @randValue %4 =2 THEN 1 WHEN @randValue%4 =3 THEN 1 END,
		CASE WHEN @randValue %4 = 0 THEN 1 WHEN @randValue %4 = 1 THEN 1 WHEN @randValue %4 =2 THEN 0 WHEN @randValue%4 =3 THEN 1 END,
		CASE WHEN @randValue %4 = 0 THEN 1 WHEN @randValue %4 = 1 THEN 1 WHEN @randValue %4 =2 THEN 0 WHEN @randValue%4 =3 THEN 0 END,
		CASE WHEN @randValue %4 = 0 THEN 0 WHEN @randValue %4 = 1 THEN 0 WHEN @randValue %4 =2 THEN 1 WHEN @randValue%4 =3 THEN 1 END,
		CASE WHEN @randValue %4 = 0 THEN 0 WHEN @randValue %4 = 1 THEN 0 WHEN @randValue %4 =2 THEN 1 WHEN @randValue%4 =3 THEN 0 END,
		CASE WHEN @randValue %4 = 0 THEN 1 WHEN @randValue %4 = 1 THEN 0 WHEN @randValue %4 =2 THEN 0 WHEN @randValue%4 =3 THEN 1 END,
		CASE WHEN @randValue %4 = 0 THEN 0 WHEN @randValue %4 = 1 THEN 0 WHEN @randValue %4 =2 THEN 0 WHEN @randValue%4 =3 THEN 1 END,
		CASE WHEN @randValue %4 = 0 THEN 1 WHEN @randValue %4 = 1 THEN 1 WHEN @randValue %4 =2 THEN 0 WHEN @randValue%4 =3 THEN 0 END,
		CASE WHEN @randValue %4 = 0 THEN 0 WHEN @randValue %4 = 1 THEN 0 WHEN @randValue %4 =2 THEN 1 WHEN @randValue%4 =3 THEN 1 END
	)
    SET @counter = @counter + 1
END;
go

--評價評論系統
create table review_table(
	review_id bigint primary key identity(1,1),
	house_id bigint,
	user_id bigint,
	rating int,
	review_text nvarchar(300),
	date datetime,
	foreign key (house_id) references house_table(house_id),
	foreign key (user_id) references user_table(user_id)
);
go

--data
DECLARE @counter INT = 1;
DECLARE @randValue INT = 1;
WHILE @counter <= 50
BEGIN
	SET @randValue = CAST((RAND() * 100) AS INT)
    INSERT INTO review_table(house_id,user_id,rating,review_text,date)
    VALUES (
        @counter,
		@counter,
		CASE WHEN @randValue % 5 = 0 THEN 1 WHEN @randValue % 5 = 1 THEN 2 WHEN @randValue % 5 = 2 THEN 3 WHEN @randValue %5 = 3 THEN 4 WHEN @randValue%5 =4 THEN 5 END,
		CASE WHEN @randValue % 5 = 0 THEN '租金太貴' WHEN @randValue % 5 = 1 THEN '環境不好' WHEN @randValue % 5 = 2 THEN '還好' WHEN @randValue %5 = 3 THEN '房東人很好' WHEN @randValue%5 =4 THEN '買東西很方便' END,
        GETDATE() -- createtime
    );

    SET @counter = @counter + 1
END;
go

CREATE TABLE complaints (
    complaints_id INT PRIMARY KEY IDENTITY(1,1),
	user_id int NOT NULL,
    username NVARCHAR(20) NOT NULL,     
    category NVARCHAR(10) NOT NULL,     
    subject NVARCHAR(50) NOT NULL,      
    content NVARCHAR(100) NOT NULL,
	note NVARCHAR(100) DEFAULT '無',
	status NVARCHAR(10) default '待處裡',
    submission_date DATETIME DEFAULT GETDATE()
);

----data
DECLARE @counter INT = 1;
DECLARE @randValue INT;
DECLARE @category NVARCHAR(10), @title NVARCHAR(50), @content NVARCHAR(100), @note NVARCHAR(100);

WHILE @counter <= 50
BEGIN
    SET @randValue = CAST((RAND() * 100) AS INT);

    SET @category = CASE 
                        WHEN @randValue % 5 = 0 THEN '刊登問題'
                        WHEN @randValue % 5 = 1 THEN '儲值問題'
                        WHEN @randValue % 5 = 2 THEN '會員問題'
                        WHEN @randValue % 5 = 3 THEN '檢舉不法'
                        ELSE '其它' 
                    END;

    SET @title = CASE 
                     WHEN @category = '刊登問題' THEN '刊登商品時出現錯誤'
                     WHEN @category = '儲值問題' THEN '儲值扣款但未到賬'
                     WHEN @category = '會員問題' THEN '會員帳號無法登入'
                     WHEN @category = '檢舉不法' THEN '平台上有人違規操作'
                     ELSE '系統功能異常'
                 END;

    SET @content = CASE 
                   WHEN @category = '刊登問題' THEN 
                       CASE 
                           WHEN @randValue % 3 = 0 THEN '我在刊登商品時遇到了技術問題，請協助解決。'
                           WHEN @randValue % 3 = 1 THEN '每次上傳圖片都會失敗，無法完成刊登流程。'
                           ELSE '刊登商品後未顯示在平台上，請幫忙檢查原因！'
                       END
                   WHEN @category = '儲值問題' THEN 
                       CASE 
                           WHEN @randValue % 3 = 0 THEN '餘額已扣款，但一直未更新，請幫助核實。'
                           WHEN @randValue % 3 = 1 THEN '儲值過程中發生了錯誤，餘額消失不見。'
                           ELSE '支付完成後系統顯示錯誤，無法確認交易，請協助處理。'
                       END
                   WHEN @category = '會員問題' THEN 
                       CASE 
                           WHEN @randValue % 3 = 0 THEN '會員帳號登錄總是提示密碼錯誤，但密碼是正確的。'
                           WHEN @randValue % 3 = 1 THEN '我已多次嘗試重置密碼，但仍無法登入。'
                           ELSE '會員資格顯示異常，請幫忙確認我的帳號狀態。'
                       END
                   WHEN @category = '檢舉不法' THEN 
                       CASE 
                           WHEN @randValue % 3 = 0 THEN '有用戶發布不實信息，可能造成誤導，請及時處理！'
                           WHEN @randValue % 3 = 1 THEN '平台上出現了違法交易信息，建議立即下架。'
                           ELSE '發現一個不法用戶正在進行欺詐行為，請儘快介入調查。'
                       END
                   ELSE 
                       CASE 
                           WHEN @randValue % 3 = 0 THEN '系統運行速度過慢，經常無法正常操作，影響工作效率。'
                           WHEN @randValue % 3 = 1 THEN '功能頻繁出現卡頓或閃退，幾乎無法使用。'
                           ELSE '操作過程中不斷出現未知錯誤，請幫忙查明問題。'
                       END
               END;

    SET @note = CASE 
                    WHEN @randValue % 4 = 0 THEN '已分派給負責人'
                    WHEN @randValue % 4 = 1 THEN '正在聯繫客戶'
                    WHEN @randValue % 4 = 2 THEN '等待進一步資訊'
                    ELSE '無'
                END;

    INSERT INTO complaints (username, user_id, category, subject, content, note, status, submission_date)
    VALUES (
        CONCAT('User', @counter),
        @counter,
        @category,
        @title,
        @content,
        @note,
        CASE 
            WHEN @randValue % 2 = 0 THEN '待處理'
            ELSE '處理中'
        END,
        DATEADD(DAY, -@counter, GETDATE())
    );

    SET @counter = @counter + 1;
END;
go


CREATE TABLE photos (
    photo_id INT PRIMARY KEY IDENTITY(1,1),
    complaints_id INT,
    fileName NVARCHAR(255) NOT NULL,
    FOREIGN KEY (complaints_id) REFERENCES complaints(complaints_id) ON DELETE CASCADE
);
go

--data
DECLARE @counter INT = 1;
DECLARE @randValue INT = 1;
WHILE @counter <= 50
BEGIN
    SET @randValue = CAST((RAND() * 100) AS INT);

    INSERT INTO photos (complaints_id,fileName)
    VALUES (
        @counter,
		CONCAT('Filename', @counter)
    );

    SET @counter = @counter + 1;
END;
go