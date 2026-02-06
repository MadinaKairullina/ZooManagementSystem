package zoo.repository.jdbc;

import zoo.config.DatabaseConnection;
import zoo.domain.Zookeeper;
import zoo.repository.ZookeeperRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcZookeeperRepository implements ZookeeperRepository {

    @Override
    public Zookeeper create(Zookeeper z) {
        String sql = """
            INSERT INTO zookeeper (name, experience_years, zoo_id)
            VALUES (?, ?, ?) RETURNING id
            """;

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, z.getName());
            ps.setInt(2, z.getExperienceYears());
            ps.setInt(3, z.getZooId());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Zookeeper(
                        rs.getInt("id"),
                        z.getName(),
                        z.getExperienceYears(),
                        z.getZooId()
                );
            }
            throw new SQLException("Zookeeper insert failed");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Zookeeper> findById(Integer id) {
        String sql = "SELECT * FROM zookeeper WHERE id = ?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(map(rs));
            }
            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Zookeeper> findAll() {
        String sql = "SELECT * FROM zookeeper";

        try (Connection c = DatabaseConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            List<Zookeeper> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Zookeeper> findByZooId(int zooId) {
        String sql = "SELECT * FROM zookeeper WHERE zoo_id = ?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, zooId);
            ResultSet rs = ps.executeQuery();

            List<Zookeeper> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Zookeeper z) {
        String sql = """
            UPDATE zookeeper
            SET name=?, experience_years=?, zoo_id=?
            WHERE id=?
            """;

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, z.getName());
            ps.setInt(2, z.getExperienceYears());
            ps.setInt(3, z.getZooId());
            ps.setInt(4, z.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM zookeeper WHERE id = ?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Zookeeper map(ResultSet rs) throws SQLException {
        return new Zookeeper(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("experience_years"),
                rs.getInt("zoo_id")
        );
    }
}