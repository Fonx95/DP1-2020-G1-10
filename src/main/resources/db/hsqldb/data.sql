
-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (1,'admin1','admin');
--Proveedores
INSERT INTO users(username,password,enabled) VALUES ('proveedor1','Prove3dor1',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (2,'proveedor1','proveedor');
INSERT INTO proveedors(empresa,direccion,cif,username) VALUES ('COFARES','Direccion1','S4953142I','proveedor1');

INSERT INTO users(username,password,enabled) VALUES ('proveedor2','Prove3dor1',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (3,'proveedor2','proveedor');
INSERT INTO proveedors(empresa,direccion,cif,username) VALUES ('CECOFAR','Direccion2','W7181885J','proveedor2');
--CLientes

INSERT INTO users(username,password,enabled) VALUES ('farm1','farm1',TRUE);

INSERT INTO users(username,password,enabled) VALUES ('client1','client1',TRUE);


INSERT INTO authorities(id,username,authority) VALUES (5,'farm1','farmaceutico');
INSERT INTO authorities(id,username,authority) VALUES (6,'client1','cliente');

INSERT INTO farmaceutico VALUES (7, 'Pepe', 'Rodriguez Rodriguez', '12345689H','Calle Vieja','farm1');






--INSERT INTO users(username,password,enabled) VALUES ('farm1','farm1',TRUE);
--INSERT INTO authorities(id,username,authority) VALUES (4,'farm1','farmaceutico');




INSERT INTO cliente(id,name,surnames,dni,provincia,localidad,direccion,por_pagar_total,username) VALUES (8, 'Luis', 'Rodriguez Mendez', '12345789H','Sevilla','Sevilla','Calle Maria 1','10.0','client1');

--Productos
INSERT INTO productos(name,code,product_type,pvp,pvf,stock,min_stock) VALUES ('Aspirina','PR-001','FARMACOSINRECETA','5.25','4.62','15','5');
INSERT INTO productos(name,code,product_type,pvp,pvf,stock,min_stock) VALUES ('Dalsy','PR-002','FARMACOCONRECETA','6.75','5.74','3','2');
INSERT INTO productos(name,code,product_type,pvp,pvf,stock,min_stock) VALUES ('Dolocatil','PR-003','FARMACOSINRECETA','7.55','6.11','4','4');
INSERT INTO productos(name,code,product_type,pvp,pvf,stock,min_stock) VALUES ('Mariguanilla','PR-004','ESTUPEFACIENTE','12.60','10.68','1','1');
INSERT INTO productos(name,code,product_type,pvp,pvf,stock,min_stock) VALUES ('Cinfatos','PR-005','FARMACOSINRECETA','9.20','8.34','4','3');
INSERT INTO productos(name,code,product_type,pvp,pvf,stock,min_stock) VALUES ('Gelocatil Gripe','PR-006','FARMACOCONRECETA','6.10','5.23','15','10');
INSERT INTO productos(name,code,product_type,pvp,pvf,stock,min_stock) VALUES ('Flumil Forte','PR-007','FARMACOSINRECETA','2.60','2.03','7','7');
INSERT INTO productos(name,code,product_type,pvp,pvf,stock,min_stock) VALUES ('Pasta Diente Lacer','PR-008','PARAFARMACIA','2.65','1.84','7','7');
INSERT INTO productos(name,code,product_type,pvp,pvf,stock,min_stock) VALUES ('Potito Manzana y Platano','PR-009','PARAFARMACIA','1.40','0.72','7','7');

--Pedidos
INSERT INTO pedidos(codigo,fecha_pedido,fecha_entrega,Estado,proveedor_id) VALUES ('P-006',null,null,2,null);
INSERT INTO pedidos(codigo,fecha_pedido,fecha_entrega,Estado,proveedor_id) VALUES ('P-005','2020-11-30',null,0,1);
INSERT INTO pedidos(codigo,fecha_pedido,fecha_entrega,Estado,proveedor_id) VALUES ('P-004','2020-11-29',null,3,1);
INSERT INTO pedidos(codigo,fecha_pedido,fecha_entrega,Estado,proveedor_id) VALUES ('P-003','2020-11-28','2020-11-29',1,1);
INSERT INTO pedidos(codigo,fecha_pedido,fecha_entrega,Estado,proveedor_id) VALUES ('P-002','2020-11-27','2020-11-28',1,2);
INSERT INTO pedidos(codigo,fecha_pedido,fecha_entrega,Estado,proveedor_id) VALUES ('P-001','2020-11-26','2020-11-27',1,2);


--LineaPedidos
INSERT INTO linea_Pedidos(cantidad,pedido_id,producto_id) VALUES (2,1,1);
INSERT INTO linea_Pedidos(cantidad,pedido_id,producto_id) VALUES (1,1,6);
INSERT INTO linea_Pedidos(cantidad,pedido_id,producto_id) VALUES (3,1,7);
INSERT INTO linea_Pedidos(cantidad,pedido_id,producto_id) VALUES (2,2,4);
INSERT INTO linea_Pedidos(cantidad,pedido_id,producto_id) VALUES (2,2,9);
INSERT INTO linea_Pedidos(cantidad,pedido_id,producto_id) VALUES (1,2,1);
INSERT INTO linea_Pedidos(cantidad,pedido_id,producto_id) VALUES (1,3,2);
INSERT INTO linea_Pedidos(cantidad,pedido_id,producto_id) VALUES (1,3,6);
INSERT INTO linea_Pedidos(cantidad,pedido_id,producto_id) VALUES (5,4,8);
INSERT INTO linea_Pedidos(cantidad,pedido_id,producto_id) VALUES (3,4,4);
INSERT INTO linea_Pedidos(cantidad,pedido_id,producto_id) VALUES (1,4,3);
INSERT INTO linea_Pedidos(cantidad,pedido_id,producto_id) VALUES (2,5,2);
INSERT INTO linea_Pedidos(cantidad,pedido_id,producto_id) VALUES (2,5,9);
INSERT INTO linea_Pedidos(cantidad,pedido_id,producto_id) VALUES (2,5,7);
INSERT INTO linea_Pedidos(cantidad,pedido_id,producto_id) VALUES (2,5,5);
INSERT INTO linea_Pedidos(cantidad,pedido_id,producto_id) VALUES (2,6,5);
INSERT INTO linea_Pedidos(cantidad,pedido_id,producto_id) VALUES (2,6,1);
INSERT INTO linea_Pedidos(cantidad,pedido_id,producto_id) VALUES (2,6,6);
INSERT INTO linea_Pedidos(cantidad,pedido_id,producto_id) VALUES (2,6,3);
INSERT INTO linea_Pedidos(cantidad,pedido_id,producto_id) VALUES (2,6,7);



