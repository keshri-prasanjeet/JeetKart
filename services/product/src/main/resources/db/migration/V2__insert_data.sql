-- Insert categories
INSERT INTO category (id, description, name) VALUES (nextval('category_seq'), 'Computer Keyboards', 'Keyboards');
INSERT INTO category (id, description, name) VALUES (nextval('category_seq'), 'Computer Monitors', 'Monitors');
INSERT INTO category (id, description, name) VALUES (nextval('category_seq'), 'Display Screens', 'Screens');
INSERT INTO category (id, description, name) VALUES (nextval('category_seq'), 'Computer Mice', 'Mice');
INSERT INTO category (id, description, name) VALUES (nextval('category_seq'), 'Computer Accessories', 'Accessories');

-- Insert products for the 'Keyboards' category
INSERT INTO product (id, description, name, available_quantity, price, category_id)
VALUES
    (nextval('product_seq'), 'Mechanical keyboard with RGB lighting', 'Mechanical Keyboard 1', 10, 99.99, (SELECT id FROM category WHERE name = 'Keyboards')),
    (nextval('product_seq'), 'Wireless compact keyboard', 'Wireless Compact Keyboard 1', 15, 79.99, (SELECT id FROM category WHERE name = 'Keyboards')),
    (nextval('product_seq'), 'Backlit gaming keyboard with customizable keys', 'Gaming Keyboard 1', 20, 129.99, (SELECT id FROM category WHERE name = 'Keyboards')),
    (nextval('product_seq'), 'Mechanical keyboard with wrist rest', 'Ergonomic Keyboard 1', 25, 109.99, (SELECT id FROM category WHERE name = 'Keyboards')),
    (nextval('product_seq'), 'Wireless keyboard and mouse combo', 'Wireless Combo 1', 18, 69.99, (SELECT id FROM category WHERE name = 'Keyboards'));

-- Insert products for the 'Monitors' category
INSERT INTO product (id, description, name, available_quantity, price, category_id)
VALUES
    (nextval('product_seq'), '27-inch IPS monitor with 4K resolution', '4K Monitor 1', 30, 399.99, (SELECT id FROM category WHERE name = 'Monitors')),
    (nextval('product_seq'), 'Ultra-wide gaming monitor with HDR support', 'Ultra-wide Gaming Monitor 1', 25, 499.99, (SELECT id FROM category WHERE name = 'Monitors')),
    (nextval('product_seq'), '24-inch LED monitor for office use', 'Office Monitor 1', 22, 179.99, (SELECT id FROM category WHERE name = 'Monitors')),
    (nextval('product_seq'), '32-inch curved monitor with AMD FreeSync', 'Curved Monitor 1', 28, 329.99, (SELECT id FROM category WHERE name = 'Monitors')),
    (nextval('product_seq'), 'Portable USB-C monitor for laptops', 'Portable Monitor 1', 35, 249.99, (SELECT id FROM category WHERE name = 'Monitors'));

-- Insert products for the 'Screens' category
INSERT INTO product (id, description, name, available_quantity, price, category_id)
VALUES
    (nextval('product_seq'), 'Curved OLED gaming screen with 240Hz refresh rate', 'Curved OLED Gaming Screen 1', 15, 799.99, (SELECT id FROM category WHERE name = 'Screens')),
    (nextval('product_seq'), 'Flat QLED monitor with 1440p resolution', 'QLED Monitor 1', 18, 599.99, (SELECT id FROM category WHERE name = 'Screens')),
    (nextval('product_seq'), '27-inch touch screen display for creative work', 'Touch Screen Display 1', 22, 699.99, (SELECT id FROM category WHERE name = 'Screens')),
    (nextval('product_seq'), 'Ultra-slim 4K HDR display for multimedia', 'Ultra-slim 4K HDR Display 1', 20, 449.99, (SELECT id FROM category WHERE name = 'Screens')),
    (nextval('product_seq'), 'Gaming projector with low input lag', 'Gaming Projector 1', 25, 899.99, (SELECT id FROM category WHERE name = 'Screens'));

-- Insert products for the 'Mice' category
INSERT INTO product (id, description, name, available_quantity, price, category_id)
VALUES
    (nextval('product_seq'), 'Wireless gaming mouse with customizable RGB lighting', 'RGB Gaming Mouse 1', 30, 59.99, (SELECT id FROM category WHERE name = 'Mice')),
    (nextval('product_seq'), 'Ergonomic wired mouse for productivity', 'Ergonomic Wired Mouse 1', 28, 29.99, (SELECT id FROM category WHERE name = 'Mice')),
    (nextval('product_seq'), 'Ambidextrous gaming mouse with high DPI', 'Ambidextrous Gaming Mouse 1', 32, 69.99, (SELECT id FROM category WHERE name = 'Mice')),
    (nextval('product_seq'), 'Travel-sized compact mouse for laptops', 'Travel Mouse 1', 26, 19.99, (SELECT id FROM category WHERE name = 'Mice')),
    (nextval('product_seq'), 'Vertical ergonomic mouse for reduced strain', 'Vertical Ergonomic Mouse 1', 35, 39.99, (SELECT id FROM category WHERE name = 'Mice'));

-- Insert products for the 'Accessories' category
INSERT INTO product (id, description, name, available_quantity, price, category_id)
VALUES
    (nextval('product_seq'), 'Adjustable laptop stand with cooling fan', 'Adjustable Laptop Stand 1', 25, 34.99, (SELECT id FROM category WHERE name = 'Accessories')),
    (nextval('product_seq'), 'Wireless charging pad for smartphones', 'Wireless Charging Pad 1', 20, 24.99, (SELECT id FROM category WHERE name = 'Accessories')),
    (nextval('product_seq'), 'Gaming headset stand with RGB lighting', 'RGB Headset Stand 1', 28, 49.99, (SELECT id FROM category WHERE name = 'Accessories')),
    (nextval('product_seq'), 'Bluetooth mechanical keypad for tablets', 'Bluetooth Keypad 1', 22, 39.99, (SELECT id FROM category WHERE name = 'Accessories')),
    (nextval('product_seq'), 'External hard drive enclosure with USB-C', 'External Hard Drive Enclosure 1', 30, 29.99, (SELECT id FROM category WHERE name = 'Accessories'));