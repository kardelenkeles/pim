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
