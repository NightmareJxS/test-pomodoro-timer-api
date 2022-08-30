CREATE DATABASE PomodoroTimer
go

use PomodoroTimer
go

CREATE TABLE [User] (
	Id nvarchar(50) primary key,
	Email nvarchar(50),
	[Password] varchar(50),
	TimeFocusToday int,
	TimeFocusThisWeek int
)

go

CREATE TABLE Task(
	Id int identity primary key,
	UserId nvarchar(50) REFERENCES [User](Id),
	[Name] nvarchar(50),
	Duration int,
	StartTime Date,
	CompleteTime Date,
	[Status] int
) 

go

-----[User]
insert into [User] values ('SE160947', 'testing@test.com', '123456', 13575, 794850)
insert into [User] values ('PTU55748', 'testing1@test.com', '654321', 10596, 305863)
insert into [User] values ('SE160111', 'testing2@test.com', '666666', 5947, 50437)
insert into [User] values ('PTU00007', 'testing3@test.com', '555555', 0, 0)
insert into [User] values ('PTU35235', 'testing4@test.com', '444444', 5464, 705833)
insert into [User] values ('PTU33513', 'testing5@test.com', '333333', 4432, 475623)
insert into [User] values ('PTU67545', 'testing6@test.com', '222222', 2768, 428783)
insert into [User] values ('PTU32452', 'testing7@test.com', '111111', 8675, 67857)
insert into [User] values ('PTU87980', 'testing8@test.com', '000000', 15068, 990472)
-------Task
set identity_insert Task on
insert into Task(Id, UserId, [Name], Duration, StartTime, CompleteTime, [Status]) values (17, 'SE160947', 'OJT Signup', 7200, '2022-08-13', '2022-08-14', 0)
insert into Task(Id, UserId, [Name], Duration, StartTime, CompleteTime, [Status]) values (18, 'SE160947', 'Something1', 600, '2022-08-15', '2022-08-15', 0)
insert into Task(Id, UserId, [Name], Duration, StartTime, CompleteTime, [Status]) values (19, 'SE160947', 'Something2', 120, '2022-08-15', '2022-08-15', 0)
insert into Task(Id, UserId, [Name], Duration, StartTime, CompleteTime, [Status]) values (12, 'SE160947', 'Something3', 172800, '2022-08-16', '2022-08-18', 0)
insert into Task(Id, UserId, [Name], Duration, StartTime, CompleteTime, [Status]) values (1, 'SE160947', 'Something4', 1209600, '2022-08-18', '2022-09-01', 1)
insert into Task(Id, UserId, [Name], Duration, StartTime, CompleteTime, [Status]) values (5, 'PTU55748', 'Something5', 7200, '2022-08-13', '2022-08-14', 1)
insert into Task(Id, UserId, [Name], Duration, StartTime, CompleteTime, [Status]) values (55, 'PTU67545', 'Something6', 120, '2022-08-15', '2022-08-15', 3)
insert into Task(Id, UserId, [Name], Duration, StartTime, CompleteTime, [Status]) values (48, 'PTU87980', 'Something7', 120, '2022-08-15', '2022-08-15', 2)
set identity_insert Task off

insert into Task(UserId, [Name], Duration, StartTime, CompleteTime, [Status]) values ('PTU67545', 'Something7', 120, '2022-08-15', '2022-08-15', 2)

go

------Test Query------
SELECT * FROM [User] 
SELECT * FROM Task
SELECT * FROM [User] WHERE Id = 'SE160947'
SELECT Id, Email, [Password], TimeFocusToday, TimeFocusThisWeek FROM [User] WHERE Id = 'SE160947' AND [Password] = '123456'
SELECT Id, UserId, [Name], Duration, StartTime, CompleteTime, [Status] FROM Task WHERE UserId = 'SE160947' 
INSERT INTO [User](Id, Email, [Password], TimeFocusToday, TimeFocusThisWeek) VALUES (?, ?, ?, ?, ?) 
SELECT Email FROM [User] WHERE Id = ? 
INSERT INTO Task(Id, UserId, [Name], Duration, StartTime, CompleteTime, [Status]) VALUES (?, ?, ?, ?, ?, ?, ?) 