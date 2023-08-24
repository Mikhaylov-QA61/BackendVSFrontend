package data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {

    private static final QueryRunner queryRunner= new QueryRunner();

    private SQLHelper(){}

    private static Connection getConnect() throws SQLException {
        return DriverManager.getConnection(System.getProperty("dbMySQL.url"), "app", "pass");
    }

    @SneakyThrows
    public static DataHelper.VerificationCode getVerificationCode() {
        var requestSQL = "SELECT code FROM auth_codes ORDER BY created DESC Limit 1";
        var connection = getConnect();
        var result = queryRunner.query(connection,requestSQL, new BeanHandler<>(SQLAuthCode.class));
        return new DataHelper.VerificationCode(result.getCode());
    }

    @Data
    @NoArgsConstructor
    public static class SQLAuthCode {
        private String id;
        private String user_id;
        private String code;
        private String created;
    }
}
