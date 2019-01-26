-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Jan 27, 2019 at 02:23 AM
-- Server version: 10.1.9-MariaDB
-- PHP Version: 5.6.15

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `phonegameapp`
--

-- --------------------------------------------------------

--
-- Table structure for table `filesup`
--

CREATE TABLE `filesup` (
  `id` int(100) NOT NULL,
  `uname` varchar(100) NOT NULL,
  `title` varchar(100) NOT NULL,
  `category` varchar(100) NOT NULL,
  `descption` longtext NOT NULL,
  `location` longtext NOT NULL,
  `filename` varchar(100) NOT NULL,
  `datetaken` varchar(100) NOT NULL,
  `upvotes` int(100) NOT NULL,
  `downvotes` int(100) NOT NULL,
  `datesup` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `filesup`
--

INSERT INTO `filesup` (`id`, `uname`, `title`, `category`, `descption`, `location`, `filename`, `datetaken`, `upvotes`, `downvotes`, `datesup`) VALUES
(1, 'bamwine', 'goods', 'people', 'its a good felling', 'kampala', 'bamwine.phg', '', 0, 0, '0'),
(2, 'bamwine', 'bams', 'people', 'its a bad felling', 'kampala', 'bamwine.phg', '', 0, 0, '0');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(100) NOT NULL,
  `uname` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `dates` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `uname`, `email`, `dates`) VALUES
(1, 'bamwine', 'bamws@gmail.com', '2019-01-25 03:29:21'),
(6, 'bamsbams', 'dog@majk.com', '2019-01-25 18:41:01');

-- --------------------------------------------------------

--
-- Table structure for table `voting`
--

CREATE TABLE `voting` (
  `id` int(100) NOT NULL,
  `usern` varchar(100) NOT NULL,
  `article` varchar(100) NOT NULL,
  `vote` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `voting`
--

INSERT INTO `voting` (`id`, `usern`, `article`, `vote`) VALUES
(10, 'bamwine', '1', 'n'),
(11, 'bamwine', '2', 'y');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `filesup`
--
ALTER TABLE `filesup`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `voting`
--
ALTER TABLE `voting`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `filesup`
--
ALTER TABLE `filesup`
  MODIFY `id` int(100) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(100) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `voting`
--
ALTER TABLE `voting`
  MODIFY `id` int(100) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
