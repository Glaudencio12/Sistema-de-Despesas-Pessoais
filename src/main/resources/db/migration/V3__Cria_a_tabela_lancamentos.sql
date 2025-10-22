CREATE TABLE IF NOT EXISTS `lancamento` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data` datetime(6) NOT NULL,
  `descricao` varchar(150) NOT NULL,
  `tipo` enum('DESPESA','RECEITA') NOT NULL,
  `valor` decimal(15,2) NOT NULL,
  `categoria_id` bigint DEFAULT NULL,
  `usuario_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_categoria_lacamento` FOREIGN KEY (`categoria_id`) REFERENCES `categoria` (`id`),
  CONSTRAINT `FK_lancamento_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`)
)