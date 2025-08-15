-- Insere um organizador de teste
INSERT INTO users (id, name, email, created_at, photo)
VALUES (123, 'Organizer Test', 'organizer@test.com', NOW(), 'https://example.com/organizer_photo.png');

-- Insere participantes de teste
INSERT INTO users (id, name, email, created_at, photo)
VALUES 
  (456, 'Participant Test 1', 'participant1@test.com', NOW(), 'https://example.com/participant1_photo.png');

INSERT INTO users (id, name, email, created_at, photo)
VALUES 
  (457, 'Participant Test 2', 'participant2@test.com', NOW(), 'https://example.com/participant2_photo.png');

-- Insere um evento de teste
INSERT INTO event 
    (id, name, location, sport, level, gender, date, start_time, end_time, price, description, organizer_id, organizer_photo)
VALUES 
    (3, 'Test Event', 'Test Location', 'Soccer', 'Intermediate', 'M', '2025-08-20', '16:00:00', '18:00:00', 20.00, 'This is a test event description.', 123, 'https://example.com/organizer_photo.png');

-- Insere registros de entrada (event_entry) para simular os participantes já cadastrados no evento
INSERT INTO event_entry (id, event_id, user_id, requested_at, status)
VALUES (1, 3, 456, NOW(), 'ACCEPTED');

INSERT INTO event_entry (id, event_id, user_id, requested_at, status)
VALUES (2, 3, 457, NOW(), 'ACCEPTED');

-- Opcional: Insere notificações para o organizador informando que houve solicitação de entrada
INSERT INTO notification 
    (id, user_id, event_id, type, icon_name, title, description, timestamp, related_event_id, read_flag)
VALUES 
    (1, 123, 3, 'entry_request', 'info', 'Pedido de entrada', 'Um usuário solicitou entrada no evento: Test Event', NOW(), 3, FALSE);