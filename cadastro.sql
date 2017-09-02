-- --------------------------------------------------------
-- Servidor:                     200.207.224.87
-- Versão do servidor:           10.1.13-MariaDB - mariadb.org binary distribution
-- OS do Servidor:               Win64
-- HeidiSQL Versão:              9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Copiando estrutura do banco de dados para cadastro
CREATE DATABASE IF NOT EXISTS `cadastro` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `cadastro`;

-- Copiando estrutura para tabela cadastro.cliente
CREATE TABLE IF NOT EXISTS `cliente` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(200) NOT NULL DEFAULT '',
  `email` varchar(200) NOT NULL DEFAULT '',
  `setor` int(11) DEFAULT NULL,
  `endereco` varchar(200) NOT NULL DEFAULT '',
  `ativo` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_setor_id` (`setor`),
  CONSTRAINT `fk_setor_id` FOREIGN KEY (`setor`) REFERENCES `setor` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- Copiando dados para a tabela cadastro.cliente: ~4 rows (aproximadamente)
DELETE FROM `cliente`;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
INSERT INTO `cliente` (`id`, `nome`, `email`, `setor`, `endereco`, `ativo`) VALUES
	(1, 'Cliente A', 'cliente@cliente', 1, 'Rua A', 1),
	(2, 'Cliente B', 'clienteb@clienteb', 2, 'Rua B', 0),
	(3, 'Cliente C', 'clientec@clientec', 4, 'Rua C', 0),
	(4, 'Cliente D', 'cliented@cliented', 3, 'Rua D', 1),
	(5, 'Cliente E', 'clienteE@cliente', 1, 'Rua E', 1),
	(6, 'Cliente F', 'clienteF@clienteF', 10, 'Rua F', 1),
	(7, 'Cliente G', 'clienteG@clienteG', 4, 'Rua G', 1);
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;

-- Copiando estrutura para tabela cadastro.setor
CREATE TABLE IF NOT EXISTS `setor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;

-- Copiando dados para a tabela cadastro.setor: ~33 rows (aproximadamente)
DELETE FROM `setor`;
/*!40000 ALTER TABLE `setor` DISABLE KEYS */;
INSERT INTO `setor` (`id`, `nome`) VALUES
	(1, 'Água, gás e saneamento'),
	(2, 'Alimentos processados'),
	(3, 'Bancos'),
	(4, 'Carnes e derivados'),
	(5, 'Construção civil e intermediação'),
	(6, 'Construção pesada e engenharia'),
	(7, 'Energia elétrica'),
	(8, 'Equipamentos, máquinas e peças'),
	(9, 'Holdings'),
	(10, 'Hotelaria e Restaurantes'),
	(11, 'Imóveis comerciais e shoppings'),
	(12, 'Indústrias de alimentos'),
	(13, 'Indústrias em geral'),
	(14, 'Materiais diversos'),
	(15, 'Mineração'),
	(16, 'Negócios de lazer e eventos'),
	(17, 'Papel e madeira'),
	(18, 'Petróleo, gás e combustíveis'),
	(19, 'Química e petroquímica'),
	(20, 'Roupas, calçados e acessórios'),
	(21, 'Serviços diversos'),
	(22, 'Serviços financeiros'),
	(23, 'Setor de educação'),
	(24, 'Setor de saúde'),
	(25, 'Setor de seguros'),
	(26, 'Setor de transporte'),
	(27, 'Siderurgia e metalurgia'),
	(28, 'Tecnologia da informação'),
	(29, 'Telecomunicações'),
	(30, 'Têxteis'),
	(31, 'Utilidades domésticas'),
	(32, 'Varejo'),
	(33, 'Agropecuária');
/*!40000 ALTER TABLE `setor` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
