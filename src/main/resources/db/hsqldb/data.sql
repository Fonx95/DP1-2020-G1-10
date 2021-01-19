
-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities(username,authority) VALUES ('admin1','admin');

--Proveedores
INSERT INTO users(username,password,enabled) VALUES ('proveedor1','Prove3dor1',TRUE);
INSERT INTO authorities(username,authority) VALUES ('proveedor1','proveedor');
INSERT INTO proveedors(empresa,direccion,cif,username) VALUES ('COFARES','Direccion1','S4953142I','proveedor1');

INSERT INTO users(username,password,enabled) VALUES ('proveedor2','Prove3dor1',TRUE);
INSERT INTO authorities(username,authority) VALUES ('proveedor2','proveedor');
INSERT INTO proveedors(empresa,direccion,cif,username) VALUES ('CECOFAR','Direccion2','W7181885J','proveedor2');

--Clientes
INSERT INTO users(username,password,enabled) VALUES ('client1','client1',TRUE);
INSERT INTO authorities(username,authority) VALUES ('client1','cliente');
INSERT INTO clientes(name,surnames,dni,provincia,localidad,direccion,por_pagar_total,username) VALUES ('Luis', 'Rodriguez Mendez', '12345789H','Sevilla','Sevilla','Calle Maria 1','0.0','client1');

INSERT INTO users(username,password,enabled) VALUES ('client2','client2',TRUE);
INSERT INTO authorities(username,authority) VALUES ('client2','cliente');
INSERT INTO clientes(name,surnames,dni,provincia,localidad,direccion,por_pagar_total,username) VALUES ('Pedro', 'Fernandez Perez', '987654321H','Sevilla','Sevilla','Calle Maria 1','2.36','client2');

INSERT INTO users(username,password,enabled) VALUES ('client3','client3',TRUE);
INSERT INTO authorities(username,authority) VALUES ('client3','cliente');
INSERT INTO clientes(name,surnames,dni,provincia,localidad,direccion,por_pagar_total,username) VALUES ('Ana Maria', 'Pozo Cobos', '458796254A','Sevilla','Sevilla','Calle Maria 1','9.15','client3');

INSERT INTO users(username,password,enabled) VALUES ('client4','client4',TRUE);
INSERT INTO authorities(username,authority) VALUES ('client4','cliente');
INSERT INTO clientes(name,surnames,dni,provincia,localidad,direccion,por_pagar_total,username) VALUES ('Patricia', 'Mendez Pelayo', '763154952C','Sevilla','Sevilla','Calle Maria 1','0.0','client4');

--Farmaceutico
INSERT INTO users(username,password,enabled) VALUES ('farm1','farm1',TRUE);
INSERT INTO authorities(username,authority) VALUES ('farm1','farmaceutico');
INSERT INTO farmaceutico(name,surnames,dni,address,username) VALUES ('Pepe', 'Rodriguez Rodriguez', '12345689H','Calle Vieja','farm1');

--Productos
INSERT INTO productos(name,code,product_type,pvp,pvf,stock,min_stock) VALUES ('Aspirina','PR-001','FARMACOSINRECETA','5.25','4.62','15','5');
INSERT INTO productos(name,code,product_type,pvp,pvf,stock,min_stock) VALUES ('Dalsy','PR-002','FARMACOCONRECETA','6.75','5.74','3','2');
INSERT INTO productos(name,code,product_type,pvp,pvf,stock,min_stock) VALUES ('Dolocatil','PR-003','FARMACOSINRECETA','7.55','6.11','4','4');
INSERT INTO productos(name,code,product_type,pvp,pvf,stock,min_stock) VALUES ('Mariguanilla','PR-004','ESTUPEFACIENTE','12.60','10.68','1','1');
INSERT INTO productos(name,code,product_type,pvp,pvf,stock,min_stock) VALUES ('Cinfatos','PR-005','FARMACOSINRECETA','9.20','8.34','4','3');
INSERT INTO productos(name,code,product_type,pvp,pvf,stock,min_stock) VALUES ('Gelocatil Gripe','PR-006','FARMACOCONRECETA','6.10','5.23','15','10');
INSERT INTO productos(name,code,product_type,pvp,pvf,stock,min_stock) VALUES ('Flumil Forte','PR-007','FARMACOSINRECETA','2.60','2.03','7','7');
INSERT INTO productos(name,code,product_type,pvp,pvf,stock,min_stock) VALUES ('Dentrífico Lacer','PR-008','PARAFARMACIA','2.65','1.84','7','7');
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

--Ventas
INSERT INTO ventas(fecha,importe_total,pagado,por_pagar,estado,cliente_id) VALUES('2020-11-26','5.25','5.25','0.0',1,null);
INSERT INTO ventas(fecha,importe_total,pagado,por_pagar,estado,cliente_id) VALUES('2020-11-27','9.15','0.0','9.15',1,3);
INSERT INTO ventas(fecha,importe_total,pagado,por_pagar,estado,cliente_id) VALUES('2020-11-28','5.43','5.0','0.43',1,2);
INSERT INTO ventas(fecha,importe_total,pagado,por_pagar,estado,cliente_id) VALUES('2020-11-28','0.0','0.0','0.0',1,null);
INSERT INTO ventas(fecha,importe_total,pagado,por_pagar,estado,cliente_id) VALUES('2020-11-29','6.93','5.0','1.93',1,2);
INSERT INTO ventas(fecha,importe_total,pagado,por_pagar,estado,cliente_id) VALUES(null,'0.0','0.0','0.0',0,null);

--Comprador
INSERT INTO comprador(name,apellidos,dni,venta_id) VALUES('Pedro','Fernandez Perez','987654321H',3);
INSERT INTO comprador(name,apellidos,dni,venta_id) VALUES('Valeria','Soto Perea','459834196D',5);

--LineaVentas
INSERT INTO linea_Venta(tipo_Tasa,cantidad,importe,venta_id,producto_id) VALUES('TSI001',1,'5.25',1,1);
INSERT INTO linea_Venta(tipo_Tasa,cantidad,importe,venta_id,producto_id) VALUES('TSI002',1,'6.50',2,3);
INSERT INTO linea_Venta(tipo_Tasa,cantidad,importe,venta_id,producto_id) VALUES('TSI001',1,'2.65',2,8);
INSERT INTO linea_Venta(tipo_Tasa,cantidad,importe,venta_id,producto_id) VALUES('TSI001',2,'2.80',3,9);
INSERT INTO linea_Venta(tipo_Tasa,cantidad,importe,venta_id,producto_id) VALUES('TSI004',1,'1.64',3,4);
INSERT INTO linea_Venta(tipo_Tasa,cantidad,importe,venta_id,producto_id) VALUES('TSI004',1,'0.99',3,3);
INSERT INTO linea_Venta(tipo_Tasa,cantidad,importe,venta_id,producto_id) VALUES('TSI005',1,'0.0',4,5);
INSERT INTO linea_Venta(tipo_Tasa,cantidad,importe,venta_id,producto_id) VALUES('TSI005',1,'0.0',4,6);
INSERT INTO linea_Venta(tipo_Tasa,cantidad,importe,venta_id,producto_id) VALUES('TSI003',1,'6.93',5,4);




