package me.snykkk.guidenpc.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.entity.Player;

import me.snykkk.guidenpc.GuideNPC;
import me.snykkk.guidenpc.libs.FServer;

public class MySQL {

	private final GuideNPC plugin = GuideNPC.getPlugin();
	
	private Connection connection;
	
	public MySQL() {
		start();
		
		try {
			Statement stmt = this.connection.createStatement();
			stmt.executeUpdate("CREATE TABLE if not exists `" + plugin.getDefaultConfig().getConfig().getString("mysql.table")
					+ "` ( `id` INT(11) NOT NULL AUTO_INCREMENT , `playername` text NOT NULL , `uuid` text NOT NULL , `npcguide` text NOT NULL , PRIMARY KEY (`id`)) ENGINE = MyISAM");
		} catch (SQLException ex) {
			plugin.getLogger().warning("Cannot create table, please check your database!");
		}
	}
	
	public void start() {
		if (this.connection != null) {
			try {
				this.connection.close();
			} catch (SQLException ex) {
				plugin.getLogger().warning("Cannot close connection, please check your database!");
			}
		}
		
		try {
	    	this.connection = getConnection(
	          "jdbc:mysql://" + plugin.getDefaultConfig().getConfig().getString("mysql.host") + ":" + plugin.getDefaultConfig().getConfig().getInt("mysql.port") + 
	          "/" + plugin.getDefaultConfig().getConfig().getString("mysql.database") + "?autoReconnect=true", 
	          plugin.getDefaultConfig().getConfig().getString("mysql.user"), plugin.getDefaultConfig().getConfig().getString("mysql.password"));
	      
	    	if (this.connection == null) {
	    		plugin.getLogger().warning("Cannot connect to database, this plugin will be disabled!");
	    		plugin.getPluginLoader().disablePlugin(plugin);
	    	}
	    
	    } catch (Exception ex) {
	    	plugin.getLogger().warning("Cannot connect to database, please check configuration!");
	    }
	}
	
	public Connection getConnection(String dbURL, String userName, String password) {
	    Connection conn = null;
	    try {
	    	Class.forName("com.mysql.jdbc.Driver");
	    	conn = DriverManager.getConnection(dbURL, userName, password);
	    	FServer.consoleLog("§bNPCGuide Connect mysql successfully!");
	    } catch (Exception ex) {
	    	FServer.consoleLog("§bNPCGuide Connect mysql failure!");
	    } 
	    return conn;
	}
	
	public String getPlayerGuide(Player p) {
		try {
			if (this.connection.isClosed()) {
				start();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Statement stmt = this.connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `" + plugin.getDefaultConfig().getConfig().getString("mysql.table") + "` WHERE `uuid` = '" + p.getUniqueId().toString() + "'");
			if (rs.next()) {
		        return rs.getString("npcguide");
			}
		} catch (SQLException ex) {
			plugin.getLogger().warning("Cannot connect to database, please check configuration!");
		}
		return null;
	}
	
	public boolean playerExists(Player p) {
		try {
			if (this.connection.isClosed()) {
				start();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Statement stmt = this.connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `" + plugin.getDefaultConfig().getConfig().getString("mysql.table") + "` WHERE `uuid` = '" + p.getUniqueId().toString() + "'");
			if (rs.next()) {
				return true;
			} else return false;
		} catch (Exception ex) {
	    	plugin.getLogger().warning("Cannot connect to database, please check configuration!");
	    	return false;
		}
	}
	
	public void insertPlayer(Player p) {
		try {
			if (this.connection.isClosed()) {
				start();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Statement stmt = this.connection.createStatement();
			if (!playerExists(p)) {
				stmt.executeUpdate(
						"INSERT INTO `" + plugin.getDefaultConfig().getConfig().getString("mysql.table") + "` (`playername`, `uuid`, `npcguide`) VALUES ('" + p.getName() + "', '" + p.getUniqueId().toString() + "', 'NONE')"
				);
			}
		} catch (Exception ex) {
	    	plugin.getLogger().warning("Cannot connect to database, please check configuration!");
		}
	}
	
	public void setPlayerGuide(String uid, String guide) {
		try {
			if (this.connection.isClosed()) {
				start();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Statement stmt = this.connection.createStatement();
			stmt.executeUpdate("UPDATE " + plugin.getDefaultConfig().getConfig().getString("mysql.table") + " SET npcguide='" + guide + "' WHERE uuid= '" + uid + "'");
			stmt.close();
		} catch (Exception ex) {
	    	plugin.getLogger().warning("Cannot connect to database, please check configuration!");
		}
	}
	
	public void resetAll() {
		try {
			if (this.connection.isClosed()) {
				start();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Statement stmt = this.connection.createStatement();
			stmt.executeUpdate("UPDATE `" + plugin.getDefaultConfig().getConfig().getString("mysql.table") + "` SET `npcguide` = 'NONE'");
			stmt.close();
		} catch (Exception ex) {
	    	plugin.getLogger().warning("Cannot connect to database, please check configuration!");
		}
	}
	
	public ResultSet query(String query) {
	    try {
	    	Statement stmt = this.connection.createStatement();
	      
	    	return stmt.executeQuery(query);
	    
	    }
	    catch (SQLException e) {
	    	e.printStackTrace();
	    	return null;
	    } 
	}
	
}
