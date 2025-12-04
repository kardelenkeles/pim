-- Product Information Management System Database Schema
-- PostgreSQL Database Schema

-- Drop tables if exists
DROP TABLE IF EXISTS product_image CASCADE;
DROP TABLE IF EXISTS product_attribute CASCADE;
DROP TABLE IF EXISTS quality CASCADE;
DROP TABLE IF EXISTS product CASCADE;
DROP TABLE IF EXISTS category CASCADE;
DROP TABLE IF EXISTS brand CASCADE;

-- Create Brand Table
CREATE TABLE brand (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    slug VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Category Table
CREATE TABLE category (
    id SERIAL PRIMARY KEY,
    parent_category_id INTEGER,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    slug VARCHAR(100) NOT NULL UNIQUE,
    "order" INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_category_id) REFERENCES category(id) ON DELETE SET NULL
);

-- Create Product Table
CREATE TABLE product (
    id SERIAL PRIMARY KEY,
    barcode VARCHAR(50) NOT NULL UNIQUE,
    category_id INTEGER NOT NULL,
    brand_id INTEGER NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL CHECK (status IN ('DRAFT', 'ACTIVE', 'INACTIVE', 'DISCONTINUED')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE,
    FOREIGN KEY (brand_id) REFERENCES brand(id) ON DELETE CASCADE
);

-- Create Quality Table
CREATE TABLE quality (
    id SERIAL PRIMARY KEY,
    product_id INTEGER NOT NULL UNIQUE,
    score INTEGER NOT NULL,
    result JSON,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);

-- Create Product Attribute Table
CREATE TABLE product_attribute (
    id SERIAL PRIMARY KEY,
    product_id INTEGER NOT NULL,
    key VARCHAR(100) NOT NULL,
    value TEXT,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);

-- Create Product Image Table
CREATE TABLE product_image (
    id SERIAL PRIMARY KEY,
    product_id INTEGER NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    alt_text VARCHAR(255),
    "order" INTEGER,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);

-- Create Indexes for Performance
CREATE INDEX idx_product_barcode ON product(barcode);
CREATE INDEX idx_product_category_id ON product(category_id);
CREATE INDEX idx_product_brand_id ON product(brand_id);
CREATE INDEX idx_product_status ON product(status);
CREATE INDEX idx_category_parent_id ON category(parent_category_id);
CREATE INDEX idx_category_slug ON category(slug);
CREATE INDEX idx_brand_slug ON brand(slug);
CREATE INDEX idx_quality_product_id ON quality(product_id);
CREATE INDEX idx_product_attribute_product_id ON product_attribute(product_id);
CREATE INDEX idx_product_attribute_key ON product_attribute(key);
CREATE INDEX idx_product_image_product_id ON product_image(product_id);

-- Create function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers for updated_at
CREATE TRIGGER update_brand_updated_at BEFORE UPDATE ON brand
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_category_updated_at BEFORE UPDATE ON category
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_product_updated_at BEFORE UPDATE ON product
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_quality_updated_at BEFORE UPDATE ON quality
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Insert sample data for testing

-- Sample Brands
INSERT INTO brand (name, slug) VALUES
('Samsung', 'samsung'),
('Apple', 'apple'),
('Sony', 'sony'),
('LG', 'lg'),
('Nike', 'nike');

-- Sample Categories
INSERT INTO category (parent_category_id, name, description, slug, "order") VALUES
(NULL, 'Electronics', 'Electronic devices and accessories', 'electronics', 1),
(NULL, 'Clothing', 'Apparel and fashion items', 'clothing', 2),
(1, 'Mobile Phones', 'Smartphones and mobile devices', 'mobile-phones', 1),
(1, 'Televisions', 'TV and display devices', 'televisions', 2),
(2, 'Shoes', 'Footwear for all occasions', 'shoes', 1);

-- Sample Products
INSERT INTO product (barcode, category_id, brand_id, title, description, status) VALUES
('1234567890123', 3, 1, 'Samsung Galaxy S24', 'Latest Samsung flagship smartphone', 'ACTIVE'),
('2234567890123', 3, 2, 'iPhone 15 Pro', 'Apple iPhone 15 Pro with A17 chip', 'ACTIVE'),
('3234567890123', 4, 3, 'Sony Bravia 55"', '55 inch 4K OLED TV', 'ACTIVE'),
('4234567890123', 5, 5, 'Nike Air Max 2024', 'Comfortable running shoes', 'ACTIVE'),
('5234567890123', 3, 1, 'Samsung Galaxy A54', 'Mid-range Samsung smartphone', 'DRAFT');

-- Sample Product Attributes
INSERT INTO product_attribute (product_id, key, value) VALUES
(1, 'color', 'Phantom Black'),
(1, 'storage', '256GB'),
(1, 'ram', '8GB'),
(2, 'color', 'Natural Titanium'),
(2, 'storage', '512GB'),
(2, 'ram', '8GB'),
(3, 'screen_size', '55 inches'),
(3, 'resolution', '4K UHD'),
(4, 'size', '42'),
(4, 'color', 'Black/White');

-- Sample Product Images
INSERT INTO product_image (product_id, image_url, alt_text, "order") VALUES
(1, 'https://example.com/images/galaxy-s24-1.jpg', 'Samsung Galaxy S24 front view', 1),
(1, 'https://example.com/images/galaxy-s24-2.jpg', 'Samsung Galaxy S24 back view', 2),
(2, 'https://example.com/images/iphone15pro-1.jpg', 'iPhone 15 Pro front view', 1),
(3, 'https://example.com/images/sony-bravia-1.jpg', 'Sony Bravia TV', 1),
(4, 'https://example.com/images/nike-airmax-1.jpg', 'Nike Air Max shoes', 1);

-- Sample Quality Scores
INSERT INTO quality (product_id, score, result) VALUES
(1, 95, '{"completeness": 100, "accuracy": 90, "richness": 95}'),
(2, 98, '{"completeness": 100, "accuracy": 95, "richness": 100}'),
(3, 85, '{"completeness": 90, "accuracy": 80, "richness": 85}'),
(4, 75, '{"completeness": 80, "accuracy": 70, "richness": 75}');
