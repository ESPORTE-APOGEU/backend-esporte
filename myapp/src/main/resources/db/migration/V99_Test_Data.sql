
VALUES (123, 'Organizer Test', 'organizer@test.com', NOW(), 'https://example.com/organizer_photo.png');

-- Insere o usuário participante (ID 456)
INSERT INTO users (id, name, email, created_at, photo)
VALUES (456, 'Participant Test', 'participant@test.com', NOW(), 'https://example.com/participant_photo.png');

-- Insere um evento completo com o organizador já definido (ID 3)
INSERT INTO event 
    (id, name, location, sport, level, gender, date, start_time, end_time, price, description, organizer_id, organizer_photo)
VALUES 
    (3, 'Test Event', 'Test Location', 'Soccer', 'Intermediate', 'M', '2025-08-20', '16:00:00', '18:00:00', 20.00, 'This is a test event description.', 123, 'https://example.com/organizer_photo.png');

-- Insere uma entrada de evento solicitada pelo participante (ID 1)
INSERT INTO event_entry (id, event_id, user_id, requested_at, status)
VALUES (1, 3, 456, NOW(), 'PENDING');

-- Insere uma notificação para o organizador informando um pedido de entrada
INSERT INTO notification 
    (id, user_id, event_id, type, icon_name, title, description, timestamp, related_event_id, read_flag)
VALUES 
    (1, 123, 3, 'entry_request', 'info', 'Pedido de entrada', 'Um usuário solicitou entrada no evento: Test Event', NOW(), 3, FALSE);