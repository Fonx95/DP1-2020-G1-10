-- One admin user, named admin1 with passwor 4dm1n and authority admin
INSERT INTO users(username,password,enabled) VALUES ('admin1','4dm1n',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (1,'admin1','admin');

INSERT INTO users(username,password,enabled) VALUES ('farm1','farm1',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (2,'farm1','admin');

INSERT INTO users(username,password,enabled) VALUES ('client1','client1',TRUE);
INSERT INTO authorities(id,username,authority) VALUES (3,'client1','admin');

INSERT INTO farmaceutico VALUES (1, 'Pepe', 'Rodriguez Rodriguez', '123456789H','Calle Vieja','farm1');
