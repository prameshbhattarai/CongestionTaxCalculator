-- insert value in vehicles table
INSERT INTO vehicles (id, name, is_tax_exempt)
VALUES (1, 'Motorcycle', true),
       (2, 'Tractor', true),
       (3, 'Emergency', true) ON CONFLICT DO NOTHING;

-- insert value in city table
INSERT INTO city (id, name, max_taxable_amount, single_charge_time)
VALUES (1, 'Gothenborg', 60, 60),
       (2, 'Stockholm', 80, 60),
       (3, 'Malmo', 50, 60) ON CONFLICT DO NOTHING;

-- insert value in toll fees for Gothenborg city
INSERT INTO toll_fees (id, city_id, from_hour, from_minute, to_hour, to_minute, rate)
VALUES (1, 1, 6, 0, 6, 29, 8),
       (2, 1, 6, 30, 6, 59, 13),
       (3, 1, 7, 0, 7, 59, 18),
       (4, 1, 8, 0, 8, 29, 13),
       (5, 1, 8, 30, 14, 59, 8),
       (6, 1, 15, 0, 15, 29, 13),
       (7, 1, 15, 30, 16, 59, 18),
       (8, 1, 17, 0, 17, 59, 13),
       (9, 1, 18, 0, 18, 29, 8),
       (10, 1, 18, 30, 23, 0, 0),
       (11, 1, 0, 0, 5, 59, 0) ON CONFLICT DO NOTHING;

-- insert value in holidays table
INSERT INTO holidays (id, month, day)
VALUES (1, 1, 1),
       (2, 3, 28),
       (3, 3, 29),
       (4, 4, 1),
       (5, 4, 30),
       (5, 5, 1),
       (5, 5, 8),
       (5, 5, 9),
       (6, 6, 5),
       (6, 6, 6),
       (6, 11, 1),
       (6, 12, 24),
       (6, 12, 25),
       (6, 12, 26),
       (6, 12, 31) ON CONFLICT DO NOTHING;
