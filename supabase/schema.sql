-- Basic users table
CREATE TABLE IF NOT EXISTS users (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  email text UNIQUE NOT NULL,
  full_name text,
  phone text,
  role text DEFAULT 'student', -- student, teacher, admin
  is_admin boolean DEFAULT false,
  profile_pic text,
  created_at timestamptz DEFAULT now()
);

-- Teachers table
CREATE TABLE IF NOT EXISTS teachers (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id uuid REFERENCES users(id) ON DELETE CASCADE,
  business_lat double precision,
  business_lon double precision,
  approved boolean DEFAULT false,
  profile_json jsonb,
  created_at timestamptz DEFAULT now()
);

-- Subjects
CREATE TABLE IF NOT EXISTS subjects (
  id serial PRIMARY KEY,
  name text NOT NULL,
  category text
);

-- Teacher subjects (approval per subject)
CREATE TABLE IF NOT EXISTS teacher_subjects (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  teacher_id uuid REFERENCES teachers(id) ON DELETE CASCADE,
  subject_id int REFERENCES subjects(id),
  approved boolean DEFAULT false,
  documents jsonb,
  created_at timestamptz DEFAULT now()
);

-- Student selections
CREATE TABLE IF NOT EXISTS student_subjects (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id uuid REFERENCES users(id) ON DELETE CASCADE,
  subject_id int REFERENCES subjects(id),
  matched_teacher_id uuid NULL,
  created_at timestamptz DEFAULT now()
);

-- Locations
CREATE TABLE IF NOT EXISTS locations (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id uuid REFERENCES users(id) ON DELETE CASCADE,
  address text,
  latitude double precision,
  longitude double precision,
  created_at timestamptz DEFAULT now()
);

-- Subscriptions (payments)
CREATE TABLE IF NOT EXISTS subscriptions (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id uuid REFERENCES users(id) ON DELETE CASCADE,
  teacher_id uuid REFERENCES teachers(id),
  subject_id int REFERENCES subjects(id),
  price_usd numeric,
  period text,
  status text DEFAULT 'pending',
  provider text,
  provider_reference text,
  created_at timestamptz DEFAULT now()
);

-- Teacher documents
CREATE TABLE IF NOT EXISTS teacher_documents (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  teacher_id uuid REFERENCES teachers(id) ON DELETE CASCADE,
  file_path text,
  file_type text,
  uploaded_at timestamptz DEFAULT now()
);

-- Groups
CREATE TABLE IF NOT EXISTS groups (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  teacher_id uuid REFERENCES teachers(id),
  subject_id int REFERENCES subjects(id),
  price_per_student numeric,
  max_seats int DEFAULT 10,
  session_duration_minutes int DEFAULT 60,
  schedule_json jsonb,
  created_at timestamptz DEFAULT now()
);
