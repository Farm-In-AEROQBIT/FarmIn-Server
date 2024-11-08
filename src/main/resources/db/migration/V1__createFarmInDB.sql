CREATE TABLE `User` (
                        `id` integer PRIMARY KEY AUTO_INCREMENT,
                        `password` varchar(100) NOT NULL,
                        `username` varchar(50) UNIQUE NOT NULL,
                        `name` varchar(50) NOT NULL,
                        `email` varchar(100) NOT NULL,
                        `phonenum` VARCHAR(100) NULL,
                        `role` varchar(10) NOT NULL,
                        `updated_at` datetime NULL,
                        `created_at` datetime NOT NULL
);

CREATE TABLE `FarmInfo` (
                            `FarmID` integer PRIMARY KEY AUTO_INCREMENT,
                            `FarmName` VARCHAR(100) NOT NULL,
                            `UserID` integer NOT NULL,
                            FOREIGN KEY (`UserID`) REFERENCES `User`(`id`)
);

CREATE TABLE `Finishing` (
                             `FinishingID` integer PRIMARY KEY AUTO_INCREMENT,
                             `FarmID` integer NOT NULL,
                             FOREIGN KEY (`FarmID`) REFERENCES `FarmInfo`(`FarmID`)
);

CREATE TABLE `Growing` (
                           `GrowingID` integer PRIMARY KEY AUTO_INCREMENT,
                           `FarmID` integer NOT NULL,
                           FOREIGN KEY (`FarmID`) REFERENCES `FarmInfo`(`FarmID`)
);

CREATE TABLE `Maternity` (
                             `MaternityID` integer PRIMARY KEY AUTO_INCREMENT,
                             `FarmID` integer NOT NULL,
                             FOREIGN KEY (`FarmID`) REFERENCES `FarmInfo`(`FarmID`)
);

CREATE TABLE `Gestation` (
                             `GestationID` integer PRIMARY KEY AUTO_INCREMENT,
                             `FarmID` integer NOT NULL,
                             FOREIGN KEY (`FarmID`) REFERENCES `FarmInfo`(`FarmID`)
);

CREATE TABLE `Piglet` (
                          `PigletID` integer PRIMARY KEY AUTO_INCREMENT,
                          `FarmID` integer NOT NULL,
                          FOREIGN KEY (`FarmID`) REFERENCES `FarmInfo`(`FarmID`)
);

CREATE TABLE `Boars` (
                         `BoarsID` integer PRIMARY KEY AUTO_INCREMENT,
                         `FarmID` integer NOT NULL,
                         FOREIGN KEY (`FarmID`) REFERENCES `FarmInfo`(`FarmID`)
);

CREATE TABLE `Reserve` (
                           `ReserveID` integer PRIMARY KEY AUTO_INCREMENT,
                           `FarmID` integer NOT NULL,
                           FOREIGN KEY (`FarmID`) REFERENCES `FarmInfo`(`FarmID`)
);

CREATE TABLE `ReserveSensor` (
                                 `SensorID` integer PRIMARY KEY AUTO_INCREMENT,
                                 `ReserveID` integer NOT NULL,
                                 `Co2` VARCHAR(50) NULL,
                                 `Nh3` VARCHAR(50) NULL,
                                 `PM` VARCHAR(50) NULL,
                                 `Temper` VARCHAR(50) NULL,
                                 `Humidity` VARCHAR(50) NULL,
                                 FOREIGN KEY (`ReserveID`) REFERENCES `Reserve`(`ReserveID`)
);

CREATE TABLE `PigletSensor` (
                                `SensorID` integer PRIMARY KEY AUTO_INCREMENT,
                                `PigletID` integer NOT NULL,
                                `Co2` VARCHAR(50) NULL,
                                `Nh3` VARCHAR(50) NULL,
                                `PM` VARCHAR(50) NULL,
                                `Temper` VARCHAR(50) NULL,
                                `Humidity` VARCHAR(50) NULL,
                                FOREIGN KEY (`PigletID`) REFERENCES `Piglet`(`PigletID`)
);

CREATE TABLE `MaternitySensor` (
                                   `SensorID` integer PRIMARY KEY AUTO_INCREMENT,
                                   `MaternityID` integer NOT NULL,
                                   `Co2` VARCHAR(50) NULL,
                                   `Nh3` VARCHAR(50) NULL,
                                   `PM` VARCHAR(50) NULL,
                                   `Temper` VARCHAR(50) NULL,
                                   `Humidity` VARCHAR(50) NULL,
                                   FOREIGN KEY (`MaternityID`) REFERENCES `Maternity`(`MaternityID`)
);

CREATE TABLE `GestationSensor` (
                                   `SensorID` integer PRIMARY KEY AUTO_INCREMENT,
                                   `GestationID` integer NOT NULL,
                                   `Co2` VARCHAR(50) NULL,
                                   `Nh3` VARCHAR(50) NULL,
                                   `PM` VARCHAR(50) NULL,
                                   `Temper` VARCHAR(50) NULL,
                                   `Humidity` VARCHAR(50) NULL,
                                   FOREIGN KEY (`GestationID`) REFERENCES `Gestation`(`GestationID`)
);

CREATE TABLE `GrowingSensor` (
                                 `SensorID` integer PRIMARY KEY AUTO_INCREMENT,
                                 `GrowingID` integer NOT NULL,
                                 `Co2` VARCHAR(50) NULL,
                                 `Nh3` VARCHAR(50) NULL,
                                 `PM` VARCHAR(50) NULL,
                                 `Temper` VARCHAR(50) NULL,
                                 `Humidity` VARCHAR(50) NULL,
                                 FOREIGN KEY (`GrowingID`) REFERENCES `Growing`(`GrowingID`)
);

CREATE TABLE `FinishingSensor` (
                                   `SensorID` integer PRIMARY KEY AUTO_INCREMENT,
                                   `FinishingID` integer NOT NULL,
                                   `Co2` VARCHAR(50) NULL,
                                   `Nh3` VARCHAR(50) NULL,
                                   `PM` VARCHAR(50) NULL,
                                   `Temper` VARCHAR(50) NULL,
                                   `Humidity` VARCHAR(50) NULL,
                                   FOREIGN KEY (`FinishingID`) REFERENCES `Finishing`(`FinishingID`)
);

CREATE TABLE `BoarsSensor` (
                               `SensorID` integer PRIMARY KEY AUTO_INCREMENT,
                               `BoarsID` integer NOT NULL,
                               `Co2` VARCHAR(50) NULL,
                               `Nh3` VARCHAR(50) NULL,
                               `PM` VARCHAR(50) NULL,
                               `Temper` VARCHAR(50) NULL,
                               `Humidity` VARCHAR(50) NULL,
                               FOREIGN KEY (`BoarsID`) REFERENCES `Boars`(`BoarsID`)
);