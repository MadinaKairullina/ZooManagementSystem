package zoo.repository.jdbc;

import zoo.config.DatabaseConnection;
import zoo.domain.Zoo;
import zoo.repository.ZooRepository;

import java.sql.*;
import java.util.*;

public class JdbcZooRepository implements ZooRepository {

    @Override
    public Zoo create(Zoo z) {
        String sql = "INSERT INTO zoo (name, city) VALUES (?, ?) RETURNING id";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, z.getName());
            ps.setString(2, z.getCity());
            ResultSet rs = ps.executeQuery();
            rs.next();
            return new Zoo(rs.getInt("id"), z.getName(), z.getCity());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Zoo> findById(Integer id) {
        String sql = "SELECT * FROM zoo WHERE id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return Optional.of(new Zoo(rs.getInt("id"), rs.getString("name"), rs.getString("city")));
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Zoo> findAll() {
        try (Connection c = DatabaseConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM zoo")) {

            List<Zoo> list = new ArrayList<>();
            while (rs.next())
                list.add(new Zoo(rs.getInt("id"), rs.getString("name"), rs.getString("city")));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Zoo z) {
        String sql = "UPDATE zoo SET name=?, city=? WHERE id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, z.getName());
            ps.setString(2, z.getCity());
            ps.setInt(3, z.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM zoo WHERE id=?")) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}