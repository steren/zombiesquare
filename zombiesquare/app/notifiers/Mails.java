package notifiers;

import play.*;
import play.i18n.Messages;
import play.mvc.*;
import java.util.*;

import models.Player;
import models.Venue;
import models.Weapon;

public class Mails extends Mailer {

	public static String SENDER = "Square of the Dead <steren.giannini@gmail.com>";

	public static void playerContaminatedByVenue(Player player, Venue venue) {
		setFrom(SENDER);
		setSubject(Messages.get("playerContaminatedByVenue.subject"));
		addRecipient(player.email);
		send(player, venue);
	}
	
	public static void playerDeContaminated(Player player, Venue venue, Player decontaminator) {
		setFrom(SENDER);
		setSubject("You were de-contaminated by "+ decontaminator.firstName);
		addRecipient(player.email);
		send(player, venue, decontaminator);
	}

	public static void playerContaminatedVenue(Player player, Venue venue) {
		setFrom(SENDER);
		setSubject("You contaminated "+ venue.name + "!");
		addRecipient(player.email);
		send(player, venue);
	}
	
	public static void playerGetWeapon(Player player, Venue venue) {
		setFrom(SENDER);
		Weapon[] weapons = Weapon.values();
		int indexWeapon = (int) (weapons.length*Math.random());
		Weapon weapon = weapons[indexWeapon];
		setSubject("You found a "+weapon.getName()+" in "+venue.name);
		addRecipient(player.email);
		send(player, venue);
	}
	
	public static void playerUseWeapon(Player player, Venue venue, int addScore, int zombiesDecontaminated) {
		setFrom(SENDER);
		setSubject("Nice fight, you decontaminated "+ venue.name +"!");
		addRecipient(player.email);
		send(player, venue, addScore, zombiesDecontaminated);
	}
	
	public static void testMail() {
		setFrom(SENDER);
		setSubject("Test Mail");
		addRecipient("steren.giannini@gmail.com");
		String testString = "Steren Giannini";
		send(testString);
	}
}