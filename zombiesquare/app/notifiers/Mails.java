package notifiers;

import play.*;
import play.i18n.Messages;
import play.mvc.*;
import java.util.*;

import models.Player;
import models.Venue;

public class Mails extends Mailer {

	public static String SENDER = "Square of the Dead <steren.giannini@gmail.com>";

	public static void playerContaminatedByVenue(Player player, Venue venue) {
		setFrom(SENDER);
		setSubject(Messages.get("playerContaminatedByVenue.subject"));
		addRecipient(player.email);
		send(player, venue);
	}

	public static void playerContaminatedVenue(Player player, Venue venue) {
		setFrom(SENDER);
		setSubject("You contaminated "+ venue.name + "!");
		addRecipient(player.email);
		send(player, venue);
	}
	
	public static void playerGetWeapon(Player player, Venue venue) {
		setFrom(SENDER);
		setSubject("You found a weapon!");
		addRecipient(player.email);
		send(player, venue);
	}
	
	public static void playerUseWeapon(Player player, Venue venue) {
		setFrom(SENDER);
		setSubject("Nice fight, you decontaminated "+ venue.name +"!");
		addRecipient(player.email);
		send(player, venue);
	}
	
	public static void testMail() {
		setFrom(SENDER);
		setSubject("Test Mail");
		addRecipient("steren.giannini@gmail.com");
		String testString = "Steren Giannini";
		send(testString);
	}
}