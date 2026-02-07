package zoo.repository.jdbc;

import zoo.config.DatabaseConnection;
import zoo.domain.Zoo;
import zoo.repository.ZooRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcZooRepository implements ZooRepository {

    @Override
    public Zoo create(Zoo z) {
        String sql = "INSERT INTO zoo (name, city) VALUES (?, ?) RETURNING id";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, z.getName());
            ps.setString(2, z.getCity());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Zoo(rs.getInt("id"), z.getName(), z.getCity());
                }
            }
            throw new SQLException("Zoo insert failed: no id returned");

        } catch (SQLException e) {
            throw new RuntimeException("DB error while creating zoo", e);
        }
    }

    @Override
    public Optional<Zoo> findById(Integer id) {
        String sql = "SELECT id, name, city FROM zoo WHERE id = ?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Zoo(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("city")
                    ));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("DB error while finding zoo by id=" + id, e);
        }
    }

    @Override
    public List<Zoo> findAll() {
        String sql = "SELECT id, name, city FROM zoo";

        try (Connection c = DatabaseConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            List<Zoo> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new Zoo(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("city")
                ));
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("DB error while listing all zoos", e);
        }
    }

    @Override
    public boolean update(Zoo z) {
        String sql = "UPDATE zoo SET name = ?, city = ? WHERE id = ?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, z.getName());
            ps.setString(2, z.getCity());
            ps.setInt(3, z.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("DB error while updating zoo id=" + z.getId(), e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM zoo WHERE id = ?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("DB error while deleting zoo id=" + id, e);
        }
    }
}