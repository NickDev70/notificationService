CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    full_name TEXT NOT NULL
);

CREATE TABLE notification_periods (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    day_of_week VARCHAR(10) NOT NULL CHECK (day_of_week IN ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY')),
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    CHECK (start_time < end_time)
);

CREATE TABLE events (
    id SERIAL PRIMARY KEY,
    message TEXT NOT NULL,
    occured_at TIMESTAMP NOT NULL
);

CREATE TYPE notification_status AS ENUM ('SCHEDULED', 'SENT', 'FAILED');

CREATE TABLE notifications (
    id SERIAL PRIMARY KEY,
    event_id INTEGER NOT NULL REFERENCES events(id) ON DELETE CASCADE,
    user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    scheduled_for TIMESTAMP,
    status notification_status,
    sent_at TIMESTAMP
);

CREATE INDEX idx_notifications_scheduled ON notifications (status, scheduled_for);