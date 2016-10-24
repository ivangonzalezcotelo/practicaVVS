-- ----------------------------------------------------------------------------
-- Put here INSERT statements for inserting data required by the application
-- in the "pojoTest" database.
-------------------------------------------------------------------------------


INSERT INTO Category VALUES(4,'Decoración');
INSERT INTO Category VALUES(5,'Música');

INSERT INTO UserProfile VALUES(3, 'user3', 'John', 'Day', 'john@gmail.com', 'PASS');
INSERT INTO UserProfile VALUES(4, 'user4', 'Mike', 'Johanson', 'mike@gmail.com', 'PASS');
INSERT INTO UserProfile VALUES(5, 'user5', 'Carl', 'Petersen', 'carl@gmail.com', 'PASS');



--INSERT INTO Product VALUES(prodId, prodName, description, launchPrice, sendInfo, createDate, 
--								finishDate, winnerBid, actualValue, owner, version, category);

INSERT INTO Product VALUES(013, 'Florero', 'Florero rojo', 7.50, 'Por correos', '2015-05-08 02:26:31', 
								'2015-06-24 01:49:31', NULL, 7.50, 3, 0, 4);
INSERT INTO Product VALUES(014, 'CD', 'Beatles', 10.00, 'Por correos', '2015-05-08 02:26:32', 
								'2015-06-24 02:49:31', NULL, NULL, 3, 0, 5);
INSERT INTO Product VALUES(015, 'CD', 'Nirvana', 6.75, 'Por correos', '2015-05-08 02:26:33',
								'2015-06-24 03:49:31', NULL, 6.75, 3, 1, 5); 

INSERT INTO Bid VALUES(6, 13, 4, 7.50, 12.00, '2016-05-09 19:30:33', 4);
UPDATE Product SET winnerBid = 6 WHERE prodId = 13;
INSERT INTO Bid VALUES(7, 14, 5, 10.00, 15.00, '2016-05-09 19:30:33', 4);
UPDATE Product SET winnerBid = 7 WHERE prodId = 14;
INSERT INTO Bid VALUES(8, 15, 5, 6.75, 9.00, '2016-05-09 19:30:33', 4);
UPDATE Product SET winnerBid = 8 WHERE prodId = 15;												