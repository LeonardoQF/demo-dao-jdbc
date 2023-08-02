package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

	Connection conn;

	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("id"));
		dep.setName(rs.getString("name"));

		return dep;
	}
	
	//CRUD

	/**
	 * Este método recebe um objeto Department e o insere na tabela department do
	 * banco de dados.
	 * 
	 * 
	 * @param obj o objeto Department a ser inserido.
	 * @throws DbException Caso nenhuma linha seja afetada pelo insert.
	 * 
	 **/
	@Override
	public void insert(Department obj) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("""
					INSERT INTO department(name)
					VALUES(?)
					""", Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getName());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);

			} else {
				throw new DbException("Unexpected Error, No rows affected by the insert");
			}

		} catch (SQLException e) {
			throw new DbException("Could not insert values into the database");
		} finally {
			DB.closeStatement(st);
		}

	}

	/**
	 * Atualiza um dado no banco de dados passado um objeto de tipo Department.
	 * 
	 * @param obj um objeto Department contendo os atributos a serem atualizados.
	 * @throws DbException
	 */

	@Override
	public void update(Department obj) {
		PreparedStatement st = null;

		try {

			st = conn.prepareStatement("""
					UPDATE department
					SET name = ?
					WHERE id = ?;
					""");

			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getLocalizedMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	/**
	 * Deletes a department entry from the database, given an id.
	 * 
	 * @param id the id of the department to be deleted.
	 * @throws DbException if an error occurs
	 */
	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("""
					DELETE FROM department
					WHERE id = ?
					""");

			st.setInt(1, id);

			int rowsAffected = st.executeUpdate();

			if (rowsAffected == 0) {
				throw new DbException("Entered Id does not exist");
			}

		} catch (SQLException e) {
			throw new DbException(e.getLocalizedMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {

			st = conn.prepareStatement("""
					SELECT * FROM department
					WHERE id = ?
					""");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) {
				Department dep = instantiateDepartment(rs);
				return dep;
				
			}

		} catch (SQLException e) {
			throw new DbException(e.getLocalizedMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}

		return null;
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("""
					SELECT * FROM department
					""");
			
			rs = st.executeQuery();
			
			List<Department> list = new ArrayList<>();
			
			while(rs.next()) {
				Department dep = instantiateDepartment(rs);
				list.add(dep);
			}
			
			return list;
			
			
			
			
		} catch (SQLException e) {
			throw new DbException(e.getLocalizedMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

}
