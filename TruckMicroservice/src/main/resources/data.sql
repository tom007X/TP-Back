SET FOREIGN_KEY_CHECKS = 1;


-- 4 DRIVERS
INSERT INTO driver (name, surname, phone) VALUES
 ('Martín',   'González',  '+54 9 351 555-0101'),
 ('Lucía',    'Fernández', '+54 9 11 4444-0202'),
 ('Sofía',    'Pereyra',   '+54 9 341 333-0303'),
 ('Diego',    'Suárez',    '+54 9 261 222-0404');

-- 5 TRUCKS (mix old ABC123 and new AA123AA formats; unique license_plate)
-- Link some trucks to drivers via driver_id 1..4 (adjust if your PKs start elsewhere)
INSERT INTO truck (license_plate, driver_id, max_weight, max_volume, cost_per_km, fuel_consuptiom_per_km, is_available) VALUES
    ('ABC123',   1,    12000, 38.5, 350.00, 0.32, 1),
    ('AA123AA',  2,    14000, 45.0, 380.00, 0.34, 1),
    ('ACB987',   3,    10000, 30.0, 320.00, 0.28, 1),
    ('AB234CD',  4,    16000, 50.0, 410.00, 0.36, 0),
     ('ZZZ001',   NULL,  8000, 24.0, 300.00, 0.26, 1);