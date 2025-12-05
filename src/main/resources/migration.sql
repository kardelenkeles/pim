-- Migration script to make category_id and brand_id nullable
-- Run this script manually in your PostgreSQL database

-- Connect to pim_db database first
-- \c pim_db

-- Remove NOT NULL constraint from category_id
ALTER TABLE product ALTER COLUMN category_id DROP NOT NULL;

-- Remove NOT NULL constraint from brand_id  
ALTER TABLE product ALTER COLUMN brand_id DROP NOT NULL;

-- Verify the changes
\d product
