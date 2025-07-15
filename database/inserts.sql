-- INSERTS PARA TABLA LOCALES
INSERT INTO Locales (nombre, ubicacion, horario, horarioDespacho, imagenUrl, abierto, telefono, metodosPago)
VALUES ('Comedor de Arte', 'Facultad de Arte', '8:00 AM - 9:00 PM', '11:00 AM - 2:45 PM',
        'static/images/comedor_arte.png', 1, '+51 970 749 560', 'Efectivo,Tarjeta,Yape,Plin'),
       ('Comedor de Letras', 'Facultad de Letras', '8:00 AM - 9:00 PM', '11:00 AM - 2:45 PM',
        'static/images/comedor_letras.png', 1, '+51 970 749 233', 'Efectivo,Tarjeta,Yape,Plin'),
       ('Comedor Central', 'Al costado del Auditorio de la Facultad de Derecho', '8:00 AM - 9:00 PM', NULL,
        'static/images/comedor_central.png', 1, NULL, 'Efectivo,Tarjeta,Yape,Plin'),
       ('Kilomío', 'Aulario del Complejo de Innovación Académica', '8:00 AM - 9:00 PM', NULL,
        'static/images/kilomio.png', 1, NULL, 'Efectivo,Tarjeta,Yape,Plin,QR,Transferencia'),
       ('El Puesto', 'Al costado de Estudios Generales Ciencias', '7:45 AM - 9:00 PM', NULL,
        'static/images/el_puesto.png', 1, NULL, 'Efectivo,Tarjeta,Yape,Plin'),
       ('Refilo', 'Patio de comidas del Tinkuy', '9:00 AM - 9:00 PM', NULL, 'static/images/refilo.png', 1, NULL,
        'Efectivo,Tarjeta'),
       ('Charlotte', 'Complejo Mac Gregor (pabellón N)', '8:00 AM - 4:00 PM', NULL, 'static/images/charlotte.png', 0,
        NULL, 'Efectivo,Tarjeta,Yape,Plin');

-- INSERTS PARA USUARIOS
INSERT INTO Usuarios (nombre, correo, tipo, userName, contrasena, fotoPerfil)
VALUES ('Juan Pérez', 'juan.perez@pucp.edu.pe', 'Cliente', 'JP', 'password123', NULL),
       ('María López', 'maria.lopez@pucp.edu.pe', 'Encargado', 'ML', 'password456', NULL),
       ('Carlos Ruiz', 'carlos.ruiz@pucp.edu.pe', 'Cliente', 'CR', 'password789', NULL),
       ('Ana Torres', 'ana.torres@pucp.edu.pe', 'Encargado', 'AT', 'password321', NULL);

-- INSERTS PARA sqlite_sequence (mantener consistencia de AUTOINCREMENT si necesario)
INSERT INTO sqlite_sequence (name, seq)
VALUES ('Usuarios', 4);
INSERT INTO sqlite_sequence (name, seq)
VALUES ('Locales', 7);


-- Tipos de locales
INSERT INTO TiposLocales (nombre)
VALUES ('Comedor'),
       ('Cafetería'),
       ('Quiosco'),
       ('Refilo');

-- Relación Local-Tipo
INSERT INTO Local_Tipo (localId, tipoId)
VALUES (1, 1),
       (1, 2),
       (1, 3), -- Comedor de Arte
       (2, 1),
       (2, 2),
       (2, 3), -- Comedor de Letras
       (6, 4), -- Refilo
       (7, 2);
-- Charlotte

-- Menús del día
INSERT INTO Menus (nombre, tipo, entrada, principal, postre, bebida, precio, imagenUrl, fecha, localId)
VALUES ('Menú Newrest', 'Regular', 'Sopa de pollo', 'Lomo saltado con arroz', 'Gelatina', 'Chicha morada', 9.80,
        'static/images/menu_newrest.png', '2025-07-15', 1),
       ('Menú Vegano', 'Vegano', 'Ensalada de quinua', 'Guiso de lentejas', 'Compota de manzana', 'Agua de hierbas',
        14.00, 'static/images/menu_vegano.png', '2025-07-15', 1),
       ('Combo Desayuno', 'Desayuno', NULL, 'Sándwich de jamón y queso', NULL, 'Jugo de naranja', 10.50,
        'static/images/combo_desayuno.png', '2025-07-15', 6);

-- Promociones
INSERT INTO Promociones (titulo, descripcion, precio, colorHex, localId)
VALUES ('Almuerzo con descuento', 'Menú regular con 10% de descuento', 8.80, '#FFD700', 1),
       ('Desayuno 2x1', 'Compra un desayuno y llévate otro gratis', 10.50, '#FF69B4', 6);

-- Favoritos
INSERT INTO Favoritos (usuarioId, localId)
VALUES (1, 1),
       (3, 2),
       (1, 6);

-- Historial
INSERT INTO Historial (usuarioId, localId, fecha, detalle)
VALUES (1, 1, '2025-07-14', 'Menú Newrest reservado'),
       (3, 2, '2025-07-14', 'Menú Vegano reservado');

-- Reservas
INSERT INTO Reservas (codigoReserva, localId, usuarioId, menuId, fecha, hora, estado, mesa, metodoPago)
VALUES ('PUCP-123456', 1, 1, 1, '2025-07-15', '12:30 PM', 'Confirmada', 'A1', 'Efectivo'),
       ('PUCP-789012', 2, 3, 2, '2025-07-15', '1:00 PM', 'Confirmada', 'B2', 'Yape');