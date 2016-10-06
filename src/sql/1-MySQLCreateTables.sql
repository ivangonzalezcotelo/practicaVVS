-- Indexes for primary keys have been explicitly created.

-- ---------- Table for validation queries from the connection pool. ----------

DROP TABLE PingTable;
CREATE TABLE PingTable (foo CHAR(1));

-- ------------------------------ UserProfile ----------------------------------

ALTER TABLE Product DROP FOREIGN KEY Product_WinnerBidFK;
DROP TABLE Bid;
DROP TABLE Product;
DROP TABLE Category;
DROP TABLE UserProfile;

CREATE TABLE UserProfile (
    usrId BIGINT NOT NULL AUTO_INCREMENT,
    loginName VARCHAR(30) COLLATE latin1_bin NOT NULL,
    firstName VARCHAR(30) NOT NULL,
    lastName VARCHAR(40) NOT NULL,
    email VARCHAR(60) NOT NULL,
    enPassword VARCHAR(13) NOT NULL, 
    CONSTRAINT UserProfile_PK PRIMARY KEY (usrId),
    CONSTRAINT LoginNameUniqueKey UNIQUE (loginName));

CREATE INDEX UserProfileIndexByLoginName ON UserProfile (loginName);

CREATE TABLE Category (
	catId BIGINT NOT NULL AUTO_INCREMENT,
	catName VARCHAR(50) NOT NULL,
	CONSTRAINT Category_PK PRIMARY KEY (catId));

CREATE INDEX CategoryIndexByCategoryName ON Category (catName);

CREATE TABLE Product (
	prodId BIGINT NOT NULL AUTO_INCREMENT,
	prodName VARCHAR(50),
	description VARCHAR(100),
	launchPrice DECIMAL(10,2) NOT NULL,
	sendInfo VARCHAR(50),
	createDate DATETIME,
	finishDate DATETIME,
	winnerBid BIGINT,
	actualValue DECIMAL(10,2),
	owner BIGINT,
	version BIGINT,
	category BIGINT,
	CONSTRAINT Product_PK PRIMARY KEY (prodId),
	CONSTRAINT Product_OwnerFK FOREIGN KEY (owner) REFERENCES UserProfile(usrId),
	CONSTRAINT Product_CatFK FOREIGN KEY (category) REFERENCES Category(catId));

CREATE INDEX ProductIndexByProductName ON Product (prodName);

CREATE TABLE Bid (
	bidId BIGINT NOT NULL AUTO_INCREMENT,
	productId BIGINT,
	userId BIGINT,
	currentValue DECIMAL(10,2) NOT NULL,
	maxBid DECIMAL(10,2) NOT NULL,
	bidDate DATETIME,
	actualWinner BIGINT,
	CONSTRAINT Bid_PK PRIMARY KEY (bidId),
	CONSTRAINT Bid_ActualWinner_FK FOREIGN KEY (actualWinner) REFERENCES UserProfile(usrId),
	CONSTRAINT Bid_Owner_FK FOREIGN KEY (userId) REFERENCES UserProfile(usrId),
	CONSTRAINT Bid_Product_FK FOREIGN KEY (productId) REFERENCES Product(prodId));

ALTER TABLE Product ADD CONSTRAINT Product_WinnerBidFK FOREIGN KEY (winnerBid) REFERENCES Bid(bidId);

