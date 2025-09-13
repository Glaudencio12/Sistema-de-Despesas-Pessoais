DELIMITER $$

CREATE TRIGGER log_para_insert_usuario
AFTER INSERT ON usuario
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
        'usuario', 'INSERT', NEW.id, NOW(),
        CONCAT('Cadastro de: ', NEW.nome, ', email: ', NEW.email, ' realizado')
    );
END $$

DELIMITER ;
