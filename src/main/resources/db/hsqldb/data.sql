
-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (1,'admin1','admin');
--Proveedores
INSERT INTO users(username,password,enabled) VALUES ('proveedor1','Prove3dor1',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (2,'proveedor1','proveedor');
INSERT INTO proveedores(empresa,direccion,cif) VALUES ('Empresa1','Direccion1','S4953142I');

INSERT INTO users(username,password,enabled) VALUES ('proveedor2','Prove3dor1',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (3,'proveedor2','proveedor');
INSERT INTO proveedores(empresa,direccion,cif) VALUES ('Empresa2','Direccion2','W7181885J');
--CLientes


-- One admin user, named admin1 with passwor 4dm1n and authority admin


INSERT INTO users(username,password,enabled) VALUES ('farm1','farm1',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (5,'farm1','admin');

INSERT INTO users(username,password,enabled) VALUES ('client1','client1',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (6,'client1','admin');

INSERT INTO farmaceutico VALUES (7, 'Pepe', 'Rodriguez Rodriguez', '12345689H','Calle Vieja','farm1');

INSERT INTO cliente(id,name,surnames,dni,provincia,localidad,direccion,por_pagar_total,username) VALUES (8, 'Luis', 'Rodriguez Mendez', '12345789H','Sevilla','Sevilla','Calle Maria 1','10.0','client1');

