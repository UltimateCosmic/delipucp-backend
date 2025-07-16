-- Inserts para Locales
INSERT INTO Locales (id, nombre, ubicacion, horario, imagenUrl, abierto, telefono, metodosPago)
VALUES (1, 'Comedor de Arte', 'Facultad de Arte', '8:00 AM - 9:00 PM', 'comedor_arte.png', 1, '+51 970 749 560', 'Efectivo, Tarjeta, Yape, Plin'),
       (2, 'Comedor de Letras', 'Facultad de Letras', '8:00 AM - 9:00 PM', 'comedor_letras.png', 1, '+51 970 749 233', 'Efectivo, Tarjeta, Yape, Plin'),
       (3, 'Comedor Central', 'Al costado del Auditorio de la Facultad de Derecho', '8:00 AM - 9:00 PM', 'comedor_central.png', 1, NULL, 'Efectivo, Tarjeta, Yape, Plin'),
       (4, 'Kilomío', 'Aulario del Complejo de Innovación Académica', '8:00 AM - 9:00 PM', 'kilomio.png', 1, NULL, 'Efectivo, Tarjeta, Yape, Plin, QR, Transferencia'),
       (5, 'El Puesto', 'Al costado de Estudios Generales Ciencias', '7:45 AM - 9:00 PM', 'el_puesto.png', 1, NULL, 'Efectivo, Tarjeta, Yape, Plin'),
       (6, 'Refilo', 'Patio de comidas del Tinkuy', '9:00 AM - 9:00 PM', 'refilo.png', 1, NULL, 'Efectivo, Tarjeta'),
       (7, 'Charlotte', 'Complejo Mac Gregor (pabellón N)', '8:00 AM - 4:00 PM', 'charlotte.png', 0, NULL, 'Efectivo, Tarjeta, Yape, Plin');

-- Inserts para Platillos y relación con Locales abiertos
INSERT INTO Platillos (nombre, descripcion, imagenResId, precio, stock, localId) VALUES
-- Comedor de Arte (id=1)
('Adobo de Chancho', 'Delicioso adobo de chancho acompañado de arroz y camote.', 1, 15.0, 20, 1),
('Ají de Pollo', 'Clásico ají de pollo servido con arroz blanco.', 2, 12.0, 25, 1),
-- Comedor de Letras (id=2)
('Arroz con Pollo', 'Arroz verde con pollo y ensalada criolla.', 3, 13.0, 20, 2),
('Lomo Saltado', 'Tradicional lomo saltado acompañado de arroz y papas fritas.', 4, 14.0, 10, 2),
-- Comedor Central (id=3)
('Ají de Pollo', 'Clásico ají de pollo servido con arroz blanco.', 2, 12.0, 25, 3),
('Arroz a la jardinera', 'Arroz con verduras frescas y pollo salteado.', 5, 10.0, 15, 3),
-- Kilomío (id=4)
('Adobo de Chancho', 'Delicioso adobo de chancho acompañado de arroz y camote.', 1, 15.0, 20, 4),
('Arroz con Pollo', 'Arroz verde con pollo y ensalada criolla.', 3, 13.0, 20, 4),
-- El Puesto (id=5)
('Lomo Saltado', 'Tradicional lomo saltado acompañado de arroz y papas fritas.', 4, 14.0, 10, 5),
('Arroz a la jardinera', 'Arroz con verduras frescas y pollo salteado.', 5, 10.0, 15, 5),
-- Refilo (id=6)
('Arroz con Pollo', 'Arroz verde con pollo y ensalada criolla.', 3, 13.0, 20, 6),
('Adobo de Chancho', 'Delicioso adobo de chancho acompañado de arroz y camote.', 1, 15.0, 20, 6);
-- Charlotte (id=7) está cerrado, no se asignan platillos.
