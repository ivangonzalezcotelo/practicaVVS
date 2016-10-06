-- ----------------------------------------------------------------------------
-- Put here INSERT statements for inserting data required by the application
-- in the "pojo" database.
-------------------------------------------------------------------------------

--INSERT INTO Category VALUES(catId, catName);

INSERT INTO Category VALUES(1,'Electrónica');
INSERT INTO Category VALUES(2,'Ropa');
INSERT INTO Category VALUES(3,'Informatica');



--INSERT INTO UserProfile VALUES(usrId, loginName, firstName, lastName, email, enPassword);
--Password de ejemplo, 1q2w3e4r = GZCEzNgJyUy2A

INSERT INTO UserProfile VALUES(1, 'user1', 'User1', 'seller', 'user1@seller.com', 'GZCEzNgJyUy2A');
INSERT INTO UserProfile VALUES(2, 'user2', 'User2', 'buyer', 'user2@buyer.com', 'GZCEzNgJyUy2A');



--INSERT INTO Product VALUES(prodId, prodName, description, launchPrice, sendInfo, createDate, 
--								finishDate, winnerBid, actualValue, owner, version, category);

INSERT INTO Product VALUES(001, 'Pantalon', 'Pantalon de pana', 12.50, 'Por correos', '2015-05-08 02:26:31', 
								'2015-06-24 01:49:31', NULL, NULL, 1, 0, 2);
INSERT INTO Product VALUES(002, 'Chaqueta', 'Chaqueta rota', 10.00, 'Por correos', '2015-05-08 02:26:32', 
								'2015-06-24 02:49:31', NULL, NULL, 1, 0, 2);
INSERT INTO Product VALUES(003, 'Pantalon corto', 'Gris oscuro', 6.75, 'Por correos', '2015-05-08 02:26:33', 
								'2015-06-24 03:49:31', NULL, 6.75, 1, 1, 2);
INSERT INTO Product VALUES(004, 'Zapatos', 'Piel marrones', 25.00, 'Por correos', '2015-05-08 02:26:34', 
								'2015-06-24 04:49:31', NULL, 25.00, 1, 1, 2);
INSERT INTO Product VALUES(005, 'Chandal', 'Adidas negro/blanco', 20.50, 'Por correos', '2015-05-08 02:26:35', 
								'2015-06-24 05:49:31', NULL, 20.50, 1, 1, 2);
INSERT INTO Product VALUES(006, 'Chaleco', 'Chaleco a cuadros', 7.00, 'Por correos', '2015-05-08 02:26:36', 
								'2015-06-24 08:49:31', NULL, 7.00, 1, 1, 2);
INSERT INTO Product VALUES(007, 'Traje', 'Negro talla 50', 80.00, 'Por correos', '2015-05-08 02:26:37', 
								'2015-06-24 06:49:31', NULL, 80.00, 1, 1, 2);
INSERT INTO Product VALUES(008, 'Ropa invierno', 'Conjunto de prendas de invierno', 100.00, 'Por correos', '2015-05-08 02:26:38', 
								'2015-05-08 08:49:31', NULL, NULL, 1, 0, 2);
INSERT INTO Product VALUES(009, 'Gorra', 'Gorra de los Yankies', 4.00, 'Por correos', '2015-05-08 02:26:31', 
								'2015-05-08 08:49:31', NULL, NULL, 1, 0, 2);
INSERT INTO Product VALUES(010, 'Ordenador portatil', 'i5, 4GB de ram, intel 4000, SSD 128GB', 120.00, 'Por correos', '2015-05-08 02:26:39', 
								'2015-06-24 08:49:31', NULL, NULL, 1, 0, 3);
INSERT INTO Product VALUES(011, 'Nexus 5', 'Esquina inferior rallada', 150.00, 'Por correos', '2015-05-08 02:26:40', 
								'2015-06-24 08:49:31', NULL, NULL, 2, 0, 3);
INSERT INTO Product VALUES(012, 'Radio FM', 'Antigua, con antena', 12.00, 'Por correos', '2015-05-08 02:26:41', 
								'2015-06-24 08:49:31', NULL, NULL, 2, 0, 1);
								
								
--INSERT INTO Bid VALUES(bidId, productId, userId, currentValue, maxBid, bidDate, actualWinner);

INSERT INTO Bid VALUES(1, 3, 2, 6.75, 12.00, '2015-05-09 19:30:33', 2);
UPDATE Product SET winnerBid = 1 WHERE prodId = 3;

INSERT INTO Bid VALUES(2, 4, 2, 25.00, 25.00, '2015-05-09 19:35:33', 2);
UPDATE Product SET winnerBid = 2 WHERE prodId = 4;

INSERT INTO Bid VALUES(3, 5, 2, 20.50, 25.50, '2015-05-09 19:40:33', 2);
UPDATE Product SET winnerBid = 3 WHERE prodId = 5;

INSERT INTO Bid VALUES(4, 6, 2, 7.00, 13.50, '2015-05-09 19:45:33', 2);
UPDATE Product SET winnerBid = 4 WHERE prodId = 6;

INSERT INTO Bid VALUES(5, 7, 2, 80.00, 120.00, '2015-05-09 19:50:33', 2);
UPDATE Product SET winnerBid = 5 WHERE prodId = 7;
					