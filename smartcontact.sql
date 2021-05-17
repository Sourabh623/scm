-- phpMyAdmin SQL Dump
-- version 5.0.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 28, 2021 at 07:29 AM
-- Server version: 10.4.14-MariaDB
-- PHP Version: 7.2.34

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `smartcontact`
--

-- --------------------------------------------------------

--
-- Table structure for table `contact`
--

CREATE TABLE `contact` (
  `c_id` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `second_name` varchar(255) DEFAULT NULL,
  `work` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `atime` datetime DEFAULT NULL,
  `regi_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `contact`
--

INSERT INTO `contact` (`c_id`, `description`, `email`, `image`, `name`, `phone`, `second_name`, `work`, `user_id`, `atime`, `regi_date`) VALUES
(30, '<p>pro</p>', 'sourabhfulmali623@gmail.com', 'sorry.png', 'Sourabh Fulmali', '08962263623', 'sam', 'Software Engineer', 24, NULL, NULL),
(31, '<p>this man is Gambler</p>', 'rohitnariya@gmail.com', 'e.png', 'Rohit Nariya', '8817058194', 'RN', 'Gambler', 25, NULL, NULL),
(32, '<p>Marketer</p>', 'Kartik@gmail.com', 'f.jpg', 'Kartik', '3435435343', 'kartik', 'Marketing', 24, NULL, NULL),
(35, '<p>this is Network Eng.</p>', 'abhi@gmail.com', 'e.png', 'Abhishek Dubey', '8269106225', 'abhi', ' Network Eng.', 25, NULL, NULL),
(38, '<p>this is my mom</p>', 'mom@gmail.com', 'j.png', 'Mom', '8120256785', 'mom', 'House Wife', 25, NULL, NULL),
(39, '<p>pro</p>', 'sourabhfulmali623@gmail.com', 't.jpg', 'Sourabh Fulmali', '08962263623', 'Fulmali', 'Software Engineer', 24, NULL, NULL),
(40, '<p>this is digital marketer</p>', 'shivam@gmail.com', 'profile.png', 'Shivam Shinde', '78777725552', 'shivam', 'Software Engineer', 25, NULL, NULL),
(45, '<p>DJ wala Babu</p>', 'ramizdj@gmail.com', 'profile.png', 'Ramiz', '7000854503', 'RRK', 'DJ wala Babu', 25, NULL, NULL),
(50, '', 'sourabhfulmali623@gmail.com', 'BeautyPlus_20170731195604_save.jpg', 'Sourabh Fulmali', '08962263623', 'Fulmali', 'Software Engineer', 49, NULL, NULL),
(54, '<p>rh</p>', 'r@gmail.com', 'g.png', 'rahul', '7877458585', 'rahul', 'Marketing', 25, NULL, NULL),
(59, '<p>programmer</p>', 'sourabhfulmali623@gmail.com', 's.jpg', 'Sourabh Fulmali', '08962263623', 'sobhu', 'Software Engineer', 58, NULL, NULL),
(82, '', 'sourabhfulmali623@gmail.com', 's.jpg', 'Sourabh Fulmali', '7987858979', '', '', 25, NULL, NULL),
(87, '<p>sf</p>', 'sourabhfulmali623@gmail.com', 'e.png894292', 'Sourabh Fulmali', '08962263623', 'sam', 'Software Engineer', 85, NULL, '2021-04-26 20:07:55'),
(90, '<p>hero</p>', 'sourabhfulmali623@gmail.com', 'contacts.png', 'Sourabh Fulmali', '08962263623', 'sobhu', 'Web Developer', 25, NULL, '2021-04-27 15:30:23'),
(91, '<p>haa</p>', 'sourabhfulmali623@gmail.com', 'contact.png', 'Sourabh Fulmali', '7987858979', 'sam', 'Software Engineer', 25, NULL, '2021-04-27 15:38:55'),
(92, '<p>boot</p>', 'sourabhfulmali623@gmail.com', 'defaultprofile.svg', 'Sourabh Fulmali', '08962263623', 'sobhu', 'Web Developer', 25, NULL, '2021-04-27 15:46:42');

-- --------------------------------------------------------

--
-- Table structure for table `hibernate_sequence`
--

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `hibernate_sequence`
--

INSERT INTO `hibernate_sequence` (`next_val`) VALUES
(93);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `about` varchar(255) DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `regi_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `email`, `name`, `about`, `enabled`, `image_url`, `password`, `role`, `regi_date`) VALUES
(23, 'RahitMalviya248@gmail.com', 'Rohit Malviya', 'i am using smartcontactmanager', b'1', 'default.png', '$2a$10$QFVfFHYlOC2MbhJPhxDRGuHnakyqwpSocU1Z.Vw2OqVt7fvokvplG', 'ROLE_USER', NULL),
(24, 'kaka@gmail.com', 'kaka', 'i am using smartcontactmanager', b'1', 'default.png', '$2a$10$ZmmOssEMfp3IGjWoFFKJuuZIljOBypne07DvUzhzbcxrbT3RYyJCS', 'ROLE_USER', NULL),
(25, 'sourabhfulmali623@gmail.com', 'Sourabh Fulmali', 'i am using smartcontactmanager', b'1', 'default.png', '$2a$10$IpfebwNPsHJvHShCqy62UeJkoaytL4yoWyq.JPXx5lqO2GgB7dRWa', 'ROLE_USER', NULL),
(49, 'sourabhfulmali08@gmail.com', 'Sourabh Fulmali', 'i am using smartcontactmanager', b'1', 'default.png', '$2a$10$YSBmKKJQLtaUm1SBsV6MUeg9TcC/FMkOHmHPzpxZ0fyDNV/ZLit6m', 'ROLE_USER', NULL),
(58, 'shivamshinde786@gmail.com', 'Shivam Shinde', 'i am using smartcontactmanager', b'1', 'default.png', '$2a$10$sBrCGQmP8QJWXIHxUt4iEOuDbWILRVsQUjv.ZE.XAvHXKTugsvPHW', 'ROLE_USER', NULL),
(85, 'sourabh.fulmaliknw@gmail.com', 'sobhu', 'i am using smartcontactmanager', b'1', 'default.png', '$2a$10$yyWLEitjQQJJPiHrfKMf2u4xQeMj4bo9PwNgqyLClMCHxUGO/Sgc6', 'ROLE_USER', '2021-04-26 19:48:11');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `contact`
--
ALTER TABLE `contact`
  ADD PRIMARY KEY (`c_id`),
  ADD KEY `FKe07k4jcfdophemi6j1lt84b61` (`user_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `contact`
--
ALTER TABLE `contact`
  ADD CONSTRAINT `FKe07k4jcfdophemi6j1lt84b61` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
