package notifiers;

import play.*;
import play.i18n.Messages;
import play.mvc.*;
import java.util.*;

import models.Player;
import models.Venue;

public class Mails extends Mailer {

	public static String SENDER = "Robot <robot@thecompany.com>";

	public static void playerContaminatedByVenue(Player player, Venue venue) {
		setFrom(SENDER);
		setSubject(Messages.get("playerContaminatedByVenue.subject"));
		addRecipient(player.email);
		send(player, venue);
	}

	public static void playerContaminatedVenue(Player player, Venue venue) {
		setFrom(SENDER);
		setSubject(Messages.get("playerContaminatedVenue.subject"));
		addRecipient(player.email);
		send(player, venue);
	}
}