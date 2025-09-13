DELIMITER $$

CREATE TRIGGER log_para_delete_usuario
AFTER DELETE ON usuario
FOR EACH ROW
BEGIN
    INSERT INTO logs(
        tabela_afetada,
        operacao,
        registro_id,
        data_operacao,
        detalhes
    )
    VALUES(
        'usuario', 'DELETE', OLD.id, NOW(),
        CONCAT(OLD.nome, ', email: ', OLD.email, ' excluído da base de dados')
    );
END $$

DELIMITER ;
