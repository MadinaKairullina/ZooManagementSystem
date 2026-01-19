package zoo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ZookeeperDAO {
    public void addZookeeper(Zookeeper zookeeper) throws SQLException {
        String sql = "INSERT INTO zookeeper(name, experience_years) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, zookeeper.getName());
            ps.setInt(2, zookeeper.getExperienceYears());
            ps.executeUpdate();
        }
    }
    public List<Zookeeper> getAllZookeepers() throws SQLException {
        List<Zookeeper> list = new ArrayList<>();
        String sql = "SELECT name, experience_years FROM zookeeper";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Zookeeper(
                        rs.getString("name"),
                        rs.getInt("experience_years")
                ));
            }
        }
        return list;
    }
    public void updateExperience(String name, int years) throws SQLException {
        String sql = "UPDATE zookeeper SET experience_years = ? WHERE name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, years);
            ps.setString(2, name);
            ps.executeUpdate();
        }
    }
    public void deleteZookeeper(String name) throws SQLException {
        String sql = "DELETE FROM zookeeper WHERE name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.executeUpdate();
        }
    }
}