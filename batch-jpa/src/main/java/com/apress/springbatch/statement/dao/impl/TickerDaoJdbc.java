/**
 * 
 */
package com.apress.springbatch.statement.dao.impl;

/**
 * @author Alain Narcisse SOBNGWI
 *
 */
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import com.apress.springbatch.statement.dao.TickerDao;
import com.apress.springbatch.statement.domain.Ticker;
import com.apress.springbatch.statement.domain.Transaction;

public class TickerDaoJdbc extends JdbcTemplate implements TickerDao {
    private static final String FIND_BY_SYMBOL = "select * from ticker t where ticker = ?";
    private static final String SAVE_TICKER = "insert into ticker (ticker, currentPrice) values (?,?)";
    @SuppressWarnings("unchecked")
   
	@Override
    public Ticker findTickerBySymbol(String symbol) {
		List<Ticker> tickers = query(FIND_BY_SYMBOL, new Object[] { symbol },
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Ticker ticker = new Ticker();
						ticker.setId(rs.getLong("id"));
						ticker.setPrice(rs.getBigDecimal("currentPrice"));
						ticker.setTc(rs.getString("ticker"));
						return ticker;
					}
				});
		if (tickers != null && tickers.size() > 0) {
			return tickers.get(0);
		} else {
			return null;
		}
	}

    @Override
	public void saveTicker(Ticker ticker) {
		update(SAVE_TICKER,
				new Object[] { ticker.getTc(), ticker.getPrice() });
	}
}