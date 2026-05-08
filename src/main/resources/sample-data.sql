-- sample-data.sql
--
-- Optional sample data for Vaadin Scooter App.
-- This file is intended for H2 database usage.
--
-- The data below was used during the development of the application.
-- All entries were originally added through the application's user interface
-- and later exported from the H2 console into this file.
--
-- H2 Console connection settings:
------------------------------------------------
-- Driver Class: org.h2.Driver
-- JDBC URL:     jdbc:h2:./data/scooterdb
-- User Name:    sa
------------------------------------------------
--
-- Run this file manually in the H2 Console if you want to populate
-- the database with example data.
--
-- The statements use MERGE so the script can be executed multiple times
-- without causing duplicate primary key errors.


-- =========================================================
-- APP_USER
-- =========================================================
MERGE INTO APP_USER (ID, PASSWORD_HASH, PROFILE_IMAGE_PATH, USERNAME) KEY(ID) VALUES
(9, '$2a$10$Z8DFqT304oHGIkXGWcaLy.ZGuWQek4a4LLKdr4uF2EAZo0EGGDI.W', '/images/user-9.png', 'Admin'),
(10, '$2a$10$IVCjiteoEIlk0sJgQYHw0.kfvNgbHJ/.KfBZxiJrUyYUQDoEoLDLa', NULL, 'Super'),
(11, '$2a$10$9Fc1OahF3X03/SBcUd6D2eBotBDsC4lkUgh9Stx.AALf5uEjbqn.q', '/profile-images/user-11.png', 'User'),
(12, '$2a$10$xtLcCZrGJ6/NvE6PTYC69u5xNGqrbq3OdWAOUMPR384B.DhnGAixe', NULL, 'Testi1'),
(13, '$2a$10$V8u3twiz9.c2dDFbXtGAmOlRbRfZD9JK5Ef/z05cDHUq935ucE98W', NULL, 'Test');

-- =========================================================
-- APP_USER_ROLES
-- =========================================================
MERGE INTO APP_USER_ROLES (USER_ID, ROLE) KEY(USER_ID, ROLE) VALUES
(9, 'ADMIN'),
(10, 'SUPER'),
(11, 'USER'),
(12, 'USER'),
(13, 'USER');

-- =========================================================
-- FEATURE
-- =========================================================
MERGE INTO FEATURE (ID, ACTIVE, CATEGORY, DESCRIPTION, INSTALLATION_COST, NAME) KEY(ID) VALUES
(6, TRUE, 'Display', 'Näyttää ajotiedot', 50.0, 'LED Display'),
(7, TRUE, 'Navigation', 'Mahdollistaa potkulaudan sijainnin seurannan.', 49.9, 'GPS Tracking'),
(8, TRUE, 'Security', 'Hälytysjärjestelmä suojaa potkulautaa varkauksilta.', 59.9, 'Alarm system'),
(9, FALSE, 'Security', 'Lukitus toimii Bluetooth-yhteyden avulla.', 29.9, 'Bluetooth Lock');

-- =========================================================
-- STATION
-- =========================================================
MERGE INTO STATION (ID, ADDRESS, AREA, CAPACITY, CITY, NAME) KEY(ID) VALUES
(1, 'Kauppakatu 45', 'Keskusta', 20, 'Kuopio', 'Tori'),
(2, 'Niiralankatu 2', 'Keskusta', 15, 'Kuopio', 'Teatteri'),
(3, 'Microkatu 1', 'Savilahti', 12, 'Kuopio', 'Savonia'),
(6, 'Ajajantie 12', 'Levänen', 50, 'Kuopio', 'Kotivarasto'),
(7, 'Matkusniementie 2', 'Saaristokaupunki', 10, 'Kuopio', 'S-market Keilankanta'),
(8, 'Jalkasenkatu 5', 'Petonen', 6, 'Kuopio', 'K-market Petonen'),
(9, 'Kalevantori 3', 'Männistö', 9, 'Kuopio', 'Männistön tori'),
(10, 'Puijonkatu 19', 'Keskusta', 20, 'Kuopio', 'K-market Veljmies'),
(11, 'Minna Canthin katu 4', 'Satama', 7, 'Kuopio', 'S-market Satama'),
(12, 'Puutarhakatu 3', 'Keskusta', 12, 'Kuopio', 'Matkakeskus'),
(13, 'Puijontie 135', 'Puijo', 5, 'Kuopio', 'Torni'),
(14, 'Maitotie 3', 'Jynkkä', 6, 'Kuopio', 'Sale Jynkkä');

-- =========================================================
-- SCOOTER
-- =========================================================
MERGE INTO SCOOTER (ID, BATTERY_LEVEL, MANUFACTURE_YEAR, MODEL, SERIAL_NUMBER, STATUS, STATION_ID) KEY(ID) VALUES
(1, 80, 2024, 'Ninebot Max', 'SN1001', 'OUT_OF_SERVICE', 1),
(2, 85, 2024, 'Xiaomi Pro 2', 'SC-2002', 'AVAILABLE', 1),
(3, 60, 2023, 'Segway Ninebot', 'SC-2003', 'IN_USE', 2),
(4, 92, 2025, 'Augment', 'SC-3001', 'AVAILABLE', NULL),
(5, 2, 2016, 'Ninebot', 'SC-1002', 'OUT_OF_SERVICE', NULL),
(6, 50, 2017, 'Birdi', 'SN-9999', 'MAINTENANCE', 2),
(7, 89, 2020, 'AimBot', 'SN-9874', 'MAINTENANCE', 1),
(8, 84, 2019, 'Tier', 'SC-3456', 'AVAILABLE', 12),
(9, 43, 2019, 'Tier', 'SC-3458', 'OUT_OF_SERVICE', 8);

-- =========================================================
-- SCOOTER_DETAIL
-- =========================================================
MERGE INTO SCOOTER_DETAIL (ID, FIRMWARE_VERSION, LAST_INSPECTION_DATE, MAX_SPEED, QR_CODE, WEIGHT, SCOOTER_ID) KEY(ID) VALUES
(1, 'FW-1.0.0', DATE '2026-03-01', 25, 'QR-1001', 15.0, 1),
(2, 'FW-1.1.0', DATE '2026-03-05', 25, 'QR-2002', 20.0, 2),
(3, 'FW-1.0.5', DATE '2026-03-08', 20, 'QR-2003', 25.0, 3),
(7, 'FW-2.0.1', DATE '2026-03-12', 18, 'QR-1234', 10.0, 6);

-- =========================================================
-- SCOOTER_FEATURE
-- =========================================================
MERGE INTO SCOOTER_FEATURE (SCOOTER_ID, FEATURE_ID) KEY(SCOOTER_ID, FEATURE_ID) VALUES
(6, 6),
(6, 7),
(6, 9),
(7, 8),
(8, 6),
(8, 8),
(8, 9),
(9, 6),
(9, 7),
(9, 9);

-- =========================================================
-- RIDE
-- =========================================================
MERGE INTO RIDE (ID, DISTANCE_KM, END_TIME, PRICE, START_TIME, STATUS, END_STATION_ID, SCOOTER_ID, START_STATION_ID) KEY(ID) VALUES
(1, 3.5, TIMESTAMP '2026-03-10 08:25:00', 5.9, TIMESTAMP '2026-03-10 08:10:00', 'COMPLETED', 2, 1, 1),
(2, 2.8, TIMESTAMP '2026-03-10 12:18:00', 4.8, TIMESTAMP '2026-03-10 12:00:00', 'COMPLETED', 3, 2, 2),
(3, 4.2, TIMESTAMP '2026-03-11 17:55:00', 6.7, TIMESTAMP '2026-03-11 17:40:00', 'COMPLETED', 1, 3, 3),
(4, 10.1, TIMESTAMP '2026-03-12 10:17:00', 10.2, TIMESTAMP '2026-03-12 10:01:00', 'COMPLETED', 2, 4, 1),
(5, 4.0, TIMESTAMP '2026-03-16 12:10:00', 3.4, TIMESTAMP '2026-03-16 12:00:00', 'COMPLETED', 3, 5, 2),
(6, 10.0, TIMESTAMP '2026-03-23 14:05:00', 5.0, TIMESTAMP '2026-03-23 13:49:00', 'CANCELLED', 3, 6, 2),
(7, 2.0, TIMESTAMP '2026-03-22 15:10:00', 0.6, TIMESTAMP '2026-03-22 15:00:00', 'FAILED', 2, 2, 6),
(8, 3.1, TIMESTAMP '2026-03-18 08:19:00', 4.2, TIMESTAMP '2026-03-18 08:05:00', 'COMPLETED', 2, 1, 1),
(9, 3.2, TIMESTAMP '2026-03-24 08:24:00', 4.3, TIMESTAMP '2026-03-24 08:10:00', 'COMPLETED', 2, 1, 1),
(10, 4.8, TIMESTAMP '2026-03-12 16:10:00', 6.1, TIMESTAMP '2026-03-12 15:46:00', 'COMPLETED', 12, 2, 7),
(11, 3.9, TIMESTAMP '2026-03-03 12:01:00', 8.6, TIMESTAMP '2026-03-03 11:09:00', 'COMPLETED', 9, 8, 12),
(12, 5.4, TIMESTAMP '2026-03-07 17:31:00', 6.9, TIMESTAMP '2026-03-07 17:06:00', 'COMPLETED', 7, 4, 1),
(13, 3.5, TIMESTAMP '2026-03-02 03:45:00', 2.9, TIMESTAMP '2026-03-02 03:05:00', 'FAILED', 2, 9, 10),
(14, 3.1, TIMESTAMP '2026-03-16 17:00:00', 1.9, TIMESTAMP '2026-03-16 16:45:00', 'CANCELLED', 12, 8, 11),
(15, 7.5, TIMESTAMP '2026-03-22 12:02:00', 12.0, TIMESTAMP '2026-03-22 11:45:00', 'COMPLETED', 8, 7, 12),
(16, 5.1, TIMESTAMP '2026-03-01 10:38:00', 6.0, TIMESTAMP '2026-03-01 10:03:00', 'INTERRUPTED', 8, 1, 14),
(17, 2.0, TIMESTAMP '2026-03-19 20:20:00', 5.0, TIMESTAMP '2026-03-19 20:03:00', 'COMPLETED', 10, 6, 12);
