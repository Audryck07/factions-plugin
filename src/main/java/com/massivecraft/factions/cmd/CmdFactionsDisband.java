package com.massivecraft.factions.cmd;

import com.massivecraft.factions.cmd.arg.ARFaction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MFlag;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.event.EventFactionsDisband;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Perm;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsDisband extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCTION
	// -------------------------------------------- //
	
	public CmdFactionsDisband()
	{
		// Alias
		this.addAliases("disband");

		// Arguments
		this.addOptionalArg("faction", "you");

		// Conditions requises
		this.addRequirements(ReqHasPerm.get(Perm.DISBAND.node));
	}

	// -------------------------------------------- //
	// SURCHARGE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		// Arguments
		Faction faction = this.arg(0, ARFaction.get(), msenderFaction);
		if (faction == null) return;
		
		// Permission
		if ( ! MPerm.getPermDisband().has(msender, faction, true)) return;

		// Vérification
		if (faction.getFlag(MFlag.getFlagPermanent()))
		{
			msg("<i>Ce pays est permanente, vous ne pouvez donc pas le disband.");
			return;
		}

		// Événement
		EventFactionsDisband event = new EventFactionsDisband(me, faction);
		event.run();
		if (event.isCancelled()) return;

		// Fusion des actions et des informations
		
		// Exécuter l'événement pour chaque joueur de la faction
		for (MPlayer mplayer : faction.getMPlayers())
		{
			EventFactionsMembershipChange membershipChangeEvent = new EventFactionsMembershipChange(sender, mplayer, FactionColl.get().getNone(), MembershipChangeReason.DISBAND);
			membershipChangeEvent.run();
		}

		// Informer
		for (MPlayer mplayer : faction.getMPlayersWhereOnline(true))
		{
			mplayer.msg("<h>%s<i> a disband votre pays.", msender.describeTo(mplayer));
		}
		
		if (msenderFaction != faction)
		{
			msender.msg("<i>Vous avez disband <h>%s<i>.", faction.describeTo(msender));
		}
		
		// Journaliser
		if (MConf.get().logFactionDisband)
		{
			Factions.get().log(Txt.parse("<i>Le pays <h>%s <i>(<h>%s<i>) a été disband par <h>%s<i>.", faction.getName(), faction.getId(), msender.getDisplayName(IdUtil.getConsole())));
		}		
		
		// Appliquer
		faction.detach();
	}
	
}
