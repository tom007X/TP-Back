SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO address (city, postal_code, street, number, latitude, longitude) VALUES
('Córdoba',                '5000', 'Av. Colón',             '1234', -31.4045, -64.2070), -- 1
('Buenos Aires',           'C1425','Av. Santa Fe',          '3456', -34.5884, -58.4055), -- 2
('Rosario',                '2000', 'Bv. Oroño',             '789',  -32.9496, -60.6445), -- 3
('Mendoza',                '5500', 'Av. San Martín',        '1523', -32.8908, -68.8272), -- 4
('La Plata',               '1900', 'Calle 7',               '890',  -34.9214, -57.9544), -- 5
('Mar del Plata',          '7600', 'Av. Colón',             '2210', -38.0055, -57.5426), -- 6
('San Miguel de Tucumán',  '4000', 'Av. Mate de Luna',      '4510', -26.8241, -65.2226), -- 7
('Salta',                  '4400', 'Av. Belgrano',          '330',  -24.7883, -65.4106), -- 8
('Neuquén',                '8300', 'Av. Argentina',         '270',  -38.9516, -68.0591), -- 9
('San Carlos de Bariloche','8400', 'Mitre',                 '555',  -41.1335, -71.3103), -- 10
('Villa Carlos Paz',       '5152', 'Av. San Martín',        '300',  -31.4240, -64.4977),  -- 11
('Río Cuarto',             '5800', 'Av. España',            '950',  -33.1232, -64.3493),  -- 12
('Villa María',            '5900', 'Bv. España',            '1200', -32.4105, -63.2439),  -- 13
('San Francisco (Cba)',    '2400', 'Av. Libertador Sur',    '500',  -31.4300, -62.0900),  -- 14
('Alta Gracia',            '5186', 'Av. Belgrano',          '250',  -31.6520, -64.4281),  -- 15
('Bahía Blanca',           '8000', 'Av. Alem',              '1200', -38.7183, -62.2663),  -- 16
('Posadas',                '3300', 'Av. López y Planes',    '4550', -27.3621, -55.9009),  -- 17
('Resistencia',            '3500', 'Av. 25 de Mayo',        '2100', -27.4516, -58.9863),  -- 18
('Paraná',                 '3100', 'Bv. Racedo',            '780',  -31.7319, -60.5238),  -- 19
('Santa Fe',               '3000', 'Bv. Pellegrini',        '2500', -31.6420, -60.7000),  -- 20
('San Juan',               '5400', 'Av. Ignacio de la Roza','345',  -31.5375, -68.5364),  -- 21
('San Luis',               '5700', 'Av. Illia',             '910',  -33.2997, -66.3397),  -- 22
('Trelew',                 '9100', 'Av. Fontana',           '620',  -43.2509, -65.3051),  -- 23
('Comodoro Rivadavia',     '9000', 'Av. Rivadavia',         '1010', -45.8647, -67.4800),  -- 24
('Ushuaia',                '9410', 'San Martín',            '750',  -54.8019, -68.3030),  -- 25
('Río Gallegos',           '9400', 'Av. Kirchner',          '1300', -51.6230, -69.2168),  -- 26
('San Salvador de Jujuy',  '4600', 'Av. 19 de Abril',       '1880', -24.1861, -65.2995),  -- 27
('Formosa',                '3600', 'Av. 25 de Mayo',        '600',  -26.1825, -58.1767),  -- 28
('Catamarca',              '4700', 'Rivadavia',             '430',  -28.4696, -65.7852),  -- 29
('Santiago del Estero',    '4200', 'Belgrano',              '980',  -27.7844, -64.2642);  -- 30

INSERT INTO deposit (name, address_id, daily_storage_cost) VALUES
('Depósito Córdoba Colón',                   1, 10000),
('Depósito BA Santa Fe',                     2, 20000),
('Depósito Rosario Oroño',                   3, 15000),
('Depósito Mendoza San Martín',              4, 10060),
('Depósito La Plata Calle 7',                5, 13000),
('Depósito MDP Colón',                       6, 10640),
('Depósito Tucumán Mate de Luna',            7, 5600),
('Depósito Salta Belgrano',                  8, 16000),
('Depósito Neuquén Av. Argentina',           9, 25000),
('Depósito Bariloche Mitre',                10, 32200),
('Depósito Villa Carlos Paz San Martín',    11, 11800),
('Depósito Río Cuarto España',              12, 11200),
('Depósito Villa María Bv. España',         13, 10900),
('Depósito San Francisco Libertador',       14, 10400),
('Depósito Alta Gracia Belgrano',           15, 10150),
('Depósito Bahía Blanca Alem',              16, 17200),
('Depósito Posadas López y Planes',         17, 9800),
('Depósito Resistencia 25 de Mayo',         18, 9400),
('Depósito Paraná Racedo',                  19, 10200),
('Depósito Santa Fe Pellegrini',            20, 13600),
('Depósito San Juan Ignacio de la Roza',    21, 12000),
('Depósito San Luis Illia',                 22, 10800),
('Depósito Trelew Fontana',                 23, 9500),
('Depósito Comodoro Rivadavia Rivadavia',   24, 14900),
('Depósito Ushuaia San Martín',             25, 21000),
('Depósito Río Gallegos Kirchner',          26, 18000),
('Depósito Jujuy 19 de Abril',              27, 9700),
('Depósito Formosa 25 de Mayo',             28, 9000),
('Depósito Catamarca Rivadavia',            29, 8800),
('Depósito Santiago del Estero Belgrano',   30, 9200);

INSERT INTO container (code, weight, volume, container_state_id, client_id) VALUES
('CONT001', 2000, 12.5, 1, 1),
('CONT002', 1500, 10.0, 2, 2),
('CONT003', 3000, 15.0, 3, 3),
('CONT004', 2500, 13.0, 1, 4),
('CONT005', 1800, 11.0, 2, 5);

INSERT INTO tariff (subject, metric, value, available) VALUES
('Base',    '_',        100, 1),
('Truck',   'distance', 250, 1),
('Deposit', 'day',       50, 1),
('Container', 'volume',   5, 1),
('Container', 'weight',   10, 1);