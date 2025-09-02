-- Enable RLS example on users table
ALTER TABLE users ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Users can select own row" ON users FOR SELECT USING (auth.uid() = id);
CREATE POLICY "Users insert own row" ON users FOR INSERT WITH CHECK (auth.uid() = id);

-- Subscriptions: only user can read own subscriptions
ALTER TABLE subscriptions ENABLE ROW LEVEL SECURITY;
CREATE POLICY "User access own subscriptions" ON subscriptions FOR ALL USING (auth.uid() = user_id);

-- Teachers cannot self-approve (admin only)
ALTER TABLE teachers ENABLE ROW LEVEL SECURITY;
CREATE POLICY "Teacher self read" ON teachers FOR SELECT USING (auth.uid() = user_id);
-- Admins should be allowed to update approval via server-side admin role
