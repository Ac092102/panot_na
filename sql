CREATE DATABASE school_appointments;

USE school_appointments;

CREATE TABLE appointments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_name VARCHAR(100),
    appointment_date DATE,
    appointment_time TIME,
    reason TEXT
);
