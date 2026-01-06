CREATE TABLE tbl_VehicleType(
	id integer primary key generated always as identity
	, brand VARCHAR(255)
	, modell VARCHAR(255)
	, HP NUMERIC(3,0)
	, maxKph NUMERIC(3,0)
	, mietSatz NUMERIC(4,2));

CREATE TABLE tbl_Shop (
	id integer primary key generated always as identity,
	ort VARCHAR(255));

CREATE TABLE tbl_Vehicle (
	id integer primary key generated always as identity,
	inWerkstatt NUMERIC(1,0),
	fahrgestellNummer VARCHAR(50),
	Vehicle_fk integer);

CREATE TABLE tbl_Vehicle_Shop_Rel (
	Vehicle_fk integer
	, Shop_fk integer);
	
CREATE TABLE TBL_Car(
	id integer primary key generated always as identity,
	doors NUMERIC(1,0),
	Vehicle_type_fk integer
);

CREATE TABLE TBL_TRUCK(
	id integer primary key generated always as identity,
	maxLoad NUMERIC(3,1),
	Vehicle_type_fk integer
);

CREATE TABLE tbl_User(
	id integer primary key generated always as identity,
	name VARCHAR(50),
	email VARCHAR(255)
);

CREATE TABLE tbl_customer(
	id integer primary key generated always as identity,
	customerReferenceNumber VARCHAR(50),
	bankleitzahl VARCHAR(50),
	user_fk integer
);

CREATE TABLE tbl_Reservation
(
	id integer primary key generated always as identity,
	von TIMESTAMP,
	bis TIMESTAMP,
	shop_fk integer,
	vehicle_fk integer,
	customer_fk integer
);
