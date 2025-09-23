-- Fix PostgreSQL schema - convert bytea columns to text
-- This is the root cause of the "lower(bytea) does not exist" error

-- First, check the current column types to confirm the issue
SELECT table_name, column_name, data_type, character_maximum_length
FROM information_schema.columns 
WHERE table_schema = 'public' 
  AND table_name IN ('provider', 'debtor') 
  AND column_name IN ('name', 'first_name', 'last_name');

-- Convert provider.name from bytea to text if needed
DO $$ 
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'provider' AND column_name = 'name' AND data_type = 'bytea'
    ) THEN
        -- Convert existing bytea data to text
        ALTER TABLE provider ALTER COLUMN name TYPE TEXT USING convert_from(name, 'UTF8');
    END IF;
END $$;

-- Convert debtor.first_name from bytea to text if needed
DO $$ 
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'debtor' AND column_name = 'first_name' AND data_type = 'bytea'
    ) THEN
        ALTER TABLE debtor ALTER COLUMN first_name TYPE TEXT USING convert_from(first_name, 'UTF8');
    END IF;
END $$;

-- Convert debtor.last_name from bytea to text if needed
DO $$ 
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'debtor' AND column_name = 'last_name' AND data_type = 'bytea'
    ) THEN
        ALTER TABLE debtor ALTER COLUMN last_name TYPE TEXT USING convert_from(last_name, 'UTF8');
    END IF;
END $$;

-- Verify the changes
SELECT table_name, column_name, data_type, character_maximum_length
FROM information_schema.columns 
WHERE table_schema = 'public' 
  AND table_name IN ('provider', 'debtor') 
  AND column_name IN ('name', 'first_name', 'last_name');