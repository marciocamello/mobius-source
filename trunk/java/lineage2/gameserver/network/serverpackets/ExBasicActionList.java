/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Player;

public class ExBasicActionList extends L2GameServerPacket
{
	private static final int[] BasicActions =
	{
		0, // ​​switch Exit. (/Sit, //Stand)
		1, // switch Run /Walk. (/Walk, /run)
		2, // ​​Attack the selected goal (s). Click while holding the mouse Ctrl, to force attack. (/Attack, /attackforce)
		3, // Request for trade with the selected player. (/Trade)
		4, // Select the nearest target for attack. (/Targetnext)
		5, // ​​pick up items around. (/Pickup)
		6, // ​​Switch on the target selected player. (/Assist)
		7, // Invite selected player in your group. (/Invite)
		8, // Leave group. (/Leave)
		9, // If you are the group leader, delete the selected player (s) of group. (/Dismiss)
		10, // Reset the personal shop for sale items. (/Vendor)
		11, // Display the window "Selection Panel" to find groups or members of your group. (/Partymatching)
		12, // Emotion: greet others. (/Socialhello)
		13, // Emotion: Show that you or someone else won //Win! (/Socialvictory)
		14, // Emotion: Inspire your allies (/socialcharge)
		15, // ​​or your pet follows you, or left in place.
		16, // Attack target.
		17, // ​​Abort the current action.
		18, // Find nearby objects.
		19, // ​​Removes Pet inventory.
		20, // Use special skill.
		21, // ​​or your minions follow you, or remain in place.
		22, // Attack target.
		23, // ​​Abort the current action.
		24, // Emotion: Reply in the affirmative. (/Socialyes)
		25, // Emotion: Reply negatively. (/Socialno)
		26, // Emotion: bow, as a sign of respect. (/Socialbow)
		27, // Use special skill.
		28, // Reset the personal shop to purchase items. (/Buy)
		29, // Emotion: I do not understand what is happening. (/Socialunaware)
		30, // Emotion: I'm waiting ... (/socialwaiting)
		31, // Emotion: From a good laugh. (/Sociallaugh)
		32, // ​​Toggle between attack /movement.
		33, // Emotion: Applause. (/Socialapplause)
		34, // Emotion: Show everyone your best dance. (/Socialdance)
		35, // Emotion: I am sad. (/Socialsad)
		36, // Poison Gas Attack.
		37, // Reset the personal studio to create objects using recipes Dwarves fee. (/Dwarvenmanufacture)
		38, // Switch to ride /dismount when you are near pet that you can ride. (/Mount, /dismount, Mountdismount)
		39, // ​​Friendly exploding corpses.
		40, // ​​Increases score goal (/evaluate)
		41, // Attack the castle gates, walls or staffs shot from a cannon.
		42, // Returns the damage back to the enemy.
		43, // Attack the enemy, creating a swirling vortex.
		44, // Attack the enemy with a powerful explosion.
		45, // Restores MP summoner.
		46, // Attack the enemy, calling destructive storm.
		47, // At the same time damages the enemy and heal his servant.
		48, // Attack the enemy shot from a cannon.
		49, // Attack in a fit of rage.
		50, // ​​Selected group member becomes the leader. (/Changepartyleader)
		51, // Create an object using the usual recipe for reward. (/Generalmanufacture)
		52, // ​​Removes ties with EP and releases it.
		53, // Move to the target.
		54, // Move to the target.
		55, // record switch to stop recording and repeats. (/Start_videorecording, /end_videorecording, //Startend_videorecording)
		56, // ​​Invite a selected target in command channel. (/Channelinvite)
		57, // ​​Displays personal messages and store personal workshop containing the desired word. (/Findprivatestore)
		58, // Call another player to a duel. (/Duel)
		59, // ​​Cancel the duel means a loss. (/Withdraw)
		60, // Call another group to a duel. (/Partyduel)
		61, // Opens personal store packages for sale (/packagesale)
		62, // Charming posture (/charm)
		63, // ​​Starts fun and simple mini-game that can be play at any time. (Command: /minigame)
		64, // Opens a free teleport, which allows to move between locations with teleporters. (Command: /teleportbookmark)
		65, // ​​report suspicious behavior of an object, whose actions suggest the use of a bot program.
		66, // Pose "Confusion" (command: /shyness)
		67, // ​​control ship
		68, // Termination control of the ship
		69, // ​​Departure ship
		70, // Descent from the ship
		71, // Bow
		72, // Give Five
		73, // Dance Together
		74, // On /Off status data
		75, // ​​Tactical Sign: Heart
		76, // ​​Invite a friend
		77, // On /Off. Record
		78, // Use the Mark 1
		79, // Use the Mark 2
		80, // Use the Mark 3
		81, // Use the Mark 4
		82, // avtopritsel Emblem 1
		83, // 2 avtopritsel Emblem
		84, // avtopritsel Emblem 3
		85, // 4 avtopritsel Emblem
		86, // Start /abort automatic search group
		87, // ​​Propose
		88, // ​​Provoke
		90, // ​​Command: /instancezone
		1000, // Attack the castle gates, walls and staffs a powerful blow.
		1001, // Reckless, but powerful attack, use it with great caution.
		1002, // To provoke others to attack you.
		1003, // unexpected attack that deals damage and stuns the opponent.
		1004, // Instant significantly increases P. Def. Def. and Mag. Def. Use this skill can not move.
		1005, // Magic Attack
		1006, // Restores HP pet.
		1007, // In case of a successful application temporarily increases the power attack group and a chance for a critical hit.
		1008, // Temporarily increases P. Def. Atk. and accuracy of your group.
		1009, // There is a chance to lift the curse with the group members.
		1010, // Increases MP regeneration of your group.
		1011, // Decreases the cooldown of your spells command.
		1012, // Removes the curse from your group.
		1013, // Taunt opponent and hit, curse, decreases P. Def. Def. and Mag. Def.
		1014, // Provokes to attack many enemies and hit with curse, lowering their P.. Def. and Mag. Def.
		1015, // Sacrifices HP to regenerate HP selected target.
		1016, // Strikes opponent powerful critical attack.
		1017, // Stunning explosion, causing damage and stunning the enemy.
		1018, // Overlay deadly curse, sucking the enemy's HP.
		1019, // skill number 2, used Cat
		1020, // skill number 2 used Meow
		1021, // skill number 2 used Kai
		1022, // skill number 2 used Jupiter
		1023, // skill number 2 used Mirage
		1024, // Skill number 2 used Bekarev
		1025, // skill number 2 used Shadow
		1026, // Skill number one used by Shadow
		1027, // skill number 2 used Hecate
		1028, // Skill number 1 used Resurrection
		1029, // Skill number 2 used Resurrection
		1030, // skill number 2 used vicious
		1031, // The King of Cats: A powerful cutting attack. Maximum damage.
		1032, // The King of Cats: Cuts nearby enemies during rotation air. Maximum damage.
		1033, // The King of Cats: Freezes enemies standing close
		1034, // Magnus: Slam hind legs, striking and stunning enemy. Maximum damage.
		1035, // Magnus: Strikes multiple objectives giant masses water.
		1036, // Wraithlord: corpse bursts, affecting adjacent enemies.
		1037, // Wraithlord: The blades in each hand applied devastating damage. Maximum damage.
		1038, // Curse of the adjacent enemies, and reducing toxic them soon. Atk.
		1039, // Siege Gun: Fires a projectile a short distance. Consumes 4 units. Gunpowder sparkling.
		1040, // Siege Gun: Fires a shell for a long distance. Consumes 5 units. Sparkling powder.
		1041, // Horrible bite the enemy
		1042, // Scratch enemy with both paws. Causes bleeding.
		1043, // Suppress the enemy with a powerful roar
		1044, // Wakes secret power
		1045, // Decreases the P.. Atk. /Mag. Atk. at nearby enemies.
		1046, // Decreases Speed. Atk. /Sprint. Mag. at nearby enemies.
		1047, // Horrible bite the enemy
		1048, // Brings double damage and stuns the enemy simultaneously.
		1049, // breathe fire in your direction.
		1050, // Suppresses surrounding enemies powerful roar.
		1051, // Increases max. amount of HP.
		1052, // Increases max. number of MP.
		1053, // Temporarily increases Atk. Atk.
		1054, // Temporarily increases speed reading spells.
		1055, // Decreases the MP cost of the selected target. Consumes runestones.
		1056, // Temporarily increases M. Def. Atk.
		1057, // Rank Temporarily increases critical strike and force magic attacks
		1058, // Temporarily increases critical strike.
		1059, // Increases the critical strike chance
		1060, // Temporarily increases Accuracy
		1061, // A strong attack from ambush. You can only use use the skill "Awakening".
		1062, // Quick double attack
		1063, // Strong twisting attack does not only damage, but also stun the enemy.
		1064, // Falling from the sky stones cause damage to enemies.
		1065, // Exits the latent state
		1066, // Friendly thunderous forces
		1067, // Quick magical enemies in sight
		1068, // Attacks multiple enemies by lightning
		1069, // slosh ambush. You can only use in the application of skill "Awakening".
		1070, // Can not impose positive effects on the wearer. Step 5 minutes.
		1071, // A strong attack on the facility
		1072, // Powerful penetrating attack on the facility
		1073, // ​​Attack enemies disperse their ranks as a tornado hit
		1074, // Attack the enemy standing in front of a powerful throw spears
		1075, // Victory cry, enhancing their own skills
		1076, // A strong attack on the facility
		1077, // Attack the enemy standing in front of the internal energy
		1078, // Attack front facing enemies using electricity
		1079, // Shouting, enhancing their own skills
		1080, // fast approaching the enemy and inflicts
		1081, // Removes negative effects from the facility
		1082, // recline flame
		1083, // A powerful bite, inflicting damage to the enemy
		1084, // Switches between the attacking /defensive mode
		1086, // Limit the number of positive effects to one
		1087, // Increases dark side to 25
		1088, // Trims important skills
		1089, // Attack the enemy standing in front with the help of the tail.
		1090, // Horrible bite the enemy
		1091, // the enemy plunged into horror and makes escape from the battlefield.
		1092, // Increases movement speed.
		1093,
		1094,
		1095,
		1096,
		1097,
		1098,
		1099,
		1100,
		1101,
		1102,
		1103,
		1104,
		1105,
		1106,
		1107,
		1108,
		1109,
		1110,
		1111,
		1112,
		1113,
		1114,
		1115,
		1116,
		1117,
		1118,
		1119,
		1120,
		1121,
		1122,
		5000, // ​​can pat Rudolf. Fills scale fidelity on 25%. Can not use in time reincarnation!
		5001, // Increases Max. HP, Max. MP and Speed ​​by 20% resistance to de-buff by 10%. Time reuse: 10 min. When using the skill spent 3 essences Rose. Can not be used with the Beyond temptation. Duration: 5 min.
		5002, // Increases Max. HP /MP /CP, P.. Def. and Mag. Def. 30% Speed ​​by 20%, P. Def. Atk. 10%, Mag. Atk. 20%, and decreases MP consumption by 15%. Reuse time: 40 min. When using the skill consumes 10 Essences Rose. Duration: 20 min.
		5003, // Strikes enemies power of thunder.
		5004, // Strikes enemies standing near lightning magic attack.
		5005, // Strikes nearby enemies power of thunder.
		5006, // Do not allow to impose on host any effects. Time for 5 minutes.
		5007, // Pet pierces the enemy in deadly attacks.
		5008, // Attacks nearby enemies.
		5009, // thrust the sword into the ranks vperedistoyaschego enemies.
		5010, // Enhances your skills.
		5011, // Attacks the enemy with a powerful blow.
		5012, // Explodes accumulated in the body for energy ranks Vperedistoyaschego enemies.
		5013, // Fires a shockwave on vperedistoyaschego enemy.
		5014, // Greatly enhances their skills.
		5015, // Change the attacker /auxiliary state pet.
	};
	
	private static final int[] TransformationActions =
	{
		1, // switch Run Walk. (/Walk, /run)
		2, // ​​Attack the selected goal (s). Click while holding the mouse Ctrl, to force attack. (/Attack, /attackforce)
		3, // Request for trade with the selected player. (/Trade)
		4, // Select the nearest target for attack. (/Targetnext)
		5, // ​​pick up items around. (/Pickup)
		6, // ​​Switch on the target selected player. (/Assist)
		7, // Invite selected player in your group. (/Invite)
		8, // Leave group. (/Leave)
		9, // If you are the group leader, delete the selected player (s) of group. (/Dismiss)
		11, // Display the window "Selection Panel" to find groups or members for your group. (/Partymatching)
		15, // ​​or your pet follows you, or left in place.
		16, // Attack target.
		17, // ​​Abort the current action.
		18, // Find nearby objects.
		19, // ​​Removes Pet inventory.
		21, // ​​or your minions follow you, or remain in place.
		22, // Attack target.
		23, // ​​Abort the current action.
		40, // ​​Increases score goal (/evaluate)
		50, // ​​Selected group member becomes the leader. (/Changepartyleader)
		52, // ​​Removes ties with EP and releases it.
		53, // Move to the target.
		54, // Move to the target.
		55, // record switch to stop recording and repeats. (/Start_videorecording, /end_videorecording, /Startend_videorecording)
		56, // ​​Invite a selected target in command channel. (/Channelinvite)
		57, // ​​Displays personal messages and store personal workshop containing the desired word. (/Findprivatestore)
		63, // ​​Starts fun and simple mini-game that can be play at any time. (Command: /minigame)
		64, // Opens a free teleport, which allows to move between locations with teleporters. (Command: /freeteleport)
		65, // ​​report suspicious behavior of an object, whose actions suggest the use of BOT-program.
		67, // ​​control ship
		68, // Termination control of the ship
		69, // ​​Departure ship
		70, // Descent from the ship
		74, // On /Off status data
		76, // ​​Invite a friend
		77, // On /Off. Record
		78, // Use the Mark 1
		79, // Use the Mark 2
		80, // Use the Mark 3
		81, // Use the Mark 4
		82, // avtopritsel Emblem 1
		83, // 2 avtopritsel Emblem
		84, // avtopritsel Emblem 3
		85, // 4 avtopritsel Emblem
		86, // Start /abort automatic search group
		87, // ​​Propose
		88, // ​​Provoke
		1000, // Attack the castle gates, walls and staffs a powerful blow.
		1001, // Reckless, but powerful attack, use it with great caution.
		1002, // To provoke others to attack you.
		1003, // unexpected attack that deals damage and stuns the opponent.
		1004, // Instant significantly increases P. Def. Def. and Mag. Def. Use this skill can not move.
		1005, // Magic Attack
		1006, // Restores HP pet.
		1007, // In case of a successful application temporarily increases the power attack group and a chance for a critical hit.
		1008, // Temporarily increases P. Def. Atk. and accuracy of your group.
		1009, // There is a chance to lift the curse with the group members.
		1010, // Increases MP regeneration of your group.
		1011, // Decreases the cooldown of your spells command.
		1012, // Removes the curse from your group.
		1013, // Taunt opponent and hit, curse, Decreases P. Def. Def. and Mag. Def.
		1014, // Provokes to attack many enemies and hit with curse, lowering their P.. Def. and Mag. Def.
		1015, // Sacrifices HP to regenerate HP selected target.
		1016, // Strikes opponent powerful critical attack.
		1017, // Stunning explosion, causing damage and stunning the enemy.
		1018, // Overlay deadly curse, sucking the enemy's HP.
		1019, // skill number 2, used Cat
		1020, // skill number 2 used Meow
		1021, // skill number 2 used Kai
		1022, // skill number 2 used Jupiter
		1023, // skill number 2 used Mirage
		1024, // Skill number 2 used Bekarev
		1025, // skill number 2 used Shadow
		1026, // Skill number one used by Shadow
		1027, // skill number 2 used Hecate
		1028, // Skill number 1 used Resurrection
		1029, // Skill number 2 used Resurrection
		1030, // skill number 2 used vicious
		1031, // The King of Cats: A powerful cutting attack. Maximum damage.
		1032, // The King of Cats: Cuts nearby enemies during rotation Air. Maximum damage.
		1033, // The King of Cats: Freezes enemies standing close
		1034, // Magnus: Slam hind legs, striking and stunning enemy. Maximum damage.
		1035, // Magnus: Strikes multiple objectives giant masses water.
		1036, // Wraithlord: corpse bursts, affecting adjacent enemies.
		1037, // Wraithlord: The blades in each hand applied devastating damage. Maximum damage.
		1038, // Curse of the adjacent enemies, and reducing toxic them soon. Atk.
		1039, // Siege Gun: Fires a projectile a short distance. Consumes 4 units. Gunpowder sparkling.
		1040, // Siege Gun: Fires a shell for a long distance. Consumes 5 units. Sparkling powder.
		1041, // Horrible bite the enemy
		1042, // Scratch enemy with both paws. Causes bleeding.
		1043, // Suppress the enemy with a powerful roar
		1044, // Wakes secret power
		1045, // Decreases the P.. Atk. /Mag. Atk. at nearby enemies.
		1046, // Decreases Speed. Atk. /Sprint. Mag. at nearby enemies.
		1047, // Horrible bite the enemy
		1048, // Brings double damage and stuns the enemy simultaneously.
		1049, // breathe fire in your direction.
		1050, // Suppresses surrounding enemies powerful roar.
		1051, // Increases max. amount of HP.
		1052, // Increases max. number of MP.
		1053, // Temporarily increases Atk. Atk.
		1054, // Temporarily increases speed reading spells.
		1055, // Decreases the MP cost of the selected target. Consumes runestones.
		1056, // Temporarily increases M. Def. Atk.
		1057, // Rank Temporarily increases critical strike and force magic attacks
		1058, // Temporarily increases critical strike.
		1059, // Increases the critical strike chance
		1060, // Temporarily increases Accuracy
		1061, // A strong attack from ambush. You can only use the skill "Awakening".
		1062, // Quick double attack
		1063, // Strong twisting attack does not only damage, but also stun the enemy.
		1064, // Falling from the sky stones cause damage to enemies.
		1065, // Exits the latent state
		1066, // Friendly thunderous forces
		1067, // Quick magical enemies in sight
		1068, // Attacks multiple enemies by lightning
		1069, // slosh ambush. You can only use in the application of skill "Awakening".
		1070, // Can not impose positive effects on the wearer. Step 5 minutes.
		1071, // A strong attack on the facility
		1072, // Powerful penetrating attack on the facility
		1073, // ​​Attack enemies disperse their ranks as a tornado hit
		1074, // Attack the enemy standing in front of a powerful throw spears
		1075, // Victory cry, enhancing their own skills
		1076, // A strong attack on the facility
		1077, // Attack the enemy standing in front of the internal energy
		1078, // Attack front facing enemies using electricity
		1079, // Shouting, enhancing their own skills
		1080, // fast approaching the enemy and inflicts
		1081, // Removes negative effects from the facility
		1082, // recline flame
		1083, // A powerful bite, inflicting damage to the enemy
		1084, // Switches between the attacking /defensive mode
		1086, // Limit the number of positive effects to one
		1087, // Increases dark side to 25
		1088, // Trims important skills
		1089, // Attack the enemy standing in front with the help of the tail.
		1090, // Horrible bite the enemy
		1091, // the enemy plunged into horror and makes escape from the battlefield.
		1092, // Increases movement speed.
		5000, // You can pat Rudolph. Fills the scale fidelity of 25%. Can not be used during reincarnation!
		5001, // Increases Max. HP, Max. MP and Speed ​​by 20% Resistance to de-buff by 10%. Time reuse: 10 min. When using the skill spent 3 essences Rose. Can not be used with the Beyond temptation. Duration: 5 min.
		5002, // Increases Max. HP /MP /CP, P.. Def. and Mag. Def. 30% Speed ​​by 20%, P. Def. Atk. 10%, Mag. Atk. 20%, and decreases MP consumption by 15%. Reuse time: 40 min. When using the skill consumes 10 Essences Rose. Duration: 20 min.
		5003, // Strikes enemies power of thunder.
		5004, // Strikes enemies standing near lightning magic attack.
		5005, // Strikes nearby enemies power of thunder.
		5006, // Do not allow to impose on host any effects. Time for 5 minutes.
		5007, // Pet pierces the enemy in deadly attacks.
		5008, // Attacks nearby enemies.
		5009, // thrust the sword into the ranks vperedistoyaschego enemies.
		5010, // Enhances your skills.
		5011, // Attacks the enemy with a powerful blow.
		5012, // Explodes accumulated in the body for energy ranks Vperedistoyaschego enemies.
		5013, // Fires a shockwave on vperedistoyaschego enemy.
		5014, // Greatly enhances their skills.
		5015, // Change the attacker /auxiliary state pet ..
	};
	
	private final int[] actions;
	
	public ExBasicActionList(Player activeChar)
	{
		actions = activeChar.getTransformation() == 0 ? BasicActions : TransformationActions;
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0x60);
		writeDD(actions, true);
	}
}