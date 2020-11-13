-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (1,'admin1','admin');
--Proveedores
INSERT INTO users(username,password,enabled) VALUES ('proveedor1','Prove3dor1',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (2,'proveedor1','proveedor');
INSERT INTO proveedores(empresa,direccion,cif) VALUES ('Empresa1','Direccion1','S4953142I');