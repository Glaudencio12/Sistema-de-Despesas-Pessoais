CREATE TABLE IF NOT EXISTS `categoria` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `tipo` enum('DESPESA','RECEITA') NOT NULL,
  `usuario_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_categoria_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`)
)