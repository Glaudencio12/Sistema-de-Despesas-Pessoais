DELIMITER $$

CREATE TRIGGER log_para_update_usuario
AFTER UPDATE ON usuario
FOR EACH ROW
BEGIN
   IF OLD.nome <> NEW.nome THEN
       INSERT INTO logs(tabela_afetada, operacao, registro_id, data_operacao, detalhes)
       VALUES('usuario', 'UPDATE', NEW.id, NOW(), CONCAT('Nome alterado de ', OLD.nome, ' para ', NEW.nome));
   END IF;

   IF OLD.email <> NEW.email THEN
       INSERT INTO logs(tabela_afetada, operacao, registro_id, data_operacao, detalhes)
       VALUES('usuario', 'UPDATE', NEW.id, NOW(), CONCAT('Email alterado de ', OLD.email, ' para ', NEW.email));
   END IF;

   IF OLD.senha <> NEW.senha THEN
          INSERT INTO logs(tabela_afetada, operacao, registro_id, data_operacao, detalhes)
          VALUES('usuario', 'UPDATE', NEW.id, NOW(), CONCAT('Senha alterada de ', OLD.senha, ' para ', NEW.senha));
   END IF;
END $$

DELIMITER ;
