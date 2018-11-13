-- запрос для showAllEvents
SELECT
city.name as city,
field.name as field,
event.date as date,
event.starttime as time
FROM
event INNER JOIN city ON city._id = event.city
      INNER JOIN field ON field._id = event.field;
      
-- запрос для авторизации
SELECT
user.login as login, 
user.email as email,
user.password as password
FROM
user;

--запрос для записи нового пользователя
INSERT INTO user (login, password, name, phone_number, def_city, email) 
VALUES ("pavel85", "4a45sd", "PAvel", "1564132", 4, "asd@asdf.ru");

--данные для спинера cityName
SELECT name FROM city;

--данные для спинера fieldName
SELECT field.name FROM field WHERE city = 3;

--данные для спинера region
SELECT name FROM region;

--данные для спинера coating
SELECT type FROM coating;

SELECT events._id FROM events WHERE events.user_id = 7
UNION
Select events._id FROM participants INNER JOIN events ON events._id =     participants.event_id WHERE participants.user_id = 7;

--для фрагмента showUserEvent
SELECT cities.name, fields.name, events.date, events.time FROM events 
INNER JOIN fields ON fields._id = events.field_id 
INNER JOIN cities ON cities._id = events.city_id 
WHERE events.user_id = 7 UNION SELECT cities.name, fields.name, events.date, events.time FROM participants
INNER JOIN events ON events._id = participants.event_id 
INNER JOIN fields ON fields._id = events.field_id 
INNER JOIN cities ON cities._id = events.city_id 
WHERE participants.user_id = 7;

-- для меню aboutEventShowAllEvent
SELECT cities.name, fields.name, date, time, duration, price, phone FROM events INNER JOIN cities ON cities._id = events.city_id 
INNER JOIN fields ON fields._id = events.field_id 
WHERE events._id = 4;

--поиск определенного пользователя в определенном событии 
SELECT events._id FROM events 
WHERE events._id = 4 AND events.user_id = 8
UNION
SELECT participants._id FROM participants 
WHERE participants.event_id = 3 AND participants.user_id = 1;