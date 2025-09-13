DELIMITER $$

CREATE TRIGGER log_para_insert_lancamento
AFTER INSERT ON lancamento
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
        'lancamento', 'INSERT', NEW.id, NOW(),
        CONCAT('Lançamento tipo: ', NEW.tipo, ' no valor de: ', NEW.valor, ' realizado pelo usuário de id ', NEW.usuario_id)
    );
END $$

DELIMITER ;