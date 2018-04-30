import com.sun.istack.internal.NotNull;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.function.Predicate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SqlController implements IController
{
    @NotNull private String url, user, password;
    Connection connection;
    Statement statement;
    public SqlController()
    {
        //todo check if tables exist
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            String sql = "CREATE TYPE defectStatesEnum AS ENUM ("
                    + String.join(", ", Stream.of(DefectStatesEnum.values())
                            .map(defectStatesEnum -> "'"+ defectStatesEnum.toString()+ "'")
                            .collect(Collectors.toList()))
                    + ");";
            statement.executeUpdate(sql);

            sql = "CREATE TYPE defectVersionStatesEnum AS ENUM ("
                    + String.join(", ", Stream.of(DefectVersionStatesEnum.values())
                    .map(defectVersionStatesEnum -> "'"+ defectVersionStatesEnum.toString()+ "'")
                    .collect(Collectors.toList()))
                    + ");";
            statement.executeUpdate(sql);

            sql = "CREATE TABLE DEFECTS (" +
                    "name CHAR(13) PRIMARY KEY NOT NULL, " +
                    "reportedVersion CHAR(4) NOT NULL, " +
                    "state defectStatesEnum NOT NULL" +
                    "comment CHAR(200) NOT NULL);";
            statement.executeUpdate(sql);

            sql = "CREATE TABLE VERSIONS (" +
                    "defectName CHAR(13) NOT NULL, " +
                    "version CHAR(4) NOT NULL, " +
                    "state defectVersionStatesEnum NOT NULL," +
                    "PRIMARY KEY (defectName, version));";
            statement.executeUpdate(sql);
            statement.close();
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {

        }
    }

    public void addDefect(@NotNull IDefectInfoProvider defectInfo)
    {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            String sql = "INSERT INTO DEFECTS (name, reportedVersion, state, comment) VALUES('" +
                    defectInfo.getDefectName() + "', '" +
                    defectInfo.getReportedVersion() + "', '" +
                    defectInfo.getDefectState().toString() + "', '" +
                    defectInfo.getComment() + "');";
            statement.executeUpdate(sql);

            sql = "INSERT INTO VERSIONS (defectName, version, state) VALUES(" +
                    String.join("), (", defectInfo.getVersionInfoList().stream()
                            .map(defectVersionInfo -> "'" + defectInfo.getDefectName() + "', '" +
                                    defectVersionInfo.getVersion() + "', '" +
                                    defectVersionInfo.getState().toString() + "'").collect(Collectors.toList())) +
                    ");";
            statement.executeUpdate(sql);
            statement.close();
            connection.close();

        } catch (ClassNotFoundException | SQLException e) {

        }
    }
    public void changeDefect(@NotNull IDefectInfoProvider defectInfo)
    {
        try {
            //todo to do)
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            String sql = "UPDATE DEFECTS set reportedVersion = " + defectInfo.getReportedVersion()
                    + "state = " + defectInfo.getDefectState().toString()
                    + "comment = " + defectInfo.getComment()
                    + "where name = " + defectInfo.getDefectName();
            statement.executeUpdate(sql);
            statement.close();
            connection.close();

        } catch (ClassNotFoundException | SQLException e) {

        }
    }
    public void cleanData()
    {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            String sql = "DELETE FROM * WHERE *;";
            statement.executeUpdate(sql);
            statement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {

        }
    }

    public List<IDefectInfoProvider> getDefectInfoSublist(Predicate<IDefectInfoProvider> validator)
    {

    }

    public List<IDefectInfo> getDefectInfoList()
    {

    }
}
