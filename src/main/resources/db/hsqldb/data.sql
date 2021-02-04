
-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities(username,authority) VALUES ('admin1','admin');

--Proveedores
INSERT INTO users(username,password,enabled) VALUES ('proveedor1','Prove3dor1',TRUE);
INSERT INTO authorities(username,authority) VALUES ('proveedor1','proveedor');
INSERT INTO proveedors(empresa,direccion,cif,username) VALUES ('COFARES','Poligono industrial Sevilla','F-28140119','proveedor1');

INSERT INTO users(username,password,enabled) VALUES ('proveedor2','Prove3dor1',TRUE);
INSERT INTO authorities(username,authority) VALUES ('proveedor2','proveedor');
INSERT INTO proveedors(empresa,direccion,cif,username) VALUES ('CECOFAR','Poligono industrial Sevilla','F-90280116','proveedor2');

--Clientes
INSERT INTO users(username,password,enabled) VALUES ('client1','client1',TRUE);
INSERT INTO authorities(username,authority) VALUES ('client1','cliente');
INSERT INTO clientes(name,surnames,dni,provincia,localidad,direccion,por_pagar_total,username) VALUES ('Luis', 'Rodriguez Mendez', '12345789H','Sevilla','Sevilla','Calle Reina Mercedes','0.0','client1');

INSERT INTO users(username,password,enabled) VALUES ('client2','client2',TRUE);
INSERT INTO authorities(username,authority) VALUES ('client2','cliente');
INSERT INTO clientes(name,surnames,dni,provincia,localidad,direccion,por_pagar_total,username) VALUES ('Pedro', 'Fernandez Perez', '987654321H','Sevilla','Sevilla','Calle Reina Mercedes','2.36','client2');

INSERT INTO users(username,password,enabled) VALUES ('client3','client3',TRUE);
INSERT INTO authorities(username,authority) VALUES ('client3','cliente');
INSERT INTO clientes(name,surnames,dni,provincia,localidad,direccion,por_pagar_total,username) VALUES ('Ana Maria', 'Pozo Cobos', '458796254A','Sevilla','Sevilla','Calle Reina Mercedes','9.15','client3');

INSERT INTO users(username,password,enabled) VALUES ('client4','client4',TRUE);
INSERT INTO authorities(username,authority) VALUES ('client4','cliente');
INSERT INTO clientes(name,surnames,dni,provincia,localidad,direccion,por_pagar_total,username) VALUES ('Patricia', 'Mendez Pelayo', '763154952C','Sevilla','Sevilla','Calle Reina Mercedes','0.0','client4');

INSERT INTO clientes(name,surnames,dni,provincia,localidad,direccion,por_pagar_total) VALUES ('Maria', 'Rodriguez Bonilla', '99999999C','Sevilla','Sevilla','Calle Reina Mercedes','0.0');

--Farmaceutico
INSERT INTO users(username,password,enabled) VALUES ('farm1','farm1',TRUE);
INSERT INTO authorities(username,authority) VALUES ('farm1','farmaceutico');
INSERT INTO farmaceutico(name,surnames,dni,address,username) VALUES ('Jose', 'Rodriguez Garcia', '12345689H','Calle Sor Gregoria','farm1');

--Productos
INSERT INTO productos(name,code,product_type,pvp,pvf,stock,min_stock) VALUES ('ASPIRINA','PR-001','FARMACOSINRECETA','5.25','4.62','15','5');--Analgesico, Anticoagulante
INSERT INTO productos(name,code,product_type,pvp,pvf,stock,min_stock) VALUES ('DALSY','PR-002','FARMACOSINRECETA','6.75','5.74','3','2');--Analgesico, Antipiretico
INSERT INTO productos(name,code,product_type,pvp,pvf,stock,min_stock) VALUES ('ZAMENE','PR-003','FARMACOCONRECETA','7.55','6.11','4','4');--Corticoide
INSERT INTO productos(name,code,product_type,pvp,pvf,stock,min_stock) VALUES ('MORFINA','PR-004','ESTUPEFACIENTE','12.60','10.68','1','1');--Analgesico
INSERT INTO productos(name,code,product_type,pvp,pvf,stock,min_stock) VALUES ('DUROGESIC MATRIX','PR-005','ESTUPEFACIENTE','12.60','10.68','1','1');--Analgesico
INSERT INTO productos(name,code,product_type,pvp,pvf,stock,min_stock) VALUES ('CINFATOS','PR-006','FARMACOSINRECETA','9.20','8.34','4','3');--Antitusivo
INSERT INTO productos(name,code,product_type,pvp,pvf,stock,min_stock) VALUES ('METFORMINA','PR-007','FARMACOCONRECETA','6.10','5.23','15','10');--Hipoglucemiante
INSERT INTO productos(name,code,product_type,pvp,pvf,stock,min_stock) VALUES ('AMERIDE','PR-008','FARMACOCONRECETA','2.60','2.03','7','7');--Diuretico, Antihipertensivo
INSERT INTO productos(name,code,product_type,pvp,pvf,stock,min_stock) VALUES ('POLARAMINE','PR-009','FARMACOCONRECETA','2.60','2.03','7','7');--Antihistaminico
INSERT INTO productos(name,code,product_type,pvp,pvf,stock,min_stock) VALUES ('TRINOMIA','PR-010','FARMACOCONRECETA','2.60','2.03','7','7');--Antiagregante Plaquetario, Antihipertensivo, Hipolipemiante
INSERT INTO productos(name,code,product_type,pvp,pvf,stock,min_stock) VALUES ('TOBRADEX','PR-011','FARMACOCONRECETA','2.60','2.03','7','7');--Antibiotico, Corticoide
INSERT INTO productos(name,code,product_type,pvp,pvf,stock,min_stock) VALUES ('DENTRIFICO LACER','PR-012','PARAFARMACIA','2.65','1.84','7','7');
INSERT INTO productos(name,code,product_type,pvp,pvf,stock,min_stock) VALUES ('POTITO MANZANA Y PLATANO','PR-013','PARAFARMACIA','1.40','0.72','7','7');

--TipoMedicamento
INSERT INTO tipo_medicamentos(tipo,descripcion) VALUES ('Analgesico','Son medicinas que reducen o alivian los dolores de cabeza, musculares, artríticos o muchos otros achaques y dolores.');
INSERT INTO tipo_medicamentos(tipo,descripcion) VALUES ('Anticoagulante','Son medicamentos que previenen la formación de coágulos sanguíneos. También evitan que los coágulos de sangre ya existentes se hagan más grandes.');
INSERT INTO tipo_medicamentos(tipo,descripcion) VALUES ('Antipiretico','Son medicamentos que disminuyen la fiebre. Suelen ser medicamentos que tratan la fiebre de una forma sintomática, sin actuar sobre su causa.');
INSERT INTO tipo_medicamentos(tipo,descripcion) VALUES ('Corticoide','Estas sustancias operan de modo fisiológico para atenuar las respuestas del cuerpo a los procesos inflamatarios, controlar situaciones de estrés, inhibir la fagocitosis y otras cuestiones.');
INSERT INTO tipo_medicamentos(tipo,descripcion) VALUES ('Antitusivo','Son medicamentos que se emplean para tratar la tos seca irritativa, no productiva. Los antitusígenos son compuestos que actúan sobre el sistema nervioso central o periférico para suprimir el reflejo de la tos.');
INSERT INTO tipo_medicamentos(tipo,descripcion) VALUES ('Hipoglucemiante','Son un tipo de medicamentos empleados para disminuir los niveles de azúcar en la sangre, principalmente en los casos de resistencia a la insulina y la diabetes. Existen muchos tipos de medicamentos hipoglucemiantes, que se clasifican de acuerdo a su estructura molecular y su mecanismo de acción.');
INSERT INTO tipo_medicamentos(tipo,descripcion) VALUES ('Diuretico','Son un tipo de medicamento que hace que los riñones produzcan más orina. Los diuréticos ayudan al cuerpo a eliminar el líquido y la sal sobrante. Se usan para tratar la presión arterial alta, el edema (líquido extra en los tejidos) y otras afecciones.');
INSERT INTO tipo_medicamentos(tipo,descripcion) VALUES ('Antihipertensivo','Son medicamentos utilizados para disminuir el riesgo cardiovascular en los pacientes con hipertensión arterial controlando la presión arterial hasta niveles adecuados. La hipertensión arterial es una enfermedad de la pared arterial de los vasos sanguíneos.');
INSERT INTO tipo_medicamentos(tipo,descripcion) VALUES ('Antihistaminico','Tipo de medicamento que bloquea la acción de la histamina, sustancia que puede causar fiebre, picazón, estornudos, mucosidad nasal y lagrimeo. Los antihistamínicos se utilizan para prevenir la fiebre en los pacientes que se someten a transfusiones de sangre y para el tratamiento de alergias, tos y resfriados.');
INSERT INTO tipo_medicamentos(tipo,descripcion) VALUES ('Antiagregante Plaquetario','Son un grupo de fármacos cuyo principal efecto es inhibir el funcionalismo de las plaquetas, evitando así su agregación y la consiguiente formación de trombos en el interior de los vasos sanguíneos.');
INSERT INTO tipo_medicamentos(tipo,descripcion) VALUES ('Hipolipemiante','Es una sustancia farmacológicamente activa que tenga la propiedad de disminuir los niveles de lípidos en sangre. En el sistema de clasificación anatómica, terapéutica y química, forman un grupo homogéneo denominado C10.');
INSERT INTO tipo_medicamentos(tipo,descripcion) VALUES ('Antibiotico','Son medicamentos que combaten las infecciones bacterianas en personas y animales. Funcionan matando las bacterias o dificultando su crecimiento y multiplicación.');

--Relacion_productos_tipoMedicamentos                                              tipo/producto
INSERT INTO rel_productos_tipo_medicamentos(Id_producto,Id_tipo_medicamento) VALUES (1,1);
INSERT INTO rel_productos_tipo_medicamentos(Id_producto,Id_tipo_medicamento) VALUES (1,2);
INSERT INTO rel_productos_tipo_medicamentos(Id_producto,Id_tipo_medicamento) VALUES (2,1);
INSERT INTO rel_productos_tipo_medicamentos(Id_producto,Id_tipo_medicamento) VALUES (2,3);
INSERT INTO rel_productos_tipo_medicamentos(Id_producto,Id_tipo_medicamento) VALUES (3,4);
INSERT INTO rel_productos_tipo_medicamentos(Id_producto,Id_tipo_medicamento) VALUES (4,1);
INSERT INTO rel_productos_tipo_medicamentos(Id_producto,Id_tipo_medicamento) VALUES (5,1);
INSERT INTO rel_productos_tipo_medicamentos(Id_producto,Id_tipo_medicamento) VALUES (6,5);
INSERT INTO rel_productos_tipo_medicamentos(Id_producto,Id_tipo_medicamento) VALUES (7,6);
INSERT INTO rel_productos_tipo_medicamentos(Id_producto,Id_tipo_medicamento) VALUES (8,7);
INSERT INTO rel_productos_tipo_medicamentos(Id_producto,Id_tipo_medicamento) VALUES (8,8);
INSERT INTO rel_productos_tipo_medicamentos(Id_producto,Id_tipo_medicamento) VALUES (9,9);
INSERT INTO rel_productos_tipo_medicamentos(Id_producto,Id_tipo_medicamento) VALUES (10,10);
INSERT INTO rel_productos_tipo_medicamentos(Id_producto,Id_tipo_medicamento) VALUES (10,8);
INSERT INTO rel_productos_tipo_medicamentos(Id_producto,Id_tipo_medicamento) VALUES (10,11);
INSERT INTO rel_productos_tipo_medicamentos(Id_producto,Id_tipo_medicamento) VALUES (11,12);
INSERT INTO rel_productos_tipo_medicamentos(Id_producto,Id_tipo_medicamento) VALUES (11,4);

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




