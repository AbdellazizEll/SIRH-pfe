-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : sam. 30 nov. 2024 à 13:38
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `lastdb`
--

-- --------------------------------------------------------

--
-- Structure de la table `absence`
--

CREATE TABLE `absence` (
  `absence` bigint(20) NOT NULL,
  `type_abs` enum('EMERGENCY_LEAVE','MARRIAGE_LEAVE','MATERNITY_LEAVE','PAID_LEAVE','PARENTAL_LEAVE','SICK_LEAVE','STUDY_LEAVE','UNPAID_LEAVE') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `absence`
--

INSERT INTO `absence` (`absence`, `type_abs`) VALUES
(1, 'PAID_LEAVE'),
(2, 'UNPAID_LEAVE'),
(3, 'SICK_LEAVE'),
(4, 'MATERNITY_LEAVE'),
(5, 'PARENTAL_LEAVE'),
(6, 'STUDY_LEAVE'),
(7, 'MARRIAGE_LEAVE'),
(8, 'EMERGENCY_LEAVE');

-- --------------------------------------------------------

--
-- Structure de la table `account_verifications`
--

CREATE TABLE `account_verifications` (
  `collaborator_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `url` varchar(1024) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `catalogue`
--

CREATE TABLE `catalogue` (
  `id` bigint(20) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `catalogue`
--

INSERT INTO `catalogue` (`id`, `description`, `title`) VALUES
(1, 'Catalogue dédié pour les langues ', 'Langues'),
(2, 'Catalogue dédié pour les  formations techniques', 'Technique'),
(3, 'Catalogue dédié pour les  formations de collaboration et communications ', 'Collaboration et communication');

-- --------------------------------------------------------

--
-- Structure de la table `collaborateur`
--

CREATE TABLE `collaborateur` (
  `age` int(11) DEFAULT NULL,
  `archived` bit(1) DEFAULT NULL,
  `is_collab` bit(1) DEFAULT NULL,
  `is_enabled` bit(1) DEFAULT NULL,
  `is_gestrh` bit(1) DEFAULT NULL,
  `is_manager` bit(1) DEFAULT NULL,
  `solde` int(11) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `equipe_id_equipe` bigint(20) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `last_login_time` datetime(6) DEFAULT NULL,
  `poste_occupe_id_poste` bigint(20) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `firstname` varchar(255) NOT NULL,
  `lastname` varchar(255) NOT NULL,
  `manager_type` enum('DEPARTMENT_RESPONSIBLE','EQUIPE_MANAGER','NONE') DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `needs_password_change` bit(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `collaborateur`
--

INSERT INTO `collaborateur` (`age`, `archived`, `is_collab`, `is_enabled`, `is_gestrh`, `is_manager`, `solde`, `created_at`, `deleted_at`, `equipe_id_equipe`, `id`, `last_login_time`, `poste_occupe_id_poste`, `phone`, `email`, `firstname`, `lastname`, `manager_type`, `password`, `needs_password_change`) VALUES
(NULL, NULL, NULL, b'1', NULL, NULL, 30, NULL, NULL, NULL, 1, NULL, NULL, '+1234567890', 'john@example.com', 'John', 'Doe', NULL, 'admin123', b'0'),
(NULL, NULL, NULL, b'1', NULL, NULL, 30, NULL, NULL, NULL, 2, NULL, NULL, '+9876543210', 'jane@example.com', 'Jane', 'Smith', NULL, 'admin123', b'0'),
(NULL, NULL, NULL, b'1', NULL, NULL, 30, NULL, NULL, NULL, 3, NULL, NULL, '+2468135790', 'alice@example.com', 'Alice', 'Johnson', NULL, 'admin123', b'0'),
(NULL, NULL, NULL, b'1', NULL, NULL, 30, NULL, NULL, NULL, 4, NULL, NULL, '+1357924680', 'bob@example.com', 'Bob', 'Brown', NULL, 'admin123', b'0'),
(NULL, NULL, NULL, b'1', NULL, NULL, 30, NULL, NULL, NULL, 5, NULL, NULL, '+3698521470', 'david@example.com', 'David', 'Taylor', NULL, 'admin123', b'0'),
(NULL, NULL, NULL, b'1', NULL, NULL, 30, NULL, NULL, NULL, 6, NULL, NULL, '+7539514680', 'emily@example.com', 'Emily', 'Wilson', NULL, 'admin123', b'0'),
(NULL, NULL, NULL, b'1', NULL, NULL, 30, NULL, NULL, NULL, 7, NULL, NULL, '+9876541230', 'grace@example.com', 'Grace', 'Martinez', NULL, 'admin123', b'0'),
(NULL, NULL, NULL, b'1', NULL, NULL, 30, NULL, NULL, NULL, 8, NULL, NULL, '+3692581470', 'henry@example.com', 'Henry', 'Garcia', NULL, '$2a$12$cT6s95arnfrbj0RKGdXmVerzhfT3RYwCtzSL/neVsSzUG09uNS/XG', b'0'),
(NULL, NULL, NULL, b'1', NULL, NULL, 30, NULL, NULL, NULL, 9, NULL, NULL, '+4569871230', 'kevin@example.com', 'Kevin', 'Thomas', NULL, '$2a$12$cT6s95arnfrbj0RKGdXmVerzhfT3RYwCtzSL/neVsSzUG09uNS/XG', b'0'),
(NULL, NULL, NULL, b'1', NULL, NULL, 30, NULL, NULL, NULL, 10, NULL, NULL, '+7894561230', 'michael@example.com', 'Michael', 'Johnson', NULL, '$2a$12$cT6s95arnfrbj0RKGdXmVerzhfT3RYwCtzSL/neVsSzUG09uNS/XG', b'0'),
(NULL, NULL, NULL, b'1', NULL, NULL, 30, NULL, NULL, NULL, 11, NULL, NULL, '+2587419300', 'sarah@example.com', 'Sarah', 'Miller', NULL, '$2a$12$cT6s95arnfrbj0RKGdXmVerzhfT3RYwCtzSL/neVsSzUG09uNS/XG', b'0'),
(NULL, NULL, NULL, b'1', NULL, NULL, 30, NULL, NULL, NULL, 12, NULL, NULL, '+3698527410', 'william@example.com', 'William', 'Brown', NULL, '$2a$12$cT6s95arnfrbj0RKGdXmVerzhfT3RYwCtzSL/neVsSzUG09uNS/XG', b'0'),
(NULL, NULL, NULL, b'1', NULL, NULL, 30, NULL, NULL, NULL, 13, NULL, NULL, '+9741236580', 'jennifer@example.com', 'Jennifer', 'Davis', NULL, '$2a$12$cT6s95arnfrbj0RKGdXmVerzhfT3RYwCtzSL/neVsSzUG09uNS/XG', b'0'),
(NULL, NULL, NULL, b'1', NULL, NULL, 30, NULL, NULL, NULL, 14, NULL, NULL, '+8524173900', 'davidg@example.com', 'David', 'Garcia', NULL, '$2a$12$cT6s95arnfrbj0RKGdXmVerzhfT3RYwCtzSL/neVsSzUG09uNS/XG', b'0'),
(NULL, NULL, NULL, b'1', NULL, NULL, 30, NULL, NULL, NULL, 15, NULL, NULL, '+6541239870', 'ashley@example.com', 'Ashley', 'Robinson', NULL, '$2a$12$cT6s95arnfrbj0RKGdXmVerzhfT3RYwCtzSL/neVsSzUG09uNS/XG', b'0'),
(NULL, NULL, NULL, b'1', NULL, NULL, 30, NULL, NULL, NULL, 16, NULL, NULL, '+4917832156', 'brandon@example.com', 'Brandon', 'Clark', NULL, '$2a$12$cT6s95arnfrbj0RKGdXmVerzhfT3RYwCtzSL/neVsSzUG09uNS/XG', b'0'),
(NULL, NULL, NULL, b'1', NULL, NULL, 30, NULL, NULL, NULL, 17, NULL, NULL, '+3374196320', 'christina@example.com', 'Christina', 'Lewis', NULL, '$2a$12$cT6s95arnfrbj0RKGdXmVerzhfT3RYwCtzSL/neVsSzUG09uNS/XG', b'0'),
(NULL, NULL, NULL, b'1', NULL, NULL, 30, NULL, NULL, NULL, 18, NULL, NULL, '+1237894560', 'daniel@example.com', 'Daniel', 'Walker', NULL, '$2a$12$cT6s95arnfrbj0RKGdXmVerzhfT3RYwCtzSL/neVsSzUG09uNS/XG', b'0'),
(NULL, NULL, NULL, b'1', NULL, NULL, 30, NULL, NULL, NULL, 19, NULL, NULL, '+5419873260', 'elizabeth@example.com', 'Elizabeth', 'Hall', NULL, '$2a$12$cT6s95arnfrbj0RKGdXmVerzhfT3RYwCtzSL/neVsSzUG09uNS/XG', b'0'),
(NULL, NULL, NULL, b'1', NULL, NULL, 30, NULL, NULL, NULL, 20, NULL, NULL, '+6145678901', 'joseph@example.com', 'Joseph', 'Baker', NULL, '$2a$12$cT6s95arnfrbj0RKGdXmVerzhfT3RYwCtzSL/neVsSzUG09uNS/XG', b'0'),
(NULL, b'0', b'1', b'1', b'1', b'1', 30, '2024-11-19 18:33:16.000000', NULL, NULL, 41, NULL, NULL, '+216123456789', 'admin@anywr.com', 'admin', 'admin', 'NONE', '$2a$10$mUF6htKKPJbIfnhqrVYT4.8plqv2lJkpnC25SsI4xNnhsa62aESGS', b'0'),
(NULL, b'0', NULL, b'1', NULL, b'1', 30, '2024-11-19 18:58:24.000000', NULL, NULL, 42, NULL, 1, '+21647589678', 'Aziz.attia@anywr.com', 'Aziz', 'Attia', 'DEPARTMENT_RESPONSIBLE', '$2a$10$A7y5R.I4sIjZ8KcBHXPniOr1WNLmGHEaxbiny1FN/aMEAirjEwIj.', b'0'),
(NULL, b'0', NULL, b'1', NULL, b'1', 30, '2024-11-19 19:02:24.000000', NULL, NULL, 43, NULL, 2, '+21624752026', 'abdellaziz.elloumi@anywr.com', 'Abdellaziz', 'Elloumi', 'EQUIPE_MANAGER', '$2a$10$DKiM787GoK3PKIdnPmGMueCjXmmDhhDufNbknPM6vN0btuGlcxvGS', b'0'),
(NULL, b'0', b'1', b'1', NULL, NULL, 21, '2024-11-20 16:41:48.000000', NULL, 3, 44, NULL, 3, '+21675486930', 'DevJunior@anywr.com', 'Dev', 'Junior', 'NONE', '$2a$10$tilrgCKjDr16vmbLgkVyZelkeNbzuwAd1TmGPjzt9KlJ7mC1e6ur6', b'0'),
(NULL, b'0', b'1', b'1', NULL, NULL, 30, '2024-11-20 16:42:20.000000', NULL, 3, 45, NULL, 4, '+21675894385', 'DevSenior@anywr.com', 'Dev', 'Senior', 'NONE', '$2a$10$SegBROPnmjaqgRiGA2pnveITx77izxMWcnSogK4eC6Sacl7GhC88e', b'0'),
(NULL, b'0', b'1', b'1', NULL, NULL, 30, '2024-11-25 14:08:17.000000', NULL, NULL, 187, NULL, NULL, '+21647566893', 'firstAuth@anywr.com', 'First', 'Auth', 'NONE', '$2a$10$JoVLcyBDBFNr.U6sKAC.YOIHYd93bie4ZeRMZAlomvZBeFdu9xOrC', b'0');

-- --------------------------------------------------------

--
-- Structure de la table `collaborateur_competence`
--

CREATE TABLE `collaborateur_competence` (
  `collaborateur_id` bigint(20) NOT NULL,
  `competence_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `evaluation` varchar(255) DEFAULT NULL,
  `scale_type` enum('DESCRIPTIF','NUMERIC','STARS') DEFAULT NULL,
  `type_competence` enum('ACQUISE','REQUISE') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `collaborateur_competence`
--

INSERT INTO `collaborateur_competence` (`collaborateur_id`, `competence_id`, `id`, `evaluation`, `scale_type`, `type_competence`) VALUES
(42, 1, 1, '1 STAR', 'STARS', 'ACQUISE'),
(42, 3, 2, '1', 'NUMERIC', 'ACQUISE'),
(42, 4, 3, 'FAIBLE', 'DESCRIPTIF', 'ACQUISE'),
(42, 2, 4, '1 STAR', 'STARS', 'ACQUISE'),
(43, 1, 5, '1 STAR', 'STARS', 'ACQUISE'),
(43, 2, 6, '1 STAR', 'STARS', 'ACQUISE'),
(43, 4, 7, 'FAIBLE', 'DESCRIPTIF', 'ACQUISE'),
(43, 3, 8, '1', 'NUMERIC', 'ACQUISE'),
(44, 1, 9, '1 STAR', 'STARS', 'ACQUISE'),
(44, 3, 10, '1', 'NUMERIC', 'ACQUISE'),
(44, 2, 11, '2 STARS', 'STARS', 'ACQUISE'),
(44, 4, 12, 'FAIBLE', 'DESCRIPTIF', 'ACQUISE'),
(45, 1, 13, '2 STARS', 'STARS', 'ACQUISE'),
(45, 2, 14, '1 STAR', 'STARS', 'ACQUISE'),
(45, 4, 15, 'FAIBLE', 'DESCRIPTIF', 'ACQUISE'),
(45, 3, 16, '1', 'NUMERIC', 'ACQUISE'),
(187, 4, 17, 'FAIBLE', 'DESCRIPTIF', 'ACQUISE'),
(187, 1, 18, '1 STAR', 'STARS', 'ACQUISE'),
(187, 2, 19, '1 STAR', 'STARS', 'ACQUISE'),
(187, 3, 20, '1', 'NUMERIC', 'ACQUISE');

-- --------------------------------------------------------

--
-- Structure de la table `collaborateur_roles`
--

CREATE TABLE `collaborateur_roles` (
  `collaborateur_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `collaborateur_roles`
--

INSERT INTO `collaborateur_roles` (`collaborateur_id`, `role_id`) VALUES
(1, 3),
(2, 2),
(3, 1),
(4, 3),
(5, 2),
(6, 1),
(7, 2),
(8, 1),
(9, 3),
(10, 2),
(11, 1),
(12, 3),
(13, 2),
(14, 1),
(15, 3),
(16, 2),
(17, 1),
(18, 3),
(19, 2),
(20, 1),
(41, 1),
(41, 2),
(41, 3),
(42, 2),
(43, 2),
(44, 3),
(45, 3),
(187, 3);

-- --------------------------------------------------------

--
-- Structure de la table `competence`
--

CREATE TABLE `competence` (
  `id` bigint(20) NOT NULL,
  `description` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `scale_type` enum('DESCRIPTIF','NUMERIC','STARS') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `competence`
--

INSERT INTO `competence` (`id`, `description`, `name`, `scale_type`) VALUES
(1, 'English All The Time ', 'Anglais', 'STARS'),
(2, 'French ', 'Français', 'STARS'),
(3, 'Java Programming Language', 'Java', 'NUMERIC'),
(4, 'Soft skills', 'Soft Skills', 'DESCRIPTIF');

-- --------------------------------------------------------

--
-- Structure de la table `competence_history`
--

CREATE TABLE `competence_history` (
  `change_date` datetime(6) DEFAULT NULL,
  `collaborator_id` bigint(20) NOT NULL,
  `competence_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `change_reason` varchar(255) DEFAULT NULL,
  `new_evaluation` varchar(255) DEFAULT NULL,
  `old_evaluation` varchar(255) DEFAULT NULL,
  `growth` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `competence_history`
--

INSERT INTO `competence_history` (`change_date`, `collaborator_id`, `competence_id`, `id`, `change_reason`, `new_evaluation`, `old_evaluation`, `growth`) VALUES
('2024-11-29 16:15:38.000000', 45, 1, 1, 'Training completion', '2 STARS', '1 STAR', 0),
('2024-11-29 16:15:49.000000', 44, 2, 2, 'Training completion', '2 STARS', '1 STAR', 0);

-- --------------------------------------------------------

--
-- Structure de la table `competence_possible_values`
--

CREATE TABLE `competence_possible_values` (
  `competence_id` bigint(20) NOT NULL,
  `possible_values` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `competence_possible_values`
--

INSERT INTO `competence_possible_values` (`competence_id`, `possible_values`) VALUES
(1, '1 STAR'),
(1, '2 STARS'),
(1, '3 STARS'),
(1, '4 STARS'),
(2, '1 STAR'),
(2, '2 STARS'),
(2, '3 STARS'),
(2, '4 STARS'),
(3, '1'),
(3, '2'),
(3, '3'),
(3, '4'),
(3, '5'),
(4, 'FAIBLE'),
(4, 'MOYEN'),
(4, 'BON'),
(4, 'EXCELLENT');

-- --------------------------------------------------------

--
-- Structure de la table `demande_absence`
--

CREATE TABLE `demande_absence` (
  `approved_by_manager` tinyint(4) DEFAULT NULL,
  `approved_by_responsable_dep` tinyint(4) DEFAULT NULL,
  `approved_by_rh` tinyint(4) DEFAULT NULL,
  `requested_days` int(11) NOT NULL,
  `status_demande` tinyint(4) DEFAULT NULL,
  `absence_id` bigint(20) DEFAULT NULL,
  `collaborateur_id` bigint(20) DEFAULT NULL,
  `date_debut` datetime(6) DEFAULT NULL,
  `datefin` datetime(6) DEFAULT NULL,
  `department_id` bigint(20) DEFAULT NULL,
  `equipe_id` bigint(20) DEFAULT NULL,
  `id_demande_ab` bigint(20) NOT NULL,
  `is_created_at` datetime(6) NOT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `justificatif_path` varchar(255) DEFAULT NULL,
  `refusal_reason` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `demande_absence`
--

INSERT INTO `demande_absence` (`approved_by_manager`, `approved_by_responsable_dep`, `approved_by_rh`, `requested_days`, `status_demande`, `absence_id`, `collaborateur_id`, `date_debut`, `datefin`, `department_id`, `equipe_id`, `id_demande_ab`, `is_created_at`, `comment`, `justificatif_path`, `refusal_reason`) VALUES
(2, 3, 4, 5, 5, 1, 44, '2024-11-24 00:00:00.000000', '2024-11-29 00:00:00.000000', 1, 3, 1, '2024-11-21 00:21:37.000000', 'test', NULL, NULL),
(2, 3, 4, 4, 5, 3, 44, '2024-12-02 00:00:00.000000', '2024-12-06 00:00:00.000000', 1, 3, 2, '2024-11-28 19:29:28.000000', 'test maladie', '/uploads/justificatifs/429753965_416858520820148_3785383721365586962_n.jpg', NULL);

-- --------------------------------------------------------

--
-- Structure de la table `demande_formation`
--

CREATE TABLE `demande_formation` (
  `created_at` datetime(6) DEFAULT NULL,
  `employee_id` bigint(20) DEFAULT NULL,
  `formation_id` bigint(20) DEFAULT NULL,
  `id` bigint(20) NOT NULL,
  `manager_id` bigint(20) DEFAULT NULL,
  `justification` varchar(255) DEFAULT NULL,
  `rejection_reason` varchar(255) DEFAULT NULL,
  `status` enum('ACCEPTED','PENDING','REJECTED') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `demande_formation`
--

INSERT INTO `demande_formation` (`created_at`, `employee_id`, `formation_id`, `id`, `manager_id`, `justification`, `rejection_reason`, `status`) VALUES
('2024-11-29 16:14:20.000000', 44, 2, 1, 43, 'Requested by manager', NULL, 'ACCEPTED'),
('2024-11-29 16:14:23.000000', 45, 1, 2, 43, 'Requested by manager', NULL, 'ACCEPTED');

-- --------------------------------------------------------

--
-- Structure de la table `departement`
--

CREATE TABLE `departement` (
  `id_dep` bigint(20) NOT NULL,
  `responsible_id` bigint(20) DEFAULT NULL,
  `nom_dep` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `departement`
--

INSERT INTO `departement` (`id_dep`, `responsible_id`, `nom_dep`) VALUES
(1, 42, 'IT');

-- --------------------------------------------------------

--
-- Structure de la table `equipe`
--

CREATE TABLE `equipe` (
  `departement_id` bigint(20) DEFAULT NULL,
  `id_equipe` bigint(20) NOT NULL,
  `manager_equipe_id` bigint(20) DEFAULT NULL,
  `nom` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `equipe`
--

INSERT INTO `equipe` (`departement_id`, `id_equipe`, `manager_equipe_id`, `nom`) VALUES
(1, 3, 43, 'SIRH-ANYWR');

-- --------------------------------------------------------

--
-- Structure de la table `events`
--

CREATE TABLE `events` (
  `id` bigint(20) NOT NULL,
  `description` varchar(255) NOT NULL,
  `type` enum('ACCOUNT_SETTINGS_UPDATE','LOGIN_ATTEMPT','LOGIN_ATTEMPT_FAILURE','LOGIN_ATTEMPT_SUCCESS','PASSWORD_UPDATE','PROFILE_UPDATE','ROLE_UPDATE') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `formation`
--

CREATE TABLE `formation` (
  `current_level` int(11) NOT NULL,
  `duration_in_days` int(11) NOT NULL,
  `finishing_at` date DEFAULT NULL,
  `progress` double NOT NULL,
  `starting_at` date DEFAULT NULL,
  `target_level` int(11) NOT NULL,
  `catalogue_id` bigint(20) DEFAULT NULL,
  `competence_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `formation`
--

INSERT INTO `formation` (`current_level`, `duration_in_days`, `finishing_at`, `progress`, `starting_at`, `target_level`, `catalogue_id`, `competence_id`, `id`, `description`, `title`) VALUES
(1, 0, '2024-12-04', 0, '2024-12-02', 2, 1, 1, 1, 'Anglais 1-2', 'Anglais'),
(1, 0, '2024-12-04', 0, '2024-12-02', 2, 1, 2, 2, 'Français 1-2', 'Français');

-- --------------------------------------------------------

--
-- Structure de la table `inscription`
--

CREATE TABLE `inscription` (
  `completed` bit(1) NOT NULL,
  `completion_date` date DEFAULT NULL,
  `is_enrolled` bit(1) NOT NULL,
  `is_pending_evaluation` bit(1) NOT NULL,
  `progress` int(11) NOT NULL,
  `collaborator_id` bigint(20) NOT NULL,
  `enrollment_date` datetime(6) DEFAULT NULL,
  `formation_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `last_evaluation_date` datetime(6) DEFAULT NULL,
  `evaluation_feedback` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `inscription`
--

INSERT INTO `inscription` (`completed`, `completion_date`, `is_enrolled`, `is_pending_evaluation`, `progress`, `collaborator_id`, `enrollment_date`, `formation_id`, `id`, `last_evaluation_date`, `evaluation_feedback`) VALUES
(b'1', '2024-11-29', b'0', b'0', 100, 44, '2024-11-29 16:14:38.000000', 2, 1, NULL, NULL),
(b'1', '2024-11-29', b'0', b'0', 100, 45, '2024-11-29 16:14:41.000000', 1, 2, NULL, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `poste`
--

CREATE TABLE `poste` (
  `id_poste` bigint(20) NOT NULL,
  `titre_poste` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `poste`
--

INSERT INTO `poste` (`id_poste`, `titre_poste`) VALUES
(1, 'Chef Département'),
(3, 'Développeur Junior'),
(4, 'Développeur Sénior'),
(2, 'Directeur technique');

-- --------------------------------------------------------

--
-- Structure de la table `poste_competence`
--

CREATE TABLE `poste_competence` (
  `competence_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `poste_id` bigint(20) NOT NULL,
  `evaluation` varchar(255) DEFAULT NULL,
  `scale_type` enum('DESCRIPTIF','NUMERIC','STARS') DEFAULT NULL,
  `type_competence` enum('ACQUISE','REQUISE') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `poste_competence`
--

INSERT INTO `poste_competence` (`competence_id`, `id`, `poste_id`, `evaluation`, `scale_type`, `type_competence`) VALUES
(1, 1, 1, '3 STARS', 'STARS', 'REQUISE'),
(2, 2, 1, '3 STARS', 'STARS', 'REQUISE'),
(4, 3, 1, 'BON', 'DESCRIPTIF', 'REQUISE'),
(3, 4, 1, '3', 'NUMERIC', 'REQUISE'),
(4, 5, 2, 'BON', 'DESCRIPTIF', 'REQUISE'),
(1, 6, 2, '3 STARS', 'STARS', 'REQUISE'),
(3, 7, 2, '4', 'NUMERIC', 'REQUISE'),
(2, 8, 2, '3 STARS', 'STARS', 'REQUISE'),
(3, 9, 3, '3', 'NUMERIC', 'REQUISE'),
(4, 10, 3, 'MOYEN', 'DESCRIPTIF', 'REQUISE'),
(1, 11, 3, '3 STARS', 'STARS', 'REQUISE'),
(2, 12, 3, '3 STARS', 'STARS', 'REQUISE'),
(3, 13, 4, '4', 'NUMERIC', 'REQUISE'),
(1, 14, 4, '3 STARS', 'STARS', 'REQUISE'),
(2, 15, 4, '3 STARS', 'STARS', 'REQUISE'),
(4, 16, 4, 'MOYEN', 'DESCRIPTIF', 'REQUISE');

-- --------------------------------------------------------

--
-- Structure de la table `roles`
--

CREATE TABLE `roles` (
  `id` bigint(20) NOT NULL,
  `name` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `roles`
--

INSERT INTO `roles` (`id`, `name`) VALUES
(1, 'ROLE_RH'),
(2, 'ROLE_MANAGER'),
(3, 'ROLE_COLLABORATOR');

-- --------------------------------------------------------

--
-- Structure de la table `token`
--

CREATE TABLE `token` (
  `expired` bit(1) NOT NULL,
  `id` int(11) NOT NULL,
  `revoked` bit(1) NOT NULL,
  `collaborateur_id` bigint(20) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `expires_at` datetime(6) DEFAULT NULL,
  `token` varchar(512) DEFAULT NULL,
  `token_type` enum('BEARER','VERIFICATION') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `token`
--

INSERT INTO `token` (`expired`, `id`, `revoked`, `collaborateur_id`, `created_at`, `expires_at`, `token`, `token_type`) VALUES
(b'1', 2, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzIwMzc1OTksImV4cCI6MTczMjEyMzk5OSwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.Az9HX5jOinb5YEUvsly4mdhpQDwFaE-nuq-BPNgZxLF08H-zdG1aF1ftfxWBs2tvKAklbLH9GSsFlGUrjGl-UQ', 'BEARER'),
(b'1', 3, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzIwMzg2MzksImV4cCI6MTczMjEyNTAzOSwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.FiL3qw-AioQCxIcAIvdBnq1uAD6M87XUSIKVKdT83Qdshe2E651lmspj1i5u93ZrTvN3qh77lJdOa2rY_uP0BQ', 'BEARER'),
(b'1', 5, b'1', 42, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBeml6LmF0dGlhQGFueXdyLmNvbSIsImlhdCI6MTczMjAzOTEwNywiZXhwIjoxNzMyMTI1NTA3LCJ1c2VyOiI6IkF6aXouYXR0aWFAYW55d3IuY29tIiwicm9sZXMiOlsiUk9MRV9NQU5BR0VSIl19.VWVp3vR6z_Q0EjxGntraI2qAzBXnDnDlqqRACt-tXrwmpZeulQjOD86lnF30EE8nzaSSPqRge4Rbncr1JP1EXQ', 'BEARER'),
(b'1', 7, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyMDM5MzQ3LCJleHAiOjE3MzIxMjU3NDcsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.y8yMOqqIKPogw_mvioz23EBN-m17XmXigoiXFwE4uSE6w_s4THcoYoRFgy_dQG3f1jC4XfPykvKmzo8M2Gs2iA', 'BEARER'),
(b'1', 9, b'1', 44, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJEZXZKdW5pb3JAYW55d3IuY29tIiwiaWF0IjoxNzMyMTE3MzExLCJleHAiOjE3MzIyMDM3MTEsInVzZXI6IjoiRGV2SnVuaW9yQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfQ09MTEFCT1JBVE9SIl19._58fwXGmd207ymhsLPLsgtgYIKEEyyQCsHmjvVJa3WRKUvAujWYthCj_-6aKgXYpbFM7XqIOY18EoWyLpbW8pg', 'BEARER'),
(b'1', 11, b'1', 45, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJEZXZTZW5pb3JAYW55d3IuY29tIiwiaWF0IjoxNzMyMTE3MzQ0LCJleHAiOjE3MzIyMDM3NDQsInVzZXI6IjoiRGV2U2VuaW9yQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfQ09MTEFCT1JBVE9SIl19.WLFcZzzBG7EBqWXMrETM_ip3Z6xwN7Vmcogxhs0EdpSOMOuRSxJNBgy2qPvs3WPpGtWG7Rjb2953hOm92kBmVg', 'BEARER'),
(b'1', 12, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyMTE3NjI1LCJleHAiOjE3MzIyMDQwMjUsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.-Vk3NO5g9kL_hd9a7eBaBwiSE00c8mlG8hEf2wcc234UfVb8U1vttwUKwXlUffpJ-eRo6K_qaA3X2702BRNGZA', 'BEARER'),
(b'1', 13, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyMTE3ODExLCJleHAiOjE3MzIyMDQyMTEsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.L6mV9JGHPkMOxvvegBAkqcZNxpNTG-WRCjRnlQmwrYA2P9fKfGUIrPBZWd4fs5I6gYprntRlKsBKfUDf2OGoiA', 'BEARER'),
(b'1', 14, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyMTE3ODg0LCJleHAiOjE3MzIyMDQyODQsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.wLLcpAqmh2J3z7tw7i8bZki1t1O2bTyCjgg0UOAJSEXs7gn4JQ_KU1JtxNC4GN4bH5N0Kxh2OroIMnkXW3gSmw', 'BEARER'),
(b'1', 15, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzIxMTc5MDEsImV4cCI6MTczMjIwNDMwMSwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.73gS0pXIFfPCnVB5r4mkkJdyTMKD0LKwLTIZ8hAjZsULEO5TwAUU4n8SnyytahTKCBFKSwJno9zdzZvCLltP5w', 'BEARER'),
(b'1', 16, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyMTIwNjMxLCJleHAiOjE3MzIyMDcwMzEsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.qCIyWYDnAt5Kbl1tLUwYTa2KtK-NFOrFnw5RKZRErmMlJkOY9O6K_iMYqH_kADjO4KP1GF0zZ4ZiYSglAt1APA', 'BEARER'),
(b'1', 17, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzIxMjE3NDEsImV4cCI6MTczMjIwODE0MSwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.pkgs_Tf9WBna0D45A2T9qRZUelD8_-t6PXskkWkzUydw53m5gbwF51-Ha49Dt7toY0VO_9q3yd8SI7eyoZ8hFQ', 'BEARER'),
(b'1', 18, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyMTIyMDMwLCJleHAiOjE3MzIyMDg0MzAsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.02dREOgboCT5LOCQVnH1xtGBihJ8pwdzM696XH9v0C4fgJxmu3NpP3D7nSD_uOupnxGFx5SACYG6nkmDpGWOGg', 'BEARER'),
(b'1', 19, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzIxMjI5MTgsImV4cCI6MTczMjIwOTMxOCwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.xMmfchmWc8C7a9ZGSLfBFyzGnCmGThv9_I3dXrvSec4ZhXPwYo_KU0Dtb4NYhL42j4pnotSvPW7ZcLtamhPO4A', 'BEARER'),
(b'1', 20, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyMTIzNjM1LCJleHAiOjE3MzIyMTAwMzUsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.1q7XM-evRtCORb_qlgBRLRBVxc8ssG9jiag8SP9hJlx_LZAcPtEHYzeSyhfUwLw1cZ0SnW60uWhffkTDq1cJ5Q', 'BEARER'),
(b'1', 21, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzIxMzA3NTcsImV4cCI6MTczMjIxNzE1NywidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.kxTA5CCtBykbv6tl58Yvz_pYJuQHrIIm3qreszA9FNeFgYj9V4YqnVyb_Z9gy8gTnujOoxKQCLVjE2yT2kiYQg', 'BEARER'),
(b'1', 22, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzIxMzg4NTYsImV4cCI6MTczMjIyNTI1NiwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.Nq_4Sa2h44DEfMrFatGHnWFsYMiO9Dts7ZbFMIxK7jOO66ZI6YrQ7ftzQUnZ22de1QQr5aMW3CW8WBnJ0bCH5g', 'BEARER'),
(b'1', 23, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzIxNDQ3NzcsImV4cCI6MTczMjIzMTE3NywidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.6XTG6SNB1gLyp_6MVcH-fRVC6j270qSu_uOlrexF-ezaec4yJtg2Tj6TauHVHWJySkyiFLVYMEPXlXzc2DdIMg', 'BEARER'),
(b'1', 24, b'1', 42, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBeml6LmF0dGlhQGFueXdyLmNvbSIsImlhdCI6MTczMjE0NDc4OCwiZXhwIjoxNzMyMjMxMTg4LCJ1c2VyOiI6IkF6aXouYXR0aWFAYW55d3IuY29tIiwicm9sZXMiOlsiUk9MRV9NQU5BR0VSIl19.5lcT5v3AcfkdR7QEpyzTge7NehiHJCmIqVJlI1-bxSWjeMw52ojHOexrzdtAxjIAb49GjAmP0eTvnLjP5MT3lg', 'BEARER'),
(b'1', 25, b'1', 44, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJEZXZKdW5pb3JAYW55d3IuY29tIiwiaWF0IjoxNzMyMTQ0ODg0LCJleHAiOjE3MzIyMzEyODQsInVzZXI6IjoiRGV2SnVuaW9yQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfQ09MTEFCT1JBVE9SIl19.OlsWxXVUVWXc-nth14PYz9hsYyE3YW8ZVtHHgIFiC7i3lvJ2xTCG-aXRuMW6XAAYUrmi58t12DLFlFM9EdCgkA', 'BEARER'),
(b'1', 26, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyMTQ0OTEyLCJleHAiOjE3MzIyMzEzMTIsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.ldiuwPn2iQ-GBgTRyfNZcxsXB12RBJ2A_l7RxFfe2sVWTx_j3WLE2-3x46ZWLbOUsOvJpop10AlXZEvexKmtVQ', 'BEARER'),
(b'1', 27, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzIxNDUwMjMsImV4cCI6MTczMjIzMTQyMywidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.G4qp0OWk-IOpJ4QoSUN2QEYEyMOcb8YcgIdYFdLbwFtKQaoL5bOhFTTfCYQn-feih8-XiS2pXbESMXSCCGsiZA', 'BEARER'),
(b'1', 28, b'1', 42, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBeml6LmF0dGlhQGFueXdyLmNvbSIsImlhdCI6MTczMjE0NTAzNCwiZXhwIjoxNzMyMjMxNDM0LCJ1c2VyOiI6IkF6aXouYXR0aWFAYW55d3IuY29tIiwicm9sZXMiOlsiUk9MRV9NQU5BR0VSIl19.DjWHldFfIEK1E5GkoKg8-El7jmj9VqOlvvX2z01tSGJQ0HTTeiI03qhBvOBc1XdPG4oam6U9cuGrRvXt8L9uKw', 'BEARER'),
(b'1', 29, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzIxNDU1NjEsImV4cCI6MTczMjIzMTk2MSwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.WbfQxOAxcnjLiLxCOCvvwo-8YCdrXsjzvJIhNqRuuMRg6dLKnP8Fxwnd3f0BOOJfMeAZR9Ifz799RO8qGZbF-Q', 'BEARER'),
(b'1', 30, b'1', 42, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBeml6LmF0dGlhQGFueXdyLmNvbSIsImlhdCI6MTczMjE0NTYwNCwiZXhwIjoxNzMyMjMyMDA0LCJ1c2VyOiI6IkF6aXouYXR0aWFAYW55d3IuY29tIiwicm9sZXMiOlsiUk9MRV9NQU5BR0VSIl19.W7OWD38H91v6JBIjtByd9Og9KFpwv6gwQUjsIRjPRK2K5OmbMLJ3t1WwV583koUARBL60wGfymsl3e9tkgKnxA', 'BEARER'),
(b'1', 31, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzIxNDcxMDAsImV4cCI6MTczMjIzMzUwMCwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.3qsIPlzVAgyL2gPrVhIeVBAPu9AVgDNvBKk3mcTASZizz89Bxy3gmsVD0Uwp-yGaqLP15gDQCgoYWoAOGhSIsg', 'BEARER'),
(b'1', 32, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzIxNDc0NzEsImV4cCI6MTczMjIzMzg3MSwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.E7rU8HPU_aaaUu77YscktW0QCi5yTfFb6FfHoeKPBKgR3CtBQW-PKdqu2euNHSd2T6Nx0SQ9tlbj4ZAcQkDctg', 'BEARER'),
(b'1', 33, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzIxNDg3NDIsImV4cCI6MTczMjIzNTE0MiwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.bmgRaUyKD2g0_2cb8quZ3s9tBLKylIeTIYjLgpglM3Cj0b02M4kC2My7t6F14xcErDuGaFA_4YSC7pWGZ7Dahw', 'BEARER'),
(b'1', 34, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzIxNDk2NzMsImV4cCI6MTczMjIzNjA3MywidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.MK5FV4rvcfTFy9fd-f1UPA8zwP7ykeSSJYA0WfI0IjErN5TzfqrlU_2PJFFwkn5LyMNGaqgbRoxv5QFV6QdgEw', 'BEARER'),
(b'1', 35, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzIxNTAxNzQsImV4cCI6MTczMjIzNjU3NCwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.OQeVng2kG7TXPPW1H4Mg5cJ9Nr6dPl_-b4Fn4csVFiw6aJ9FszRtq5QgghW6Vkv0N99T20Y77j7Z7dyf68slKw', 'BEARER'),
(b'1', 36, b'1', 42, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBeml6LmF0dGlhQGFueXdyLmNvbSIsImlhdCI6MTczMjE1MDE5MCwiZXhwIjoxNzMyMjM2NTkwLCJ1c2VyOiI6IkF6aXouYXR0aWFAYW55d3IuY29tIiwicm9sZXMiOlsiUk9MRV9NQU5BR0VSIl19.W1JQbY-9dCH6B84WaPhlqsTkTXWw6EthHkfOIPszdUyR1c09nKlV7E0wLo10wJjKmMLzluEPx-WM4mTBRWDg0Q', 'BEARER'),
(b'1', 37, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzIxOTgxNzksImV4cCI6MTczMjI4NDU3OSwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.1LFL59TEr0NhmR5ehHYGo85JTH2LsJGi0RDYTPpTJACGoZqfwvauqkv7JbS4LWMJe0Pyu1ckZu_9USs0nhdvhw', 'BEARER'),
(b'1', 38, b'1', 42, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBeml6LmF0dGlhQGFueXdyLmNvbSIsImlhdCI6MTczMjE5ODM1MCwiZXhwIjoxNzMyMjg0NzUwLCJ1c2VyOiI6IkF6aXouYXR0aWFAYW55d3IuY29tIiwicm9sZXMiOlsiUk9MRV9NQU5BR0VSIl19.3sbH3uS7TydCwaIuNquID6N-rGE3MD1DnbPSbVAosJjasYOm6n573Y3awaEaWETFvDpFiqRjwspfjvT9z_yP2w', 'BEARER'),
(b'1', 39, b'1', 42, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBeml6LmF0dGlhQGFueXdyLmNvbSIsImlhdCI6MTczMjE5ODU0NCwiZXhwIjoxNzMyMjg0OTQ0LCJ1c2VyOiI6IkF6aXouYXR0aWFAYW55d3IuY29tIiwicm9sZXMiOlsiUk9MRV9NQU5BR0VSIl19.4opnV-gj_0YXjElAUILVBZ12wJ29iIvp_cX2QmCpw77iivuwqvThatl2amMocBLso0os4sR-_XiUrKd03gAhow', 'BEARER'),
(b'1', 40, b'1', 42, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBeml6LmF0dGlhQGFueXdyLmNvbSIsImlhdCI6MTczMjIwMDQxMSwiZXhwIjoxNzMyMjg2ODExLCJ1c2VyOiI6IkF6aXouYXR0aWFAYW55d3IuY29tIiwicm9sZXMiOlsiUk9MRV9NQU5BR0VSIl19.M7qUYrxkmQV7Art4xRzEhJpq7akpZVFYhrFDdPd4e5f0ualwLAoFMW8dYUzoNyQALTSWobeL9XMvZ36p-tK2bQ', 'BEARER'),
(b'1', 41, b'1', 42, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBeml6LmF0dGlhQGFueXdyLmNvbSIsImlhdCI6MTczMjIwMDQxMiwiZXhwIjoxNzMyMjg2ODEyLCJ1c2VyOiI6IkF6aXouYXR0aWFAYW55d3IuY29tIiwicm9sZXMiOlsiUk9MRV9NQU5BR0VSIl19.nxvUPKsxw6szIp0-06xaoTi17t-pwGqRXmGzpspxFK5g6Ym6V2t0E_okwvTojIl_qTqwkj-Amjppws_LELlnyw', 'BEARER'),
(b'1', 42, b'1', 42, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBeml6LmF0dGlhQGFueXdyLmNvbSIsImlhdCI6MTczMjIwMDQ3NSwiZXhwIjoxNzMyMjg2ODc1LCJ1c2VyOiI6IkF6aXouYXR0aWFAYW55d3IuY29tIiwicm9sZXMiOlsiUk9MRV9NQU5BR0VSIl19.3vCd9P5Kb38uLU-S3pFvvMimZyyp_G2QE6yLpIw_zGK9ZxTYuSxINnPJFwkpLlvUQmMZw854Q5AX-3Lo_oTG8g', 'BEARER'),
(b'1', 43, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyMjg0NDQyLCJleHAiOjE3MzIzNzA4NDIsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.uqD76rFHFCLgL00zyAB1IhNpHwlWYCSbCNEIC3zzIMNOodtnkPShS5xD6_ZxM4B8ZT05INDQcG-13XCXnyIVRA', 'BEARER'),
(b'1', 44, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyMjg0NDQ5LCJleHAiOjE3MzIzNzA4NDksInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.wWqvN-beTVGaCOaxrniJ0BhBJG6FUfmG2a5p1z0r9p6qT9CASebrvl7DWh0fLfvyBodrcVPnspznNAuosYWkDQ', 'BEARER'),
(b'1', 45, b'1', 42, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBeml6LmF0dGlhQGFueXdyLmNvbSIsImlhdCI6MTczMjI4NDQ1OSwiZXhwIjoxNzMyMzcwODU5LCJ1c2VyOiI6IkF6aXouYXR0aWFAYW55d3IuY29tIiwicm9sZXMiOlsiUk9MRV9NQU5BR0VSIl19.vds5uwqYDyg9mmPEBtITC5KcAwo2T4X-6p4XqfzPND1NNdW0VQTH10xHCVamOelTe6WooP33pSyabjGt501j_w', 'BEARER'),
(b'1', 46, b'1', 42, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBeml6LmF0dGlhQGFueXdyLmNvbSIsImlhdCI6MTczMjI4NDQ2MSwiZXhwIjoxNzMyMzcwODYxLCJ1c2VyOiI6IkF6aXouYXR0aWFAYW55d3IuY29tIiwicm9sZXMiOlsiUk9MRV9NQU5BR0VSIl19.oITC1D_X5Ux1bFsdPIuXj7wEZt0RJ_h8820jzKTShkugzI3JDbIu6KxBPREeiMSq94RH20TNt_ywAyQT_dvWgA', 'BEARER'),
(b'1', 47, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzIyODczMTEsImV4cCI6MTczMjM3MzcxMSwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.--6o72svtcaAczAcBGm3JDC1oPRQTIBkH_uNpdTXbzaa9beP-Oq_pwSOaxipljYkM03_5j4GECOngnsfM46gvg', 'BEARER'),
(b'1', 48, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzIyODc2MjgsImV4cCI6MTczMjM3NDAyOCwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.O2qwLR7RwJKqDA3QE5MBvYC617UhnMPkqGPtSTc86ZkJLuxHv06DgNCB9lyzZ_cHn8CiiGZEskA2n92Rvcmm4A', 'BEARER'),
(b'1', 49, b'1', 42, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBeml6LmF0dGlhQGFueXdyLmNvbSIsImlhdCI6MTczMjI4NzY1NCwiZXhwIjoxNzMyMzc0MDU0LCJ1c2VyOiI6IkF6aXouYXR0aWFAYW55d3IuY29tIiwicm9sZXMiOlsiUk9MRV9NQU5BR0VSIl19.-MWBAQRd2CPdSaYUytezmT13E0g6tgsnFuuhvdwpriO0EXu3R4myj8BDjgA_FBKbNimZwNQFPBAoRPgqZ-jvGA', 'BEARER'),
(b'1', 50, b'1', 42, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBeml6LmF0dGlhQGFueXdyLmNvbSIsImlhdCI6MTczMjI4NzY1NiwiZXhwIjoxNzMyMzc0MDU2LCJ1c2VyOiI6IkF6aXouYXR0aWFAYW55d3IuY29tIiwicm9sZXMiOlsiUk9MRV9NQU5BR0VSIl19.3kPwbQvP-1rH5MWZ9X-9V6AlPZtFmFDlqoxqYP6TynDpnEawG7PckGd1R1Ek78QWanIPwDoZG7AjYzft0oRPHg', 'BEARER'),
(b'1', 51, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyMjg5MzQ0LCJleHAiOjE3MzIzNzU3NDQsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.EgA_k9hN0uMyR3r-t8FAZA-gnuRR6KEVB6KPJd9sTH47SoeuFegEoyUWuvClAL4ZRYQFJM8HHwCXUjoDDPgzyw', 'BEARER'),
(b'1', 52, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyMjg5MzQ1LCJleHAiOjE3MzIzNzU3NDUsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.YjKTZxU7JAd1WEE3s-zy_s953l_7bmjwjMY-cbUbjP1_m7KeYA8kviK_Ot-BKmSNLLoLMu-2okkvPlleTxjUvQ', 'BEARER'),
(b'1', 53, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzIyODk0OTksImV4cCI6MTczMjM3NTg5OSwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.-pLQ5bI9Uwlsixg0r1Y5ZyG0V5_yW0o-ktPOVWFmk8Ppdd0S5H9M__7FtgLR2oOhXflzd9bL8X3UrnVlq338RQ', 'BEARER'),
(b'1', 54, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyMjg5Njc4LCJleHAiOjE3MzIzNzYwNzgsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.9B3OWTDU_6fPVJ1lpvp-ESYONHmyMRCvOEWaAN7NPwugRVON_NJsFbeDoa1b1YIRyp3ZqieiPyFFWkAdgML22g', 'BEARER'),
(b'1', 55, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyMjg5Njc5LCJleHAiOjE3MzIzNzYwNzksInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.t5tIQEFPrrmqBqn92ni9ypL4NCTO4O8rFjtXKJCkXI7CiovZE4EyMySgzrM_rhPF5Ekb4haGOnZNIOsNLINWIg', 'BEARER'),
(b'1', 56, b'1', 42, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBeml6LmF0dGlhQGFueXdyLmNvbSIsImlhdCI6MTczMjI4OTgwMiwiZXhwIjoxNzMyMzc2MjAyLCJ1c2VyOiI6IkF6aXouYXR0aWFAYW55d3IuY29tIiwicm9sZXMiOlsiUk9MRV9NQU5BR0VSIl19.JPVZbtj6hbW6sXtO-D5YJZCKFYi1RWq2sEx36E1ZsHNU5fvH_oSHYxw9xzi2v-2OlSVGeAbLjP2PxbKSpDB3wQ', 'BEARER'),
(b'1', 57, b'1', 42, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBeml6LmF0dGlhQGFueXdyLmNvbSIsImlhdCI6MTczMjI4OTgwMywiZXhwIjoxNzMyMzc2MjAzLCJ1c2VyOiI6IkF6aXouYXR0aWFAYW55d3IuY29tIiwicm9sZXMiOlsiUk9MRV9NQU5BR0VSIl19.CCnrfKQcwb1nU-q1xzI9OzSr96s0-n7gn-eYcJO6VCqT5ruSJAeO6Rsxs_rJl45fpSQJRix5Snw5sTzC0GY2-g', 'BEARER'),
(b'1', 58, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzIyOTAxNTEsImV4cCI6MTczMjM3NjU1MSwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.RUzjUDCDeF3NVfU2Z3b6_NrDtofYC2sl8qIjtBCrRob8kBn2nLRf5Wn1UgpoBSJ1VetlWnCLyt2Cvp-oScotQw', 'BEARER'),
(b'1', 59, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyMjkwMTc1LCJleHAiOjE3MzIzNzY1NzUsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.8mQPv6p5mL5pQUaBJWd6d9pKKyYqgD9lLVGx3Z6NphRxTWhKZ5VkWi1_XGqhqOUp6xz4A9k4AqTCpxrtbwe0aw', 'BEARER'),
(b'1', 60, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyMjkwMTc5LCJleHAiOjE3MzIzNzY1NzksInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.FBXH-BICGv1Kn5Eb2QUoeeUKwU2Z_4tHwQnW_0HK28m7UCh-k9ngmqZaUOChNxBnocPYd4U_j5I02-zEZSkgNg', 'BEARER'),
(b'1', 61, b'1', 42, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBeml6LmF0dGlhQGFueXdyLmNvbSIsImlhdCI6MTczMjI5MDIzMSwiZXhwIjoxNzMyMzc2NjMxLCJ1c2VyOiI6IkF6aXouYXR0aWFAYW55d3IuY29tIiwicm9sZXMiOlsiUk9MRV9NQU5BR0VSIl19.AjyuOrngUpn2Xh8BH0VPo1gQLjo2cH0DHrDq96VMChj-5cK36yrXRn38Q9vdzPgW0dRBbd1DbckJNqgrVFJcXQ', 'BEARER'),
(b'1', 62, b'1', 42, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBeml6LmF0dGlhQGFueXdyLmNvbSIsImlhdCI6MTczMjI5MDIzMiwiZXhwIjoxNzMyMzc2NjMyLCJ1c2VyOiI6IkF6aXouYXR0aWFAYW55d3IuY29tIiwicm9sZXMiOlsiUk9MRV9NQU5BR0VSIl19.hSLK_kJcqBRU8LT0vI6hMW9J3mj7V6cmsRsLfllWcBVK7BssAplrYxyS_R_8H4PADhV-njHALGLn6ukVMTTjsw', 'BEARER'),
(b'1', 63, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzIyOTAyODAsImV4cCI6MTczMjM3NjY4MCwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.r1GWflCXDYFoSISbLRBIeNZ2BJy45aSxfehvDI84DrBKvjiMfoj_nK8EBSrQvJ49r7A4luQRFcv3HQkScHI-Eg', 'BEARER'),
(b'1', 64, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzIyOTA0OTIsImV4cCI6MTczMjM3Njg5MiwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.CHxRQ2yhb8ostUv9el73HJAEWCKcR-1PGKANCndKcRr5ayednMvpNetvVNTAxNNIaPDCDXn-0pECoz21Sdvbqw', 'BEARER'),
(b'1', 65, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzIyOTA2MzcsImV4cCI6MTczMjM3NzAzNywidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.kgr0hDib5YkjT8IOpa2cowAIiP4Xqt_T5xwVoinnk6an4C2TUsQwbMqhxOH_MiGh9l6g8pumodrssXfei_iBkw', 'BEARER'),
(b'1', 66, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzIyOTA2OTMsImV4cCI6MTczMjM3NzA5MywidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.9Kigr81Z8oY8GcUPqoCjVddFjnWe5zzFtnI0KSTqjhfgrY78SWNJCSNbSH0oTYtxz9gW8MNi8BzNrEUw1dUmpg', 'BEARER'),
(b'1', 67, b'1', 42, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBeml6LmF0dGlhQGFueXdyLmNvbSIsImlhdCI6MTczMjI5MDc2MSwiZXhwIjoxNzMyMzc3MTYxLCJ1c2VyOiI6IkF6aXouYXR0aWFAYW55d3IuY29tIiwicm9sZXMiOlsiUk9MRV9NQU5BR0VSIl19.9hDqy3wUkxExrfrM4B7e85RyOGp0P6UilxxVbsatKtJF-4ant84F5X2lWx9va318xFjYi4X9R4yMn8jILlW-Gg', 'BEARER'),
(b'1', 68, b'1', 42, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBeml6LmF0dGlhQGFueXdyLmNvbSIsImlhdCI6MTczMjI5MDc2NywiZXhwIjoxNzMyMzc3MTY3LCJ1c2VyOiI6IkF6aXouYXR0aWFAYW55d3IuY29tIiwicm9sZXMiOlsiUk9MRV9NQU5BR0VSIl19.CW6NctP--LLnJEWhqgNy6AxSGK05ju7KmNFP7ZzNPAP3tMNidioVz1oQCGlBVzI3z1J9uiPa1aDzaMr1RCwxFA', 'BEARER'),
(b'1', 69, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyMjkwOTg0LCJleHAiOjE3MzIzNzczODQsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.CYJ07ze43Q126knIJQaAJPALhse7rnCWoXqdKQfZnJTXzNPy-6kyYcwhqgySGj2C-HSiE9kcTWZEWfyVX_9_7g', 'BEARER'),
(b'1', 70, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyMjkwOTg2LCJleHAiOjE3MzIzNzczODYsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.fIgFhudzxmASzlAZW56WifEFMxMsiUGAiEwmA70LK_2w9UA3X2UoCM575JTcVIUEs7uHv5Kl4RuFmgov7a1_kg', 'BEARER'),
(b'1', 71, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzIyOTE5MDcsImV4cCI6MTczMjM3ODMwNywidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0._QFRzCteFN3BDYa0wsyncU-Y5QVeUbJh-lvGcrOHk-6fx-eh8w-H_Y-gjH_AegZnXR5LmUHYihooWrZBusoKvQ', 'BEARER'),
(b'1', 72, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzIyOTIxNzksImV4cCI6MTczMjM3ODU3OSwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.TGhd0sj94FtQ07fFv3r6MV72C0I8t9ylwPo53W7t1Wt6AIiLSulYfi5KHo1OnU6wMvvt7LXuQafyF6v4sjnRJg', 'BEARER'),
(b'1', 73, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyMjkyNDM1LCJleHAiOjE3MzIzNzg4MzUsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.6i5Fmz-uAp0G3Dm1nV0mc2gAS6TiaydmZbyiIpgYVKokV0hvT88y5216hiABi_SYewWFeKy-RLnFZsMiKydxNQ', 'BEARER'),
(b'1', 74, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyMjkyNDM2LCJleHAiOjE3MzIzNzg4MzYsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.-GcueOAXmhaWhgoRBBpr4qRcG3SJnCRdIDA-Z7z3bcoaKqDZvYs6ensIS5O_bj69UTRND8RrLT2EWGLpKnf17w', 'BEARER'),
(b'1', 75, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzIyOTI0NzEsImV4cCI6MTczMjM3ODg3MSwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.45N1QSuKZW0Oybc1SNwf5_GYfj33rx1wh_Pk4g2dWQVr-xjvBUHpcJiD2J0w5SgHlaVuBdwn0lZDH9zCBmbluw', 'BEARER'),
(b'1', 76, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzI1Mzk5OTEsImV4cCI6MTczMjYyNjM5MSwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.7Imm0PTuFyBZqkpASbCEg5nvR_reh3KwN3K-DUm02NvfBKi4OJrPqNxWlFtekG7iGgpULfTmYduf11TaoucLVA', 'BEARER'),
(b'1', 78, b'1', 187, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmaXJzdEF1dGhAYW55d3IuY29tIiwiaWF0IjoxNzMyNTQwMTAxLCJleHAiOjE3MzI2MjY1MDEsInVzZXI6IjoiZmlyc3RBdXRoQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfQ09MTEFCT1JBVE9SIl19.bxZrEz_Qq3gULtszVo6meIwCEKT_EzwQcILNdbqkfVmJE4oyEnKYgppd6xtWSq0rRozksSviDCs0qH8DUGNf4A', 'BEARER'),
(b'1', 79, b'1', 187, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmaXJzdEF1dGhAYW55d3IuY29tIiwiaWF0IjoxNzMyNTQwMTUzLCJleHAiOjE3MzI2MjY1NTMsInVzZXI6IjoiZmlyc3RBdXRoQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfQ09MTEFCT1JBVE9SIl19.sGIEr4voyFKTpExn-B2o6_xy-yNClzMsVDdMfPzxhTBeHN3BbSutzbXwaFqTpr4yazlW6cnSrVFX-NqjLG7OeA', 'BEARER'),
(b'1', 80, b'1', 187, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmaXJzdEF1dGhAYW55d3IuY29tIiwiaWF0IjoxNzMyNTQwNjMyLCJleHAiOjE3MzI2MjcwMzIsInVzZXI6IjoiZmlyc3RBdXRoQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfQ09MTEFCT1JBVE9SIl19.hhcm5At1vuPnHZ998njqzI6sOE0WiEY2evnA72g0nP5PftAZJgJ0cPw6jPytnogQnq_L59boddnwMz_xyPKpYg', 'BEARER'),
(b'1', 81, b'1', 187, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmaXJzdEF1dGhAYW55d3IuY29tIiwiaWF0IjoxNzMyNTQwODMzLCJleHAiOjE3MzI2MjcyMzMsInVzZXI6IjoiZmlyc3RBdXRoQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfQ09MTEFCT1JBVE9SIl19.CYuA_dw7n606N19afKhdk3mr7VYR6jA875ElJJeSot6-9rMqTB3YGrWP6dxxdWPez3nktaWOn24zuv9vq-PwdQ', 'BEARER'),
(b'1', 82, b'1', 187, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmaXJzdEF1dGhAYW55d3IuY29tIiwiaWF0IjoxNzMyNTQxODkyLCJleHAiOjE3MzI2MjgyOTIsInVzZXI6IjoiZmlyc3RBdXRoQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfQ09MTEFCT1JBVE9SIl19.o5aY3c2N2FOpLhK6Bk5AgPjXyoyp68YB7nDD5xvKGpyg5nf3rZL4VYTzbSIKTTbgpmEa8RMP1BZAFl8dTgOE4g', 'BEARER'),
(b'1', 83, b'1', 187, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmaXJzdEF1dGhAYW55d3IuY29tIiwiaWF0IjoxNzMyNTQyMzUyLCJleHAiOjE3MzI2Mjg3NTIsInVzZXI6IjoiZmlyc3RBdXRoQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfQ09MTEFCT1JBVE9SIl19.hnZolyVkqvAB21xVMez2i-Smxvk7d62TnucFQIuYe6h8JW3O6_HQnmCL-ji2ckhNdM59-XPWxZdh9H5ExVvQYQ', 'BEARER'),
(b'1', 84, b'1', 187, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmaXJzdEF1dGhAYW55d3IuY29tIiwiaWF0IjoxNzMyNTQzMjY3LCJleHAiOjE3MzI2Mjk2NjcsInVzZXI6IjoiZmlyc3RBdXRoQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfQ09MTEFCT1JBVE9SIl19.VxyhkkWuAGcQbiPmmGIHF8JWsh0Rvjusp01i-k3qSrpKF7AunER5ij1ZQlyS1n7yOPSnuQRy-MZLwH-kMlKXpQ', 'BEARER'),
(b'1', 85, b'1', 187, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmaXJzdEF1dGhAYW55d3IuY29tIiwiaWF0IjoxNzMyNTQzNDIyLCJleHAiOjE3MzI2Mjk4MjIsInVzZXI6IjoiZmlyc3RBdXRoQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfQ09MTEFCT1JBVE9SIl19.xFYotYRl9sIBYCF33act7kWJXH9ISWMHdYVazx3IPgwK7xN1_r7p0L58rSTjyOs3K5kOtykpiK20MX12yjZjfQ', 'BEARER'),
(b'1', 86, b'1', 187, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmaXJzdEF1dGhAYW55d3IuY29tIiwiaWF0IjoxNzMyNTQzNzg5LCJleHAiOjE3MzI2MzAxODksInVzZXI6IjoiZmlyc3RBdXRoQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfQ09MTEFCT1JBVE9SIl19.XuR8A32w_lT-drteZZP_oV5LFqGZhHXOXewasNSldYFIxB46-zwN_z-sjmsAHCIrxCsCmVYli3kAcMnRlGdaMQ', 'BEARER'),
(b'1', 87, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzI4MTgxNjAsImV4cCI6MTczMjkwNDU2MCwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.69_hvJJacjMN7ef34LQ_5xBc5RZoJXvcZYFZUTg-IU91SclChn2dv9Yu0Kq_GjkZYdXWlk1H8PVwTkIDAdBJJQ', 'BEARER'),
(b'1', 88, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzI4MTgxNjEsImV4cCI6MTczMjkwNDU2MSwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.cNwjXeD3cmrlIqtQoV9ptTUrnapvsxftZe0zRYgXcIh6vDvVuigA5RYOxvigQqi3pJl6zQlEKwxbh7Zc0MUCBg', 'BEARER'),
(b'1', 89, b'1', 44, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJEZXZKdW5pb3JAYW55d3IuY29tIiwiaWF0IjoxNzMyODE4NTE0LCJleHAiOjE3MzI5MDQ5MTQsInVzZXI6IjoiRGV2SnVuaW9yQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfQ09MTEFCT1JBVE9SIl19.DaTRTPwhQu1pNafLxn8VlTS4XB3Zlc4eoSj9wyNcF51gMahuFyGKW_vKvisDSXJ9VzC_l9DXCl91V6u-zFhHBg', 'BEARER'),
(b'1', 90, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODE4NTk4LCJleHAiOjE3MzI5MDQ5OTgsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.BuPCGRcwTOX-A4EuLwlxjMcZ3iAucAQw1z-8SPbrph9smwN2XXgPllnlx82IuIKflGv1jwPTkY7BCwofXqfAzg', 'BEARER'),
(b'1', 91, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODE4NjAwLCJleHAiOjE3MzI5MDUwMDAsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.c2zN3525u_91Gj1jKHHmiA8uKuWQt3EF1juCAZzrpkK9x8JW1HyIiMZ-_fwh2rjSJd94cAFFj-KuVdOh9PPDYg', 'BEARER'),
(b'1', 92, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODE4NjAzLCJleHAiOjE3MzI5MDUwMDMsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.FOHY8-tYJXe8maMU1pagIPD9DVCnNktvN5lak1N65UQf2feiyzZbbjl2Sa-RKFS5JQnEZXnkBUmlBEQTXXkxRQ', 'BEARER'),
(b'1', 94, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzI4MTg2ODYsImV4cCI6MTczMjkwNTA4NiwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.qqVBac8EY_rRCMQy9ZDf1DAXaStiMSIncryx76jI-ktqqYMDsCZBJ81zRC2UhO8wwCRE4p2DNo_uB4FtVm_XEw', 'BEARER'),
(b'1', 95, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODE4NzA3LCJleHAiOjE3MzI5MDUxMDcsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.KOdJAwPW5W0OOfMLQZNv36DAU5qdysKh2diGgIeJGzSqmcmEL0s1ldw9itX55DvdwUsGyLLPfJw2OCywf-Xosw', 'BEARER'),
(b'1', 96, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODE4NzA5LCJleHAiOjE3MzI5MDUxMDksInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.Q_XsIi5eQc2jK2n4zGcjGhsbueO7FptCCI1POLdnj9r8u4NzQWN7xly_pB-4DzgXayjuQwlJvydkycafApBVcw', 'BEARER'),
(b'1', 97, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODE4NzIyLCJleHAiOjE3MzI5MDUxMjIsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.U9rZxJJN9kq4RO60_J2oJ5v883X2-p469F48gXZvgGPSE8EQDcEXmOK4FgoX-twIz47oJ3dO_SjuukK6nBwaow', 'BEARER'),
(b'1', 99, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzI4MTg3MzMsImV4cCI6MTczMjkwNTEzMywidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.1cvTu-6Vsz_2X1eocLCUHskZp5nv0Z6s7bwO-6uhd7sZcqKwuBHOOforh2Pp_k_eg5XYHqkmKAmx0Al29xjBRw', 'BEARER'),
(b'1', 100, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzI4MTg3NTUsImV4cCI6MTczMjkwNTE1NSwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.-Bky7JCgITACD1dGwmnmW_EZqfRnPvW3vRCMGR_6Hr6UiDPsBZPReEpc_Iie4uW3UMXBy3xxmZzQzuIWDqYkbw', 'BEARER'),
(b'1', 101, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODE4NzgyLCJleHAiOjE3MzI5MDUxODIsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.GpBBXjEkLMu008vsCMftAxcJfwXfn0B2sS4RF-PaU12_MnoiYz81Ss786ADt-wbJ7ZlM7tBbl3cs5Bvk9MdFaA', 'BEARER'),
(b'1', 102, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODE4Nzg0LCJleHAiOjE3MzI5MDUxODQsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.xCoBT3kbw68w0HegGYxZLqE-DFyB5t0sBh5ibP8I1bqPjHCQEMeBHmcj_MLcj9jxKAtI2fs1V0uJZGiicMAazA', 'BEARER'),
(b'1', 103, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODE4ODAwLCJleHAiOjE3MzI5MDUyMDAsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.Y3lfB0fBjlIJ8w0-OUfryvKGuv2MVGH4O5RTghroqKSdA3Z5Z-Gyqid9H87gwkV7E_rR0RKI1iSpAKMSRKAFJA', 'BEARER'),
(b'1', 105, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODE4ODY3LCJleHAiOjE3MzI5MDUyNjcsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.vN1bXuTfCS33-7fdeDdI9cLu0cRd2z1oswPLem8ypSeufA1BW9b0u6d0Xhdj3flwviDvPfT_9IPJOSIHXjIFvw', 'BEARER'),
(b'1', 106, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODE4ODY4LCJleHAiOjE3MzI5MDUyNjgsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.0--Wt6p5_vU2_E-bI1PGC-Na325W3kKeVfplx_q8KMVUn70sz7UM3-xgUfhOYl1FOubFT7j7WscMrysyRtbvwA', 'BEARER'),
(b'1', 107, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODE4ODY5LCJleHAiOjE3MzI5MDUyNjksInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.EH-y2Co5W7uVKTuDYg63ehKExTPF3f0E5s63_SudzZQTaEjy1ztIC3yPXbZS_J0Zo_uQ-styhgApRPDAGYDeVw', 'BEARER'),
(b'1', 109, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODE5MDAxLCJleHAiOjE3MzI5MDU0MDEsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.q0NzJjjbcW0qXSXOOSqd6wfvCOn2BoqDNXTAn2zAB4AFzN-lYovWpYqzSLqteVbEHu6dI0jd3xqH0KNVTa4zrA', 'BEARER'),
(b'1', 110, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODE5MDAyLCJleHAiOjE3MzI5MDU0MDIsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.m9GQKapX0TO_awoHh3CKl5X8PAaAT64oV0TKacji4qv4rqPgzK9hEQ527-gAgM-Z8l05PX1GEfshMlsSbrWIAQ', 'BEARER'),
(b'1', 111, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODE5MDE0LCJleHAiOjE3MzI5MDU0MTQsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.dft1DHSSIAMsNCVobnYNLfriCeUp3EXgGn8dmOuVsBSKmZXERD_YGnhmJtidThRhvNkzPx65AsqZb5XmOCvEBg', 'BEARER'),
(b'1', 112, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODE5MDcyLCJleHAiOjE3MzI5MDU0NzIsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.otPKFyURjOlZKEYQrT5-Fr9O5I3kgvqilYx0UBTBoql7ADHDKTWxlK_OPf_Z8YNez3Y-2KaLjdNz0MCdOwidEw', 'BEARER'),
(b'1', 113, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODE5MDg4LCJleHAiOjE3MzI5MDU0ODgsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.fyxVAzSqvNoEc1Mrfq1-VxU9kzZvBo5F8QIw-E-G-mkA4vIP-As5i1Vckak8nTFDElFGdTWI6vlxm5RXbqiiQw', 'BEARER'),
(b'1', 114, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODE5MDkwLCJleHAiOjE3MzI5MDU0OTAsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.Np9HPNMMf2WbNELCRthnetP5nkMedySQnwwYbE2tQdbe_XNk9oCSsWImlXyQVGymnv91Um4KxSaCLHO9Cl6gtA', 'BEARER'),
(b'1', 115, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzI4MTkxMDIsImV4cCI6MTczMjkwNTUwMiwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.xy8y90I64tuIN0GSBmP34g7jFQKTkp2MQqmqBu_dw9hoPjd8-5WSlqx7cQmzoHDppUIcpKqcq5edi0crVlYDNg', 'BEARER'),
(b'1', 116, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODE5NTMzLCJleHAiOjE3MzI5MDU5MzMsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.OPeFkWpTYhRn3MYjCJs98NOFzY80l88CxvZn5ovvhUBE9Kh7yFQA_QJO5TWf6iLwr4J0L-YSR1MENMty7ZEK8A', 'BEARER'),
(b'1', 117, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODE5NTM0LCJleHAiOjE3MzI5MDU5MzQsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.ludp0E8z-AFw9glUSEqRx7RP12lbs10m-RnDV7L4_cAbkxV7woZOmvdn3zKsmswekwLainSuKuzlRhxT0B-YPg', 'BEARER'),
(b'1', 118, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzI4MTk1NDMsImV4cCI6MTczMjkwNTk0MywidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.yNoG0xgNQ7xNdlXo11XaflUMXWsCBDpMDcO99dpKesUKDuRtI8ogYkNjGxLVEwxTogM9pthk71DhTrdq9szZYw', 'BEARER'),
(b'1', 119, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzI4MTk1NTcsImV4cCI6MTczMjkwNTk1NywidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.eBLd3fo_J86KuFxnSZu8QzdS7bF_DAeH3pretqQPOQZPgnwS_tAyc7lgjQ21jkQOrp9P7o0y9sAt8xIMAs6sRw', 'BEARER'),
(b'1', 120, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzI4MTk1NjUsImV4cCI6MTczMjkwNTk2NSwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.MQ-_66mUUYRZwfVX4n9TDDP_Fm8Ok91bVtwJ2cc9DlRXVsP2nzUnnI0bN2fOYxOD71MnGaZa1UWTe7F83gEHFg', 'BEARER'),
(b'1', 121, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzI4MTk1NzQsImV4cCI6MTczMjkwNTk3NCwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.HmPFxliA2ojzwLFp19URBpq2th8VOmg5nYeDyx6A5dF5vlDgM2uKe7jfzvUDIt9zy9lss8ITKjvJSVD-JykMyg', 'BEARER'),
(b'1', 122, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODE5NTk1LCJleHAiOjE3MzI5MDU5OTUsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.jITpOqVlE7zS_v9xN7ErGjH4CS65I_t6frGNROHZNpKXw6inPbeHw83lOzjAl-2xu0-9Z6bm9ji7Rkhc2RYDCA', 'BEARER'),
(b'0', 123, b'0', 187, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmaXJzdEF1dGhAYW55d3IuY29tIiwiaWF0IjoxNzMyODE5NzY5LCJleHAiOjE3MzI5MDYxNjksInVzZXI6IjoiZmlyc3RBdXRoQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfQ09MTEFCT1JBVE9SIl19.mlkqjX3NS861QwqDt4mguWQM4MKsy7FarO34KnfoQE6n1JG6gevF9_FMPA9HTqlZGRr3R-1ITOMv5-S8nwrLnQ', 'BEARER'),
(b'0', 124, b'0', 44, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJEZXZKdW5pb3JAYW55d3IuY29tIiwiaWF0IjoxNzMyODE5ODA2LCJleHAiOjE3MzI5MDYyMDYsInVzZXI6IjoiRGV2SnVuaW9yQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfQ09MTEFCT1JBVE9SIl19.uyyG0nLJ1MynjPIjxfIMdaYiNV6HyLrPYjghXNvA2BTiWNGrzsMFab4CEON9zfDWoKXL02RcWhmZiccXBrpmGw', 'BEARER'),
(b'1', 125, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzI4MTk4NDEsImV4cCI6MTczMjkwNjI0MSwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.o3UN8DyHXxTwjDa5bpNBMzb0yOhpTyXdh_9yKvpgFbp6r-a-e-DCSTtUqmI8UZ8_KSzKeBRYNq7-VBarsRZFpg', 'BEARER'),
(b'1', 126, b'1', 42, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBeml6LmF0dGlhQGFueXdyLmNvbSIsImlhdCI6MTczMjgxOTg1OSwiZXhwIjoxNzMyOTA2MjU5LCJ1c2VyOiI6IkF6aXouYXR0aWFAYW55d3IuY29tIiwicm9sZXMiOlsiUk9MRV9NQU5BR0VSIl19.5Fe0b9ok0NpbeYVyRXqc1rrbHesbjvefAExxbQHal_Hs3dx5fyn2iku3fdb_yxzWxSAVbRBbeYf0g1ETHtym9Q', 'BEARER'),
(b'1', 127, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODE5OTI5LCJleHAiOjE3MzI5MDYzMjksInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.0Gyt2gdGniiDRaxRsaJ7v9n1m3ckfgyHOwZlK4a60U1zdDFuPZeoOBDm5BBNoD-tknptgVbzNGhw58fXXsT_Uw', 'BEARER'),
(b'1', 128, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODE5OTUwLCJleHAiOjE3MzI5MDYzNTAsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.acEQI6tG7AfbxdvMUcXOdSYS_wh_OUIqbCnAbb5yUtHp_SrrBP1NH-am-MCjaIHNj39BopFCvZbUFFp8-YghZw', 'BEARER'),
(b'1', 129, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODE5OTUyLCJleHAiOjE3MzI5MDYzNTIsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.zlHoZ4CMOe2SRqAZ0BMvRfRh4DkVidh1WnrWXRkC6BtMQMRRwcT9LoCeJbgu4HJvglqird3XDwI2RiLi1LgrfA', 'BEARER'),
(b'0', 130, b'0', 45, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJEZXZTZW5pb3JAYW55d3IuY29tIiwiaWF0IjoxNzMyODE5OTg5LCJleHAiOjE3MzI5MDYzODksInVzZXI6IjoiRGV2U2VuaW9yQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfQ09MTEFCT1JBVE9SIl19.L404r-eBHpJBid6y1VpDW0pQQkFNQdbYPmYRx6wTmuXPXCGVa1qx9Z4eG30yaPhYqH_yIHhPRzRYeotvKAt17A', 'BEARER'),
(b'1', 131, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODIwMTI1LCJleHAiOjE3MzI5MDY1MjUsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.cLAqhNHoJfmB21u5eTvjORz-bpIa9YJ7Vt-PBHtk6mkKqptxLJ2RKA79aAih-IKrpqfbWvUCULR-Q-X0su0HyQ', 'BEARER'),
(b'1', 132, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODIwMzA0LCJleHAiOjE3MzI5MDY3MDQsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.8OIuFqIj7NdYreRvEx5fWe-4VyAA5E5pvLunnkqXNETGub3_g_8wuS_JwuPoKhz1Pm1o9HygNX4DChsSaRtnTQ', 'BEARER'),
(b'1', 133, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODIyNTA2LCJleHAiOjE3MzI5MDg5MDYsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.jUXIF_mcVvVffxI3Be_Ez-NF7onFwARQQyP7aow0RdwIOHDnTnJsm2rL4gesPmRN3qzztcyY4tNJwhxbQ5aTmQ', 'BEARER'),
(b'1', 134, b'1', 42, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBeml6LmF0dGlhQGFueXdyLmNvbSIsImlhdCI6MTczMjgyMjUyMSwiZXhwIjoxNzMyOTA4OTIxLCJ1c2VyOiI6IkF6aXouYXR0aWFAYW55d3IuY29tIiwicm9sZXMiOlsiUk9MRV9NQU5BR0VSIl19.qHTbLIQ-cNXKXck4tLnmaYcZVH7h6ebWnuzWHTn5ReN8MGYSA-xgQzmgeKHWDR09o-c8RplJ5nzerS369e5asg', 'BEARER'),
(b'1', 135, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzI4ODIwNjIsImV4cCI6MTczMjk2ODQ2MiwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.BTHoqj4w-yRhCOfdk9kV5VtjC58hI7J8rAdeM5ZYdOXKdqF3ZKIr6bXoL91XIL60VjyeNceco1CC8ocKRFQj0g', 'BEARER'),
(b'1', 136, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODgyMDc2LCJleHAiOjE3MzI5Njg0NzYsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.Yg3Chawmb4vYj8y3zG4D_mUOYEwFEnnZdsedBMRy7sLe0qDsMoLpMyeC9HO9umSYscT7ZWlLiCuylGKWnt6uLg', 'BEARER'),
(b'0', 137, b'0', 42, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBeml6LmF0dGlhQGFueXdyLmNvbSIsImlhdCI6MTczMjg4MjE0MiwiZXhwIjoxNzMyOTY4NTQyLCJ1c2VyOiI6IkF6aXouYXR0aWFAYW55d3IuY29tIiwicm9sZXMiOlsiUk9MRV9NQU5BR0VSIl19.p_Mf5q-gb2O_DxyNd2R4Z04tsVS7UVGwfwPaS8j111RzeOA-jAG6ZGB99Xi8tFyuhFvKSgQQQ2MLxLQK4sPe2A', 'BEARER'),
(b'1', 138, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzI4ODI4NjUsImV4cCI6MTczMjk2OTI2NSwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.c4FpL8EWWGDY3DWWAm8eLyOi-mRaEuzIMU8ZWX4epmSZ1VPJsiXqFTrfCvMcvbZx43PO-N_urjBZ3Jaxe37tQQ', 'BEARER'),
(b'1', 139, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODg5NDU0LCJleHAiOjE3MzI5NzU4NTQsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.40PmRCAsV_hva8NNWFgj1Zj6YpE0fqZugabtfOyj9t5tSvb-HsNhBWIGgglNf8e5HYKGOOHE-R8779fWGBbseA', 'BEARER'),
(b'1', 140, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzI4ODk2NzYsImV4cCI6MTczMjk3NjA3NiwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.5e5x_7NQAYrytjzguLVQkrnAppDjBVrw5FJNwkQafmcotRGQ97u24rR2h9SRGpaZLfxg9gGaadUe9MIKBCJeNg', 'BEARER'),
(b'1', 141, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzI4OTAyMjUsImV4cCI6MTczMjk3NjYyNSwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.jbgZ_fs_F79vqjmhEMh8VpoulX18fqv6eSqDYOb6BZqY79upL1vGJt_NTzJiNlgU3wPJCXWCd0uZU75whvIv4A', 'BEARER'),
(b'1', 142, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzI4OTIwMTgsImV4cCI6MTczMjk3ODQxOCwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.CdpqNRXJvw61VaY-ElTAVoEcqiU_AozmTPfUBDWAkpCMBZPi-EFQYLyeVhZfM0FGiDBncdyDJRyXG4vH82HH1Q', 'BEARER'),
(b'1', 143, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzI4OTI1MTUsImV4cCI6MTczMjk3ODkxNSwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.CYwSS618w88SkU2MNyLcxJuAagifPt4LTz6S2zfIFy3v9MsEJ4d8vM0Cj66-4btzyfFZjRkX4J_HDIIXL4cPYA', 'BEARER'),
(b'1', 144, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODkzMDE0LCJleHAiOjE3MzI5Nzk0MTQsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.lPP27dn-uJ7GV6ZdHPbyTL6JS07gND86svTHgp64-iHxBd2BkhT_u60c5gvX9XS2k4XHTdML6D3qglBcfuyPjw', 'BEARER'),
(b'1', 145, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzI4OTMxMzMsImV4cCI6MTczMjk3OTUzMywidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.WTfp_ia338l9IggYM4IRajeH3kQPgKstFIKYrrMKq-olvq11AVW9iwmnUgIcuFoSy8rYseGsNLGRVh7n_nSezw', 'BEARER'),
(b'1', 146, b'1', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODkzMjUwLCJleHAiOjE3MzI5Nzk2NTAsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.H7JLDQYJnWCaHp3HWRRGgXYOLUT2QbXJH6pxQQq25llhehOqlYL5TmwPSCkOrg7BvhVcLlqsyT37TaNqZzuGRg', 'BEARER'),
(b'1', 147, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzI4OTMyNjksImV4cCI6MTczMjk3OTY2OSwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.s2WE0lS7Zc1BvYNGgfTCaKKFdwNjgF1CRWb-xoT-8TejqrcNpeEQhg-5yibp-vPmZnh01kC4zXxRVOXV8izujw', 'BEARER'),
(b'0', 148, b'0', 43, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmRlbGxheml6LmVsbG91bWlAYW55d3IuY29tIiwiaWF0IjoxNzMyODkzMzAxLCJleHAiOjE3MzI5Nzk3MDEsInVzZXI6IjoiYWJkZWxsYXppei5lbGxvdW1pQGFueXdyLmNvbSIsInJvbGVzIjpbIlJPTEVfTUFOQUdFUiJdfQ.BYHyStVVJ_DJwNC_4u7Bq7zQF_tDhBLY1Ut99ABf3BsjatOqZZw4f09w-pc5nPFWR6wzrzwfKulxsGlP90n9Sw', 'BEARER'),
(b'1', 149, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzI4OTMzOTYsImV4cCI6MTczMjk3OTc5NiwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.6qIBh8Xw0IFeIUZN6JpFsXmMKOszrpKjvPOim0NB_zl_AENNhy1Z6VuOv_DwU_KnpBBPChWg4dc9otrtO-BKcQ', 'BEARER'),
(b'1', 150, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzI5MDAzMjksImV4cCI6MTczMjk4NjcyOSwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.ykbcWBMfH-_4-A7KLT1aAguliyCR0RpEWgRGQhMlEvkNbEJa_R-fOfAt6eWdZ6w0_vGm7jURkhib4yzDnrRGsA', 'BEARER'),
(b'1', 151, b'1', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzI5MDA2NjYsImV4cCI6MTczMjk4NzA2NiwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.emWSLiXq8r-uZ94vFpkoE82_jMB-jqEwHt7Dd0bL62b0YkXSc4tIQ9O9hhPXlfXtztfnbJT1pZuBQsOL69Doeg', 'BEARER'),
(b'0', 152, b'0', 41, NULL, NULL, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBhbnl3ci5jb20iLCJpYXQiOjE3MzI5MDE1ODAsImV4cCI6MTczMjk4Nzk4MCwidXNlcjoiOiJhZG1pbkBhbnl3ci5jb20iLCJyb2xlcyI6WyJST0xFX0NPTExBQk9SQVRPUiIsIlJPTEVfUkgiLCJST0xFX01BTkFHRVIiXX0.sFs-F8Hayw6haGyNSJeQWqlLSJ2WeTB_gUUcbrn8sg6uoAMDHd_o9noDX9VFe8fX2-idCLIwYHKJ2-E2IWUqHg', 'BEARER');

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `absence`
--
ALTER TABLE `absence`
  ADD PRIMARY KEY (`absence`);

--
-- Index pour la table `account_verifications`
--
ALTER TABLE `account_verifications`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_th3jkr7ivsusj0ar1scq7nkc8` (`collaborator_id`);

--
-- Index pour la table `catalogue`
--
ALTER TABLE `catalogue`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `collaborateur`
--
ALTER TABLE `collaborateur`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_iy02q5aj0mys5qp9nmowo6pym` (`phone`),
  ADD UNIQUE KEY `UK_cjdxtfvf2hvxugerwml1ja0l8` (`email`),
  ADD KEY `FKa46u7d1p7dphuqvn1wmt19u54` (`equipe_id_equipe`),
  ADD KEY `FKj9ujjl7v0l5g74rov1owvtrdy` (`poste_occupe_id_poste`);

--
-- Index pour la table `collaborateur_competence`
--
ALTER TABLE `collaborateur_competence`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKobd2qg1wlxydg05pecmureqcq` (`collaborateur_id`),
  ADD KEY `FKh0ecqs30w3h0qhw4wi2y8mgwk` (`competence_id`);

--
-- Index pour la table `collaborateur_roles`
--
ALTER TABLE `collaborateur_roles`
  ADD PRIMARY KEY (`collaborateur_id`,`role_id`),
  ADD KEY `FKghlp8cvg0plymiapx0qvrdb5s` (`role_id`);

--
-- Index pour la table `competence`
--
ALTER TABLE `competence`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `competence_history`
--
ALTER TABLE `competence_history`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKploa1pyp8no3ospksj303sf8n` (`collaborator_id`),
  ADD KEY `FKsjw481ynxads2pf77cxg9w2jb` (`competence_id`);

--
-- Index pour la table `competence_possible_values`
--
ALTER TABLE `competence_possible_values`
  ADD KEY `FKcdmejbb9nougni0bpl58g4r09` (`competence_id`);

--
-- Index pour la table `demande_absence`
--
ALTER TABLE `demande_absence`
  ADD PRIMARY KEY (`id_demande_ab`),
  ADD KEY `FK320n9denmgpbuv3oby7t813cj` (`absence_id`),
  ADD KEY `FKe0xfht513eo88er5bmvw28n95` (`collaborateur_id`),
  ADD KEY `FK69r4fkqltftd7lri0cae3ll1o` (`department_id`),
  ADD KEY `FKoda1qnhiqxd6cclctyggw8x9k` (`equipe_id`);

--
-- Index pour la table `demande_formation`
--
ALTER TABLE `demande_formation`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKce5mjl29ybx3uus8o9kgrepd7` (`employee_id`),
  ADD KEY `FKj06o4yoiu7ocjemdhr4ulj0wg` (`formation_id`),
  ADD KEY `FKcb1nn3mhi93grtl4glg3rkxx4` (`manager_id`);

--
-- Index pour la table `departement`
--
ALTER TABLE `departement`
  ADD PRIMARY KEY (`id_dep`),
  ADD UNIQUE KEY `UK_gsffj2bf7v5dn0y7oq4r2kkw8` (`responsible_id`);

--
-- Index pour la table `equipe`
--
ALTER TABLE `equipe`
  ADD PRIMARY KEY (`id_equipe`),
  ADD UNIQUE KEY `UK_evame2q4ocuvmtu25tme6333q` (`manager_equipe_id`),
  ADD KEY `FKj60l90kc8dyf7kamy4i6sharj` (`departement_id`);

--
-- Index pour la table `events`
--
ALTER TABLE `events`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_bbxgcgobp3n3765jyx98an8wy` (`type`);

--
-- Index pour la table `formation`
--
ALTER TABLE `formation`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKjh44o5kfami1hhrwkglwsqhj3` (`catalogue_id`),
  ADD KEY `FK6e5yiqs0avg0ux6jhgy9wrv22` (`competence_id`);

--
-- Index pour la table `inscription`
--
ALTER TABLE `inscription`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKd5mv839cpi8qcn32ylblx6if0` (`collaborator_id`),
  ADD KEY `FKntxdsyn12worhd9ymk20jum4b` (`formation_id`);

--
-- Index pour la table `poste`
--
ALTER TABLE `poste`
  ADD PRIMARY KEY (`id_poste`),
  ADD UNIQUE KEY `UK_esb1xjwsjcym6n44yrxykt5gk` (`titre_poste`);

--
-- Index pour la table `poste_competence`
--
ALTER TABLE `poste_competence`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK6bcjpya7raa5g31tgxsj0mlpl` (`competence_id`),
  ADD KEY `FKspsfxte1jvklltjioipc5oo9k` (`poste_id`);

--
-- Index pour la table `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `token`
--
ALTER TABLE `token`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_pddrhgwxnms2aceeku9s2ewy5` (`token`),
  ADD KEY `FKdlahhwwn98r1leuqur1ixf0l2` (`collaborateur_id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `absence`
--
ALTER TABLE `absence`
  MODIFY `absence` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT pour la table `account_verifications`
--
ALTER TABLE `account_verifications`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `catalogue`
--
ALTER TABLE `catalogue`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `collaborateur`
--
ALTER TABLE `collaborateur`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=429;

--
-- AUTO_INCREMENT pour la table `collaborateur_competence`
--
ALTER TABLE `collaborateur_competence`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT pour la table `competence`
--
ALTER TABLE `competence`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `competence_history`
--
ALTER TABLE `competence_history`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `demande_absence`
--
ALTER TABLE `demande_absence`
  MODIFY `id_demande_ab` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `demande_formation`
--
ALTER TABLE `demande_formation`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `departement`
--
ALTER TABLE `departement`
  MODIFY `id_dep` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT pour la table `equipe`
--
ALTER TABLE `equipe`
  MODIFY `id_equipe` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `events`
--
ALTER TABLE `events`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `formation`
--
ALTER TABLE `formation`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `inscription`
--
ALTER TABLE `inscription`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `poste`
--
ALTER TABLE `poste`
  MODIFY `id_poste` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT pour la table `poste_competence`
--
ALTER TABLE `poste_competence`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT pour la table `roles`
--
ALTER TABLE `roles`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `token`
--
ALTER TABLE `token`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=153;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `account_verifications`
--
ALTER TABLE `account_verifications`
  ADD CONSTRAINT `fk_collaborator_id` FOREIGN KEY (`collaborator_id`) REFERENCES `collaborateur` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `collaborateur`
--
ALTER TABLE `collaborateur`
  ADD CONSTRAINT `FKa46u7d1p7dphuqvn1wmt19u54` FOREIGN KEY (`equipe_id_equipe`) REFERENCES `equipe` (`id_equipe`),
  ADD CONSTRAINT `FKj9ujjl7v0l5g74rov1owvtrdy` FOREIGN KEY (`poste_occupe_id_poste`) REFERENCES `poste` (`id_poste`);

--
-- Contraintes pour la table `collaborateur_competence`
--
ALTER TABLE `collaborateur_competence`
  ADD CONSTRAINT `FKh0ecqs30w3h0qhw4wi2y8mgwk` FOREIGN KEY (`competence_id`) REFERENCES `competence` (`id`),
  ADD CONSTRAINT `FKobd2qg1wlxydg05pecmureqcq` FOREIGN KEY (`collaborateur_id`) REFERENCES `collaborateur` (`id`);

--
-- Contraintes pour la table `collaborateur_roles`
--
ALTER TABLE `collaborateur_roles`
  ADD CONSTRAINT `FK27sukqvgjarbgveyskxnl336i` FOREIGN KEY (`collaborateur_id`) REFERENCES `collaborateur` (`id`),
  ADD CONSTRAINT `FKghlp8cvg0plymiapx0qvrdb5s` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`);

--
-- Contraintes pour la table `competence_history`
--
ALTER TABLE `competence_history`
  ADD CONSTRAINT `FKploa1pyp8no3ospksj303sf8n` FOREIGN KEY (`collaborator_id`) REFERENCES `collaborateur` (`id`),
  ADD CONSTRAINT `FKsjw481ynxads2pf77cxg9w2jb` FOREIGN KEY (`competence_id`) REFERENCES `competence` (`id`);

--
-- Contraintes pour la table `competence_possible_values`
--
ALTER TABLE `competence_possible_values`
  ADD CONSTRAINT `FKcdmejbb9nougni0bpl58g4r09` FOREIGN KEY (`competence_id`) REFERENCES `competence` (`id`);

--
-- Contraintes pour la table `demande_absence`
--
ALTER TABLE `demande_absence`
  ADD CONSTRAINT `FK320n9denmgpbuv3oby7t813cj` FOREIGN KEY (`absence_id`) REFERENCES `absence` (`absence`),
  ADD CONSTRAINT `FK69r4fkqltftd7lri0cae3ll1o` FOREIGN KEY (`department_id`) REFERENCES `departement` (`id_dep`),
  ADD CONSTRAINT `FKe0xfht513eo88er5bmvw28n95` FOREIGN KEY (`collaborateur_id`) REFERENCES `collaborateur` (`id`),
  ADD CONSTRAINT `FKoda1qnhiqxd6cclctyggw8x9k` FOREIGN KEY (`equipe_id`) REFERENCES `equipe` (`id_equipe`);

--
-- Contraintes pour la table `demande_formation`
--
ALTER TABLE `demande_formation`
  ADD CONSTRAINT `FKcb1nn3mhi93grtl4glg3rkxx4` FOREIGN KEY (`manager_id`) REFERENCES `collaborateur` (`id`),
  ADD CONSTRAINT `FKce5mjl29ybx3uus8o9kgrepd7` FOREIGN KEY (`employee_id`) REFERENCES `collaborateur` (`id`),
  ADD CONSTRAINT `FKj06o4yoiu7ocjemdhr4ulj0wg` FOREIGN KEY (`formation_id`) REFERENCES `formation` (`id`);

--
-- Contraintes pour la table `departement`
--
ALTER TABLE `departement`
  ADD CONSTRAINT `FKji2swsgx2nsromnobcbpptrhl` FOREIGN KEY (`responsible_id`) REFERENCES `collaborateur` (`id`);

--
-- Contraintes pour la table `equipe`
--
ALTER TABLE `equipe`
  ADD CONSTRAINT `FKj60l90kc8dyf7kamy4i6sharj` FOREIGN KEY (`departement_id`) REFERENCES `departement` (`id_dep`),
  ADD CONSTRAINT `FKpc4031hh21s2loabk009hrh44` FOREIGN KEY (`manager_equipe_id`) REFERENCES `collaborateur` (`id`);

--
-- Contraintes pour la table `formation`
--
ALTER TABLE `formation`
  ADD CONSTRAINT `FK6e5yiqs0avg0ux6jhgy9wrv22` FOREIGN KEY (`competence_id`) REFERENCES `competence` (`id`),
  ADD CONSTRAINT `FKjh44o5kfami1hhrwkglwsqhj3` FOREIGN KEY (`catalogue_id`) REFERENCES `catalogue` (`id`);

--
-- Contraintes pour la table `inscription`
--
ALTER TABLE `inscription`
  ADD CONSTRAINT `FKd5mv839cpi8qcn32ylblx6if0` FOREIGN KEY (`collaborator_id`) REFERENCES `collaborateur` (`id`),
  ADD CONSTRAINT `FKntxdsyn12worhd9ymk20jum4b` FOREIGN KEY (`formation_id`) REFERENCES `formation` (`id`);

--
-- Contraintes pour la table `poste_competence`
--
ALTER TABLE `poste_competence`
  ADD CONSTRAINT `FK6bcjpya7raa5g31tgxsj0mlpl` FOREIGN KEY (`competence_id`) REFERENCES `competence` (`id`),
  ADD CONSTRAINT `FKspsfxte1jvklltjioipc5oo9k` FOREIGN KEY (`poste_id`) REFERENCES `poste` (`id_poste`);

--
-- Contraintes pour la table `token`
--
ALTER TABLE `token`
  ADD CONSTRAINT `FKdlahhwwn98r1leuqur1ixf0l2` FOREIGN KEY (`collaborateur_id`) REFERENCES `collaborateur` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
