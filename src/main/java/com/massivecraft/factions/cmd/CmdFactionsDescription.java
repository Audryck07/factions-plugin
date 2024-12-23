package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Perm;
import com.massivecraft.factions.cmd.req.ReqHasFaction;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsDescriptionChange;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.mixin.Mixin;

public class CmdFactionsDescription extends FactionsCommand
{
	// -------------------------------------------- //
	// CONSTRUCTION
	// -------------------------------------------- //
	
	public CmdFactionsDescription()
	{
		// Alias
		this.addAliases("desc");

		// Arguments
		this.addRequiredArg("desc");
		this.setErrorOnToManyArgs(false);

		// Conditions requises
		this.addRequirements(ReqHasPerm.get(Perm.DESCRIPTION.node));
		this.addRequirements(ReqHasFaction.get());
	}

	// -------------------------------------------- //
	// SURCHARGE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{	
		// Arguments
		String nouvelleDescription = this.argConcatFrom(0);
		
		// Permission
		if ( ! MPerm.getPermDesc().has(msender, msenderFaction, true)) return;
		
		// Événement
		EventFactionsDescriptionChange event = new EventFactionsDescriptionChange(sender, msenderFaction, nouvelleDescription);
		event.run();
		if (event.isCancelled()) return;
		nouvelleDescription = event.getNewDescription();

		// Appliquer
		msenderFaction.setDescription(nouvelleDescription);
		
		// Informer
		for (MPlayer membre : msenderFaction.getMPlayers())
		{
			membre.msg("<i>%s <i>a changer la description de votre pays pour :\n%s", Mixin.getDisplayName(sender, membre), msenderFaction.getDescription());
		}
	}
	
}
