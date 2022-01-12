USE [my_db]
GO
CREATE SEQUENCE [dbo].[SEQ_EMPLOYEE]
    AS [bigint]
    START WITH 100
    INCREMENT BY 1
    MINVALUE -9223372036854775808
    MAXVALUE 9223372036854775807
    CACHE
GO

create sequence SEQ_USER
    minvalue 1
    start with 5
    increment by 1
    cache 5;

drop table departments;
CREATE TABLE departments
(
    id         int identity PRIMARY KEY,
    name       varchar(55),
    max_salary int,
    min_salary int
);
go

alter table employees
    add department_id int

SELECT NEXT VALUE FOR [dbo].SEQ_EMPLOYEE;

SET IDENTITY_INSERT details ON



USE [my_db]
GO
CREATE SEQUENCE [dbo].[SEQ_DETAILS]
    AS [bigint]
    START WITH 100
    INCREMENT BY 1
    MINVALUE -9223372036854775808
    MAXVALUE 9223372036854775807
    CACHE
GO

create table details
(
    id           int primary key not null default (next value for dbo.SEQ_DETAILS),
    city         varchar(15),
    phone_number varchar(25),
    email        varchar(30)
)
go

select *
from #details2
select *
from details

-- Table 'details' does not have the identity property. Cannot perform SET operation.
SET IDENTITY_INSERT details ON
go

update employees
set employees.details_id = 100
where id = 1
select *
from Details
select *
from Employees

-- [!] Задача: изменить формат первичного ключа в таблице А, которая привязана к другой по внешнему ключу к таблице Б
-- 1) скопировать данные из А в А1 (временную)
-- 2) скопировать данные о связи (один к одному) из т А и Б во временную т. АБ
-- 3) FK хранится в т Б. Поэтому, удаляем все FK из Б
-- 4) удалить т А и создать ее заново с новым форматом PK
-- 5) заполнить т А данными из А1
-- 6) восстановить внешние ключи в т Б через команду MERGE INTO (например), используя временную таблицу АБ

-- копирование данныйх в временную таблицу. Только тех записей, где есть внешний ключ
-- https://docs.microsoft.com/en-us/sql/t-sql/queries/select-into-clause-transact-sql?view=sql-server-ver15
SELECT e.id, e.details_id
INTO #tmpDetails
FROM employees e
         JOIN details d on e.details_id = d.id
GO

-- select * from #tmpDetails
-- select * from details
-- select * from employees
-- insert into #tmpDetails (id,details_id) values (1, 100)

-- https://docs.microsoft.com/en-us/sql/t-sql/statements/merge-transact-sql?view=sql-server-ver15
MERGE INTO employees DEST
using (
    select t.id, t.details_id
    from #tmpDetails t
) AS SRC
ON SRC.id = DEST.id
WHEN MATCHED THEN
    UPDATE
    SET DEST.details_id = SRC.details_id
WHEN NOT MATCHED BY TARGET THEN
    insert (details_id)
    values (NULL);
-- WHEN NOT MATCHED BY SOURCE
-- THEN
--     delete;


--  select d.id, d.details_id did,
--  (
--  INSERT INTO employees (details_id) VALUES (d.details_id)
--      WHERE employees.id = d.id
--      )
--  from #tmpDetails d

select *
from details

INSERT INTO employees (details_id)
    (select d.details_id did from #tmpDetails d) where d.id = employees.id

INSERT INTO details (city, email)
values ('Mars', 'mars')

--  INSERT INTO column_1 ( val_1, val_from_other_table )
--  VALUES('val_1', (SELECT  val_2 FROM table_2 WHERE val_2 = something))


select *
from #tmpDetails
-- как решить задачу
-- 1. пробежать по #tmpDetails
--     select d.id, d.details_id did from #tmpDetails d
-- 2. вставить
-- INSERT INTO employees (details_id) VALUES (d.details_id)
-- WHERE employees.id = d.id


-- наполнение details
insert into details (city, phone_number, id)
values ('Kiev', '333', 100)


INSERT INTO employees (name) VALUES ('Foo'), ('Bar')
update employees set employees.department_id = 6 where id in (20, 21)

select * from departments
select * from employees
ALTER TABLE Employees
    ADD details_id int NULL;
GO

-- создать внешний ключ
-- (!) это может не работать .если ест ьсущности в employee,
-- которые ссылаются на несуществующие детали , т.е. целостность данных нарушена
-- В этом случае летит ошибка
-- The ALTER TABLE statement conflicted with the FOREIGN KEY constraint "fk_details".
-- найти орфан-сущности можно так:
-- select details_id from [db_accessadmin].[Employees] WHERE details_id NOT IN (SELECT id from [db_accessadmin].[details])
-- он выдаст employee.details, которые ведут к несуществующим деталям
-- Полечить такие строки:
-- update  [db_accessadmin].employees set details_id = NULL where id = 17
-- После этого CONSTRAINT накатился
ALTER TABLE [db_accessadmin].[Employees]
    ADD CONSTRAINT fk_details FOREIGN KEY (details_id)
        REFERENCES [db_accessadmin].[details] (id)
        ON DELETE cascade ON UPDATE cascade
GO

alter table employees drop column department
go
alter table employees drop CONSTRAINT fk_department
go
ALTER TABLE [db_accessadmin].[Employees]
    ADD CONSTRAINT fk_department FOREIGN KEY (department_id)
        REFERENCES [db_accessadmin].[departments] (id)
        ON DELETE cascade ON UPDATE cascade
GO



SELECT OBJECT_NAME(f.parent_object_id)                    TableName,
       COL_NAME(fc.parent_object_id, fc.parent_column_id) ColName
FROM sys.foreign_keys AS f
         INNER JOIN
     sys.foreign_key_columns AS fc
     ON f.OBJECT_ID = fc.constraint_object_id
         INNER JOIN
     sys.tables t
     ON t.OBJECT_ID = fc.referenced_object_id
WHERE OBJECT_NAME(f.referenced_object_id) = 'details'

SELECT object_name(parent_object_id)     ParentTableName,
       object_name(referenced_object_id) RefTableName,
       name
FROM sys.foreign_keys
WHERE parent_object_id = object_id('employees')


update Employees
set employees.details_id = 107
where id = 17


select e.*, d.*
from Employees e
         left join details d on d.id = e.details_id
-- where e.id = 1

select * from details
select * from Employees
select * from departments

update employees
set employees.department_id = 4
where id = 6

INSERT INTO departments
(name, max_salary, min_salary)
VALUES ('Space_investigations', 2000, 500),
       ('Astrial_investigations', 400, 100),
       ('RnD', 4000, 100),
       ('Deep_water_oceanography', 4000, 100)
;
update employees set employees.department_id = 1
where id = 9


select next value for dbo.SEQ_DETAILS

from dbo.SEQ_DETAILS
with (
    updlock,
    rowlock)

SELECT *
FROM INFORMATION_SCHEMA.SEQUENCES

-- выводит результат ТОЛЬКО при наличии юзера и деталей
-- Когда details нет, результат пуст, при использовании inner join и right join
-- При left join результат есть всегда, когда есть юзер
select USR.id              as id1_1_0_,
       USR.department      as departme2_1_0_,
       --USR.details_id as details_5_1_0_,
       USR.name            as name3_1_0_,
       USR.surname         as surname4_1_0_,
       DETAIL.id           as id1_0_1_,
       DETAIL.city         as city2_0_1_,
       DETAIL.email        as email3_0_1_,
       DETAIL.phone_number as phone_nu4_0_1_
from employees USR
         inner join
     details DETAIL
     on USR.details_id = DETAIL.id
where USR.id = 17