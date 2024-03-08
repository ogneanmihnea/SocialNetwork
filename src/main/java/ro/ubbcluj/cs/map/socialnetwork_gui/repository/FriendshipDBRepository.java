package ro.ubbcluj.cs.map.socialnetwork_gui.repository;

import ro.ubbcluj.cs.map.socialnetwork_gui.domain.FriendRequestEnum;
import ro.ubbcluj.cs.map.socialnetwork_gui.domain.Prietenie;
import ro.ubbcluj.cs.map.socialnetwork_gui.domain.Tuple;
import ro.ubbcluj.cs.map.socialnetwork_gui.domain.Utilizator;
import ro.ubbcluj.cs.map.socialnetwork_gui.domain.validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class FriendshipDBRepository implements Repository<Tuple<Long, Long>, Prietenie> {


    private String url;
    private String username;
    private String password;
    private Validator<Prietenie> validator;

    public FriendshipDBRepository(String url, String username, String password, Validator<Prietenie> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }


    @Override
    public Optional<Prietenie> findOne(Tuple<Long, Long> longLongTuple) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT u1.first_name AS u1_first_name, u1.last_name AS u1_last_name, u2.first_name AS u2_first_name, u2.last_name AS u2_last_name, f.data AS date, id1 as id1, id2 as id2, status as status FROM friendships f JOIN users u1 ON f.id1 = u1.id JOIN users u2 ON f.id2 = u2.id where ( id1 = ? and id2 = ?) or (id2 = ? and id1 = ?)");
        ) {
            statement.setLong(1, longLongTuple.getLeft());
            statement.setLong(2, longLongTuple.getRight());
            statement.setLong(3, longLongTuple.getLeft());
            statement.setLong(4, longLongTuple.getRight());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String u1firstName = resultSet.getString("u1_first_name");
                String u1lastName = resultSet.getString("u1_last_name");
                String u2firstName = resultSet.getString("u2_first_name");
                String u2lastName = resultSet.getString("u2_last_name");
                Timestamp timestamp = resultSet.getTimestamp("date");
                String myStatus = resultSet.getString("status");

                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");


                ///Long id1 = longLongTuple.getLeft();
                ///Long id2 = longLongTuple.getRight();

                LocalDateTime MyDate = timestamp.toLocalDateTime();

                Utilizator u1 = new Utilizator(u1firstName, u1lastName,null,null);
                u1.setId(id1);
                Utilizator u2 = new Utilizator(u2firstName, u2lastName,null,null);
                u2.setId(id2);
                Tuple<Long, Long> id = new Tuple<>(id1, id2);
                Prietenie prietenie = new Prietenie(u1, u2, MyDate);
                prietenie.setId(id);

                if(Objects.equals(myStatus, "ACCEPTED")){
                    prietenie.setAcceptance(FriendRequestEnum.ACCEPTED);
                }
                if(Objects.equals(myStatus, "PENDING")){
                    prietenie.setAcceptance(FriendRequestEnum.PENDING);
                }
                if(Objects.equals(myStatus, "REJECTED")){
                    prietenie.setAcceptance(FriendRequestEnum.REJECTED);
                }

                return Optional.of(prietenie);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public Iterable<Prietenie> findAll() {
        Set<Prietenie> friendships = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT u1.first_name AS u1_first_name, u1.last_name AS u1_last_name, u2.first_name AS u2_first_name, u2.last_name AS u2_last_name, f.data AS date, id1 as id1, id2 as id2, status as status FROM friendships f JOIN users u1 ON f.id1 = u1.id JOIN users u2 ON f.id2 = u2.id" );
        ) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String u1firstName = resultSet.getString("u1_first_name");
                String u1lastName = resultSet.getString("u1_last_name");
                String u2firstName = resultSet.getString("u2_first_name");
                String u2lastName = resultSet.getString("u2_last_name");
                Timestamp timestamp = resultSet.getTimestamp("date");

                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                String myStatus = resultSet.getString("status");
                LocalDateTime MyDate = timestamp.toLocalDateTime();

                Utilizator u1 = new Utilizator(u1firstName, u1lastName,null,null);
                u1.setId(id1);
                Utilizator u2 = new Utilizator(u2firstName, u2lastName,null,null);
                u2.setId(id2);
                Tuple<Long, Long> id = new Tuple<>(id1, id2);

                Prietenie prietenie = new Prietenie(u1, u2, MyDate);
                prietenie.setId(id);

                if(Objects.equals(myStatus, "ACCEPTED")){
                    prietenie.setAcceptance(FriendRequestEnum.ACCEPTED);
                }
                if(Objects.equals(myStatus, "PENDING")){
                    prietenie.setAcceptance(FriendRequestEnum.PENDING);
                }
                if(Objects.equals(myStatus, "REJECTED")){
                    prietenie.setAcceptance(FriendRequestEnum.REJECTED);
                }

                friendships.add(prietenie);
            }
            return friendships;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Prietenie> save(Prietenie entity) {
        validator.validate(entity);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("insert into friendships(id1,id2,data,status) VALUES(?,?,?,?)")
        ){
            statement.setLong(1,entity.getUser1().getId());
            statement.setLong(2,entity.getUser2().getId());
            LocalDateTime Mydate = entity.getDate();
            Timestamp timestamp = Timestamp.valueOf(Mydate);
            statement.setTimestamp(3,timestamp);
            statement.setString(4,entity.getAcceptance().toString());
            statement.executeUpdate();
        } catch (SQLException e){
            throw  new RuntimeException(e);
        }

        return Optional.empty() ;
    }

    @Override
    public Optional<Prietenie> delete(Tuple<Long, Long> longLongTuple) {
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement("delete from friendships where( id1 = ? and id2 = ?) or (id2 = ? and id1 = ?) ")
        ){
            statement.setLong(1,longLongTuple.getLeft());
            statement.setLong(2,longLongTuple.getRight());
            statement.setLong(3,longLongTuple.getLeft());
            statement.setLong(4,longLongTuple.getRight());
            Optional<Prietenie> friendship = findOne(longLongTuple);
            if(friendship.isPresent()){
                if(statement.executeUpdate() > 0){
                    friendship.get().setId(new Tuple<>(-1L, -1L));
                    return friendship;
                }
                throw new RuntimeException("Delete failed");
            }
        }
        catch (SQLException e){
            throw  new RuntimeException(e);
        }
        return Optional.empty();
    }
    @Override
    public Optional<Prietenie> update(Prietenie entity) {
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement updateStatement = connection.prepareStatement("update friendships set status=? where id1=? AND id2=?");

        ){
            updateStatement.setLong(2, entity.getUser1().getId());
            updateStatement.setLong(3, entity.getUser2().getId());

            updateStatement.setString(1,entity.getAcceptance().toString());


            if (updateStatement.executeUpdate() > 0){
                ResultSet resultSet = updateStatement.getGeneratedKeys();
            }

        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

}
