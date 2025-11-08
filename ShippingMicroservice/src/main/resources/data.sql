-- Section Type LUT
INSERT INTO section_type_lut (code, name) VALUES (1, 'origen-deposito')
ON DUPLICATE KEY UPDATE name = VALUES(name);

INSERT INTO section_type_lut (code, name) VALUES (2, 'deposito-deposito')
ON DUPLICATE KEY UPDATE name = VALUES(name);

INSERT INTO section_type_lut (code, name) VALUES (3, 'deposito-destino')
ON DUPLICATE KEY UPDATE name = VALUES(name);

INSERT INTO section_type_lut (code, name) VALUES (4, 'origen-destino')
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- Section Status LUT
INSERT INTO section_status_lut (code, name) VALUES (1, 'estimado')
ON DUPLICATE KEY UPDATE name = VALUES(name);

INSERT INTO section_status_lut (code, name) VALUES (2, 'asignado')
ON DUPLICATE KEY UPDATE name = VALUES(name);

INSERT INTO section_status_lut (code, name) VALUES (3, 'iniciado')
ON DUPLICATE KEY UPDATE name = VALUES(name);

INSERT INTO section_status_lut (code, name) VALUES (4, 'finalizado')
ON DUPLICATE KEY UPDATE name = VALUES(name);