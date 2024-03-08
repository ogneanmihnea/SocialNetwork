package ro.ubbcluj.cs.map.socialnetwork_gui.repository;

import ro.ubbcluj.cs.map.socialnetwork_gui.domain.Utilizator;
import ro.ubbcluj.cs.map.socialnetwork_gui.domain.validators.Validator;
import ro.ubbcluj.cs.map.socialnetwork_gui.repository.paging.Page;
import ro.ubbcluj.cs.map.socialnetwork_gui.repository.paging.Pageable;
import ro.ubbcluj.cs.map.socialnetwork_gui.repository.paging.PagingRepository;

import java.sql.*;
import java.util.*;

public class UserDBPagingRepository implements PagingRepository<Long, Utilizator> {
    private String url;
    private String username;
    private String password;

    private Validator<Utilizator> validator;


    public UserDBPagingRepository(String url, String username, String password, Validator<Utilizator> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator= validator;
    }

    @Override
    public Optional<Utilizator> findOne(Long longID) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from users " +
                     "where id = ?");

        ) {
            statement.setInt(1, Math.toIntExact(longID));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                Utilizator u = new Utilizator(firstName, lastName, username, password);
                u.setId(longID);
                return Optional.ofNullable(u);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public Iterable<Utilizator> findAll() {
        Set<Utilizator> users = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from users");
             ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                Utilizator user = new Utilizator(firstName, lastName, username, password);
                user.setId(id);
                users.add(user);

            }
            return users;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Utilizator> save(Utilizator entity) {

        validator.validate(entity);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("insert into users(first_name,last_name) VALUES(?,?)")
        ){
            statement.setString(1,entity.getFirstName());
            statement.setString(2,entity.getLastName());
            statement.executeUpdate();
        } catch (SQLException e){
            throw  new RuntimeException(e);
        }

        return Optional.empty() ;
    }

    @Override
    public Optional<Utilizator> delete(Long aLong) {
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement("delete from users where( id = ? ) ")
        ){
            statement.setLong(1,aLong);
            Optional<Utilizator> user = findOne(aLong);
            if(user.isPresent()){
                if(statement.executeUpdate() > 0){
                    user.get().setId(-1L);
                    return user;
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
    public Optional<Utilizator> update(Utilizator entity) {
        try(Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement("update users set first_name=?,last_name=? where id=?")
        ) {
            statement.setString(1,entity.getFirstName());
            statement.setString(2,entity.getLastName());
            statement.setLong(3,entity.getId());
            statement.executeUpdate();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    private int returnNumberOfElements(){
        int numberOfElements=0;
        try (Connection connection = DriverManager.getConnection(url,username,password);
             // pas 2: design & execute SLQ
             PreparedStatement statement = connection.prepareStatement("select count(*) as count from users");
             ResultSet resultSet = statement.executeQuery();
        ) {
            // pas 3: process result set
            while (resultSet.next()){
                numberOfElements = resultSet.getInt("count");
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        // pas 3: return no elements
        return numberOfElements;
    }

    @Override
    public Page<Utilizator> findAll(Pageable pageable) {
        int numOfElements = returnNumberOfElements();
        int limit = pageable.getPageSize();
        int offset = pageable.getPageNumber() * limit;

        if(offset >= numOfElements) {
            return new Page<>(new ArrayList<>(), numOfElements);
        }

        List<Utilizator> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url,username,password);
            PreparedStatement statement = connection.prepareStatement("SELECT * from users limit ? offset ?");
            ){
            statement.setInt(1,limit);
            statement.setInt(2,offset);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                Long id= resultSet.getLong("id");
                String firstName=resultSet.getString("first_name");
                String lastName=resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                Utilizator user = new Utilizator(firstName, lastName, username, password);
                user.setId(id);
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new Page<>(users,numOfElements);
    }
}
