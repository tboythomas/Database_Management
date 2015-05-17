/* 
	Wenlu Cheng, ID:1336340, CSE 414 Homework 1
  	Section AB 
*/

--1.
--a.
create table R (A integer primary key, B integer);

--b.
insert into R(A,B) values(2,4);
insert into R(A,B) values(1,1);
insert into R(A,B) values(3,2);

--c.
.header on
.mode column
select * from R;      

--d.
insert into R(A,B) values('5','2');

/*No error because this is a feature of SQlite.
SQLite does not enforce data type constraints. 
Data of any type can (usually) be inserted into any column.
*/

--e.
select A from R;        

--f.
select * from R where A <= B;

--2.
create table MyRestaurants
(Name varchar(20) primary key,
Type of food varchar(20),
Distance integer,
'Date of your last visit' varchar(10),
'Whether you like it or not' integer);

--3.
insert into MyRestaurants values('China First','Chinese','0.2','2015-03-29',1);
insert into MyRestaurants values('EJ Burger','FastFood','0.5','2014-03-23',1);
insert into MyRestaurants values('Udon','Noodle','0.8','2015-03-27',0);
insert into MyRestaurants values('Zen','Noodle','0.6','2015-03-20',1);
insert into MyRestaurants values('HokngKong','Dimsum','5.5','2015-02-23',0);
insert into MyRestaurants values('BBQ','Korean','9.5','2015-01-28',NULL);

--4.
select * from MyRestaurants;

--5.
--a.
.separator ","

--b.
.mode list

--c.
.mode column
.width 15

--d.
--printing with headers: 
.header on
--printing without headers: 
.header off


--6.
select case when "Whether you like it or not" = 1 then 'I liked it'
when "Whether you like it or not" = 0 then 'I hated it'
end 
from Myrestaurants;

--7.
select Name from MyRestaurants where "whether you like it or not" = 1 AND
"Date of your last visit" < date('now','-3 month');

