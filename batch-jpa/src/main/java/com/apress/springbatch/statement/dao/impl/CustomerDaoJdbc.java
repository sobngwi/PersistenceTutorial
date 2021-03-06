/**
 * 
 */
package com.apress.springbatch.statement.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import com.apress.springbatch.statement.dao.CustomerDao;
import com.apress.springbatch.statement.domain.Address;
import com.apress.springbatch.statement.domain.Customer;
/**
 * @author Alain Narcisse SOBNGWI
 *
 */

public class CustomerDaoJdbc extends JdbcTemplate implements CustomerDao {
	private static final String FIND_BY_TAX_ID = "select * from customer c where ssn = ?";

	@SuppressWarnings("unchecked")
	@Override
	public Customer findCustomerByTaxId(String taxId) {
		List<Customer> customers = query(FIND_BY_TAX_ID,
				new Object[] { taxId }, new RowMapper() {

					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Customer customer = new Customer();
						customer.setId(rs.getLong("id"));
						customer.setFirstName(rs.getString("firstName"));
						customer.setLastName(rs.getString("lastName"));
						customer.setTaxId(rs.getString("ssn"));
						customer.setAddress(buildAddress(rs));
						return customer;
					}

					private Address buildAddress(ResultSet rs)
							throws SQLException {
						Address address = new Address();
						address.setAddress1(rs.getString("address1"));
						address.setCity(rs.getString("city"));
						address.setState(rs.getString("state"));
						address.setZip(rs.getString("zip"));
						return address;
					}
				});
		if (customers != null && customers.size() > 0) {
			return customers.get(0);
		} else {
			return null;
		}
	}
}