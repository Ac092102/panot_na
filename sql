CREATE DATABASE school_appointments;
CREATE DATABASE school_appointments;

USE school_appointments;

CREATE TABLE appointments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_name VARCHAR(100),
    appointment_date DATE,
    appointment_time TIME,
    reason TEXT,
    status ENUM('Pending', 'Approved', 'Declined') DEFAULT 'Pending'
);

USE wsers
    
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL  
);