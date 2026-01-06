INSERT INTO tbl_VehicleType (brand,modell,hp,maxKph,mietsatz) VALUES ('VW','Golf',100,190,40);
INSERT INTO tbl_VehicleType (brand,modell,hp,maxKph,mietsatz) VALUES ('Mercedes','C',110,190,55);
INSERT INTO tbl_VehicleType (brand,modell,hp,maxKph,mietsatz) VALUES ('BMW','323',150,210,55);
INSERT INTO tbl_VehicleType (brand,modell,hp,maxKph,mietsatz) VALUES ('Toyota','Corola',90,190,30);
INSERT INTO tbl_VehicleType (brand,modell,hp,maxKph,mietsatz) VALUES ('Mazda','MX 5',105,205,45);

INSERT INTO TBL_Car (doors, Vehicle_type_fk) VALUES (3,1);
INSERT INTO TBL_Car (doors, Vehicle_type_fk) VALUES (5,2);
INSERT INTO TBL_Car (doors, Vehicle_type_fk) VALUES (3,3);
INSERT INTO TBL_Car (doors, Vehicle_type_fk) VALUES (3,4);
INSERT INTO TBL_Car (doors, Vehicle_type_fk) VALUES (3,5);

INSERT INTO TBL_Vehicle (inWerkstatt, fahrgestellNummer, Vehicle_fk) VALUES (0,'111',1);
INSERT INTO TBL_Vehicle (inWerkstatt, fahrgestellNummer, Vehicle_fk) VALUES (0,'112',1);
INSERT INTO TBL_Vehicle (inWerkstatt, fahrgestellNummer, Vehicle_fk) VALUES (0,'113',1);
INSERT INTO TBL_Vehicle (inWerkstatt, fahrgestellNummer, Vehicle_fk) VALUES (1,'114',1);

INSERT INTO TBL_Vehicle (inWerkstatt, fahrgestellNummer, Vehicle_fk) VALUES (0,'211',2);
INSERT INTO TBL_Vehicle (inWerkstatt, fahrgestellNummer, Vehicle_fk) VALUES (1,'212',2);
INSERT INTO TBL_Vehicle (inWerkstatt, fahrgestellNummer, Vehicle_fk) VALUES (0,'213',2);
INSERT INTO TBL_Vehicle (inWerkstatt, fahrgestellNummer, Vehicle_fk) VALUES (0,'214',2);

INSERT INTO TBL_Vehicle (inWerkstatt, fahrgestellNummer, Vehicle_fk) VALUES (1,'311',3);
INSERT INTO TBL_Vehicle (inWerkstatt, fahrgestellNummer, Vehicle_fk) VALUES (0,'312',3);

INSERT INTO TBL_Vehicle (inWerkstatt, fahrgestellNummer, Vehicle_fk) VALUES (0,'411',4);
INSERT INTO TBL_Vehicle (inWerkstatt, fahrgestellNummer, Vehicle_fk) VALUES (0,'412',4);

INSERT INTO TBL_Vehicle (inWerkstatt, fahrgestellNummer, Vehicle_fk) VALUES (0,'511',5);

INSERT INTO TBL_Shop (location) VALUES ('Stuttgart');
INSERT INTO TBL_Shop (location) VALUES ('M�nchen');
INSERT INTO TBL_Shop (location) VALUES ('Hamburg');
INSERT INTO TBL_Shop (location) VALUES ('Frankfurt');

INSERT INTO TBL_USER(name,email) VALUES ('Hans','h@gmx.de');
INSERT INTO TBL_USER(name,email) VALUES ('Marie','m@web.de');
INSERT INTO TBL_USER(name,email) VALUES ('Matthias','matze@gmail.com');
INSERT INTO TBL_USER(name,email) VALUES ('Inge','inge@hotmail.com');

INSERT INTO TBL_KUNDE (kontonummer,bankleitzahl,user_fk) VALUES ('1','1',1);
INSERT INTO TBL_KUNDE (kontonummer,bankleitzahl,user_fk) VALUES ('12','12',3);

INSERT INTO tbl_Vehicle_Shop_Rel (Vehicle_fk, Shop_fk) VALUES (1,4);
INSERT INTO tbl_Vehicle_Shop_Rel (Vehicle_fk, Shop_fk) VALUES (2,3);
INSERT INTO tbl_Vehicle_Shop_Rel (Vehicle_fk, Shop_fk) VALUES (3,1);
INSERT INTO tbl_Vehicle_Shop_Rel (Vehicle_fk, Shop_fk) VALUES (4,1);
INSERT INTO tbl_Vehicle_Shop_Rel (Vehicle_fk, Shop_fk) VALUES (5,2);

INSERT INTO tbl_Vehicle_Shop_Rel (Vehicle_fk, Shop_fk) VALUES (6,4);
INSERT INTO tbl_Vehicle_Shop_Rel (Vehicle_fk, Shop_fk) VALUES (7,3);
INSERT INTO tbl_Vehicle_Shop_Rel (Vehicle_fk, Shop_fk) VALUES (8,1);
INSERT INTO tbl_Vehicle_Shop_Rel (Vehicle_fk, Shop_fk) VALUES (9,1);
INSERT INTO tbl_Vehicle_Shop_Rel (Vehicle_fk, Shop_fk) VALUES (10,2);


INSERT INTO tbl_Vehicle_Shop_Rel (Vehicle_fk, Shop_fk) VALUES (11,4);
INSERT INTO tbl_Vehicle_Shop_Rel (Vehicle_fk, Shop_fk) VALUES (12,3);
INSERT INTO tbl_Vehicle_Shop_Rel (Vehicle_fk, Shop_fk) VALUES (13,2);
