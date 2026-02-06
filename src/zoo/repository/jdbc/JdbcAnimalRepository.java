package zoo.repository.jdbc;

import zoo.config.DatabaseConnection;
import zoo.domain.Animal;
import zoo.repository.AnimalRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcAnimalRepository implements AnimalRepository {

    @Override
    public Animal create(Animal a) {
        // RETURNING id — удобно для Postgres (получаем новый id сразу)
        String sql = "INSERT INTO animal (name, species, age, zoo_id) VALUES (?, ?, ?, ?) RETURNING id";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, a.getName());
            ps.setString(2, a.getSpecies());
            ps.setInt(3, a.getAge());
            ps.setInt(4, a.getZooId());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int newId = rs.getInt("id");
                    return new Animal(newId, a.getName(), a.getSpecies(), a.getAge(), a.getZooId());
                }
            }

            throw new SQLException("Animal insert failed: no id returned");

        } catch (SQLException e) {
            throw new RuntimeException("DB error while creating animal", e);
        }
    }

    @Override
    public Optional<Animal> findById(Integer id) {
        String sql = "SELECT * FROM animal WHERE id = ?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("DB error while finding animal by id=" + id, e);
        }
    }

    @Override
    public List<Animal> findAll() {
        String sql = "SELECT * FROM animal";

        try (Connection c = DatabaseConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            List<Animal> list = new ArrayList<>();
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("DB error while listing all animals", e);
        }
    }

    @Override
    public List<Animal> findByZooId(int zooId) {
        String sql = "SELECT * FROM animal WHERE zoo_id = ?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, zooId);

            try (ResultSet rs = ps.executeQuery()) {
                List<Animal> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(map(rs));
                }
                return list;
            }

        } catch (SQLException e) {
            throw new RuntimeException("DB error while finding animals by zoo_id=" + zooId, e);
        }
    }

    @Override
    public boolean update(Animal a) {
        String sql = "UPDATE animal SET name = ?, species = ?, age = ?, zoo_id = ? WHERE id = ?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, a.getName());
            ps.setString(2, a.getSpecies());
            ps.setInt(3, a.getAge());
            ps.setInt(4, a.getZooId());
            ps.setInt(5, a.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("DB error while updating animal id=" + a.getId(), e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM animal WHERE id = ?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("DB error while deleting animal id=" + id, e);
        }
    }

    private Animal map(ResultSet rs) throws SQLException {
        return new Animal(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("species"),
                rs.getInt("age"),
                rs.getInt("zoo_id")
        );
    }
}