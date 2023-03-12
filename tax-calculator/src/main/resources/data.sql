-- insert value in vehicles table
INSERT INTO vehicles (id, name, is_tax_exempt)
VALUES
(1, 'Motorcycle', true),
(2, 'Tractor', true),
(3, 'Emergency', true)
ON CONFLICT DO NOTHING;

-- insert value in city table
INSERT INTO city (id, name)
VALUES
(1, 'Gothenborg'),
(2, 'Stockholm'),
(3, 'Malmo')
ON CONFLICT DO NOTHING;

-- insert value in toll fees for Gothenborg city
INSERT INTO toll_fees (id, city_id, from_hour, from_minute, to_hour, to_minute, rate)
VALUES
(1, 1, 6, 0, 6, 29, 8),
(2, 1, 6, 30, 6, 59, 13),
(3, 1, 7, 0, 7, 59, 18),
(4, 1, 8, 0, 8, 29, 13),
(5, 1, 8, 30, 14, 59, 8),
(6, 1, 15, 0, 15, 29, 13),
(7, 1, 15, 30, 16, 59, 18),
(8, 1, 17, 00, 17, 59, 13),
(9, 1, 18, 00, 18, 29, 8),
(10, 1, 18, 30, 23, 0, 0),
(11, 1, 0, 0, 5, 59, 0)
ON CONFLICT DO NOTHING;
