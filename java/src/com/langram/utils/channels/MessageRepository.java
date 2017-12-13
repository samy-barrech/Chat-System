package com.langram.utils.channels;

import com.langram.utils.exchange.database.DatabaseStore;
import com.langram.utils.messages.FileMessage;
import com.langram.utils.messages.Message;
import com.langram.utils.messages.TextMessage;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.xml.soap.Text;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

/**
 * This file is part of the project java.
 *
 * @author Guillaume
 * @version 1.0
 * @date 11/12/2017
 * @since 1.0
 */
public class MessageRepository implements RepositoryInterface<Message>
{
	private static DatabaseStore db = DatabaseStore.getInstance();
	private static MessageRepository instance = new MessageRepository();

	public static MessageRepository getInstance() {
		return instance;
	}

	@Override
	public void store(Message message) {
		switch (message.getMessageType())
		{
			case TEXT_MESSAGE:
				this.storeTextMessage((TextMessage) message);
				break;
			case FILE_MESSAGE:
				this.storeFileMessage((FileMessage) message);
				break;
		}
	}

	private void storeTextMessage(TextMessage message)
	{
		String sql = "INSERT INTO message(messageType, message_date, senderName, content, channelID) VALUES(?, ?, ?, ?, ?)";
		System.out.println("Chaine = "+message.getChannel().getChannelName());
		try {
			Connection conn = db.connect();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, message.getMessageType().toString());
			pstmt.setString(2, message.getDate());
			pstmt.setString(3, message.getSenderName());
			pstmt.setString(4, message.getText());
			pstmt.setString(5, ChannelRepository.getInstance().getChannelUUID(message.getChannel().getChannelName()).toString());
			pstmt.execute();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void storeFileMessage(FileMessage message)
	{
		throw new NotImplementedException();
	}

	@Override
	public ArrayList<Message> retrieveAll() {
		ArrayList<Message> messagesList = new ArrayList<>();
		try {
			ResultSet rs = getResultSet("SELECT messageType, message_date, senderName, content, channelID FROM message");
			while (rs.next()) {
				Message m = null;
				String channelID  = rs.getString("channelID");
				Channel c = ChannelRepository.getInstance().getChannelWithUUID(channelID);
				if(Objects.equals(rs.getString("messageType"), Message.MessageType.TEXT_MESSAGE.toString()))
				{
					m = new TextMessage(
							rs.getString("senderName"),
							rs.getString("content"),
							rs.getDate("message_date"),
							c
					);
				}else if(Objects.equals(rs.getString("messageType"), Message.MessageType.FILE_MESSAGE.toString())){
					m = new FileMessage(null);
				}

				messagesList.add(m);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return messagesList;
	}

	public ArrayList<Message> retrieveMessageFromChannel(String idChannel) {
		ArrayList<Message> messagesList = new ArrayList<>();
		try {
			ResultSet rs = getResultSet("SELECT messageType, message_date, senderName, content, channelID FROM message WHERE channelID = '" + idChannel + "';");
			while (rs.next()) {
				Message m = null;
				String channelID  = rs.getString("channelID");
				Channel c = ChannelRepository.getInstance().getChannelWithUUID(channelID);
				if(Objects.equals(rs.getString("messageType"), Message.MessageType.TEXT_MESSAGE.toString()))
				{
					SimpleDateFormat parser = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", Locale.UK);
					try {
						java.util.Date date = parser.parse(rs.getString("message_date"));
						m = new TextMessage(
								rs.getString("senderName"),
								rs.getString("content"),
								date,
								c
						);
					} catch (ParseException e) {
						e.printStackTrace();
					}

				}else if(Objects.equals(rs.getString("messageType"), Message.MessageType.FILE_MESSAGE.toString())){
					m = new FileMessage(null);
				}
				messagesList.add(m);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return messagesList;
	}

	private ResultSet getResultSet(String sql) throws SQLException {
		Connection conn = db.connect();
		Statement stmt = conn.createStatement();
		return stmt.executeQuery(sql);
	}
}